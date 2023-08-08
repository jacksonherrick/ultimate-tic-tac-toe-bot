package com.jherrick;

import java.util.Collection;

/**
 * The interface representing the game board
 */
public interface Board {

    /**
     * Apply the given Move to the Board, do all necessary checks, and return the
     * board to an idle state
     * 
     * @param move - the Move to be applied to the board
     */
    void makeMove(Move move);

    /**
     * "Take back" the last move, returning the board to the previous state.
     */
    void undoLastMove();

    /**
     * @return the BoardState of the Board - an indication of the current game
     *         phase.
     */
    BoardState getBoardState();

    /**
     * @return all complete subBoards within Board
     */
    int getXWinBoards();

    int getOWinBoards();

    int getDrawnBoards();

    /**
     * @return the Collection of moves that can currently be made
     */
    Collection<Move> getLegalMoves();

    /**
     * @return the specifics of the current Board position
     */
    SubBoard[] getBoardPosition();
}
