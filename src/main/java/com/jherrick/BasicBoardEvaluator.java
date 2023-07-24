package com.jherrick;

public class BasicBoardEvaluator implements BoardEvaluator{
    
    // =============== Instance variables ===============
    
    // Evaluation metrics. Only add or remove metrics, do not hard code in. Should be hard coded in EVAL_CONSTANTS
    private static int c0 = Constants.EVAL_CONSTANTS[0];
	private static int c1 = Constants.EVAL_CONSTANTS[1];
	private static int c2 = Constants.EVAL_CONSTANTS[2];

    public BigBoard board;
    public Side side;



    // ============== Constructors ======================
    public BasicBoardEvaluator(){
        board = new BigBoard();
        side = Side.X;
    }
    
    public BasicBoardEvaluator(BigBoard b, Side s){
        this();

        board = b;
        side = s;
    }



    // ============== Public Functions =================
    @Override
    public double evaluate(BigBoard board, Side side) {
        double xScore = evaluateForX(board);
        if(side.equals(Side.O)){
            return -1 * xScore;
        }
        return xScore;
    }



    // ============== Helper Functions ================


    // ========= Evaluation Helper Functions ==========
    private double evaluateForX(BigBoard board){
        
        // Find board that is won - This is the most extreme case (will always or never be picked)
        if (board.getBoardState() == BoardState.X_WON) {
            return Integer.MAX_VALUE;
        }
        if (board.getBoardState() == BoardState.O_WON) {
            return Integer.MIN_VALUE;
        }
        
        // Why are we initializing this? SHould we weigh the SubBoard that we are on?
        SubBoard[] boardPosition = board.getBoardPosition();
    
        return evaluateBigBoard(board);
    }

    // Apply heuristic
    private double evaluateBigBoard(BigBoard board){
        return 10 * c0 * Eval.twoInARowsWithOpenThird(board.xWinBoards, (board.oWinBoards | board.drawnBoards))
                + 10 * c1 * Eval.middleSquare(board.xWinBoards)
                - 10 * c0 * Eval.twoInARowsWithOpenThird(board.oWinBoards, (board.xWinBoards | board.drawnBoards))
                - 10 * c1 * Eval.middleSquare(board.oWinBoards);
    }
}
