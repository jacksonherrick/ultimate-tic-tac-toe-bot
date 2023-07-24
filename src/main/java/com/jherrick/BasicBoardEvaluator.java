package com.jherrick;

public class BasicBoardEvaluator implements BoardEvaluator{
    @Override
    public double evaluate(Board board, Side side) {
        double xScore = evaluateForX(board);
        if(side.equals(Side.O)){
            return -1 * xScore;
        }
        return xScore;
    }

    private double evaluateForX(Board board){
        if (board.getBoardState() == BoardState.X_WON) {
            return Integer.MAX_VALUE;
        }
        if (board.getBoardState() == BoardState.O_WON) {
            return Integer.MIN_VALUE;
        }
        SubBoard[] boardPosition = board.getBoardPosition();
        // TODO: implement actual evaluation
//        return 10 * c0 * Eval.twoInARowsWithOpenThird(xWinBoards, (oWinBoards | drawnBoards))
//                + 10 * c1 * Eval.middleSquare(xWinBoards)
//                - 10 * c0 * Eval.twoInARowsWithOpenThird(oWinBoards, (xWinBoards | drawnBoards))
//                - 10 * c1 * Eval.middleSquare(oWinBoards) + score;
        return 0;
    }
}
