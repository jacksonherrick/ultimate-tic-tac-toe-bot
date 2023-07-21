package com.jherrick;

public final class App {
    private App() {
    }

    /**
     * Main entrypoint to use the program. 
     * Initializes a new game for you to play. 
     */
    public static void main(String[] args) {
        GameImpl.initAndPlayGame();
    }
}
