/*
 *  Copyright 2026 CNM Ingenuity, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package edu.cnm.deepdive.codebreaker.app.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import java.time.Instant;

/**
 * Represents a Codebreaker game in the persistent data model.
 * The entity maintains the state of the game, including the code pool, length, 
 * start time, solve status, and the results of the most recent guess.
 */
@Entity(
    tableName = "game",
    indices = {
        @Index(value = "external_key", unique = true),
        @Index(value = {"solved", "started", "last_played"}),
        @Index(value = {"length", "guess_count"})
    }
)
public class Game {

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "game_id")
  private long id;

  @NonNull
  @ColumnInfo(name = "external_key")
  private String externalKey = "";

  @NonNull
  private String pool = "";

  private int length;

  @NonNull
  private Instant started = Instant.now();

  @ColumnInfo(name = "guess_count")
  private int guessCount;

  private boolean solved;

  @NonNull
  @ColumnInfo(name = "last_played")
  private Instant lastPlayed;

  @ColumnInfo(name = "exact_matches")
  private int exactMatches;

  @ColumnInfo(name = "near_matches")
  private int nearMatches;

  /**
   * Returns the primary key of the game.
   */
  public long getId() {
    return id;
  }

  /**
   * Sets the primary key of the game.
   *
   * @param id The unique identifier of the game record.
   */
  public void setId(long id) {
    this.id = id;
  }

  /**
   * Returns the unique external key (UUID) used for identifying the game in the service.
   */
  @NonNull
  public String getExternalKey() {
    return externalKey;
  }

  /**
   * Sets the unique external key (UUID).
   *
   * @param externalKey The identifier from the service.
   */
  public void setExternalKey(@NonNull String externalKey) {
    this.externalKey = externalKey;
  }

  /**
   * Returns the pool of characters used in the game's secret code.
   */
  @NonNull
  public String getPool() {
    return pool;
  }

  /**
   * Sets the pool of characters.
   *
   * @param pool The code pool.
   */
  public void setPool(@NonNull String pool) {
    this.pool = pool;
  }

  /**
   * Returns the length of the secret code.
   */
  public int getLength() {
    return length;
  }

  /**
   * Sets the length of the secret code.
   *
   * @param length The code length.
   */
  public void setLength(int length) {
    this.length = length;
  }

  /**
   * Returns the timestamp when the game was started.
   */
  @NonNull
  public Instant getStarted() {
    return started;
  }

  /**
   * Sets the timestamp when the game was started.
   *
   * @param started The start time.
   */
  public void setStarted(@NonNull Instant started) {
    this.started = started;
  }

  /**
   * Returns the number of guesses submitted in the game.
   */
  public int getGuessCount() {
    return guessCount;
  }

  /**
   * Sets the number of guesses.
   *
   * @param guessCount The count of guesses.
   */
  public void setGuessCount(int guessCount) {
    this.guessCount = guessCount;
  }

  /**
   * Returns a flag indicating whether the secret code has been correctly guessed.
   */
  public boolean isSolved() {
    return solved;
  }

  /**
   * Sets the solved status flag.
   *
   * @param solved {@code true} if solved, {@code false} otherwise.
   */
  public void setSolved(boolean solved) {
    this.solved = solved;
  }

  /**
   * Returns the timestamp of the last guess submitted.
   */
  @NonNull
  public Instant getLastPlayed() {
    return lastPlayed;
  }

  /**
   * Sets the timestamp of the last guess.
   *
   * @param lastPlayed The last play time.
   */
  public void setLastPlayed(@NonNull Instant lastPlayed) {
    this.lastPlayed = lastPlayed;
  }

  /**
   * Returns the number of exact matches in the most recent guess.
   */
  public int getExactMatches() {
    return exactMatches;
  }

  /**
   * Sets the number of exact matches.
   *
   * @param exactMatches The count of exact matches.
   */
  public void setExactMatches(int exactMatches) {
    this.exactMatches = exactMatches;
  }

  /**
   * Returns the number of near matches in the most recent guess.
   */
  public int getNearMatches() {
    return nearMatches;
  }

  /**
   * Sets the number of near matches.
   *
   * @param nearMatches The count of near matches.
   */
  public void setNearMatches(int nearMatches) {
    this.nearMatches = nearMatches;
  }

}
