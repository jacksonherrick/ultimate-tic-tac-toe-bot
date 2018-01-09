import java.util.HashMap;
import java.util.Map;

public final class Utils {

  private Utils() {}

  private static final int MultiplyDeBruijnBitPosition[] = new int[] { 0, 9,
          1, 10, 13, 21, 2, 29, 11, 14, 16, 18, 22, 25, 3, 30, 8, 12, 20, 28,
          15, 17, 24, 7, 19, 27, 23, 6, 26, 5, 4, 31 };

  /**
    * finds the index of the highest set bit of the passed integer
    **/
  public static int highestSetBit(int v) {
   v |= v >>> 1;
   v |= v >>> 2;
   v |= v >>> 4;
   v |= v >>> 8;
   v |= v >>> 16;
   return MultiplyDeBruijnBitPosition[(int)((v * 0x07C4ACDDL) >> 27) & 31];
  }

  /**
    * finds the index of the lowest set bit of the passed integer
    **/
  public static int lowestSetBit(int v) {
    if(v == 0) return -1;
    int c = 32; // number of 0 bits on the right
    v &= -v;
    if (v != 0) c--;
    if ((v & 0x0000FFFF) != 0) c -= 16;
    if ((v & 0x00FF00FF) != 0) c -= 8;
    if ((v & 0x0F0F0F0F) != 0) c -= 4;
    if ((v & 0x33333333) != 0) c -= 2;
    if ((v & 0x55555555) != 0) c -= 1;
    return c;
  }

  /**
    * Converts from algebraic coordinates notation to a Move object
    **/
  public static Move coordinatesToMove(String s) {
    // strip whitespace
    s.replaceAll("\\s+","");

    // parse row and column into numbers
    int col = (int)s.charAt(0) - 97;
    int row = Character.getNumericValue(s.charAt(1)) - 1;

    // create board representation
    int shift = ((row % 3) * 3) + (2 - (col % 3));
    int m = 1 << shift;

    return new Move(m, 8 - (3 * (row / 3) + (2 - col / 3)));
  }

  /**
    * Determines whether the inputted string is valid HCN
    * TODO: implement... not only regex validation, but other too.
    **/
  public static boolean isValidHCN(String s) {
    return true;
  }

}
