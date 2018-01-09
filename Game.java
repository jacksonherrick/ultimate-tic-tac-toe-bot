import java.util.HashSet;
import java.util.Scanner;

public class Game{

	// main method
	public static void main(String[] args) {

		Scanner reader = new Scanner(System.in);

		// set up the board
		Board b = new Board();

		System.out.println("Welcome. Do you want a custom position?");
		String s = reader.nextLine();
		if(s.equals("yes")) {
			b = inputCustomBoard();
		}
		play(b);
	}

	// input a particular board to play
	public static Board inputCustomBoard() {

		Scanner reader = new Scanner(System.in);

		System.out.println("Please enter the HCN string for the custom board.");

		String s = reader.nextLine();
		Board b = new Board(s);
		return b;

	}

	// play position from a board, player controls all moves
	public static void play(Board b) {

		Scanner reader = new Scanner(System.in);

		// alert the player
		System.out.println("Type the coordinates of your first move. Type \"exit\" to, well, you figure it out. Likewise for \"undo\".");

		// initialize move
		Move m = new Move(0, 0);

		// continuosly wait for input
		while(true) {
			System.out.println(b);
			System.out.print("X: " + Integer.toBinaryString(b.xWinBoards));
			System.out.print(" O: " + Integer.toBinaryString(b.oWinBoards));
			System.out.print(" Draw: " + Integer.toBinaryString(b.drawnBoards));
			System.out.println();
			String s = reader.nextLine();
			if(s.equals("quit") || s.equals("stop") || s.equals("exit")) {
				break;
			} else if(s.equals("undo")) {
				b.takeMove(m);
			} else if(s.matches("^\\s*[a-i][1-9]\\s*$")){
				m = Utils.coordinatesToMove(s);
				b.makeMove(m);
			} else {
				System.out.println("Sorry, please enter a move or command.");
			}
		}
		System.out.println("Goodbye. As a side note, Stiven's a scrub.");
	}
}
