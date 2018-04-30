package minesweeper;

import minesweeper.consoleui.ConsoleUI;
import minesweeper.core.Field;

/**
 * Main application class.
 */
public class Minesweeper {
    /** User interface. */
    private UserInterface userInterface;
    private long startMillis;
    /**
     * Constructor.
     */
    private Minesweeper() {

        userInterface = new ConsoleUI();
        Field field = new Field(9, 9, 10);
        startMillis = System.currentTimeMillis();
        userInterface.newGameStarted(field);
    }

    public int getPlayingSeconds(){
        long time = System.currentTimeMillis() - startMillis;
        return  (int) time;
    }

    /**
     * Main method.
     * @param args arguments
     */
    public static void main(String[] args) {
        new Minesweeper();
    }
}
