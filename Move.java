public class Move{
  public int move;
  public int board;

  public Move(int _move, int _board){
  	move = _move;
    board = _board;
  }

  /**
    * @overide the toString() method
    * converts integer move into readable representation
    * displays bitboard of move for sub-board, algebraic coordinates, and sub-board number
    **/
  public String toString() {
    StringBuilder result = new StringBuilder();
    String s = Integer.toBinaryString(move);

    // calculate row and column
    int index = Utils.lowestSetBit(move);
    int row = (2 - board / 3) * 3 + (index / 3) + 1;
    int col = (board % 3) * 3 + (2 - index % 3) + 97;

    // print basic information
    result.append("Board #: " + board + ", Square: " + Character.toString((char)col) + row
      + "\n");

    // print representation of the move in a 3x3 grid - optional
    if(Constants.PRINT_DETAIL > 1) {
      result.append("+---+\n|");
      int count = 0;
      for(int i = 8; i > s.length() - 1; i--) {
        result.append("0");
        count++;
        if(count % 3 == 0) result.append("|\n|");
      }
      for(int i = 0; i < s.length(); i++) {
        result.append(s.charAt(i));
        count++;
        if(count % 3 == 0) {
          result.append("|\n");
          if(count < 9) {
            result.append("|");
          }
        }
      }
      result.append("+---+\n");
    }



    return result.toString();
  }
}
