package com.jherrick;

import java.util.Scanner;

public class GameImpl implements Game {

	private final Board board;
	private final Agent xAgent;
	private final Agent oAgent;
	private final Side turn;

	private static Scanner reader;



	public GameImpl(Board board, Agent xAgent, Agent oAgent) {
		this.board = board;
		this.xAgent = xAgent;
		this.oAgent = oAgent;
		this.turn = Side.X;
	}

	
	// ========== Main Method ==========

	public static void initAndPlayGame() {
		
		reader = new Scanner(System.in);
		
		Board b = initBoard(reader);
		Agent [] agents = assignAgentType(reader, b);

		Game game = new GameImpl(b, agents[0], agents[1]);
		
		// use the inputed board
		game.play();
		
		// close scanner
		reader.close();
	}


	@Override
	public void play() {
		boolean gameOver = false;
		while(!gameOver){
			System.out.println(board);
			// TODO: Bug - Fix CPU making all moves immediately
			Move nextMove = getNextMove();

			// Exit game if move is -1
			if (nextMove.move == -1){
				break;
			}

			this.board.makeMove(nextMove);
			// TODO: Fix bug, no Game Over when BigBoard is won
			gameOver = this.board.getBoardState() != BoardState.IN_PROGRESS; 
		}
		System.out.println("Game Over!");
	}




	// ========== Helper Functions ==========

	// ========== Move Generation Helper Functions ==========
	
	private Move getNextMove() {
		if(this.turn.equals(Side.X)){
			return xAgent.pickMove(board);
		}
		else return oAgent.pickMove(board);
	}

	// ========== HCN Custom Board Helper Functions ==========

	private static Board initBoard(Scanner reader){
		
		Board b = new BigBoard();
		
		// Get input
		System.out.println("Welcome. Do you want a custom position?");
		String s = reader.nextLine();
		
		if (s.equals("yes")) {
			b = initCustomBoard();
		}
		else {
			b = initDefaultBoard();
		}

		return b;
	}

	// input a particular board to play
	private static Board initCustomBoard() {
		System.out.println("Please enter the HCN string for the custom board.");
		String s = reader.nextLine();
		Board b = new BigBoard(s);
		return b;
	}

	private static Board initDefaultBoard(){
		Board b = new BigBoard();

		return b;
	}

	// ========== Assign Agent Type Helper Functions ==========

	private static Agent[] assignAgentType(Scanner reader , Board b){
	
		Agent[] agents = new Agent[2];

		// Get X Agent Assignment from player
		System.out.println("Would you like a CPU to play as X? (Y/N)");
		String s = reader.nextLine();

		if (s.equals("Y")){
			BasicBoardEvaluator board_eval = new BasicBoardEvaluator(b, Side.X);
			agents[0] = new NegaMaxAgent(board_eval, Side.X);
		}
		else {
			agents[0] = new ConsolePlayerAgent(reader);
		}

		// Get O Agent Assignment from player - TODO: Only X can play as bot even with this functionality?

		System.out.println("Would you like a CPU to play as O? (Y/N)");
		s = reader.nextLine();

		if (s.equals("Y")){
			BasicBoardEvaluator board_eval = new BasicBoardEvaluator(b, Side.O);
			agents[1] = new NegaMaxAgent(board_eval, Side.O);
		}
		else {
			agents[1] = new ConsolePlayerAgent(reader);
		}

		return agents;
}

}
