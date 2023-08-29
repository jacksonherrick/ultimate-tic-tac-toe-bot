package com.jherrick;

public class OwenBoardEvaluator implements BoardEvaluator {

    // Can use Eval Constants, will instead change values here
    private static int c0 = 1;
    private static int c1 = 2;
    private static int c3 = 1;
    private static int c4 = 2;

    public OwenBoardEvaluator() {

    }

    // ========== Public Functions ==========
    @Override
    public double evaluate(Board board, Side side) {
        double xScore = evaluateForX(board);
        if (side.equals(Side.O)) {
            return -1 * xScore;
        }
        return xScore;
    }

    // ========== Helper Functions ==========

    // ========== Evaluation Helper Functions ==========
    private double evaluateForX(Board board) {

        // Find board that is won - This is the most extreme case (will always or never
        // be picked)
        if (board.getBoardState() == BoardState.X_WON) {
            return Integer.MAX_VALUE;
        }
        if (board.getBoardState() == BoardState.O_WON) {
            return Integer.MIN_VALUE;
        }

        return evaluateBoard(board);
    }

    // Apply heuristic
    private double evaluateBoard(Board board) {
        
        int boardEval = 0;
        
        boardEval += 10 * c0 * twoInARowsWithOpenThird(board.getXWinBoards(), (board.getOWinBoards() | board.getDrawnBoards()))
                + 10 * c1 * middleSquare(board.getXWinBoards())
                - 10 * c0 * twoInARowsWithOpenThird(board.getOWinBoards(), (board.getXWinBoards() | board.getDrawnBoards()))
                - 10 * c1 * middleSquare(board.getOWinBoards());
        
        // for (int i = 0; i < board.getBoardPosition().length; i++) {
        //     boardEval += 2 * c3 * twoInARowsWithOpenThird(board.getBoardPosition()[i].getXBoard(), board.getBoardPosition()[i].getOBoard())
        //         + 2 * c4 * middleSquare(board.getBoardPosition()[i].getXBoard())
        //         - 2 * c3 * twoInARowsWithOpenThird(board.getBoardPosition()[i].getOBoard(), board.getBoardPosition()[i].getXBoard())
        //         - 2 * c4 * middleSquare(board.getBoardPosition()[i].getOBoard());
        //}
        
        return boardEval;
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

