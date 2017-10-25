import java.util.HashSet;



public class Game{

	private int currentPlayer;

	public boolean isOver(){
		return true;
	}

	public boolean weWon(){
		return true;
	}

	//returns a set of available moves
	public int [] getAvailableMoves(){
		return new int[5];
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
}