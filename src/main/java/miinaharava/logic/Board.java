
package miinaharava.logic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import javafx.scene.image.Image;

public final class Board {
    
    private final int sizeX;
    private final int sizeY;
    private final int mineCount;
    
    private final Image COVER;
    private final Image  EMPTY;
    private final Image  ONE;
    private final Image  TWO;
    private final Image  THREE;
    private final Image  FOUR;
    private final Image  FIVE;
    private final Image  SIX;
    private final Image  SEVEN;
    private final Image  EIGHT;
    private final Image  MINE;
    private final Image  MARKED;

    private HashMap<Integer, Set<Integer>> updatedCells;
    private Cell[][] board;
    private int[][] mines;
    private  boolean showMines;
    private boolean isGameStarted;
    private boolean isGameLost;
    private int remainingCells;
    private int markCount;
    
    
    public Board(int x, int y, int miinat) {
        
        this.COVER = new Image("Images/"  + "COVER.png");
        this.EMPTY = new Image("Images/"  + "EMPTY.png");
        this.ONE = new Image("Images/"  + "1.png");
        this.TWO = new Image("Images/"  + "2.png");
        this.THREE = new Image("Images/"  + "3.png");
        this.FOUR = new Image("Images/"  + "4.png");
        this.FIVE = new Image("Images/"  + "5.png");
        this.SIX = new Image("Images/"  + "6.png");
        this.SEVEN = new Image("Images/"  + "7.png");
        this.EIGHT = new Image("Images/"  + "8.png");
        this.MINE = new Image("Images/"  + "MINE.png");
        this.MARKED = new Image("Images/"  + "MARKED.png");
        
        this.isGameStarted = false;
        this.sizeX = x;
        this.sizeY = y;
        this.remainingCells = x*y;
        this.markCount =0;
        this.mineCount = miinat;
        this.board = new Cell[x][y];
        this.mines = new int[x][y];
        this.showMines = false;
        this.isGameLost = false;

        this.updatedCells = new HashMap<>();
        
        // fill board with empty cells
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                putCell(i, j,10, false, false);
            }
        }
    }

    public boolean isGameLost() {
        return isGameLost;
    }
    
    public void reset() {
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                putCell(i, j, 10, false, false);
            }
        }
        this.markCount = 0;
        this.remainingCells = this.sizeX *this.sizeY;
        this.mines = new int[sizeX][sizeY];
        this.isGameStarted = false;
        this.showMines = false;
        this.isGameLost = false;
        this.updatedCells.clear();

//        System.out.println("lauta has been resetd");
    }
    
//    public String getMineCountString(){
//        return String.valueOf(this.mineCount);
//    }

//    public int getMineCountInt() {
//        return  this.mineCount;
//    }
    
    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }
    
    public boolean isGameStarted() {
        return this.isGameStarted;
    }

    public String getRemainingMines() {
        return String.valueOf(this.mineCount -this.markCount);
    }

    private void layMines(int mouseX, int mouseY) {
        Random rng = new Random();
        
        int i = 0;
        while (i < mineCount) {
            int x = rng.nextInt(sizeX);
            int y = rng.nextInt(sizeY);

            if ((mouseX != x) || (y != mouseY)) {
                if (mines[x][y] != 1) {
                    putCell(x, y , 9, false, false);
                    mines[x][y] = 1;
                    i++;
                }
            }
        }
        isGameStarted = true;
//        System.out.println("mines layed "+this.mineCount +", cells: "+this.remainingCells);
    }
    
    private void revealMines() {
        int lkm =0;
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {

                if (mines[i][j] == 1) {
                    board[i][j].click();
                    updateCell(i,j);
//                    this.board[i][j] = new Cell(i, j, 9, true, false);
                    lkm++;
                }
            }
        }
        System.out.println("revealed mines ("+lkm+")");
    }
    
    public boolean minesShown() {
        return this.showMines;
    }
    
    public boolean isGameWon() {
        
//        System.out.println("game win condition checked, remaining cells: "+this.remainingCells + ", GameWon = "+
//                (this.remainingCells == this.mineCount && this.markCount == this.mineCount));
        
        return this.remainingCells == this.mineCount && this.markCount == this.mineCount;
    }

    public int getRemainingCellsCount() {
        return remainingCells;
    }
    
    public Image getRuutuIcon(int x, int y) {
        Cell ruutu = getCell(x, y);
        if (ruutu.isMarked()) {
            return this.MARKED;
        } else {
            switch (ruutu.getType()) {
                case 0: return this.EMPTY;
                case 1: return this.ONE;
                case 2: return this.TWO;
                case 3: return this.THREE;
                case 4: return this.FOUR;
                case 5: return this.FIVE;
                case 6: return this.SIX;
                case 7: return this.SEVEN;
                case 8: return this.EIGHT;
                case 9: return this.MINE;
                case 10: return this.COVER;
                default:
                    throw new AssertionError(ruutu.getType());
            }
        }
    }
    
