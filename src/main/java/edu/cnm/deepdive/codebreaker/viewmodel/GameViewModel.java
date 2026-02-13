package edu.cnm.deepdive.codebreaker.viewmodel;

import edu.cnm.deepdive.codebreaker.model.Game;
import edu.cnm.deepdive.codebreaker.model.Guess;
import edu.cnm.deepdive.codebreaker.service.CodebreakerService;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import javafx.application.Platform;

@SuppressWarnings({"UnusedReturnValue", "CallToPrintStackTrace", "unused"})

/**
 * Acts as the view-model layer for the Codebreaker application, coordinating communication between
 * the UI and the {@link CodebreakerService}. This class manages the current {@link Game}, the most
 * recent {@link Guess}, error conditions, and solved-state updates. It also implements an observer
 * pattern, allowing UI components to register callbacks that are invoked when game state changes.
 *
 * <p>This class is implemented as a singleton. All callers obtain the shared instance via
 * {@link #getInstance()}.</p>
 */
public class GameViewModel {

  private static class Holder {

    static final GameViewModel INSTANCE = new GameViewModel();
  }

  private final CodebreakerService service;
  private final List<Consumer<Game>> gameObservers;
  private final List<Consumer<Guess>> guessObservers;
  private final List<Consumer<Throwable>> errorObservers;
  private final List<Consumer<Boolean>> solvedObservers;

  private Game game;
  private Guess guess;
  private Boolean solved;
  private Throwable error;

  private GameViewModel() {
    service = CodebreakerService.getInstance();
    gameObservers = new LinkedList<>();
    guessObservers = new LinkedList<>();
    errorObservers = new LinkedList<>();
    solvedObservers = new LinkedList<>();
  }

  /**
   * Returns the singleton instance of {@code GameViewModel}. All callers receive the same shared
   * instance, ensuring consistent state across the application.
   *
   * @return the shared {@code GameViewModel} instance.
   */
  public static GameViewModel getInstance() {
    return Holder.INSTANCE;
  }

  /**
   * Creates and starts a new game using the specified pool and code length. When the game is
   * successfully created, observers are notified of the new game and its solved state.
   *
   * @param pool   the set of characters allowed in the secret code.
   * @param length the length of the secret code.
   */
  public void startGame(String pool, int length) {
    Game game = new Game()
        .pool(pool)
        .length(length);
    service
        .startGame(game)
        .thenApply((startedGame) -> setGame(startedGame).getSolved())
        .thenAccept(this::setSolved)
        .exceptionally(this::logError);
  }

  /**
   * Retrieves an existing game by its ID and updates observers with the retrieved game and its
   * solved state.
   *
   * @param gameId the unique identifier of the game to retrieve.
   */
  public void getGame(String gameId) {
    service
        .getGame(gameId)
        .thenApply((game) -> setGame(game).getSolved())
        .thenAccept(this::setSolved)
        .exceptionally(this::logError);
  }

  /**
   * Requests deletion of the game with the specified ID. If an error occurs, registered error
   * observers are notified.
   *
   * @param gameId the ID of the game to delete.
   */
  public void deleteGame(String gameId) {
    service
        .deleteGame(gameId)
        .exceptionally(this::logError);
  }

  /**
   * Deletes the currently active game. After deletion, the internal game reference is cleared and
   * observers are notified.
   */
  public void deleteGame() {
    service
        .deleteGame(game.getId())
        .thenRun(() -> setGame(null))
        .exceptionally(this::logError);
  }

  /**
   * Submits a guess for the current game. If the guess solves the puzzle, the full game state is
   * refreshed; otherwise, the guess is appended to the game's guess list and observers are
   * updated.
   *
   * @param text the guess text submitted by the user.
   */
  public void submitGuess(String text) {
    Guess guess = new Guess()
        .text(text);
    service
        .submitGuess(game, guess)
        .thenApply(this::setGuess)
        .thenAccept((guessResponse) -> {
          if (Boolean.TRUE.equals(guessResponse.getSolution())) {
            getGame(game.getId());
          } else {
            game.getGuesses().add(guessResponse);
            setGame(game);
          }
        })
        .exceptionally(this::logError);
  }

