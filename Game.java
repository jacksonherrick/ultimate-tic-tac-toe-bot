import java.util.HashSet;
import java.util.Scanner;

public class Game{

	private int currentPlayer;
	private int xBoard;
	private int oBoard;


	public boolean isOver(){
		return true;
	}

	public boolean weWon(){
		return true;
	}

	//returns a set of available moves
	public MoveAndValue [] getAvailableMoves(){
		return new MoveAndValue [5];
	}

	public boolean isTied(){
		return true;
	}

	//places the mark on the board
	public void makeMove(int move){
		//write the code to actually place the 1 on the board

		changeTurns();
	}

	//used after we recurse through the tree to reset the board to how it was originally
	public void undoMove(int move){
		changeTurns();
	}

	public void changeTurns(){
		//not sure how we're encoding the current player yet, but flip the current player
	}


	public void sortMoves(MoveAndValue [] moves){
		//sorts the possible moves into order from best to worst
		int maxIndex = 0;
		for(int i =0; i< moves.length-1; i++){
			for(int j =i; j<moves.length; j++){
				if(moves[j].value > moves[maxIndex].value){
					maxIndex = j;
				}
			}
			MoveAndValue temp = moves[i];
			moves[i] = moves[maxIndex];
			moves[maxIndex] = moves[i];
		}
	}

	public void printBoard(){

	}



//Evaluation functions
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	public int evaluate(int depth){
		//returns the value of the current board
		return littleBoardsWon() + bigBoardsWon() + twoInARows();
	}

	public int littleBoardsWon(){
		return -1;
	}

	public int bigBoardsWon(){
		return -1;
	}

	public int twoInARows(){
		return -1;
	}

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
				b.makeMove(m);
			} else {
				System.out.println("Sorry, please enter a move or command.");
			}
		}
		System.out.println("Goodbye. As a side note, Stiven's a scrub.");
	}



}
