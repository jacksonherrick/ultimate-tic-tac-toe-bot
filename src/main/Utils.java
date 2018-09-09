package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashMap;

public final class Utils {

	private Utils() {
	}

	private static final int MultiplyDeBruijnBitPosition[] = new int[] { 0, 9, 1, 10, 13, 21, 2, 29, 11, 14, 16, 18, 22,
			25, 3, 30, 8, 12, 20, 28, 15, 17, 24, 7, 19, 27, 23, 6, 26, 5, 4, 31 };

	/**
	 * Finds the index of the highest set bit of the passed integer
	 **/
	public static int highestSetBit(int v) {
		v |= v >>> 1;
		v |= v >>> 2;
		v |= v >>> 4;
		v |= v >>> 8;
		v |= v >>> 16;
		return MultiplyDeBruijnBitPosition[(int) ((v * 0x07C4ACDDL) >> 27) & 31];
	}

	/**
	 * Finds the index of the lowest set bit of the passed integer
	 **/
	public static int lowestSetBit(int v) {
		if (v == 0)
			return -1;
		int c = 32; // number of 0 bits on the right
		v &= -v;
		if (v != 0)
			c--;
		if ((v & 0x0000FFFF) != 0)
			c -= 16;
		if ((v & 0x00FF00FF) != 0)
			c -= 8;
		if ((v & 0x0F0F0F0F) != 0)
			c -= 4;
		if ((v & 0x33333333) != 0)
			c -= 2;
		if ((v & 0x55555555) != 0)
			c -= 1;
		return c;
	}

	/**
	 * Converts from algebraic coordinates notation to a Move object
	 **/
	public static Move coordinatesToMove(String s) {
		// strip whitespace
		s.replaceAll("\\s+", "");

		// parse row and column into numbers
		int col = (int) s.charAt(0) - 97;
		int row = Character.getNumericValue(s.charAt(1)) - 1;

		// create board representation
		int shift = ((row % 3) * 3) + (2 - (col % 3));
		int m = 1 << shift;

		return new Move(m, 8 - (3 * (row / 3) + (2 - col / 3)));
	}

	/**
	 * Extracts HCN from a given string. 
	 * If not valid, [match].matches() will return false.
	 * @param hcn
	 * @return Matcher
	 */
	public static Matcher extractHCN(String hcn) {
		// compile the regex to match HCN's
		Pattern r = Pattern.compile(
				"^([XO1-9]{0,9})\\/([XO1-9]{0,9})\\/([XO1-9]{0,9})\\/([XO1-9]{0,9})\\/([XO1-9]{0,9})\\/([XO1-9]{0,9})\\/([XO1-9]{0,9})\\/([XO1-9]{0,9})\\/([XO1-9]{0,9})\\s([XO]){1}\\s([a-zA-Z0-9]{2}|\\-)$");

		// match the HCN
		return r.matcher(hcn);
	}

	/**
	 * Converts a SubBoard to its HCN representation
	 **/
	public static String subBoardToHCN(SubBoard sb) {
		StringBuilder result = new StringBuilder();

		// splice out "\" and newlines
		String s = sb.toString().replaceAll("[\\n\\[\\]]", "");

		// iterate through the string
		int i = 0;
		while (i < s.length()) {
			char c = s.charAt(i);
			if (c != ' ') {
				// it is not empty
				result.append(s.charAt(i));
				i++;
			} else {
				// count how many empty spaces
				int count = 0;
				while (i < s.length() && s.charAt(i) == ' ') {
					count++;
					i++;
				}
				result.append(count);
			}
		}
		return result.toString();
	}

	/**
	 * Converts a Board to its HCN representation
	 **/
	public static String boardToHCN(Board b) {
		StringBuilder result = new StringBuilder();
		// convert the SubBoards
		for (SubBoard sb : b.boards) {
			result.append(subBoardToHCN(sb) + "/");
		}
		// delete the last "/"
		result.setLength(result.length() - 1);

		// add side to move
		String side = b.side == Side.X ? "X" : "O";
		result.append(" " + side + " ");
		if(b.getLastMove() != null) {
			result.append(b.getLastMove().toString());
		} else {
			result.append("-");
		}
		return result.toString();
	}
	
	public static HashMap<SubBoard, Integer> generateSubBoardScores() throws IOException{
		String s = "src/main/board-combinations.txt";
		File f = new File(s);
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line = null;
		
		HashMap<SubBoard, Integer> values = new HashMap<SubBoard, Integer>();
		
		try {
			while ((line = br.readLine()) != null) {
				String st = convertCombinationToHCN(line);
				SubBoard temp = new SubBoard(st);
				values.put(temp, temp.evaluate());
			}
		} catch (IOException e) {
			System.out.println("Can't read file");
		}
		
			 
		br.close();
		return values;
	}
	
	public static String convertCombinationToHCN(String line) {
		
		// initialize string
		StringBuilder str = new StringBuilder();
		
		// keep track of consecutive blanks
		int count = 0;
		for (int i = 0; i < line.length(); i++) {
			char c = line.charAt(i);
			if (c == 'X' || c == 'O') {
				if (count > 0) {
					str.append(count);
					// reset blanks
					count = 0;
				}
				str.append(c);
			} else if (c == '1') {
				// increment blanks
				count++;
			} else {
				throw new java.lang.RuntimeException(
						"Unknown Character \"" + c + "\" encountered in convertCombinationToHCN()");
			}

		}
		if (count > 0) {
			str.append(count);
		}
		return str.toString();
	} 
}
