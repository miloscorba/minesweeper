package minesweeper;

import java.io.*;
import java.util.List;

public class Settings implements Serializable {
    private final int rowCount;
    private final int columnCount;
    private final int mineCount;

    public static final Settings BEGINNER = new Settings(9,9,10);
    public static final Settings INTERMEDIATE = new Settings(16, 16, 40);
    public static final Settings EXPERT = new Settings(16, 30, 99);

    private static final String SETTING_FILE = System.getProperty("user.home") + System.getProperty("file.separator") + "minesweeper.settings";

    public Settings(int rowCount, int columnCount, int mineCount) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.mineCount = mineCount;
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public int getMineCount() {
        return mineCount;
    }

    public void save(){
        try (FileOutputStream os = new FileOutputStream(SETTING_FILE);
             ObjectOutputStream oos = new ObjectOutputStream(os);) {
            oos.writeObject(this);
        } catch (Exception e){
            System.out.println("Nepodarilo sa zapisat do suboru!");
        }
    }

    public static Settings load(){
        Settings settings;
        try (FileInputStream is = new FileInputStream(SETTING_FILE);
             ObjectInputStream ois = new ObjectInputStream(is)) {
            settings = (Settings) ois.readObject();
        } catch (Exception e){
            return BEGINNER;
        }
        return settings;
    }

    @Override
    public boolean equals(Object o){
        if(o.equals(BEGINNER) || o.equals(INTERMEDIATE) || o.equals(EXPERT)){
            return true;
        }
        return false;
    }

    @Override
    public int hashCode(){
        return rowCount*columnCount*mineCount;
    }
}
