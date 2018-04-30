import minesweeper.core.Clue;
import minesweeper.core.Field;
import minesweeper.core.GameState;
import minesweeper.core.Tile;
import org.junit.Test;

import static org.junit.Assert.*;

public class FieldTest {
    static final int ROWS = 9;
    static final int COLUMNS = 9;
    static final int MINES = 10;
    Field field = new Field(ROWS, COLUMNS, MINES);

    @Test
    public void isSolved() {


        assertEquals(GameState.PLAYING, field.getState());

        int open = 0;
        for(int row = 0; row < field.getRowCount(); row++) {
            for(int column = 0; column < field.getColumnCount(); column++) {
                if(field.getTile(row, column) instanceof Clue) {
                    field.openTile(row, column);
                    open++;
                }
                if(field.getRowCount() * field.getColumnCount() - open == field.getMineCount()) {
                    assertEquals(GameState.SOLVED, field.getState());
                } else {
                    assertNotSame(GameState.FAILED, field.getState());
                }
            }
        }

        assertEquals(GameState.SOLVED, field.getState());
    }

    @Test
    public void getRowCount() {
        assertEquals(ROWS, field.getRowCount());
    }

    @Test
    public void getColumnCount() {
        assertEquals(COLUMNS, field.getColumnCount());
    }

    @Test
    public void getMineCount() {
        assertEquals(MINES, field.getMineCount());
    }

    @Test
    public void generate(){
        for(int row = 0; row < ROWS; row++){
            for(int column = 0; column < COLUMNS; column++){
                assertNotNull(field.getTile(row, column));
            }
        }

        int clueCount = 0;
        for(int row = 0; row < ROWS; row++){
            for(int column = 0; column < COLUMNS; column++){
                if(field.getTile(row, column) instanceof Clue){
                    clueCount++;
                }
            }
        }
        assertEquals(ROWS * COLUMNS - MINES, clueCount);
    }



}