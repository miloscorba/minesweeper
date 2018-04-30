package minesweeper.core;

import java.util.Random;

/**
 * Field represents playing field and game logic.
 */
public class Field {
    /**
     * Playing field tiles.
     */
    private final Tile[][] tiles;

    /**
     * Field row count. Rows are indexed from 0 to (rowCount - 1).
     */
    private final int rowCount;

    /**
     * Column count. Columns are indexed from 0 to (columnCount - 1).
     */
    private final int columnCount;

    /**
     * Mine count.
     */
    private final int mineCount;

    /**
     * Game state.
     */
    private GameState state = GameState.PLAYING;

    /**
     *  Row count
     * @return count of rows in field.
     */
    public int getRowCount() {
        return rowCount;
    }

    /**
     * Column count
     * @return count of colums in field.
     */
    public int getColumnCount() {
        return columnCount;
    }

    /**
     * Mine count
     * @return count of mines in field.
     */
    public int getMineCount() {
        return mineCount;
    }

    /**
     * Get state
     * @return state of game.
     */
    public GameState getState() {
        return state;
    }

    public Tile getTile(int row, int column) {
        return tiles[row][column];
    }
    /**
     * Constructor.
     *
     * @param rowCount    row count
     * @param columnCount column count
     * @param mineCount   mine count
     */
    public Field(int rowCount, int columnCount, int mineCount) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.mineCount = mineCount;
        tiles = new Tile[this.rowCount][this.columnCount];

        //generate the field content
        generate();
    }



    /**
     * Opens tile at specified indeces.
     *
     * @param row    row number
     * @param column column number
     */
    public void openTile(int row, int column) {
        Tile tile = tiles[row][column];
        if (tile.getState() == Tile.State.CLOSED) {
            tile.setState(Tile.State.OPEN);
            if (tile instanceof Mine) {
                state = GameState.FAILED;
                return;
            }

            if(((Clue) tile).getValue() == 0) {
                openAdjacentTiles(row, column);
            }
            if (isSolved()) {
                state = GameState.SOLVED;

                return;
            }
        }

    }

    /**
     * Marks tile at specified indeces.
     *
     * @param row    row number
     * @param column column number
     */
    public void markTile(int row, int column) {
        Tile tile = getTile(row, column);;

        if(tile.getState() == Tile.State.CLOSED)
            tile.setState(Tile.State.MARKED);
        else if(tile.getState() == Tile.State.MARKED)
            tile.setState(Tile.State.CLOSED);
    }

    /**
     * Funtion generates Mine and Clue tiles in Fields
     */
    private void generate() {
        Random random = new Random();
        int row,column,puttedMines=0;

        while(puttedMines != mineCount){
            row = random.nextInt(rowCount);
            column = random.nextInt(columnCount);
            if(tiles[row][column] == null)
                tiles[row][column] = new Mine();
            puttedMines++;
        }

        for(row=0; row < rowCount; row++){
            for(column = 0; column < columnCount; column++){
                Tile tile = getTile(row,column);
                if(!(tiles[row][column] instanceof  Mine))
                    tiles[row][column] = new Clue(countAdjacentMines(row, column));
            }
        }
    }

    /**
     * Returns true if game is solved, false otherwise.
     *
     * @return true if game is solved, false otherwise
     */
    public boolean isSolved() {
       return (rowCount*columnCount - getNumberOf(Tile.State.OPEN)) == mineCount;

    }

    /**
     * Returns number of adjacent mines for a tile at specified position in the field.
     *
     * @param row    row number.
     * @param column column number.
     * @return number of adjacent mines.
     */
    private int countAdjacentMines(int row, int column) {
        int count = 0;
        for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
            int actRow = row + rowOffset;
            if (actRow >= 0 && actRow < rowCount) {
                for (int columnOffset = -1; columnOffset <= 1; columnOffset++) {
                    int actColumn = column + columnOffset;
                    if (actColumn >= 0 && actColumn < columnCount) {
                        if (tiles[actRow][actColumn] instanceof Mine) {
                            count++;
                        }
                    }
                }
            }
        }

        return count;
    }

    public int getNumberOf(Tile.State state){
        int numberOfExposedMines = 0;

        for(int row = 0; row < getRowCount(); row++){
            for(int column = 0; column < getColumnCount(); column++){
                if(tiles[row][column].getState() == state)
                    numberOfExposedMines++;
            }
        }
        return numberOfExposedMines;
    }

    private void openAdjacentTiles(int row, int column){
        for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
            int actRow = row + rowOffset;
            if (actRow >= 0 && actRow < rowCount) {
                for (int columnOffset = -1; columnOffset <= 1; columnOffset++) {
                    int actColumn = column + columnOffset;
                    if (actColumn >= 0 && actColumn < columnCount) {
                        if (tiles[actRow][actColumn] instanceof Clue
                                && tiles[actRow][actColumn].getState() == (Tile.State.CLOSED)) {
                                openTile(actRow,actColumn);

                        }
                    }
                }
            }
        }
    }

    public int getRemainingMineCount(){
        return getMineCount() - getNumberOf(Tile.State.MARKED);
    }
}
