import java.util.Collections;

public class NegaMax{
	//maps scores to "moves"
	private MoveAndValue best_move;
	private int depth;
	private int alpha;
	private int beta;
	private int color;

	//runs the negaMax and returns the best move
	public int nextMove(Game game){
		depth = 0;
		alpha = 1000;
		beta = -1000;
		color = 1;
		int startTime = 0; /*This will be the start time of the negamax*/
		int depthCount = 1;
		//iterative deepening
		MoveAndValue [] moves = game.getAvailableMoves();
		while(10 /*this should be current time*/ - startTime < 10){
			for(int i=0; i<moves.length; i++){
				best_move.move = -1;
				best_move.value = -1;
				game.makeMove(moves[i].move);
				//Note: may have to make alpha and beta instance variables so that the iterative deepening actually helps pruning
				moves[i].value = negaMax(game, depth, depthCount, alpha, beta, color);
				game.undoMove(moves[i].move);

			}
			depthCount++;
			//explore the best moves first
			game.sortMoves(moves);
		}

		return moves[0].move;
	}

	//color keeps track of the player. It is 1 if we are the player, -1 if the opponent is the player
	private int negaMax(Game game, int depth, int depthCount, int alpha, int beta, int color){

		//if we are at the end of the game or have reached the iterative deepening depth, evaluate the state of the board and return a reward
		if(game.isOver() || depth == depthCount){
			return color * game.evaluate(depth);
		}

		//keeps track of the value of the best move we've found so far. Starts at -10000 so that the first value will override it
		int max = -10000;

		//explores each node in the tree
		for(MoveAndValue possibleMove: game.getAvailableMoves()){

			//these three lines make a move, recurivsely call the negamax on the new board state, and then un-make the move. This allows us to get the value of the move without
			//"making" the move on our actual board
			game.makeMove(possibleMove.move);
			int negaMax_value = -negaMax(game, depth +1, depthCount, -beta, -alpha, -color);
			game.undoMove(possibleMove.move);

			//if we found a move better than our max, it is our new max
			if(negaMax_value > max){
				max = negaMax_value;
			}

			//if we are at the first level of our recursion and the value of the move we are currently exploring is better than any we've found so far, the current move is the new best move
			if(depth == 0){
				if(negaMax_value > best_move.value){
					best_move = new MoveAndValue(possibleMove.move, negaMax_value);
				}
			}

			//Setting the bounds for our pruning
			if(negaMax_value > alpha){
				 alpha = negaMax_value;
			}

			//if the move we are looking at has a value greater than the worst move the opponent can make us take, we prune
			if(alpha >= beta){
				return alpha;
			}
		}

		return max;
	}

	private int scoreScenarios(Game game, int depth){
		if(game.isTied()){
			return 0;
		}
		else if(game.weWon()){
			return 1000/depth;
		}
		else{
			return -1000/depth;
		}
	}
}
