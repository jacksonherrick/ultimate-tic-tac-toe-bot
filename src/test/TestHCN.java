package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import main.Board;
import main.Utils;

/**
 * Test class to confirm that HCN functions are working as expected. HCN
 * functions include: boardToHCN(), subBoardToHCN(), and Board's HCN
 * constructor.
 */
class TestHCN {

	/**
	 * We create a board from HCN, convert that board to an HCN, and assert they are
	 * the same.
	 * 
	 * @throws IOException
	 */
	@DisplayName("Board From HCN and Back")
	@Test
	void boardFromHCNAndBack() throws IOException {
		int count = 0;
		String[] files = new String[] { "general-positions.txt", "random-positions.txt" };
		for (String s : files) {
			File f = new File("src/test/" + s);
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line = null;
			while ((line = br.readLine()) != null) {
				Assertions.assertEquals(Utils.boardToHCN(new Board(line)), line,
						"Incorrect result in boardFromHCNAndBack().");
				count++;
			}
			br.close();
		}
		System.out.println("Completed " + count + " positions for HCN tests.");
	}
}
