package com.jherrick;

public class BasicBoardEvaluator implements BoardEvaluator {

    private static int c0 = Constants.EVAL_CONSTANTS[0];
    private static int c1 = Constants.EVAL_CONSTANTS[1];

    public BasicBoardEvaluator() {

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
        return 10 * c0
                * twoInARowsWithOpenThird(board.getXWinBoards(), (board.getOWinBoards() | board.getDrawnBoards()))
                + 10 * c1 * middleSquare(board.getXWinBoards())
                - 10 * c0
                        * twoInARowsWithOpenThird(board.getOWinBoards(),
                                (board.getXWinBoards() | board.getDrawnBoards()))
                - 10 * c1 * middleSquare(board.getOWinBoards());
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
