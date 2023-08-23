package com.jherrick;

import java.util.Collection;
import java.util.ArrayList;
import java.lang.Math;

public class RandomAgent implements Agent {
    
    public Move pickMove(Board board){
        
        Collection<Move> legalMovesCollection = board.getLegalMoves();
        ArrayList<Move> legalMoves = new ArrayList<>(legalMovesCollection);

        int numLegalMoves = legalMoves.size();

        int randomMoveNumber = (int) Math.floor(Math.random() * numLegalMoves);

        return legalMoves.get(randomMoveNumber);
    }
}
