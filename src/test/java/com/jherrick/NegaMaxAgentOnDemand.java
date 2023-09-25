package com.jherrick;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class NegaMaxAgentOnDemand {
    public static NegaMaxAgent createXNegamaxAgent(){
        BoardEvaluator evaluator = new BasicBoardEvaluator();
        Side agentSide = Side.X;
        return new NegaMaxAgent(evaluator, agentSide);
    }
    @Test
    public void given_ImmediateWinningMoveAvailable_when_PickingMove_then_ReturnWinningMove(){
        // Arrange
        NegaMaxAgent xAgent = createXNegamaxAgent();
        String startingPosition = "1XX6/OOO6/OOO6/XXX6/OOO6/OOO6/XXX6/OO1OO4/OO1OO4 X g9"; // A board with an available move for X to win the game in one move: a9. All other moves will lose immediately
        Board boardWithImmediateWinningMove = new BigBoard(startingPosition);
        System.out.println(boardWithImmediateWinningMove);
        Move winningMove = new Move(Constants.BIT_MASKS[0],0);
        System.out.println(winningMove.toString());

        // Act
        Move selectedMove = xAgent.pickMove(boardWithImmediateWinningMove);
        System.out.println("Selected Move: " + selectedMove);

        // Assert
        Assertions.assertEquals(winningMove.toString(), selectedMove.toString());
    }

    @Test
    public void given_MultipleIdenticalScoringMoves_when_PickingMove_then_PickARandomOne(){
        // Arrange
        NegaMaxAgent xAgent = createXNegamaxAgent();
        String startingPosition = "XX4XX1/OOO6/OOO6/XXX6/OO1OO4/OO1OO4/XXX6/OO1OO4/OO1OO4 X g9"; // X has multiple winning moves available, each trial should pick a different one
        Board boardWithImmediateWinningMove = new BigBoard(startingPosition);
        List<String> winningMoves = Arrays.asList("c9", "a8", "b8", "c7");
        System.out.println(boardWithImmediateWinningMove);

        // Act
        Move selectedMove = xAgent.pickMove(boardWithImmediateWinningMove);
        System.out.println("Selected Move: " + selectedMove);

        // Assert
        // It should return one of c9, a8, b8, or c7. Should not return c8.
        Assertions.assertTrue(winningMoves.contains(selectedMove.toString()));
    }

    // Test that we are expecting O to minimize correctly - makes their best immediate move
    @Test
    public void given_OHasGoodMoveAvailable_when_PickingMove_then_WeAssumeTheyTakeIt(){
        // Arrange
        NegaMaxAgent xAgent = createXNegamaxAgent();
        // c7 leads to a sub board where O can win if they make the right move, but X will win if not.
        // b7 leads to a normal in-progress game.
        // we should pick b7
        String startingPosition = "XOXXOOO2/X8/OOO6/X8/X8/OOO6/X8/XX1OOXXXO/OO1XXOX1X X g9";
        Board boardWithImmediateWinningMove = new BigBoard(startingPosition);
        System.out.println(boardWithImmediateWinningMove);
        String bestMove = "b7";

        // Act
        Move selectedMove = xAgent.pickMove(boardWithImmediateWinningMove);
        System.out.println("Selected Move: " + selectedMove);

        // Assert
        Assertions.assertEquals(bestMove, selectedMove.toString());
    }

    // Evaluate to deeper win conditions and make sure it can find them

    @Test
    public void moveUtilsTest(){
        System.out.println(new BigBoard());
        for(int board = 0; board < 9; board++){
            for(int move = 0; move < 9; move++){
                Move m = new Move(Constants.BIT_MASKS[move], board);
                System.out.println(m);
            }
            System.out.println();
        }
    }
}

