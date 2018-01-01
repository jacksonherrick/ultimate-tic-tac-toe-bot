import java.util.HashMap;
import java.util.HashSet;


//NOTE: I am assuming the bot is always playing X for simplicity's sake right now
public class Eval{
	private static HashMap<SubBoard, Integer> values = new HashMap<SubBoard, Integer>();
	private static HashSet<SubBoard> possibleBoards = new HashSet<SubBoard>();
	private static int c1 = 1;
	private static int c2 = 1;
	private static int c3 = 100;

	//Boards that have two in a rows
	private static final int[] TWOINAROWS = {0x180, 0xC0, 0x30, 0x18, 0x6, 0x3, 0x120, 0x24, 0x90, 0x12, 0x48, 0x9, 0x110, 0x11, 0x50, 0x14};
	//The elements in OPENTHIRDS correspond to the third squares that have to be open for each element of TWOINAROWS to have a chance of getting a win
	private static final int[] OPENTHIRDS = {0x40, 0x100, 0x8, 0x20, 0x1, 0x4, 0x4, 0x100, 0x2, 0x80, 0x1, 0x40, 0x1, 0x100, 0x4, 0x40};



	public static void setConstants(int constant1, int constant2, int constant3){
		c1 = constant1;
		c2 = constant2;
		c3 = constant3;
	}

	//Evaluates the values of all possible small boards given the constants 
	//Used to save values for each of the board states at the start of the game to speed up the negamax. 
	//This method will find all the values of each state for X, so when we use it in the negamax, we have to take the inverse for O moves. Or we can have findValues take an argument for the side
	public static void findValues(){
		for(SubBoard b : possibleBoards){
			values.put(b, evaluate(b));
		}
	}

	public static HashMap<SubBoard, Integer> getValues(){
		return values;
	}


	public static int evaluate(SubBoard board){
		if(board.getState() == BoardState.X_WON){
			return c3;
		}
		if(board.getState() == BoardState.O_WON){
			return -1*c3;
		}
		return c1 * twoInARowsWithOpenThird(board, Side.X) + c2 * middleSquare(board, Side.X) - c1*twoInARowsWithOpenThird(board,Side.O) - c2*middleSquare(board, Side.O);
	}

	//Call this function when evaluating a board in the negamax
	public static int evaluate(Board board){
		if(board.state == BoardState.X_WON){
			return 10*c3;
		}
		int score = 0;
		for(int i =0; i<board.boards.length; i++){
			score += values.get(board.boards[i]);
		}
		return 10*c1*twoInARowsWithOpenThird(board, Side.X) + 10*c2*middleSquare(board, Side.X) - 10*c1*twoInARowsWithOpenThird(board, Side.O) - 10*c2*middleSquare(board, Side.O)+ score;
	}


	//Helper Functions---------------------------------------------------------------------------------------------------------------------------------------------------------------------


	//For Boards
	private static int twoInARowsWithOpenThird(Board board, Side s){
		int count =0;
		if(s == Side.X){
			for(int i=0; i<TWOINAROWS.length; i++){
				if(((board.xWinBoards & TWOINAROWS[i]) == TWOINAROWS[i]) && ((board.oWinBoards & OPENTHIRDS[i]) == 0) && ((board.drawnBoards & OPENTHIRDS[i]) == 0)){
					count++;
				}
			}
		}

		if(s== Side.O){
			for(int i=0; i<TWOINAROWS.length; i++){
				if(((board.oWinBoards & TWOINAROWS[i]) == TWOINAROWS[i]) && ((board.xWinBoards & OPENTHIRDS[i]) == 0) && ((board.drawnBoards & OPENTHIRDS[i]) == 0)){
					count++;
				}
			}
		}
		return count;
	}

	//For SubBoards
	private static int twoInARowsWithOpenThird(SubBoard board, Side s){
		int count =0;
		if(s == Side.X){
			for(int i=0; i<TWOINAROWS.length; i++){
				if(((board.getxBoard() & TWOINAROWS[i]) == TWOINAROWS[i]) && ((board.getoBoard() & OPENTHIRDS[i]) == 0)){
					count++;
				}
			}
		}

		if(s== Side.O){
			for(int i=0; i<TWOINAROWS.length; i++){
				if(((board.getoBoard() & TWOINAROWS[i]) == TWOINAROWS[i]) && ((board.getxBoard() & OPENTHIRDS[i]) == 0)){
					count++;
				}
			}
		}
		return count;
	}

	//For SubBoards
	private static int middleSquare(Board board, Side s){
	if(s == Side.X){
			if((board.xWinBoards & 0x10) == 0x10){
				return 1;
			}	
		}
		if(s == Side.O){
			if((board.oWinBoards & 0x10) == 0x10){
				return 1;
			}	
		}
		
	return 0;
	}

	//For Boards
	private static int middleSquare(SubBoard board, Side s){
		if(s == Side.X){
			if((board.getxBoard() & 0x10) == 0x10){
				return 1;
			}	
		}
		if(s == Side.O){
			if((board.getoBoard() & 0x10) == 0x10){
				return 1;
			}	
		}
		
	return 0;
	}


}