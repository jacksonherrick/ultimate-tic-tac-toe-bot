package test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import main.Eval;
import main.Board;

/**
 * Test class to confirm that the search is working as expected.
 * Serves as a sanity check to any search changes
 * Is NOT a measure of engine strength, only tests basic abilities
 **/
class TestTactics {

	@BeforeAll
	void setUp() {
		Eval.findValues();
	}

	@Test
	@DisplayName("Tactics Basic Test")
	void test() throws IOException {
		
		int count = 0;
		String[] files = new String[] { "tactical-positions.txt" };
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

		fail("Not yet implemented");
	}

}
