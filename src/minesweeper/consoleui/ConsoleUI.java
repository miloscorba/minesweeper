package minesweeper.consoleui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import minesweeper.UserInterface;
import minesweeper.core.*;

/**
 * Console user interface.
 */
public class ConsoleUI implements UserInterface {
    /** Playing field. */
    private Field field;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_GREEN = "\u001B[32m";

    /** Input reader. */
    private BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

    boolean start = true;

    /**
     * Reads line of text from the reader.
     * @return line as a string
     */
    private String readLine() {
        try {
            return input.readLine();
        } catch (IOException e) {
            return null;
        }
    }
    
    /**
     * Starts the game.
     * @param field field of mines and clues
     */
    @Override
    public void newGameStarted(Field field) {
        this.field = field;

        do {
            update();
            try {
                processInput();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(field.getState() == GameState.SOLVED){

                update();
                System.out.println(ANSI_GREEN + "------------------------------------------------");
                System.out.println("Wohoooo! VYHRALI STE !!");
                System.out.println("------------------------------------------------" + ANSI_RESET);
                break;
            }
            else if(field.getState() == GameState.FAILED){
                update();
                System.out.println(ANSI_RED + "------------------------------------------------");
                System.out.println("Bohuzial prehrali ste :(((");
                System.out.println("------------------------------------------------" + ANSI_RESET);
                break;
            }


        } while(true);
    }
    
    /**
     * Updates user interface - prints the field.
     */
    @Override
    public void update() {
        if(start){

            System.out.println( ANSI_GREEN + "------------------------------------------------");
            System.out.println(" *** Vitajte " + System.getProperty("user.name").toString() + " v hre Minesweeper ***" + ANSI_RESET);
            start = false;
        }
        System.out.println( ANSI_GREEN + "------------------------------------------------" + ANSI_RESET);
        System.out.println("Pocet neoznacenych min je: " + field.getRemainingMineCount());

        boolean firstRowSpace = true;
        for(int column = 0; column < field.getColumnCount(); column++){
            if(firstRowSpace == true) {
                System.out.print(ANSI_GREEN);
                System.out.printf("    0  ");
                firstRowSpace = false;
            }
            else if (column < 10)
                System.out.printf(column + "  ");
            else if (column >= 10 )
                System.out.printf(column + " ");
        }
        System.out.print(ANSI_RESET);
        System.out.println();

        for(int row = 0; row < field.getRowCount(); row++){
            System.out.print(ANSI_GREEN);
            System.out.printf("%c ", row+'A');
            System.out.print(ANSI_RESET);

            for(int column = 0; column < this.field.getColumnCount(); column++){
                Tile tile = field.getTile(row,column);

                if(tile.getState().equals(Tile.State.CLOSED) && column < 11)
                    System.out.printf("  -");
                if(tile.getState().equals(Tile.State.CLOSED) && column >= 11)
                    System.out.printf("  -");
                else if (field.getTile(row,column).getState() == Tile.State.MARKED)
                    System.out.printf(ANSI_BLUE + " M" + ANSI_RESET);
                else if ((field.getTile(row,column).getState() == Tile.State.OPEN) &&
                    field.getTile(row,column) instanceof Clue)
                    System.out.printf(" " + ((Clue) field.getTile(row,column)).getValue() );
                else if ((field.getTile(row,column).getState() == Tile.State.OPEN) &&
                        field.getTile(row,column) instanceof Mine) {
                    System.out.printf(ANSI_RED + " X" + ANSI_RESET);
                }
            }
            System.out.println();
        }

    }
    
    /**
     * Processes user input.
     * Reads line from console and does the action on a playing field according to input string.
     */
    private void processInput() throws IOException {
        int row = 0;
        int column = 0;
        char operation;

        System.out.println("------------------------------------------------");
        System.out.println("Zadajte stlpec a riadok na odkrytie: ");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String input = br.readLine();

        try {
            handleInput(input);
        } catch (WrongFormatException e) {
            System.out.println(ANSI_RED + e.getMessage() + ANSI_RESET);
            processInput();
        }

        operation = input.toLowerCase().charAt(0);
        row = input.toLowerCase().charAt(1) - 'a';
        column = Character.getNumericValue(input.toLowerCase().charAt(2));

        switch (operation) {
            case 'm':
                field.markTile(row, column);
                break;
            case 'o':
                field.openTile(row, column);
                break;
        }
    }

    private void handleInput(String input) throws WrongFormatException {
        int row = 0;
        int column = 0;

        Pattern pattern = Pattern.compile("[omOM]([A-Za-z])([0-9])");
        Matcher matcher = pattern.matcher(input.toLowerCase());
        if (matcher.matches()){
            row = input.toLowerCase().charAt(1) - 'a';
            column = Character.getNumericValue(input.toLowerCase().charAt(2));
            if (!(row < field.getRowCount() && column < field.getColumnCount())) {
                throw new WrongFormatException("Zadali ste cisla mimo pola. Skuste to este raz");
            }
        }
        else {
            throw new WrongFormatException("Zadali ste nespravny vyraz! Skuste to este raz");
        }
    }
}




