package com.jherrick;

import java.util.Scanner;

public class Game {

	private static Scanner reader;

	// main method
	public static void initAndPlayGame() {
		
		reader = new Scanner(System.in);

		// set up the board
		BigBoard b = new BigBoard();
		
		// get input
		System.out.println("Welcome. Do you want a custom position?");
		String s = reader.nextLine();
		if (s.equals("yes")) {
			b = inputCustomBoard();
		}
		
		// use the inputed board
		play(b);
		
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

	// play position from a board, player controls all moves
	public static void play(BigBoard b) {
		
		// alert the player
		System.out.println(
				"Type the coordinates of your first move. Type \"exit\" to, well, you figure it out. Likewise for \"undo\".");

		// initialize move
		Move m = new Move(0, 0);

		// continuously wait for input
		while (true) {
			System.out.println(b);
			System.out.print("  X: " + Integer.toBinaryString(b.xWinBoards));
			System.out.print(", O: " + Integer.toBinaryString(b.oWinBoards));
			System.out.print(", Draw: " + Integer.toBinaryString(b.drawnBoards));
			System.out.println();
			String s = reader.nextLine();
			if (s.equals("quit") || s.equals("stop") || s.equals("exit")) {
				break;
			} else if (s.equals("undo")) {
				b.takeMove(m);
			} else if (s.matches("^\\s*[a-i][1-9]\\s*$")) {
				m = Utils.coordinatesToMove(s);
				// checks if the move is on top of another move
				if (((b.boards[m.board].getXBoard() | b.boards[m.board].getOBoard()) & m.move) == 0) {
					// checks if the move is in the correct sub-board (it's not the first move or we
					// are in the sub-board dictated by the lat move or if that board is not in
					// progress)
					if (b.getLastMove() == null || m.board == b.getLastMove().translate()
							|| b.boards[b.getLastMove().translate()].getState() != BoardState.IN_PROGRESS) {
						b.makeMove(m);
					} else {
						System.out.println("Sorry, please move in the board corresponding to the last move");
					}
				} else {
					System.out.println("Sorry, that position is already taken. Please enter another move");
				}
			} else {
				System.out.println("Sorry, please enter a move or command.");
			}
		}
		System.out.println("Goodbye. As a side note, Stiven's a scrub.");
	}
}
