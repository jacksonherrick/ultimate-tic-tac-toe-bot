package main;

import java.util.Scanner;
import java.io.IOException;

public class Game {

	private static Scanner reader;

	// main method
	public static void main(String[] args) {
		
		boolean playVsCPU = false;
		reader = new Scanner(System.in);

		// set up the board
		Board b = new Board();
		
		// get input
		System.out.println("Welcome. Custom starting position y/n?");
		String s = reader.nextLine();
		if (s.equals("y")) {
			b = inputCustomBoard();
		}
		
		System.out.println("AI game y/n?");
		String t = reader.nextLine();
		if(t.equals("y")) {
			playVsCPU = true;
		}
		// use the inputed board
		play(b, playVsCPU);
		
		// close scanner
		reader.close();
	}

	// input a particular board to play
	public static Board inputCustomBoard() {
		System.out.println("Please enter the HCN string for the custom board.");
		String s = reader.nextLine();
		Board b = new Board(s);
		return b;
	}

	// play position from a board, player controls all moves
	public static void play(Board b, boolean playingVsCPU){
		
		if(playingVsCPU) {
			// we can definitely change this later, it's just how the NegaMax is set up for now
			System.out.println("The AI will be playing as X and you will be playing as O");
			Eval.findValues();
		}
		// alert the player
		System.out.println(
				"Type the coordinates of your first move. Type \"exit\" to, well, you figure it out. Likewise for \"undo\".");

		// initialize move
		Move m = new Move(0, 0);

		// continuously wait for input
		while (true) {
			if(playingVsCPU) {
				if(b.side == Side.X) {
					makeCPUMove(b);
				}
			}
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
					// are in the sub-board dictated by the last move or if that board is not in
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
	
	public static void makeCPUMove(Board b) {
		System.out.println(b);
		System.out.println("AI move: ");
		b.makeMove(NegaMax.nextMove(b));
	}
}
