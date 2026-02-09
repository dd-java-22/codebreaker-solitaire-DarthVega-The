package edu.cnm.deepdive.codebreaker.service;

import edu.cnm.deepdive.codebreaker.model.Game;
import edu.cnm.deepdive.codebreaker.model.Guess;
import java.util.concurrent.CompletableFuture;

enum CodebreakerService implements AbstractCodebreakerService {

  INSTANCE;

  private final CodebreakerApi;

  CodebreakerService() {
    //TODO 2026-02-09 Do initialization of Gson, and CodeBreakerApi
  }

  @Override
  public CompletableFuture<Game> startGame(Game game) {
    throw new UnsupportedOperationException("Not yet implemented.");  }

  @Override
  public CompletableFuture<Game> getGame(String gameID) {
    throw new UnsupportedOperationException("Not yet implemented.");
  }

  @Override
  public CompletableFuture<Void> delete(String gameID) {
    throw new UnsupportedOperationException("Not yet implemented.");  }

  @Override
  public CompletableFuture<Guess> submitGuess(String gameID, Guess guess) {
    throw new UnsupportedOperationException("Not yet implemented.");  }

  @Override
  public CompletableFuture<Guess> getGuess(String gameID, String guessID) {
    throw new UnsupportedOperationException("Not yet implemented.");  }
}

