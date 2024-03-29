package com.jherrick;

import java.util.List;
import java.util.Arrays;

public class NegaMaxAgent implements Agent {
	private final BoardEvaluator evaluator;
	private final Side side; // TODO: right now, the negamax can only play as X. This needs to be generalized

	public NegaMaxAgent(BoardEvaluator evaluator, Side side) {
		this.evaluator = evaluator;
		this.side = side;
	}

	@Override
	public Move pickMove(Board game) {
		// Create copy of board to simulate
		BigBoard tempGame = new BigBoard(game.getBoardPosition(), game.getSideForNextMove(), game.getLastMove());
		
		int depth = 0;
		int beta = Integer.MAX_VALUE;
		int color = 1;
		int depthCount = 1;
		int maxValue = Integer.MIN_VALUE;
		// iterative deepening
		List<Move> tempMoves = (List<Move>) tempGame.getLegalMoves();

		MoveAndValue[] moves = new MoveAndValue[tempMoves.size()];
		int count = 0;
		for (Move m : tempMoves) {
			MoveAndValue tempMove = new MoveAndValue(m, -1);
			moves[count] = tempMove;
			count++;
		}

		while (depthCount <= 5) {
			for (int i = 0; i < moves.length; i++) {
				tempGame.makeMove(moves[i].move);

				/*
				 * maxValue here starts off as -infinity. In a normal negamax, the root has a
				 * value after the first branch explored that acts as a lower bound for the rest
				 * of the search. Essentially it acts as the root's alpha.
				 */
				moves[i].value = negaMax(tempGame, depth, depthCount, maxValue, beta, color);

				// This should take care of the fact that we are doing each of the root's moves
				// separately. This takes the place of the "max" in the recursive negamax
				// function
				if (moves[i].value > maxValue) {
					maxValue = moves[i].value;
				}
				tempGame.undoLastMove();

			}
			depthCount++;
			// explore the best moves first
			Arrays.sort(moves);
		}

		return moves[0].move;
	}

	// color keeps track of the player. It is 1 if we are the player, -1 if the
	// opponent is the player
	private int negaMax(Board temp_game, int depth, int depthCount, int alpha, int beta, int color) {

		// if we are at the end of the game or have reached the iterative deepening
		// depth, evaluate the state of the board and return a reward
		// Note: Maybe factor depth into the evaluation in the future?
		if (temp_game.getBoardState() == BoardState.X_WON || temp_game.getBoardState() == BoardState.O_WON
				|| depth == depthCount) {
			return (int) (color * evaluator.evaluate(temp_game, side));
		}

		// keeps track of the value of the best move we've found so far. Starts at
		// -10000 so that the first value will override it
		int max = Integer.MIN_VALUE;

		// explores each node in the tree
		for (Move possibleMove : temp_game.getLegalMoves()) {

			// these three lines make a move, recurivsely call the negamax on the new board
			// state, and then un-make the move. This allows us to get the value of the move
			// without
			// "making" the move on our actual board
			temp_game.makeMove(possibleMove);
			int negaMax_value = -negaMax(temp_game, depth + 1, depthCount, -beta, -alpha, -color);
			temp_game.undoLastMove();

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
