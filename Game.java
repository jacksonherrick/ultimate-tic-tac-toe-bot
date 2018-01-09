import java.util.HashSet;
import java.util.Scanner;

public class Game{

	// main method
	public static void main(String[] args) {
		// set up the board
		Board b = new Board();

		// alert the player
		Scanner reader = new Scanner(System.in);
		System.out.println("Welcome. Type the coordinates of your first move. Type \"exit\" to, well, you figure it out. Likewise for \"undo\".");

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
				// checks if the move is on top of another move
				if(((b.boards[m.board].getxBoard() | b.boards[m.board].getoBoard()) & m.move) == 0){
					// checks if the move is in the correct sub-board (it's not the first move or we are in the sub-board dictated by the lat move or if that board is not in progress)
					if(b.getLastMove() == null || m.board == b.getLastMove().translate() || b.boards[b.getLastMove().translate()].getState() != BoardState.IN_PROGRESS){
						b.makeMove(m);
					}
					else{
						System.out.println("Sorry, please move in the board corresponding to the last move");
					}
				}
				else{
					System.out.println("Sorry, that position is already taken. Please enter another move");
				}
			} else {
				System.out.println("Sorry, please enter a move or command.");
			}
		}
		System.out.println("Goodbye. As a side note, Stiven's a scrub.");
	}



}
