package com.jherrick;

public class OwenBoardEvaluator implements BoardEvaluator {

    // Can use Eval Constants, will instead change values here
    private static int c0 = 1;
    private static int c1 = 4;
    private static int c2 = 0;
    private static int c3 = 0;

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

    /*
        Thoughts on how to improve heuristic.

        First off, the heuristic needs to positively weigh won subBoards.
        There is nuance to the value of winning a board, but to beat a random
        bot, it should probably prioritize winning subBoards. Suggest bigBoard
        to keep track (add instance variable)

        Second, need something that reads each subBoard, and positively weigh subBoards
        that are close to winning. On the same lines, can determine how many
        options a side has to win a subBoard or the entire board based on 
        any amount of Xs or Os present.
    */
    private double evaluateBoard(Board board) {
        
        int boardEval = 0;
        
        boardEval += c0 * twoInARowsWithOpenThird(board.getXWinBoards(), (board.getOWinBoards() | board.getDrawnBoards()))
                + c1 * middleSquare(board.getXWinBoards())
                + c2 * board.getNumXWinBoards()
                + c3 * freedomOfMovement(board, Side.X)
                - c0 * twoInARowsWithOpenThird(board.getOWinBoards(), (board.getXWinBoards() | board.getDrawnBoards()))
                - c1 * middleSquare(board.getOWinBoards())
                - c2 * board.getNumOWinBoards()
                - c3 * freedomOfMovement(board, Side.O);
        
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

    // Detects if we are sending to any subBoard
    public static int freedomOfMovement(Board board, Side side){
        if (board.isAllPossibleMoves()){
            if (board.getSideForNextMove() == side) {
                return 1;
            }
        }
        return 0;
    }
}

