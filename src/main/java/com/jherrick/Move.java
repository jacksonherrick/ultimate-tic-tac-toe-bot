package com.jherrick;

public class Move{
  public int move;
  public int board;

  public Move(int _move, int _board){
  	move = _move;
    board = _board;
  }

  // Gets the SubBoard that we are sending the next player to based on the last move. Can make this more efficient - make a hashMap of translations
  public int translate(){
    for(int i=0; i < Constants.BIT_MASKS.length; i++){
      if((move & Constants.BIT_MASKS[i]) == Constants.BIT_MASKS[i])
        return i;
    }
    return -1;
  }

  /**
    * @override the toString() method
    * converts integer move into readable representation
    * displays bitboard of move for sub-board, algebraic coordinates, and sub-board number
    **/
  public String toString() {
    StringBuilder result = new StringBuilder();

    // calculate row and column
    int index = Utils.lowestSetBit(move);
    int row = (2 - board / 3) * 3 + (index / 3) + 1;
    int col = (board % 3) * 3 + (2 - index % 3) + 97;

    // print basic information
    result.append(Character.toString((char)col) + row);
    return result.toString();
  }
}
