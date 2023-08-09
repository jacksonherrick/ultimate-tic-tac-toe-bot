package com.jherrick;

import java.util.Random;
import java.util.Scanner;

public class GameImpl implements Game {

    private final Board board;
    private final Agent xAgent;
    private final Agent oAgent;
    private static Scanner reader;

    public GameImpl(Board board, Agent xAgent, Agent oAgent) {
        this.board = board;
        this.xAgent = xAgent;
        this.oAgent = oAgent;
    }

    // ========== Main Method ==========

    public static void initAndPlayGame() {

        reader = new Scanner(System.in);

        Board b = initBoard(reader);
        Agent[] agents = initAgents(reader, b);

        Game game = new GameImpl(b, agents[0], agents[1]);

        // use the inputed board
        game.play();

        // close scanner
        reader.close();
    }

    @Override
    public void play() {
        boolean gameOver = false;
        while (!gameOver) {
            System.out.println(board);
            Move nextMove = getNextMove();

            // Exit game if move is -1
            if (nextMove.move == -1) {
                break;
            }

            this.board.makeMove(nextMove);
            gameOver = this.board.getBoardState() != BoardState.IN_PROGRESS;
        }
        printWinner(this.board.getBoardState());
        System.out.println("Game Over!");
    }

    // ========== Helper Functions ==========

    // ========== Move Generation Helper Functions ==========

    private Move getNextMove() {
        if (this.board.getSideForNextMove().equals(Side.X)) {
            return xAgent.pickMove(board);
        } else
            return oAgent.pickMove(board);
    }

    // ========== HCN Custom Board Helper Functions ==========

    private static Board initBoard(Scanner reader) {

        Board b = new BigBoard();

        // Get input
        System.out.println("Welcome. Do you want a custom position?");
        String s = reader.nextLine();

        if (s.equals("yes")) {
            b = initCustomBoard();
        } else {
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

    private static Board initDefaultBoard() {
        Board b = new BigBoard();

        return b;
    }

    // ========== Assign Agent Type Helper Functions ==========

    private static Agent[] initAgents(Scanner reader, Board b) {

        Agent[] agents = new Agent[2];

        // Get X Agent Assignment from player
        System.out.println(selectAgentPrompt(Side.X));
        String s = reader.nextLine();
		agents[0] = selectAgentType(s, Side.X, reader);

        // Get O Agent Assignment from player - TODO: Only X can play as bot even with
        // this functionality?

        System.out.println(selectAgentPrompt(Side.O));
        s = reader.nextLine();
		agents[1] = selectAgentType(s, Side.O, reader);

        return agents;
    }

	private static Agent selectAgentType(String s, Side side, Scanner reader){
		
		Agent a = new ConsolePlayerAgent(reader);
		BasicBoardEvaluator replacable = new BasicBoardEvaluator();

		switch (s) {
			case "1": {
				return new ConsolePlayerAgent(reader);
			}
			case "2": {
				return new RandomAgent();
			}
			case "3": {
				return new NegaMaxAgent(replacable, side);
			}
		}

		return a;
	}
	
	private static String selectAgentPrompt(Side side){
		String sideString;
		if (side == Side.X){
			sideString = "X";
		}
		else {
			sideString = "O";
		}

		return "What Agent would you like to play " + sideString + "? \n"
			 + "ConsoleAgent [1] \n"
			 + "RandomAgent [2] \n"
			 + "NegaMax Agent [3]";
	}

    // ========== Game Printing Helper Functions ==========

    private void printWinner(BoardState state) {
        if (state == BoardState.X_WON) {
            System.out.println("X won!");
        } else if (state == BoardState.O_WON) {
            System.out.println("O won!");
        } else if (state == BoardState.DRAWN) {
            System.out.println("Tie game!");
        } else {
            // Throw and exception?
        }
    }

}
