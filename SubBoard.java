public class SubBoard{
  private int xBoard;
  private int oBoard;
  private BoardState state;

  // constructor to create an empto board
  public SubBoard() {
    this(0, 0);
	}

  // constructor to create a board from parameters (x and o bitboards)
  public SubBoard(int _xBoard, int _oBoard) {
    xBoard = _xBoard;
    oBoard = _oBoard;
  }

  public BoardState getState() {
    return state;
  }

  /**
    * Makes the specified move, and checks for win/draw conditions
    * Take as input a 9-bit integer and the side to move
    **/
  public void makeMove(int m, Side side) {
    if(side == Side.X) {
      xBoard |= m;
    } else {
      oBoard |= m;
    }
    // TODO: is there a way to not check this every time? Maybe, maybe not.
    checkConditions();
  }

  /**
    * Takes back the specified move, and checks for win/draw conditions if applicable
    * Take as input a 9-bit integer and the side to move
    * NOTE: takeMove() does not have to check win/draw if neither were true
    * NOTE: passing a side is unecessary, since a bit cleared regardless
    **/
  public void takeMove(int m) {
    xBoard &= ~m;
    oBoard &= ~m;

    // TODO: make it so this does not check every time - only if makeMove caused a state change
    checkConditions();
  }

  /**
    * Checks if the board is drawn or won
    **/
  public void checkConditions() {
    if(!isWin() && !isDraw()) {
      state = BoardState.IN_PROGRESS;
    }
  }

  /**
    * Checks if either X or O has won. Updates board state accordingly.
    **/
  public boolean isWin() {
    // TODO: must perform maximum 16 operations to check for win. Consider look-up table.
    for(int bb : Constants.WIN_BITBOARDS) {
      if((bb & xBoard) == bb) {
        if(state == BoardState.IN_PROGRESS) {
            state = BoardState.X_WON;
        }
        return true;
      } else if((bb & oBoard) == bb) {
        if(state == BoardState.IN_PROGRESS) {
          state = BoardState.O_WON;
        }
        return true;
      }
    }
    return false;
  }

  /**
    * Checks if the game is drawn i.e. no more possible moves. Updates board state.
    * NOTE: isWon() must always be called before isDrawn()
    **/
  public boolean isDraw() {
    if(~(xBoard | oBoard) == 0) {
      state = BoardState.DRAWN;
      return true;
    }
    return false;
  }

  /**
    * Returns a bitboard representing possible moves.
    **/
  public int generateMoves() {
    return ~(xBoard | oBoard) & 0x1FF;
  }

  /**
    * Converts the SubBoard into an array representation (array of 3 strings)
    **/
  public String[] toArrayRepresentation() {
    return this.toString().split("\\n");
  }

  /**
    * @override the toString() method
    * converts the SubBoard into a string representation
    **/
  public String toString() {
    String xString = Integer.toBinaryString(xBoard);
    String oString = Integer.toBinaryString(oBoard);
    StringBuilder result = new StringBuilder();

    for(int i = 8; i > -1; i--) {
      // mask each spot and check for X's and O's
      int mask = (int)Math.pow(2, i);
      result.append("[");
      if((mask & xBoard) > 0) {
        result.append((char)Constants.X_ASCII);
      } else if ((mask & oBoard) > 0) {
        result.append((char)Constants.O_ASCII);
      } else {
        result.append(" ");
      }
      result.append("]");
      if(i % 3 == 0 && i > 0) result.append("\n");
    }
    return result.toString();
  }
}
