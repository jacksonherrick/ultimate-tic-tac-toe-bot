package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import main.Board;

/**
 * Test class to confirm move generation works as expected. Tests both on random
 * positions confirming expected number of positions, and predetermined boards.
 */
class TestMoveGeneration {

	/**
	 * We use the randomly generated positions to confirm that we have the expected
	 * number of moves.
	 * 
	 * @throws IOExceptiont
	 */
	@DisplayName("Move Generation Shallow Test")
	@Test
	void moveGenerationShallowTest() throws IOException {
		int count = 0;
		String[] files = new String[] { "general-positions.txt", "random-positions.txt" };
		for (String s : files) {
			File f = new File("src/test/" + s);
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line = null;
			while ((line = br.readLine()) != null) {
				Board b = new Board(line);
				Assertions.assertEquals(TestUtils.countMoves(b), b.generateMoves().size(),
						"Different number of moves generated in moveGenerationShallowTest()");
				count++;
			}
			br.close();
		}
		System.out.println("Completed " + count + " positions for shallow move generation.");
	}

}
