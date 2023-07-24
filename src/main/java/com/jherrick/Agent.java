package com.jherrick;

/**
 * The interface for all actors in the game (player, AI, etc)
 */
public interface Agent {
    /**
     * Given the state of the Board, the Agent picks their next move
     * @param board - the game board
     * @return the move that the Agent will make
     */
    Move pickMove(BigBoard board);
}
