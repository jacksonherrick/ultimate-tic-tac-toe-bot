package com.jherrick;

/**
 * An interface representing a single game.
 * It is simple for now, but may grow in complexity as more functionality is
 * added
 * For example: time limits on games, persisting who won and lost, running
 * multiplayer games on a server, etc
 */
public interface Game {
    void play();
}
