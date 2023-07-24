package com.jherrick;

import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BigBoard implements Board{
	
	// ============== Instance Variables =================
	
	// array to hold SubBoard instances
	public SubBoard[] boards;

	// bitboards used to check for wins and draws
	int xWinBoards;
	int oWinBoards;
	int drawnBoards;

	// represents which player is to move
	Side side;

	// state of the board
	BoardState state;

	// previous Move array and number of moves compelted
	Move[] pastMoves;
	int count;

	// Evaluation constants to make the evaluation function easier to read - SHOULD BE REMOVED
	private int c0 = Constants.EVAL_CONSTANTS[0];
	private int c1 = Constants.EVAL_CONSTANTS[1];



	// ===================== Constructors ======================

	/**
	 * Default constructor, creates a Board of 9 empty SubBoards Initializes
	 * win/draw boards
	 **/
	public BigBoard() {
		// initialize empty SubBoards
		boards = new SubBoard[9];
		for (int i = 0; i < 9; i++) {
			boards[i] = new SubBoard();
		}

		// initialize bitboards to zero
		xWinBoards = 0;
		oWinBoards = 0;
		drawnBoards = 0;

		// default side to X
		side = Side.X;

		// default BoardState
		state = BoardState.IN_PROGRESS;

		pastMoves = new Move[81];
		count = 0;
	}

	/**
	 * Overloaded constructor. Creates a Board from a Herrick-Corley Notation
	 * string. HCN notation lists the SubBoards using "X", "O", and 1-9, starting
	 * from top left.
	 **/
	public BigBoard(String hcn) {
		// call default constructor
		this();

		// compile the regex to match HCN's
		Pattern r = Pattern.compile(
				"^([XO1-9]{0,9})\\/([XO1-9]{0,9})\\/([XO1-9]{0,9})\\/([XO1-9]{0,9})\\/([XO1-9]{0,9})\\/([XO1-9]{0,9})\\/([XO1-9]{0,9})\\/([XO1-9]{0,9})\\/([XO1-9]{0,9})\\s([XO]){1}\\s([a-zA-Z0-9]{2}|\\-)$");

		// match the HCN
		Matcher m = r.matcher(hcn);
		if (m.matches()) {
			// loop through matches
			for (int i = 1; i < 12; i++) {
				if (i < 10) {
					boards[i - 1] = new SubBoard(m.group(i));
					boards[i - 1].checkConditions();
					updateStateBitboards(i - 1);
				} else if (i == 10) {
					side = m.group(i).equals("X") ? Side.X : Side.O;
				} else if (!m.group(i).equals("-")) {
					pastMoves[0] = Utils.coordinatesToMove(m.group(i));
					count = 1;
				}
			}
		} else {
			System.out.println("Invalid HCN.");
			System.exit(0);
		}
	}

	/**
	 * Overloaded constructor. Creates a Board from a list of SubBoard objects, a
	 * given Side, and a previous move. Primarily used in testing.
	 **/
	public BigBoard(SubBoard[] sbs, Side s, Move lastMove) {

		// call default constructor
		this();

		// set SubBoards
		for (int i = 0; i < sbs.length; i++) {
			boards[i] = sbs[i];
			boards[i].checkConditions();
			updateStateBitboards(i);
		}

		// set side
		side = s;

		// set previous move
		if (lastMove != null) {
			pastMoves[0] = lastMove;
			count = 1;
		}
	}



	// ======================== Public Functions =========================

	/**
	 * Makes the move (passed as an argument) Also triggers win/draw checks TODO:
	 * make sure that legality check is called for player-entered moves (check
	 * there, not here)
	 **/

	@Override
	public void makeMove(Move m) {
		
		SubBoard subboard = boards[m.board];
		subboard.makeMove(m.move, side);

		updateStateBitboards(m.board);
		toggleSide();
		putMoveInLog(m);
	}

	@Override
	public void undoLastMove() {
		takeMove(getLastMove());
	}

	@Override
	public BoardState getBoardState() {
		return this.state;
	}

	/**
	 * Generates the potential moves for all locations on the current Board. Returns
	 * a list representation of the available moves.
	 **/
	@Override
	public List<Move> getLegalMoves() {
		
		List<Move> moves = new ArrayList<>();
		Move move = this.getLastMove();

		// if first move, generate all moves
		if (move == null){
			moves = allPossibleMoves(boards);
		}

		// if target SubBoard is IN_PROGRESS, only generate moves in that SubBoard
		if (boards[move.getSubBoardTarget()].getState() == BoardState.IN_PROGRESS) {
			
			moves.addAll(getLegalMoves(move.getSubBoardTarget()));
			return moves;
		
		} else {
			moves = allPossibleMoves(boards);
		}

		return moves;
	}

	@Override
	public SubBoard[] getBoardPosition() {
		return this.boards;
	}

	/**
	 * Returns the last move, or null if there have not been any moves
	 **/
	public Move getLastMove() {
		return count == 0 ? null : pastMoves[count - 1];
	}

	/**
	 * Updates board state bitboards Takes as input the board index TODO: any way to
	 * make this cleaner? ugly ugly.
	 **/
	public void updateStateBitboards(int board) {
		BoardState s = boards[board].getState();
		if (s == BoardState.IN_PROGRESS) {
			updateInProgressBitBoards(board);
		} else if (s == BoardState.DRAWN) {
			updateDrawnBitBoards(board);
		} else if (s == BoardState.X_WON) {
			updateXWonBitBoards(board);
		} else if (s == BoardState.O_WON) {
			updateOWonBitBoards(board);
		} else if (Constants.REPORTING_LEVEL > 1) {
			throw new java.lang.RuntimeException(
					"Unknown BoardState \"" + s + "\" encountered in updateStateBitboards()");
		}
	}

	/**
	 * Takes back the move (passed as an argument) Also triggers win/draw checks
	 **/
	public void takeMove(Move m) {
		boards[m.board].takeMove(m.move);
		updateStateBitboards(m.board);
		toggleSide();
		count--;
		pastMoves[count] = null;
	}

	
	// Refactor Line ----------------------------------------------
	
	/**
	 * Returns true if a player has won on the board TODO: this check only needs to
	 * be done after many, many moves TODO: almost identical to SubBoard function.
	 * Hmmm... inheritance? TODO: Hash Map!
	 **/
	public boolean isWon() {
		for (int bb : Constants.WIN_BITBOARDS) {
			if ((bb & xWinBoards) == bb) {
				if (state == BoardState.IN_PROGRESS) {
					state = BoardState.X_WON;
				}
				return true;
			} else if ((bb & oWinBoards) == bb) {
				if (state == BoardState.IN_PROGRESS) {
					state = BoardState.O_WON;
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns true if the the board is drawn (no available moves) TODO: this check
	 * only needs to be done after many, many moves
	 **/
	public boolean isDrawn() {
		// Yeah... so... this needs to be built!
		return false;
	}
	
	public int evaluate() {
		if (state == BoardState.X_WON) {
			return Integer.MAX_VALUE;
		}
		if (state == BoardState.O_WON) {
			return Integer.MIN_VALUE;
		}
		int score = 0;
		for (int i = 0; i < boards.length; i++) {
			// score += Eval.getValues().get(boards[i]);
		}
		return 10 * c0 * Eval.twoInARowsWithOpenThird(xWinBoards, (oWinBoards | drawnBoards))
				+ 10 * c1 * Eval.middleSquare(xWinBoards)
				- 10 * c0 * Eval.twoInARowsWithOpenThird(oWinBoards, (xWinBoards | drawnBoards))
				- 10 * c1 * Eval.middleSquare(oWinBoards) + score;
	}

	/**
	 * @override toString() method
	 */
	public String toString() {
		StringBuilder result = new StringBuilder("\n  HCN: " + Utils.boardToHCN(this) + "\n");
		String spacer = "  +---------+---------+---------+\n";
		result.append(spacer);
		int counter = 9;
		for (int j = 0; j < 9; j += 3) {
			String[] b1 = boards[j].toArrayRepresentation();
			String[] b2 = boards[j + 1].toArrayRepresentation();
			String[] b3 = boards[j + 2].toArrayRepresentation();

			for (int i = 0; i < 3; i++) {
				result.append(counter + " " + "|" + b1[i] + "|" + b2[i] + "|" + b3[i] + "|\n");
				counter--;
			}
			result.append(spacer);
		}
		String bottomKey = String.format("%3s a  b  c %1s d  e  f %1s g  h  i \n", " ", " ", " ");
		result.append(bottomKey);
		return result.toString();
	}




	// ===================== Helper Functions ========================

	/**
	 * Toggles which side is to move
	 **/
	private void toggleSide() {
		side = side == Side.X ? Side.O : Side.X;
	}


	// ================ Make Move Helper Functions ==================
	
	private void putMoveInLog(Move m) {
		pastMoves[count] = m;
		count++;
	}


	// =============== Get Legal Moves Helper Functions ============
	private List<Move> allPossibleMoves(SubBoard[] boards){
		List<Move> moves = new ArrayList<>();
		
		for (int i = 0; i < boards.length; i++) {
				moves.addAll(getLegalMoves(i));
			}

		return moves;
	}

	/**
	 * Generates the potential moves for a particular SubBoard index. Returns a
	 * bitboard representation of the available moves.
	 **/
	private List<Move> getLegalMoves(int subBoardIndex) {
		// generate a bitboard for the specified SubBoard
		// a 1 represents a potential move
		int bb = boards[subBoardIndex].generateMoves();
		List<Move> moves = new ArrayList<>();

		while (bb != 0) {
			int move = bb & -bb;
			bb &= (bb - 1);
			moves.add(new Move(move, subBoardIndex));
		}

		return moves;
	}

	// ========== Update BitBoard Helper Function =============

	private void updateInProgressBitBoards (int board){
		xWinBoards &= Constants.CLRBIT[board];
		oWinBoards &= Constants.CLRBIT[board];
		drawnBoards &= Constants.CLRBIT[board];
	}

	private void updateDrawnBitBoards (int board){
		drawnBoards |= Constants.BIT_MASKS[board];
	}

	private void updateXWonBitBoards (int board){
		xWinBoards |= Constants.BIT_MASKS[board];
	}

	private void updateOWonBitBoards (int board){
		oWinBoards |= Constants.BIT_MASKS[board];
	}
}
