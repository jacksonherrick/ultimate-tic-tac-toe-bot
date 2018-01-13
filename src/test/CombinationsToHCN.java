package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.lang.StringBuilder;

/**
 * Converts all possible combinations of X, O, and 1 to HCN Does not need to be
 * run. Just run once to get all the HCNs for hashing values
 */
public class CombinationsToHCN {

	public static void main(String[] args) throws IOException {
		String s = "src/test/board-combinations.txt";
		File f = new File(s);
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line = null;
		while ((line = br.readLine()) != null) {
			System.out.println(convertCombinationToHCN(line));
		}
		br.close();
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
