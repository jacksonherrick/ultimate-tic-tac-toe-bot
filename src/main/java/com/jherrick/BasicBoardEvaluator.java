package com.jherrick;

public class BasicBoardEvaluator implements BoardEvaluator{
    
    // =============== Instance variables ===============
    
    // Evaluation metrics. Only add or remove metrics, do not hard code in. Should be hard coded in EVAL_CONSTANTS
    private static int c0 = Constants.EVAL_CONSTANTS[0];
	private static int c1 = Constants.EVAL_CONSTANTS[1];
	// private static int c2 = Constants.EVAL_CONSTANTS[2];

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
    
        return evaluateBigBoard(board);
    }

    // Apply heuristic
    private double evaluateBigBoard(BigBoard board){
        return 10 * c0 * twoInARowsWithOpenThird(board.xWinBoards, (board.oWinBoards | board.drawnBoards))
                + 10 * c1 * middleSquare(board.xWinBoards)
                - 10 * c0 * twoInARowsWithOpenThird(board.oWinBoards, (board.xWinBoards | board.drawnBoards))
                - 10 * c1 * middleSquare(board.oWinBoards);
    }

    // For Boards, blockedSquares is oWinBoards | drawnBoards. For SubBoards, it's
	// just oBoard
	public static int twoInARowsWithOpenThird(int goodSquares, int blockedSquares) {
		int count = 0;
		for (int i = 0; i < Constants.TWOINAROWS.length; i++) {
			if (((goodSquares & Constants.TWOINAROWS[i]) == Constants.TWOINAROWS[i])
					&& ((blockedSquares & Constants.OPENTHIRDS[i]) == 0)) {
				count++;
			}
		}

		return count;
	}

	// For SubBoards
	public static int middleSquare(int goodSquares) {
		if ((goodSquares & 0x10) == 0x10) {
			return 1;
		}
		return 0;
	}
}