  /**
   * Retrieves a specific guess by ID for the current game and notifies observers.
   *
   * @param guessId the ID of the guess to retrieve.
   */
  public void getGuess(String guessId) {
    service
        .getGuess(game.getId(), guessId)
        .thenAccept(this::setGuess)
        .exceptionally(this::logError);
  }

  /**
   * Shuts down the underlying {@link CodebreakerService}, releasing any resources it holds.
   */
  public void shutdown() {
    service.shutdown();
  }

  /**
   * Registers an observer that will be notified whenever the current game changes. If a game is
   * already loaded, the observer is immediately invoked with the current game.
   *
   * @param observer a consumer that receives game updates.
   */
  public void registerGameObserver(Consumer<Game> observer) {
    gameObservers.add(observer);
    if (game != null) {
      observer.accept(game);
    }
  }

  /**
   * Registers an observer that will be notified whenever a guess is updated. If a guess is already
   * available, the observer is immediately invoked.
   *
   * @param observer a consumer that receives guess updates.
   */
  public void registerGuessObserver(Consumer<Guess> observer) {
    guessObservers.add(observer);
    if (guess != null) {
      observer.accept(guess);
    }
  }

  /**
   * Registers an observer that will be notified when the solved state of the game changes. If the
   * solved state is already known, the observer is immediately invoked.
   *
   * @param observer a consumer that receives solved-state updates.
   */
  public void registerSolvedObserver(Consumer<Boolean> observer) {
    solvedObservers.add(observer);
    if (solved != null) {
      observer.accept(solved);
    }
  }

  /**
   * Registers an observer that will be notified when an error occurs. If an error has already been
   * recorded, the observer is immediately invoked.
   *
   * @param observer a consumer that receives error notifications.
   */
  public void registerErrorObserver(Consumer<Throwable> observer) {
    errorObservers.add(observer);
    if (error != null) {
      observer.accept(error);
    }
  }

  /**
   * Updates the current game and notifies all registered game observers on the JavaFX application
   * thread.
   *
   * @param game the new game state.
   * @return the same game instance, for chaining.
   */
  private Game setGame(Game game) {
    this.game = game;
    Platform.runLater(() -> gameObservers
        .forEach((consumer) -> consumer.accept(game)));
    return game;
  }

  /**
   * Updates the most recent guess and notifies all registered guess observers on the JavaFX
   * application thread.
   *
   * @param guess the guess to set.
   * @return the same guess instance, for chaining.
   */
  private Guess setGuess(Guess guess) {
    this.guess = guess;
    Platform.runLater(() -> guessObservers
        .forEach((consumer) -> consumer.accept(guess)));
    return guess;
  }

  /**
   * Updates the solved state of the current game and notifies all registered solved observers on
   * the JavaFX application thread.
   *
   * @param solved {@code true} if the game is solved; {@code false} otherwise.
   * @return the same solved value, for chaining.
   */
  private Boolean setSolved(Boolean solved) {
    this.solved = solved;
    Platform.runLater(() -> solvedObservers
        .forEach((consumer) -> consumer.accept(solved)));
    return solved;
  }

  /**
   * Records an error and notifies all registered error observers on the JavaFX application thread.
   *
   * @param error the error that occurred.
   * @return the same error instance, for chaining.
   */
  private Throwable setError(Throwable error) {
    this.error = error;
    Platform.runLater(() -> errorObservers
        .forEach((consumer) -> consumer.accept(error)));
    return error;
  }

  /**
   * Logs an error returned from an asynchronous operation, extracts its cause if present, and
   * forwards it to {@link #setError(Throwable)}.
   *
   * @param error the error encountered during an asynchronous service call.
   * @return always {@code null}, allowing use in {@code exceptionally}.
   */
  private Void logError(Throwable error) {
    setError(error.getCause() != null ? error.getCause() : error);
    return null;
  }

}

