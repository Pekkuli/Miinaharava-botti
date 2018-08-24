
package miinaharavaBot;

import java.util.*;

import miinaharava.logic.Board;
import miinaharava.logic.Cell;
import miinaharava.logic.DoubleCell;

public class MiinaHaravaBot {

    private int clicks;
    private ArrayList<Cell> availableCells;
    private ArrayList<DoubleCell> doubleCells;
    private boolean isBotOn;
    private Board board;
//    private int actions;
    
    public MiinaHaravaBot(Board board) {
        
        this.isBotOn = false;
        this.board = board;
        this.clicks = 0;
//        this.actions =0;
        this.availableCells = new ArrayList<>();
        this.doubleCells = new ArrayList<>();
    }
    
    public void plei() {
        
        if(!board.checkIfGameIsWon() && !board.isGameLost()) {

            if(!board.isGameStarted()){

                clickRandomCell();
                clicks++;

            } else {

                this.getAllAvailableCells();

                int x = this.availableCells.size();

                if(clicks == x) {

                    clickRandomCell();
                    clicks++;

                } else {

                    int remainingCells = board.getRemainingCells();

                    this.markAllCertainMines();
                    this.openSafeCells();

                    if (remainingCells == board.getRemainingCells()) {

//                        System.out.println("available cells: "+this.availableCells.size());


//                        System.out.println("DoubleCells: "+doubleCells.size());
//                        for(DoubleCell dc: doubleCells) {
//                            System.out.println(dc.toString());;
//                        }
//                        stop();

                        createDoubleCells();

                        checkCellsWithDoubleCells();


//                        clickRandomCell();
//                        stop();
                        if(remainingCells == board.getRemainingCells()) {
                            stop();
                        }
                    }
                }

            }
        } else {
            stop();
        }
    }

    private void createDoubleCells(){

        Iterator<Cell> iterator = this.availableCells.iterator();

        while(iterator.hasNext()) {

            Cell cl = iterator.next();

            if(cl.getType()-adjacentMarkedCells(cl) ==1) {

                List<Cell> lst = getSurroundingCells(cl);

                if(lst.size() ==2) {
//                    doubleCells.add(new DoubleCell(lst.get(0),lst.get(1)));
                    iterator.remove();
                    if (areCellsAdjacent(lst.get(0),lst.get(1))) {
                          doubleCells.add(new DoubleCell(lst.get(0),lst.get(1)));
                    }
                }
            }
        }
//        removing duplicates
        Set<DoubleCell> ls = new HashSet<>(doubleCells);
        doubleCells.clear();
        doubleCells.addAll(ls);
//        doubleCells = (ArrayList<DoubleCell>) doubleCells.stream().distinct().collect(Collectors.toList());

    }

    private boolean areCellsAdjacent(Cell c1, Cell c2) {

        int dx = Math.abs(c1.getX()-c2.getX());
        int dy = Math.abs(c1.getY()-c2.getY());

        return (dx +dy) == 1;

    }

    private void checkCellsWithDoubleCells() {

        for (Cell cl: availableCells) {

            ArrayList<Cell> list = getSurroundingCells(cl);
            Iterator<DoubleCell> DBCLiterator = this.doubleCells.iterator();

            int minesLeft = cl.getType() - adjacentMarkedCells(cl);

            while(DBCLiterator.hasNext()) {

                DoubleCell dc = DBCLiterator.next();

                if(dc.isNextToCell(cl)) {

                    minesLeft--;

                    ArrayList<Cell> lst = removeDoubleCell(list, dc);

                    if(minesLeft == 0) {

                        Iterator<Cell> CLiterator = lst.iterator();

                        while(CLiterator.hasNext()) {

                            Cell cll = CLiterator.next();

                            System.out.println("There is a empty cell at:"+ cll.toString()+ " "+cl.toString() + " " + dc.toString());
                            board.clickCell(cll.getX(),cll.getY());

                            CLiterator.remove();
                        }
                    }

                    if(lst.size() == 1) {

                        Cell a = lst.get(0);

                        if(minesLeft == 1 && !a.isMarked()) {

                            System.out.println("There is a mine at: "+ a.toString());
                            board.markCell(a.getX(),a.getY());

                        }
//                        else {
//
//                            System.out.println("There is a empty cell at:"+ lst.get(0).toString());
//                            board.clickCell(a.getX(),a.getY());
//                        }
                        DBCLiterator.remove();
                    }
                }
            }
        }
    }

