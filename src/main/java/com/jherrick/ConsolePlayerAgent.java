package com.jherrick;

import java.util.Scanner;

public class ConsolePlayerAgent implements Agent {

    private final Scanner reader;

    public ConsolePlayerAgent(Scanner reader) {
        this.reader = new Scanner(System.in);
        ;
    }

    // ========== Public Functions ==========
    @Override
    public Move pickMove(Board b) {

        // alert the player
        System.out.println(
                "Type the coordinates of your move. ex: e5");

        // continuously wait for input
        while (true) {
            System.out.println();
            String s = reader.nextLine();

            // Gives Agent option to exit the game if they return a move of -1.
            if (shouldExitGame(s)) {
                return exitMove();
            }
            if (isValidHCN(s)) {
                Move selectedMove = Utils.coordinatesToMove(s);
                if (b.getLegalMoves().contains(selectedMove)) {
                    return selectedMove;
                } else {
                    System.out.println("Sorry, please enter a valid move");
                }
            } else {
                System.out.println("Sorry, please enter a move in the form of [a-i][0-9]. ex: e5");
            }
        }
    }

    // ========== Helper Functions ==========

    private boolean isValidHCN(String s) {
        return s.matches("^\\s*[a-i][1-9]\\s*$");
    }

    private boolean shouldExitGame(String s) {

        if (s.equals("quit") || s.equals("stop") || s.equals("exit")) {
            return true;
        }
        return false;
    }

    private Move exitMove() {
        return new Move(-1, -1);
    }
}
