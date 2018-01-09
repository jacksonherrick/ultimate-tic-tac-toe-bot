import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Board{
  // array to hold SubBoard instances
  SubBoard[] boards;

  // bitboards used to check for wins and draws
  int xWinBoards;
  int oWinBoards;
  int drawnBoards;

  // represents which player is to move
  Side side;

  // state of the board
  BoardState state;

  // keeps track of the last move to constrain move generation. Might have to make this an array of all previous moves
  Move [] pastMoves;
  int count;

  // Evaluation constants to make the evaluation function easier to read
  private int c0 = Constants.EVAL_CONSTANTS[0];
  private int c1 = Constants.EVAL_CONSTANTS[1];

  /**
    * Default constructor, creates a Board of 9 empty SubBoards
    * Initializes win/draw boards
    **/
  public Board() {
    // initialize empty SubBoards
    boards = new SubBoard[9];
    for(int i = 0; i < 9; i++) {
      boards[i] = new SubBoard();
    }

    // initialize bitboards to zero
    xWinBoards = 0;
    oWinBoards = 0;
    drawnBoards = 0;

    // default side to X
    side = Side.X;

    // default BoardState
    state = BoardState.IN_PROGRESS;

    pastMoves = new Move [81];
    count = 0;
	}

  /**
    * Overloaded constructor. Creates a Board of 9 specified SubBoards.
    * Fills from top left to bottom right.
    **/
  public Board(SubBoard[] allSubBoards) {
    for(int i = 0; i < allSubBoards.length; i++) {
      boards[i] = allSubBoards[i];
      boards[i].checkConditions();
      updateStateBitboards(i);
    }
  }


  /**
    * Overloaded constructor. Creates a Board from a Herrick-Corley Notation string.
    * HCN notation lists the SubBoards using "X", "O", and 1-9, starting from top left.
    **/
  public Board(String hcn) {

    // compile the regex to match HCN's
    Pattern r = Pattern.compile("^([XO1-9]{0,9})\\/([XO1-9]{0,9})\\/([XO1-9]{0,9})\\/([XO1-9]{0,9})\\/([XO1-9]{0,9})\\/([XO1-9]{0,9})\\/([XO1-9]{0,9})\\/([XO1-9]{0,9})\\/([XO1-9]{0,9})\\s([XO]){1}\\s([a-zA-Z0-9]{2})$");

    // match the HCN
    Matcher m = r.matcher(hcn);
    if(m.matches()) {
      // loop through matches
      for(int i = 1; i < 12; i++) {
        if(i < 10) {
          boards[i] = new SubBoard();
          continue;
        } else if(i == 10) {
          side = m.group(i).equals("X") ? Side.X : Side.O;
        } else {
          pastMoves[0] = Utils.coordinatesToMove(m.group(i));
          count = 1;
        }
      }
    } else {
      System.out.println("Invalid HCN.");
    }



  }
  /**
    * Toggles which side is to move
    **/
  private void toggleSide() {
    side = side == Side.X ? Side.O : Side.X;
  }

  /**
    Generates the potential moves for all locations on the current board.
    Returns a bitboard representation of the available moves.
    **/
  public List<Move> generateMoves() {
    List<Move> moves = new ArrayList<>();
    // TODO: perhaps this is not the most efficient, concatting ArrayLists...

    // If the SubBoard we are sending the other user to is in progess, only generate moves in that SubBoard
    if((count != 0) && boards[pastMoves[count].translate()].getState() == BoardState.IN_PROGRESS){
      moves.addAll(generateMoves(pastMoves[count].board));
      return moves;
    }

    // If it has been won or is dead, generate moves for everywhere
    else{
      for(int i = 0; i < boards.length; i++) {
        moves.addAll(generateMoves(i));
      }
      return moves;
    }
  }

  /**
    * Generates the potential moves for a particular board.
    * Returns a bitboard representation of the available moves.
    **/
  public List<Move> generateMoves(int board) {
    // generate a bitboard for the specified board
    // a 1 represents a potential move
    int bb = boards[board].generateMoves();
    List<Move> moves = new ArrayList<>();

    while(bb != 0) {
      int move = bb & -bb;
      bb &= (bb-1);
      moves.add(new Move(move, board));
      System.out.println(new Move(move, board));
    }

    return moves;
  }

  /**
    * Updates board state bitboards
    * Takes as input the board index
    * TODO: any way to make this cleaner? ugly ugly.
    **/
  public void updateStateBitboards(int board) {
    BoardState s = boards[board].getState();
    System.out.println("S: " + s);
    if(s == BoardState.IN_PROGRESS) {
      xWinBoards &= Constants.CLRBIT[8 - board];
      oWinBoards &= Constants.CLRBIT[8 - board];
      drawnBoards &= Constants.CLRBIT[8 - board];
    } else if (s == BoardState.DRAWN) {
      drawnBoards |= Constants.BIT_MASKS[8 - board];
    } else if (s == BoardState.X_WON) {
      xWinBoards |= Constants.BIT_MASKS[8 - board];
    } else if (s == BoardState.O_WON) {
      oWinBoards |= Constants.BIT_MASKS[8 - board];
    } else if(Constants.REPORTING_LEVEL > 1){
      throw new java.lang.RuntimeException("Unknown BoardState encountered in updateStateBitboards()");
    }
  }

  /**
    * Makes the move (passed as an argument)
    * Also triggers win/draw checks
    * TODO: make sure that legality check is called for player-entered moves (check there, not here)
    **/
  public void makeMove(Move m) {
    boards[m.board].makeMove(m.move, side);
    updateStateBitboards(m.board);
    toggleSide();
    pastMoves[count] = m;
    count++;
  }

  /**
    * Takes back the move (passed as an argument)
    * Also triggers win/draw checks
    **/
  public void takeMove(Move m) {
    boards[m.board].takeMove(m.move);
    updateStateBitboards(m.board);
    toggleSide();
    count--;
    pastMoves[count] = null;
  }

  /**
    Returns true if a player has won on the board
    * TODO: this check only needs to be done after many, many moves
    * TODO: almost identical to SubBoard function. Hmmm... inheritance?
    * TODO: Hash Map!
    **/
  public boolean isWon() {
    for(int bb : Constants.WIN_BITBOARDS) {
        if((bb & xWinBoards) == bb) {
          if(state == BoardState.IN_PROGRESS) {
              state = BoardState.X_WON;
          }
          return true;
        } else if((bb & oWinBoards) == bb) {
          if(state == BoardState.IN_PROGRESS) {
            state = BoardState.O_WON;
          }
          return true;
        }
      }
      return false;
  }

  /**
    Returns true if the the board is drawn (no available moves)
    * TODO: this check only needs to be done after many, many moves
    **/
  public boolean isDrawn() {
    // Yeah... so... this needs to be built!
    return false;
  }

  public int evaluate(){
    if(state == BoardState.X_WON){
      return Integer.MAX_VALUE;
    }
    if(state == BoardState.O_WON){
      return Integer.MIN_VALUE;
    }
    int score = 0;
    for(int i =0; i<boards.length; i++){
      score += Eval.getValues().get(boards[i]);
    }
    return 10*c0*Eval.twoInARowsWithOpenThird(xWinBoards, (oWinBoards | drawnBoards)) + 10*c1*Eval.middleSquare(xWinBoards) - 10*c0*Eval.twoInARowsWithOpenThird(oWinBoards, (xWinBoards | drawnBoards)) - 10*c1*Eval.middleSquare(oWinBoards)+ score;
  }


  // @override toString() method
  public String toString() {
    StringBuilder result = new StringBuilder();
    String spacer = "  +---------+---------+---------+\n";
    result.append(spacer);
    int counter = 9;
    for(int j = 0; j < 9; j+=3) {
      String[] b1 = boards[j].toArrayRepresentation();
      String[] b2 = boards[j+1].toArrayRepresentation();
      String[] b3 = boards[j+2].toArrayRepresentation();

      for(int i = 0; i < 3; i++) {
        result.append(counter + " " + "|" + b1[i] + "|" + b2[i] + "|" + b3[i] + "|\n");
        counter--;
      }
      result.append(spacer);
    }
    String bottomKey = String.format(
      "%3s a  b  c %1s d  e  f %1s g  h  i \n", " ", " ", " "
    );
    result.append(bottomKey);
    return result.toString();
  }
}
