package com.jherrick;

public class BasicBoardEvaluator implements BoardEvaluator{
    
    
    private static int c0 = Constants.EVAL_CONSTANTS[0];
	private static int c1 = Constants.EVAL_CONSTANTS[1];
	private static int c2 = Constants.EVAL_CONSTANTS[2];

    public BigBoard board;
    public Side side;

    public BasicBoardEvaluator(BigBoard b, Side s){
        board = b;
        side = s;
    }



    @Override
    public double evaluate(BigBoard board, Side side) {
        double xScore = evaluateForX(board);
        if(side.equals(Side.O)){
            return -1 * xScore;
        }
        return xScore;
    }

    private double evaluateForX(BigBoard board){
        if (board.getBoardState() == BoardState.X_WON) {
            return Integer.MAX_VALUE;
        }
        if (board.getBoardState() == BoardState.O_WON) {
            return Integer.MIN_VALUE;
        }
        SubBoard[] boardPosition = board.getBoardPosition();
        
        // TODO: implement actual evaluation
        return 10 * c0 * Eval.twoInARowsWithOpenThird(board.xWinBoards, (board.oWinBoards | board.drawnBoards))
                + 10 * c1 * Eval.middleSquare(board.xWinBoards)
                - 10 * c0 * Eval.twoInARowsWithOpenThird(board.oWinBoards, (board.xWinBoards | board.drawnBoards))
                - 10 * c1 * Eval.middleSquare(board.oWinBoards);
    }
}
