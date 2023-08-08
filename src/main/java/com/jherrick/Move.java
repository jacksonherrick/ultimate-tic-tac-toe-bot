package com.jherrick;

public class Move {

  public int move;
  public int board;

  public Move(int _move, int _board) {
    move = _move;
    board = _board;
  }

  // ========== Public Functions ==========

  // Gets the SubBoard that we are sending the next player to based on the last
  // move. Can make this more efficient - make a hashMap of translations
  public int getSubBoardTarget() {
    for (int i = 0; i < Constants.BIT_MASKS.length; i++) {
      if (isSubBoardTargetNumber(i))
        return i;
    }
    return -1;
  }

  /**
   * @override the toString() method
   *           converts integer move into readable representation
   *           displays bitboard of move for sub-board, algebraic coordinates, and
   *           sub-board number
   **/
  public String toString() {
    StringBuilder result = new StringBuilder();

    // calculate row and column
    int[] rowCol = calculateRowandColforString();

    // print basic information
    result.append(Character.toString((char) rowCol[1]) + rowCol[0]);
    return result.toString();
  }

  @Override
  public boolean equals(Object otherObject) {
    // null check
    if (otherObject == null) {
      return false;
    }
    // self check
    if (this == otherObject) {
      return true;
    }
    // type check
    if (getClass() != otherObject.getClass()) {
      return false;
    }
    // cast
    Move otherMove = (Move) otherObject;
    // field comparison
    return this.move == otherMove.move && this.board == otherMove.board;

  }

  // ========== Helper Functions ==========

  // ========== Get SubBoard Target Helper Functions ==========
  private boolean isSubBoardTargetNumber(int possibleSubBoard) {
    return (move & Constants.BIT_MASKS[possibleSubBoard]) == Constants.BIT_MASKS[possibleSubBoard];
  }

  // ========== To String Helper Functions ==========

  private int[] calculateRowandColforString() {
    int index = Utils.lowestSetBit(move);
    int row = (2 - board / 3) * 3 + (index / 3) + 1;
    int col = (board % 3) * 3 + (2 - index % 3) + 97;

    int[] rowCol = new int[] { row, col };
    return rowCol;
  }
}