    private ArrayList<Cell> removeDoubleCell(ArrayList<Cell> list, DoubleCell dbcl) {

        Iterator<Cell> iterator = list.iterator();

        System.out.println("surrounding cells at start: "+list.size());

        int ax = dbcl.getaX();
        int ay = dbcl.getaY();

        int bx = dbcl.getbX();
        int by = dbcl.getbY();

        while(iterator.hasNext()) {

            Cell cl = iterator.next();

            if( (ax == cl.getX() && ay == cl.getY()) || (bx == cl.getX() && by == cl.getY()) ) {
                iterator.remove();
            }
        }

        System.out.println("surrounding cells at start: "+list.size());
        return list;
    }

    private void clickRandomCell() {

        Random rng = new Random();

        int s = rng.nextInt(3);

        while(true) {

            int l = rng.nextInt(board.getSizeX());
            int h = rng.nextInt(board.getSizeY());

            if(board.getCellType(l,h) == 10 && !board.getCell(l,h).isMarked()) {
                board.clickCell(l, h);
                break;
            }
        }
    }

    private void openSafeCells() {

        Iterator<Cell> iterator = this.availableCells.iterator();

        while(iterator.hasNext()) {

            Cell cl = iterator.next();

            if(cl.getType() !=10 && cl.getType() !=0) { /*only check cell if it is not cover or empty*/

                List<Cell> lst = this.getSurroundingCells(cl);

                if(adjacentMarkedCells(cl) == cl.getType()) { /*open all surrounding covered cells if cell has the same amount of neighboring marked cells as mines*/

                    for(Cell cell: lst) {

                        board.clickCell(cell.getX(), cell.getY());
                        System.out.println("bot clicked cell at: "+cell.getX()+","+cell.getY());

                    }
                    iterator.remove();
                }
            }
        }
    }
    
    private void markAllCertainMines() {

        Iterator<Cell> iterator = this.availableCells.iterator();

        while(iterator.hasNext()) {
            Cell cl = iterator.next();

            if(cl.getType() != 10) {

                List<Cell> lst = this.getSurroundingCells(cl);

                if(cl.getType() - adjacentMarkedCells(cl) == lst.size()) {

                    for(Cell cell: lst) {

                        System.out.println("bot marked cell at: "+cell.getX()+","+cell.getY());
                        board.markCell(cell.getX(), cell.getY());
                    }
                    iterator.remove();
                }
            }
        }

    }

    private void getAllAvailableCells() {

        this.availableCells.clear();

        for( int i=0;i< board.getSizeX();i++) {
            for(int j=0;j<board.getSizeY();j++) {

                if(board.getCellType(i,j) !=10 && !board.getCell(i,j).isMarked() && !this.getSurroundingCells(board.getCell(i,j)).isEmpty()) {
                    this.availableCells.add(board.getCell(i,j));
                }
            }
        }
    }
    
    private ArrayList<Cell> getSurroundingCells(Cell cell) {
        ArrayList<Cell> list = new ArrayList<>();

        int x = cell.getX();
        int y = cell.getY();

        //check 3x3 area around given coordinates
        for (int i = -1; i < 2; i++) { 
            for (int j = -1; j < 2; j++) { 
                
                // check that checked coordinates are inside of the array
                if ((x + i >= 0 && x + i < board.getSizeX() && y + j >= 0 && y + j < board.getSizeY())) { 
                    
                    //don't check the original cell
                    if (!((x + i) == x && (y + j) == y)) {

                        // if cell is a untouched cell
                        if (board.getCellType(x+i, y+j) == 10 && !board.getCell(x+i,y+j).isMarked()) {
                            list.add(board.getCell(x+i, y+j));
                        }
                    }
                }
            }
        }
        return list;
    }
    
    private int adjacentMarkedCells(Cell cell) {
        int lkm = 0;

        int x = cell.getX();
        int y = cell.getY();
        
        //check 3x3 area around given coordinates
        for (int i = -1; i < 2; i++) { 
            for (int j = -1; j < 2; j++) { 
                
                // check that checked coordinates are inside of the array
                if ((x + i >= 0 && x + i < board.getSizeX() && y + j >= 0 && y + j < board.getSizeY())) { 
                    
                    //don't check the original cell
                    if (!((x + i) == x && (y + j) == y)) {
                        
                        // if cell is marked
                        if (board.getCell(x + i, y + j).isMarked()) {
                            lkm++;
                        }
                    }
                }
            }
        }
        return lkm;
    }
    
    public boolean isBotOn() {
        return this.isBotOn;
    }
    
    public void start(){
        this.isBotOn=true;
        System.out.println("bot has been started");
    }
    
    public void stop(){
        this.isBotOn=false;
        this.clicks = 0;
        this.availableCells.clear();
        this.doubleCells.clear();
        System.out.println("bot is done");
    }

}
