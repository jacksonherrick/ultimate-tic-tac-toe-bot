import java.util.HashMap;
import java.util.Collections;

public class NegaMax{
	//maps scores to "moves"
	private Move best_move;
	private int depth;
	private int alpha;
	private int beta;
	private int color;

	//runs the negaMax and returns the best move
	public int nextMove(Game game){
		best_move = new Move(-1, -1);
		depth = 0;
		alpha = 1000;
		beta = -1000;
		color = 1;
		negaMax(game, depth, alpha, beta, color);
		return best_move.move;
	}

	//color keeps track of the player. It is 1 if we are the player, -1 if the opponent is the player
	private int negaMax(Game game, int depth, int alpha, int beta, int color){
		
		if(game.isOver()){
			return color * scoreScenarios(game, depth);
		}

		int max = -10000;

		//explores each node in the tree
		for(int move: game.getAvailableMoves()){
			game.makeMove(move);
			int negaMax_value = -negaMax(game, depth +1, -beta, -alpha, -color);
		

			game.undoMove(move);

			if(negaMax_value > max){
				max = negaMax_value;
			}

			if(depth == 0){
				if(negaMax_value > best_move.value){
					best_move = new Move(move, negaMax_value);
				}
			}

			if(negaMax_value > alpha){
				 alpha = negaMax_value;
			}

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