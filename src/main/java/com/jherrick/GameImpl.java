package com.jherrick;

import java.util.Scanner;

public class GameImpl implements Game {

	private final Board board;
	private final Agent xAgent;
	private final Agent oAgent;
	private final Side turn;

	private static Scanner reader;

	public GameImpl(Board board, Agent xAgent, Agent oAgent) {
		this.board = board;
		this.xAgent = xAgent;
		this.oAgent = oAgent;
		this.turn = Side.X;
	}

	// main method
	public static void initAndPlayGame() {
		Move m1 = Utils.coordinatesToMove("e5");
		Move m2 = Utils.coordinatesToMove("e5");
		System.out.println(m1.equals(m2));
		reader = new Scanner(System.in);

		// set up the board
		BigBoard b = new BigBoard();
		
		// get input
		System.out.println("Welcome. Do you want a custom position?");
		String s = reader.nextLine();
		if (s.equals("yes")) {
			b = inputCustomBoard();
		}

		Agent xAgent = new ConsolePlayerAgent(reader);
		Agent oAgent = new ConsolePlayerAgent(reader);

		Game game = new GameImpl(b, xAgent, oAgent);
		// use the inputed board
		game.play();
		
		// close scanner
		reader.close();
	}

	// input a particular board to play
	public static BigBoard inputCustomBoard() {
		System.out.println("Please enter the HCN string for the custom board.");
		String s = reader.nextLine();
		BigBoard b = new BigBoard(s);
		return b;
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
		System.out.println("Game Over!");
	}

	private Move getNextMove() {
		if(this.turn.equals(Side.X)){
			return xAgent.pickMove(board);
		}
		else return oAgent.pickMove(board);
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
