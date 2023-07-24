package com.jherrick;

import java.util.Scanner;

public class ConsolePlayerAgent implements Agent {
    private final Scanner reader;

    public ConsolePlayerAgent(Scanner reader) {
        this.reader = new Scanner(System.in);;
    }

    @Override
    public Move pickMove(BigBoard b) {

        // alert the player
        System.out.println(
                "Type the coordinates of your move. ex: e5");

        // continuously wait for input
        while (true) {
            System.out.println();
            String s = reader.nextLine();
            if (s.matches("^\\s*[a-i][1-9]\\s*$")) {
                Move selectedMove = Utils.coordinatesToMove(s);
                if (b.getLegalMoves().contains(selectedMove)) {
                    return selectedMove;
                } else {
                    System.out.println("Sorry, please enter a valid move");
                }
            }
            else {
                System.out.println("Sorry, please enter a move in the form of [a-i][0-9]. ex: e5");
            }
        }
    }
}
