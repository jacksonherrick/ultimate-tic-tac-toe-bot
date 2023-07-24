package com.jherrick;

/**
 * BoardEvaluators assign a numerical score to the state of a Board
 */
public interface BoardEvaluator {
    /**
     * @param board - the Board to evaluate
     * @param side - the Side we are evaluating the Board for
     * @return a numerical representation of how "good" the provided Board is for the provided Side
     */

    double evaluate(BigBoard board, Side side);
}
