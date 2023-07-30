package com.jherrick;

import java.util.Scanner;

public class GameImpl implements Game {


	// ============== Instance variables ================
	private final BigBoard board;
	private final Agent xAgent;
	private final Agent oAgent;
	private final Side turn;

	private static Scanner reader;


	// ================== Constructors ==================

	public GameImpl(BigBoard board, Agent xAgent, Agent oAgent) {
		this.board = board;
		this.xAgent = xAgent;
		this.oAgent = oAgent;
		this.turn = Side.X;
	}

	
	// ================== Main Method ===================

	public static void initAndPlayGame() {
		
		reader = new Scanner(System.in);
		
		BigBoard b = askForCustomBoard(reader);


		// Only can assign CPU to X. TODO: Create functionality to choose if X or O or neither is bot
		Agent [] agents = assignAgentType(reader, b);

		Game game = new GameImpl(b, agents[0], agents[1]);
		// use the inputed board
		game.play();
		
		// close scanner
		reader.close();
	}


	@Override
	public void play() {
		boolean gameOver = false;
		while(!gameOver){
			System.out.println(board);
			Move nextMove = getNextMove();
			this.board.makeMove(nextMove);
			gameOver = this.board.getBoardState() != BoardState.IN_PROGRESS; 
		}
		// TODO: Fix bug, no Game Over
		System.out.println("Game Over!");
	}




	// ================ Helper Functions =================

	// ======== Move Generation Helper Functions =========
	
	private Move getNextMove() {
		if(this.turn.equals(Side.X)){
			return xAgent.pickMove(board);
		}
		else return oAgent.pickMove(board);
	}

	// ======= HCN Custom Board Helper Functions ============

	private static BigBoard askForCustomBoard(Scanner reader){
		BigBoard b = new BigBoard();
		
		// Get input
		System.out.println("Welcome. Do you want a custom position?");
		String s = reader.nextLine();
		if (s.equals("yes")) {
			b = inputCustomBoard();
		}

		return b;
	}

	// input a particular board to play
	private static BigBoard inputCustomBoard() {
		System.out.println("Please enter the HCN string for the custom board.");
		String s = reader.nextLine();
		BigBoard b = new BigBoard(s);
		return b;
	}


	// ============ Assign Agent Type Helper Functions ===============

	private static Agent[] assignAgentType(Scanner reader , BigBoard b){
	
	Agent[] agents = new Agent[2];

	// Get X Agent Assignment from player
	System.out.println("Would you like a CPU to play as X? (Y/N)");
	String s = reader.nextLine();

	if (s.equals("Y")){
		BasicBoardEvaluator board_eval = new BasicBoardEvaluator(b, Side.X);
		agents[0] = new NegaMaxAgent(board_eval, Side.X);
	}
	else {
		agents[0] = new ConsolePlayerAgent(reader);
	}

	// Get O Agent Assignment from player - BUG: Only X can play as bot?
	System.out.println("Would you like a CPU to play as O? (Y/N)");
	s = reader.nextLine();

	if (s.equals("Y")){
		BasicBoardEvaluator board_eval = new BasicBoardEvaluator(b, Side.O);
		agents[1] = new NegaMaxAgent(board_eval, Side.O);
	}
	else {
		agents[1] = new ConsolePlayerAgent(reader);
	}

	return agents;
}



private void gameFlowToBeUpdated(){


//		// alert the player
//		System.out.println(
//				"Type the coordinates of your first move. Type \"exit\" to, well, you figure it out. Likewise for \"undo\".");
//
//		// initialize move
//		Move m = new Move(0, 0);
//
//		// continuously wait for input
//		while (true) {
//			System.out.println(b);
//			System.out.print("  X: " + Integer.toBinaryString(b.xWinBoards));
//			System.out.print(", O: " + Integer.toBinaryString(b.oWinBoards));
//			System.out.print(", Draw: " + Integer.toBinaryString(b.drawnBoards));
//			System.out.println();
//			String s = reader.nextLine();
//			if (s.equals("quit") || s.equals("stop") || s.equals("exit")) {
//				break;
//			} else if (s.equals("undo")) {
//				b.takeMove(m);
//			} else if (s.matches("^\\s*[a-i][1-9]\\s*$")) {
//				m = Utils.coordinatesToMove(s);
//				// checks if the move is on top of another move
//				if (((b.boards[m.board].getXBoard() | b.boards[m.board].getOBoard()) & m.move) == 0) {
//					// checks if the move is in the correct sub-board (it's not the first move or we
//					// are in the sub-board dictated by the lat move or if that board is not in
//					// progress)
//					if (b.getLastMove() == null || m.board == b.getLastMove().translate()
//							|| b.boards[b.getLastMove().translate()].getState() != BoardState.IN_PROGRESS) {
//						b.makeMove(m);
//					} else {
//						System.out.println("Sorry, please move in the board corresponding to the last move");
//					}
//				} else {
//					System.out.println("Sorry, that position is already taken. Please enter another move");
//				}
//			} else {
//				System.out.println("Sorry, please enter a move or command.");
//			}
//		}
//		System.out.println("Goodbye. As a side note, Stiven's a scrub.");
//	}
	}



}
