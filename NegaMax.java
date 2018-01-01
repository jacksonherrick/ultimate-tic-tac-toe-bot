import java.util.Collections;
import java.util.List;

public class NegaMax{
	//maps scores to "moves"
	//Don't think best-move is necessary now that I've changed the way the negamax works, but I'm gonna leave it for now
	//private static MoveAndValue best_move;
	private static int depth;
	private static int alpha;
	private static int beta;
	private static int color;

	//runs the negaMax and returns the best move
	public static Move nextMove(Board game){
		depth = 0;
		alpha = 1000;
		beta = -1000;
		color = 1;
		int startTime = 0; /*This will be the start time of the negamax*/
		int depthCount = 1;
		//iterative deepening
		List<Move> tempMoves = game.generateMoves();
		MoveAndValue [] moves = new MoveAndValue[tempMoves.size()];
		int count = 0;
		for(Move m : tempMoves){
			MoveAndValue tempMove = new MoveAndValue(m,-1);
			moves[count] = tempMove;
		}

		while(10 /*this should be current time*/ - startTime < 10){
			for(int i=0; i<moves.length; i++){
				//Don't think this is necessary, but I'm gonna leave it for now
				/*
				best_move.move = null;
				best_move.value = -1;
				*/
				game.makeMove(moves[i].move);
				//Note: may have to make alpha and beta instance variables so that the iterative deepening actually helps pruning
				moves[i].value = negaMax(game, depth, depthCount, alpha, beta, color);
				game.takeMove(moves[i].move);

			}
			depthCount++;
			//explore the best moves first
			sortMoves(moves);
		}

		return moves[0].move;
	}

	//color keeps track of the player. It is 1 if we are the player, -1 if the opponent is the player
	private static int negaMax(Board game, int depth, int depthCount, int alpha, int beta, int color){

		//if we are at the end of the game or have reached the iterative deepening depth, evaluate the state of the board and return a reward
		//Note: Maybe factor depth into the evaluation in the future?
		if(game.state == BoardState.X_WON || game.state == BoardState.O_WON || depth == depthCount){
			return color * Eval.evaluate(game);
		}

		//keeps track of the value of the best move we've found so far. Starts at -10000 so that the first value will override it
		int max = -10000;

		//explores each node in the tree
		for(Move possibleMove: game.generateMoves()){

			//these three lines make a move, recurivsely call the negamax on the new board state, and then un-make the move. This allows us to get the value of the move without
			//"making" the move on our actual board
			game.makeMove(possibleMove);
			int negaMax_value = -negaMax(game, depth +1, depthCount, -beta, -alpha, -color);
			game.takeMove(possibleMove);

			//if we found a move better than our max, it is our new max
			if(negaMax_value > max){
				max = negaMax_value;
			}

			//Don't think this is necessary, but I'm gonna leave it just in case
			/*
			//if we are at the first level of our recursion and the value of the move we are currently exploring is better than any we've found so far, the current move is the new best move
			if(depth == 0){
				if(negaMax_value > best_move.value){
					best_move = new MoveAndValue(possibleMove, negaMax_value);
				}
			}
			*/

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


	private static void sortMoves(MoveAndValue [] moves){
		//sorts the possible moves into order from best to worst
		int maxIndex = 0;
		for(int i =0; i< moves.length-1; i++){
			for(int j =i; j<moves.length; j++){
				if(moves[j].value > moves[maxIndex].value){
					maxIndex = j;
				}
			}
			MoveAndValue temp = moves[i];
			moves[i] = moves[maxIndex];
			moves[maxIndex] = moves[i];
		}
	}
}
