import java.util.HashSet;



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
	public Move [] getAvailableMoves(){
		return new Move [5];
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


	public void sortMoves(Move [] moves){
		//sorts the possible moves into order from best to worst
		int maxIndex = 0;
		for(int i =0; i< moves.length-1; i++){
			for(int j =i; j<moves.length; j++){
				if(moves[j].value > moves[maxIndex].value){
					maxIndex = j;
				}
			}
			Move temp = moves[i];
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



}






























