package main;

import java.util.List;
import java.util.Arrays;
import java.util.Collections;

public class NegaMax {
	
	public static Move nextMove(Board game) {
		int depth = 0;
		int beta = Integer.MAX_VALUE;
		int color = 1;
		int startTime = 0; /* This will be the start time of the negamax */
		int depthCount = 1;
		int maxValue = Integer.MIN_VALUE;
		
		List<Move> tempMoves = game.generateMoves();
			
		MoveAndValue[] moves = new MoveAndValue[tempMoves.size()];
		int count = 0;
		for (Move m : tempMoves) {
			MoveAndValue tempMove = new MoveAndValue(m, -1);
			moves[count] = tempMove;
			count++;
		}
		
		// iterative deepening
		while (depthCount <= 1) {
			for (int i = 0; i < moves.length; i++) {
				game.makeMove(moves[i].move);

				/*
				 * maxValue here starts off as -infinity. In a normal negamax, the root has a
				 * value after the first branch explored that acts as a lower bound for the rest
				 * of the search. Essentially it acts as the root's alpha.
				 */
				moves[i].value = negaMax(game, depth, depthCount, maxValue, beta, color);

				// This should take care of the fact that we are doing each of the root's moves
				// separately. This takes the place of the "max" in the recursive negamax
				// function
				if (moves[i].value > maxValue) {
					maxValue = moves[i].value;
				}
				game.takeMove(moves[i].move);

			}
			depthCount++;
			// explore the best moves first
			Arrays.sort(moves, Collections.reverseOrder());
		}
		System.out.println(moves[0].move);
		System.out.println(moves[0].value);
		return moves[0].move;
	}

	// color keeps track of the player. It is 1 if we are the player, -1 if the
	// opponent is the player
	private static int negaMax(Board game, int depth, int depthCount, int alpha, int beta, int color) {

		// if we are at the end of the game or have reached the iterative deepening
		// depth, evaluate the state of the board and return a reward
		// Note: Maybe factor depth into the evaluation in the future?
		if (game.state == BoardState.X_WON || game.state == BoardState.O_WON || depth == depthCount) {
			return color * game.evaluate();
		}

		// keeps track of the value of the best move we've found so far. Starts at
		// -10000 so that the first value will override it
		int max = Integer.MIN_VALUE;

		// explores each node in the tree
		for (Move possibleMove : game.generateMoves()) {

			// these three lines make a move, recursively call the negamax on the new board
			// state, and then undo the move. This allows us to get the value of the move
			// without "making" the move on our actual board
			game.makeMove(possibleMove);
			int negaMax_value = -negaMax(game, depth + 1, depthCount, -beta, -alpha, -color);
			game.takeMove(possibleMove);

			// if we found a move better than our max, it is our new max
			if (negaMax_value > max) {
				max = negaMax_value;
			}

			// Setting the bounds for our pruning
			if (negaMax_value > alpha) {
				alpha = negaMax_value;
			}

			// if the move we are looking at has a value greater than the worst move the
			// opponent can make us take, we prune
			if (negaMax_value >= beta) {
				return beta;
			}

		}

		return max;
	}

}
