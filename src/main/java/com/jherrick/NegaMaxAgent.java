package com.jherrick;

import java.util.Collections;
import java.util.List;
import java.util.Arrays;

public class NegaMaxAgent implements Agent {
	private final BoardEvaluator evaluator;
	private final Side agentSide; // the side that the agent is playing // TODO: right now, the negamax can only play as X. This needs to be generalized

	// How long the agent is allowed to think, in milliseconds. If the value is negative, it has no limit
	private long thinkTimeMs = -1L;

	// When the agent has to finish analyzing the current move. If it's negative, it can think forever
	private long timeLimit = -1L;

	public NegaMaxAgent(BoardEvaluator evaluator, Side agentSide) {
		this.evaluator = evaluator;
		this.agentSide = agentSide;

	}
	public NegaMaxAgent(BoardEvaluator evaluator, Side agentSide, Long thinkTimeMs) {
		this.evaluator = evaluator;
		this.agentSide = agentSide;
		this.thinkTimeMs = thinkTimeMs;
	}

	@Override
	public Move pickMove(Board gameBoard) {
		// Calculate when the Agent has to return a selected move
		setTimeout();
		// Create a copy of board to safely simulate moves
		BigBoard simulationBoard = new BigBoard(gameBoard.getBoardPosition(), gameBoard.getSideForNextMove(), gameBoard.getLastMove());
		
		int beta = Integer.MAX_VALUE-1;
		int color = 1;
		int iterativeMaxDepth = 1; // the temporary max depth count used by the iterative deepening algorithm. Counts up from 1 to maxDepth
		int maxDepth= 5; // the absolute maximum depth that negamax will evaluate to
		// iterative deepening
		List<Move> tempMoves = simulationBoard.getLegalMoves();

		MoveAndValue[] moves = new MoveAndValue[tempMoves.size()];
		int count = 0;
		for (Move m : tempMoves) {
			MoveAndValue tempMove = new MoveAndValue(m, -1);
			moves[count] = tempMove;
			count++;
		}

		while (iterativeMaxDepth <= maxDepth) {
			// reset the max value between rounds. If we see a great move at depth 1 that turns into a terrible move later,
			// we don't want to keep thinking we have an amazing move available
			int maxValue = Integer.MIN_VALUE+1;
			for (int i = 0; i < moves.length; i++) {
				simulationBoard.makeMove(moves[i].move);

				/*
				 * maxValue here starts off as -infinity. In a normal negamax, the root has a
				 * value after the first branch explored that acts as a lower bound for the rest
				 * of the search. Essentially it acts as the root's alpha.
				 */
				moves[i].value = -negaMax(simulationBoard,  1, iterativeMaxDepth, -beta, -maxValue, -color);

				// This should take care of the fact that we are doing each of the root's moves
				// separately. This takes the place of the "max" in the recursive negamax function
				if (moves[i].value > maxValue) {
					maxValue = moves[i].value;
				}
				simulationBoard.undoLastMove();

				// Check if the Agent has more time to think. If it does not, return the best move it has found so far
				if(atTimeout()){
					// Since our scores won't be consistent if we're mid-layer, return the best move from the last layer we completed
					return bestMoveSoFar(moves);
				}

			}
			iterativeMaxDepth++;
			// Keep track of the best moves we've seen so far and explore the best moves first
			Arrays.sort(moves, Collections.reverseOrder());
		}

		return pickRandomBestMove(moves);
	}

	// color keeps track of the player. It is 1 if we are the player, -1 if the
	// opponent is the player
	private int negaMax(Board simulationBoard, int currentDepth, int depthCount, int alpha, int beta, int color) {

		// if we are at the end of the game or have reached the iterative deepening
		// depth, evaluate the state of the board and return a reward
		// Note: Maybe factor depth into the evaluation in the future? Ie if we can win now, why win later?
		if (simulationBoard.getBoardState() == BoardState.X_WON || simulationBoard.getBoardState() == BoardState.O_WON
				|| currentDepth == depthCount) {
			return (int) (color * evaluator.evaluate(simulationBoard, agentSide));
		}

		// keeps track of the value of the best move we've found so far. Starts at
		// -10000 so that the first value will override it
		int max = Integer.MIN_VALUE;

		// explores each node in the tree
		for (Move possibleMove : simulationBoard.getLegalMoves()) {

			// these three lines make a move, recurivsely call the negamax on the new board
			// state, and then un-make the move. This allows us to get the value of the move
			// without "making" the move on our actual board
			simulationBoard.makeMove(possibleMove);
			int negaMax_value = -negaMax(simulationBoard, currentDepth + 1, depthCount, -beta, -alpha, -color);
			simulationBoard.undoLastMove();

			// Check if the Agent has more time to think. If it does not, return immediately.
			if(atTimeout()){
				// Return a neutral move score
				// The score doesn't actually matter since we don't factor it into our move choice if we time out mid level
				return 0;
			}

			// if we found a move better than our max, it is our new max
			if (negaMax_value > max) {
				max = negaMax_value;
			}

			// Update the bounds for our pruning
			if (negaMax_value > alpha) {
				alpha = negaMax_value;
			}

			// if the move we are looking at has a value greater than the worst move the
			// opponent can make us take, we stop looking at this branch since there's no way we could end up here
			if (negaMax_value > beta) {
				return negaMax_value;
			}

		}
		return max;
	}

	// ========== HELPERS ==========

	// If a number of moves are tied with the highest score, pick a random one so that the bot varies its play
	// TODO: this will lead to the behavior that if the CPU has a guaranteed win, it will make random moves instead of immediately winning.
	// 	Should be fine functionally, but might be annoying to play against. Do we need to implement some discounting?
	private Move pickRandomBestMove(MoveAndValue[] scoredMoves){
		Arrays.sort(scoredMoves, Collections.reverseOrder());
		int bestScore = scoredMoves[0].value;

		// count the number of moves tied for top score
		int numMovesWithHighScore = 1;
		while(numMovesWithHighScore < scoredMoves.length && bestScore == scoredMoves[numMovesWithHighScore].value){
			numMovesWithHighScore++;
		}

		// pick a random move from the moves tied for the highest score
		int randomHighScoringMoveIndex = (int) Math.floor(Math.random() * (double) numMovesWithHighScore);
		return scoredMoves[randomHighScoringMoveIndex].move;
	}

	/*
	 * Return the best Move the Agent has found after completing a full layer of analysis.
	 * Moves are sorted after each layer is completed, so the first move is always the best we've found so far
	 */
	private Move bestMoveSoFar(MoveAndValue[] sortedScoredMoves){
		return sortedScoredMoves[0].move;
	}

	/*
	 * Returns true if the Agent is out of time to think and must return its move choice.
	 * Returns false if the agent has more time to think.
	 */
	private boolean atTimeout(){
		// If the Agent's timeout is negative, it has no timeout and can think forever
		if(this.thinkTimeMs < 0){
			return false;
		}
		// If it has a timeout, return true if the current timestamp has exceeded the limit
		return System.currentTimeMillis() > this.timeLimit;
	}

	/*
	 * Set the timeout for this round of move selection.
	 * If there is a think-time limit of N, set the timeout as N ms after the current timestamp
	 */
	private void setTimeout() {
		if(this.thinkTimeMs >= 0){
			this.timeLimit = System.currentTimeMillis() + thinkTimeMs;
		}
	}
}