//    public int getCellType(int x, int y) {
//        return this.board[x][y].getType();
//    }
    
    public void clickCell(int x, int y) {

        Cell ruutu = getCell(x, y);
        if (!isGameStarted) {

            layMines(x, y);
        }

        if (!ruutu.isMarked() && !ruutu.isClicked()) { //only click unmarked and unclicked cells

            updateCell(x,y);

            if (ruutu.getTrueType() == 9) { // ruutu is mine -> reveal all of them and end game.

                System.out.println("clicked a mine -> lost game");

                putCell(x, y, 9, true, false);
                revealMines();
                isGameLost = true;

            } else if (adjacentMinesCount(x, y) == 0) { // cell is empty -> open adjacent cells

                remainingCells--;

                putCell(x, y, 0, true, false);
                openAdjacentCells(x, y);

            } else { //any other case mark the cell

                remainingCells--;

                putCell(x, y, adjacentMinesCount(x, y), true, false);
            }
        }
    }
    
    public void markCell(int x, int y) {
        
        Cell ruutu = getCell(x, y);

        if (!ruutu.isClicked() && isGameStarted) {

            updateCell(x,y);

            if (ruutu.isMarked()) { // unmark marked cell

                markCount--;

                board[x][y].mark();

//                    putCell(x, y, ruutu.getTrueType(), false, false);

            } else { // mark the cell

                markCount++;

                board[x][y].mark();

//                    putCell(x, y, ruutu.getTrueType(), false, true);

            }
        }
        updateCell(x,y);
    }

    private void updateCell(int x, int y) {
        Set<Integer> setti = updatedCells.get(x);
        if(setti == null) {
            setti = new HashSet<>();
        }
        setti.add(y);
        updatedCells.put(x,setti);
    }

    public HashMap<Integer, Set<Integer>> getUpdatedCells() {
        return updatedCells;
    }

    public void clearChangedCells() {
        this.updatedCells.clear();
    }
    
    private int adjacentMinesCount(int x, int y) {
        
        int lkm = 0;
        
        //check 3x3 area around given coordinates
        for (int i = -1; i < 2; i++) { 
            for (int j = -1; j < 2; j++) { 
                
                // check that checked coordinates are inside of the array
                if ((x + i >= 0 && x + i < sizeX && y + j >= 0 && y + j < sizeY)) {
                    
                    //dont check the original cell
                    if (!((x + i) == x && (y + j) == y)) {
                        
                        // if cell is a mine
                        if (getCell(x + i, y + j).getTrueType() == 9) { 
                            
                            lkm++;
                        }
                    }
                }
            }
        }
        return lkm;
    }
    
    private void openAdjacentCells(int x, int y) {

        //check 3x3 area around given coordinates
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {

                // check that given coordinates are inside of the game array
                if ((x + i >= 0 && x + i < sizeX && y + j >= 0 && y + j < sizeY)) {

                    //dont check the original cell
                    if (!((x + i) == x && (y + j) == y)) {

                        clickCell(x + i, y + j);
                    }
                }
            }
        }
    }
    
    public Cell getCell(int x, int y) {
        return board[x][y];
    }
    
    private void putCell(int x, int y, int type, Boolean clicked, Boolean marked) {
        board[x][y] = new Cell(x, y, type, clicked, marked);
    }
}
