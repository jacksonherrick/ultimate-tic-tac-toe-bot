package com.jherrick;

public class OwenBoardEvaluator implements BoardEvaluator {

    // Can use Eval Constants, will instead change values here
    private static int c0 = 0;
    private static int c1 = 0;
    private static int c2 = 100;
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

    /*
        Thoughts on how to improve heuristic.

        First off, the heuristic needs to positively weigh won subBoards.
        There is nuance to the value of winning a board, but to beat a random
        bot, it should probably prioritize winning subBoards.

        Second, need something that reads each subBoard, and positively weigh subBoards
        that are close to winning. On the same lines, can determine how many
        options a side has to win a subBoard or the entire board based on 
        any amount of Xs or Os present.

        Third, there is very little strategic benefit to sending an opponent
        to a board that is complete. Generally in games, strategic options have
        non-negative value, so giving your opponent the ability to go anywhere
        will rarely be beneficial. This should be outweighed by winning subBoards
        and the game as a whole. You could also prefer to send to a subBoard
        that has few moves left to force their decisions
    */
    private double evaluateBoard(Board board) {
        
        int boardEval = 0;
        
        boardEval += c0 * twoInARowsWithOpenThird(board.getXWinBoards(), (board.getOWinBoards() | board.getDrawnBoards()))
                + c1 * middleSquare(board.getXWinBoards())
                + c2 * freedomOfMovement(board, Side.X)
                - c0 * twoInARowsWithOpenThird(board.getOWinBoards(), (board.getXWinBoards() | board.getDrawnBoards()))
                - c1 * middleSquare(board.getOWinBoards())
                - c2 * freedomOfMovement(board, Side.O);
        
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

