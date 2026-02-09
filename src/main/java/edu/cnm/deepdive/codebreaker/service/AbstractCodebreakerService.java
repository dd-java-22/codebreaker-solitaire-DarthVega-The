package edu.cnm.deepdive.codebreaker.service;

import edu.cnm.deepdive.codebreaker.model.Game;
import edu.cnm.deepdive.codebreaker.model.Guess;
import java.util.concurrent.CompletableFuture;

public interface AbstractCodebreakerService {

  static AbstractCodebreakerService getInstance(){
    return CodebreakerService.getInstance();
  }
  CompletableFuture<Game> startGame(Game game);

  CompletableFuture<Game> getGame(String gameID);

  CompletableFuture<Void> delete(String gameID);

  CompletableFuture<Guess> submitGuess(String gameID, Guess guess);

  CompletableFuture<Guess> getGuess(String gameID, String guessID);

}
