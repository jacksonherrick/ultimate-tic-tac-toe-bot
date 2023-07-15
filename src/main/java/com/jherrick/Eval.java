package com.jherrick;

import java.util.HashMap;
import java.util.HashSet;

// NOTE: I am assuming the bot is always playing X for simplicity's sake right now
public class Eval {
	// Maps all the possible boards to their value
	private static HashMap<SubBoard, Integer> values = new HashMap<SubBoard, Integer>();
	// Set of all possible boards. If we want, we could just use values, but this
	// may make it more readable
	private static HashSet<SubBoard> possibleBoards = new HashSet<SubBoard>();

	private static int c0 = Constants.EVAL_CONSTANTS[0];
	private static int c1 = Constants.EVAL_CONSTANTS[1];
	private static int c2 = Constants.EVAL_CONSTANTS[2];

	/*
	 * Evaluates the values of all possible small boards given the constants Used to
	 * save values for each of the board states at the start of the game to speed up
	 * the negamax. This method will find all the values of each state for X, so
	 * when we use it in the negamax, we have to take the inverse for O moves. Or we
	 * can have findValues take an argument for the side
	 */
	public static void findValues() {
		for (SubBoard b : possibleBoards) {
			values.put(b, evaluate(b));
		}
	}

	public static HashMap<SubBoard, Integer> getValues() {
		return values;
	}

	public static int evaluate(SubBoard board) {
		if (board.getState() == BoardState.X_WON) {
			return c2;
		}
		if (board.getState() == BoardState.O_WON) {
			return -1 * c2;
		}
		return c0 * twoInARowsWithOpenThird(board.getXBoard(), board.getOBoard()) + c1 * middleSquare(board.getXBoard())
				- c0 * twoInARowsWithOpenThird(board.getOBoard(), board.getXBoard())
				- c1 * middleSquare(board.getOBoard());
	}

	// Helper
	// Functions---------------------------------------------------------------------------------------------------------------------------------------------------------------------

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