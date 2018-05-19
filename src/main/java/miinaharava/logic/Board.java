
package miinaharava.logic;

import java.io.IOException;
import java.util.Random;
import javafx.scene.image.Image;

public final class Board {
    
    private final int sizex;
    private final int sizey;
    private final int minecount;
    
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
    
    private Cell[][] board;
    private int[][] mines;
//    private Stack<Ruutu> paivitetytruudut;
    private  boolean showmines;
    private boolean isgamestarted;
    private boolean isgamelost;
    private int remainingcells;
    private int markcount;
    
    
    public Board(int x, int y, int miinat) throws IOException {
        
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
        
        this.isgamestarted = false;
        this.sizex = x;
        this.sizey = y;
        this.remainingcells = x*y;
        this.markcount =0;
        this.minecount = miinat;
        this.board = new Cell[x][y];
        this.mines = new int[x][y];
        this.showmines = false;
        this.isgamelost = false;
        
        // fill board with empty cells
        for (int i = 0; i < sizex; i++) {
            for (int j = 0; j < sizey; j++) {
                putCell(i, j, 10, false, false);
            }
        }
    }

    public boolean isGameLost() {
        return isgamelost;
    }
    
    public void reset() {
        for (int i = 0; i < sizex; i++) {
            for (int j = 0; j < sizey; j++) {
                putCell(i, j, 10, false, false);
            }
        }
        
        System.out.println("lauta has been resetd");
        this.mines = new int[sizex][sizey];
        this.isgamestarted = false;
        this.showmines = false;
        this.isgamelost = false;
    }
    
    public int getSizeX() {
        return sizex;
    }

    public int getSizeY() {
        return sizey;
    }
    
    public boolean isGameStarted() {
        return this.isgamestarted;
    }

    public String getMarkcount() {
        return String.valueOf(this.minecount-this.markcount);
    }
    
    
    
    public void setMines(int mouseX, int mouseY) {
        System.out.println("laying mines");
        Random rng = new Random();
        
        int i = 0;
        while (i < this.minecount) {
            int x = rng.nextInt(sizex);
            int y = rng.nextInt(sizey);
            
            if ((mouseX == x) && (y == mouseY)) {
            } else if (mines[x][y] != 1) {
                putCell(x, y , 9, false, false);
                mines[x][y] = 1;
                i++;
            } else {
            }
        }
        this.isgamestarted = true;
    }
    
    public void revealMines() {
        int lkm =0;
        for (int i = 0; i < this.sizex; i++) {
            for (int j = 0; j < this.sizey; j++) {
                
                if (this.mines[i][j] == 1) {
                    this.board[i][j] = new Cell(i, j, 9, true, false);
                    lkm++;
                }
            }
        }
        System.out.println("mines: "+lkm);
    }
    
    public boolean minesShown() {
        return this.showmines;
    }
    
    public boolean checkIfGameIsWon() {
//        int count =0;
//        // check mine array
//        for (int i = 0; i < this.sizex; i++) {
//            for (int j = 0; j < this.sizey; j++) {
//                
//                if (this.mines[i][j] == 1) {
//                    // if mine is marked add count
//                    if(this.board[i][j].isMarked()) {
//                        count++;
//                    }
//                }
//            }
//        }
//        System.out.println(count);
//        return count == this.minecount;
        System.out.println(this.remainingcells);
        return this.remainingcells == this.minecount;
    }
    
//    public Stack<Ruutu> getPaivitetytRuudut() {
//        return this.paivitetytruudut;
//    }
//    
//    public void resetPaivitetyt() {
//        this.paivitetytruudut = new Stack();
//    }
    
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
    
    public int getRuutuType(int x, int y) {
        return this.board[x][y].getType();
    }
    
    public void clickCell(int x, int y) {
        
        Cell ruutu = getCell(x, y);
        if (!this.isgamestarted) {

            this.setMines(x, y);
        }
        if (!ruutu.isMarked() && !ruutu.isClicked()) { //only click unmarked and unclicked cells

            if(!ruutu.isMarked()) {
                if (ruutu.getTrueType() == 9) { // ruutu is mine -> reveal all of them and end game.

                    this.revealMines();
                    this.isgamelost = true;

                } else if (adjacentMinesCount(x, y) == 0) {
                    
                    this.remainingcells--;
                    putCell(x, y, 0, true, false);
                    openAdjacentCells(x, y);

                } else {
                    
                    this.remainingcells--;
                    putCell(x, y, adjacentMinesCount(x, y), true, false);
                }
            }
        }
    }
    
    public void markCell(int x, int y) {
        
        Cell ruutu = getCell(x, y);

        if (!ruutu.isClicked() && !this.isgamestarted) { // only mark unclicked cell
        } else {
            if (!ruutu.isClicked()) {
                if (ruutu.isMarked()) { // unmark marked cell
                    
                    this.markcount--;
                    putCell(x, y, ruutu.getTrueType(), false, false);

                } else { // mark the cell
                    
                    this.markcount++;
                    putCell(x, y, ruutu.getTrueType(), false, true);

                }
            }
        }
    }
    
    public int adjacentMinesCount(int x, int y) {
        
        int lkm = 0;
        
        //check 3x3 area around given coordinates
        for (int i = -1; i < 2; i++) { 
            for (int j = -1; j < 2; j++) { 
                
                // check that checked coordinates are inside of the array
                if ((x + i >= 0 && x + i < this.sizex && y + j >= 0 && y + j < this.sizey)) { 
                    
                    //dont check the original cell
                    if (((x + i) == x && (y + j) == y) == false) { 
                        
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
    
    public void openAdjacentCells(int x, int y) {
        
        //check 3x3 area around given coordinates
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {

                // check that given coordinates are inside of the array
                if ((x + i >= 0 && x + i < this.sizex && y + j >= 0 && y + j < this.sizey)) { 

                    //dont check the original cell
                    if (((x + i) == x && (y + j) == y) == false) { 

                        clickCell(x + i, y + j);
                    }
                }
            }
        }
    }
    
    public Cell getCell(int x, int y) {
        return this.board[x][y];
    }
    
    public void putCell(int x, int y, int type, Boolean clicked, Boolean marked) {
        board[x][y] = new Cell(x, y, type, clicked, marked);
    }
}
