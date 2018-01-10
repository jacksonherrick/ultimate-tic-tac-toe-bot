package test;

import java.util.concurrent.ThreadLocalRandom;

import main.Board;
import main.SubBoard; 
import main.Side;
import main.Move;
import main.Constants;
import main.BoardState;

public final class TestUtils {

	private TestUtils() {
	}
	
	/**
	 * Counts the number of expected moves for a given Board
	 * Returns on the number of moves
	 */
	public static int countMoves(Board b) {
		int count = 0;
		Move m = b.getLastMove();
		int target = -1;
		if(m != null) {
			target = m.translate();
		}
		for(int i = 0; i < b.boards.length; i++) {
			if(m == null || ((m.move & Constants.BIT_MASKS[i]) > 0) || b.boards[target].getState() != BoardState.IN_PROGRESS) {
				count += Integer.bitCount(b.boards[i].generateMoves());
			}
		}
		return count;
	}

	
	/**
	 * Used to generate random boards for testing
	 * TODO: make this generate only valid boards.
	 **/
	public static Board generateRandomBoard() {
		// randomly generate board
		SubBoard[] sbs = new SubBoard[9];
		for(int i = 0; i < 9; i++) {
			int xBoard = ThreadLocalRandom.current().nextInt(0, 513);
			int oBoard = ThreadLocalRandom.current().nextInt(0, 513);
			sbs[i] = new SubBoard(xBoard, oBoard);
		}
		
		// randomly assign side
		Side s = ThreadLocalRandom.current().nextInt(0, 2) < 1 ? Side.X : Side.O;
		
		// randomly assign previous move
		Move m = null;
		if(ThreadLocalRandom.current().nextInt(0, 82) > 0) {
			int move = Constants.BIT_MASKS[ThreadLocalRandom.current().nextInt(0, 9)];
			int board = ThreadLocalRandom.current().nextInt(0, 9);
			m = new Move(move, board);
		}
		
		return new Board(sbs, s, m);
	}
}
