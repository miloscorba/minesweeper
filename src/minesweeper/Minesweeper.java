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
    private BestTimes bestTimes = new BestTimes();
    private Minesweeper instance;
    private Settings settings;
    /**
     * Constructor.
     */
    private Minesweeper() {
        instance = this;
        //setSettings(Settings.EXPERT);
        setSettings(settings.load());

        userInterface = new ConsoleUI();
        //Field field = new Field(settings.getRowCount(), settings.getColumnCount(), settings.getMineCount());
        Field field = new Field(settings);
        startMillis = System.currentTimeMillis();
        userInterface.newGameStarted(field);
    }

    public int getPlayingSeconds(){
        long time = System.currentTimeMillis() - startMillis;
        return  (int) time;
    }

    public BestTimes getBestTimes() {
        return bestTimes;
    }

    public Minesweeper getInstance() {
        return instance;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
        this.settings.save();
    }

    /**
     * Main method.
     * @param args arguments
     */
    public static void main(String[] args) {
        new Minesweeper();
    }
}
