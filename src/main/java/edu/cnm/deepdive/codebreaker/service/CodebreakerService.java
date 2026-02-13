package edu.cnm.deepdive.codebreaker.service;

import edu.cnm.deepdive.codebreaker.model.Game;
import edu.cnm.deepdive.codebreaker.model.Guess;
import java.util.concurrent.CompletableFuture;

/**
 * Defines the contract for interacting with the Codebreaker backend service. Implementations of
 * this interface provide asynchronous operations for creating, retrieving, updating, and deleting
 * {@link Game} instances, as well as submitting and retrieving {@link Guess} objects.
 *
 * <p>All methods return {@link CompletableFuture} instances, allowing callers to compose,
 * transform, and observe results without blocking the UI thread.</p>
 */
public interface CodebreakerService {

  /**
   * Returns the singleton instance of the {@code CodebreakerService}. The actual implementation is
   * provided by {@link CodebreakerServiceImpl}, but callers should depend only on this interface.
   *
   * @return the shared {@code CodebreakerService} instance.
   */
  static CodebreakerService getInstance() {
    return CodebreakerServiceImpl.getInstance();
  }

  /**
   * Starts a new game using the provided {@link Game} configuration. The returned future completes
   * with the fully initialized game, including its generated ID and secret code metadata.
   *
   * @param game the game configuration containing pool and length settings.
   * @return a future that completes with the created {@link Game}.
   */
  CompletableFuture<Game> startGame(Game game);

  /**
   * Retrieves an existing game by its unique identifier. The returned future completes with the
   * full game state, including guesses made so far.
   *
   * @param gameId the ID of the game to retrieve.
   * @return a future that completes with the requested {@link Game}.
   */
  CompletableFuture<Game> getGame(String gameId);

  /**
   * Deletes the game with the specified ID. The returned future completes when the deletion
   * operation finishes.
   *
   * @param gameId the ID of the game to delete.
   * @return a future that completes when the game has been deleted.
   */
  CompletableFuture<Void> deleteGame(String gameId);

  /**
   * Submits a guess for the specified game. The returned future completes with the evaluated
   * {@link Guess}, including correctness indicators such as exact and partial matches.
   *
   * @param game  the game for which the guess is being submitted.
   * @param guess the guess to evaluate.
   * @return a future that completes with the evaluated {@link Guess}.
   */
  CompletableFuture<Guess> submitGuess(Game game, Guess guess);

  /**
   * Retrieves a previously submitted guess by its ID for the specified game.
   *
   * @param gameId  the ID of the game containing the guess.
   * @param guessId the ID of the guess to retrieve.
   * @return a future that completes with the requested {@link Guess}.
   */
  CompletableFuture<Guess> getGuess(String gameId, String guessId);

  /**
   * Shuts down the service and releases any underlying resources. After shutdown, the service
   * instance should not be used.
   */
  void shutdown();

}
