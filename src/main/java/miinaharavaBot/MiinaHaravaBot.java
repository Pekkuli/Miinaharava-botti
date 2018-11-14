
package miinaharavaBot;

import java.util.*;

import miinaharava.logic.Board;
import miinaharava.logic.Cell;
import miinaharava.logic.MultiCell;



public class MiinaHaravaBot {

    private ArrayList<Cell> clickedCells;
    private ArrayList<Cell> avaibleCells;
    private ArrayList<MultiCell> multiCells;
    private boolean isBotOn;
    private boolean isBotActive;
    private Board board;
    private ArrayList<Cell> clickableCells;
//    private double averageMineChance;
//    private int actions;


    public MiinaHaravaBot(Board brd) {

//        this.averageMineChance = (double) valueOf(board.getMineCountString()) / (board.getSizeX()*board.getSizeY());
        isBotOn = false;
        isBotActive = false;
        board = brd;
//        this.actions =0;

        avaibleCells = new ArrayList<>();
        clickedCells = new ArrayList<>();
        clickableCells = new ArrayList<>();
        multiCells = new ArrayList<>();


//        this.averageMineChance = 1.0*board.getMineCountInt() / (board.getSizeX() * board.getSizeX());
    }
    
    public void plei() {
        
        if(this.isBotActive && !board.isGameWon() && !board.isGameLost()) {

            updateCellLists();

            if(!board.isGameStarted()){

                clickRandomCell();

            } else{

                int remainingCells = board.getRemainingCellsCount();

//                System.out.println("checking obvious cells");

                markAllCertainMines();
                openSafeCells();

                if (remainingCells == board.getRemainingCellsCount()) {

//                    System.out.println("no obvious cells. trying multicells");

                    this.createMultiCells();
                    this.checkCellsWithMultiCells();

                    if(remainingCells == board.getRemainingCellsCount()) {
//                        System.out.println("average mine chance: " + this.averageMineChance + " " + getAverageChanceAsFraction());

//                        System.out.println("no multicells! clicking randomly");

                        clickRandomCell();

//                        if(remainingCells == board.getRemainingCellsCount()) {
//                            stop();
//                        }
                    }
                }
            }
        } else {
            stop();
        }

//        System.out.println("bot done playing this round -> into next round");
    }

//    private String getAverageChanceAsFraction() {
//
//        return  "(1 / " + String.valueOf( 1/averageMineChance) + ")";
//    }

//    private String getAverageMineChanceForCell(Cell cl) {
//
//        int mines = cl.getType() - this.getAdjacentMarkedCells(cl);
//
//        int surroundingCells = getSurroundingCells(cl).size();
//
//        if(surroundingCells == 0) {
//            return "0";
//        } else {
////            return (double) (mines / surroundingCells);
//            return mines + " / " + surroundingCells;
//        }
//    }

    private void createMultiCells(){

        multiCells.clear();

//        System.out.println("creating multicells");

        for(Cell cl: clickedCells) {

            if(cl.getType()- getAdjacentMarkedCells(cl) ==1) {

                ArrayList<Cell> lst = getSurroundingCells(cl);

                if(lst.size() < 5) {
//                    multiCells.add(new MultiCell(lst.get(0),lst.get(1)));
                    multiCells.add(new MultiCell(lst));

                }
            }
        }
//        removing duplicates
        Set<MultiCell> ls = new HashSet<>(multiCells);

        multiCells.addAll(ls);
        removeDuplicateMultiCells();
        multiCells.sort(Comparator.comparing(o -> o.getCell(0).toString()));
    }

    private void removeDuplicateMultiCells() {

        ArrayList<MultiCell> list = new ArrayList<>();

        for(MultiCell mc: multiCells) {

            if(!list.contains(mc)) {
                list.add(mc);
            }
        }
        multiCells = list;
    }

//    private boolean areCellsAdjacent(Cell c1, Cell c2) {
//
//        int dx = Math.abs(c1.getX()-c2.getX());
//        int dy = Math.abs(c1.getY()-c2.getY());
//
//        return dx +dy == 1;
//
//    }

    private void checkCellsWithMultiCells() {

//        System.out.println("Checking multicells");

        for (Cell cl: clickedCells) {

//            System.out.println("Clicked cells:" + clickedCells.size());
//            System.out.println("Looking at cell:" +cl);

            for (MultiCell multiCell : multiCells) {

//                System.out.println("Multicells: " + multiCells.size());
//                System.out.println("looking at multicell:" +multiCell);

                ArrayList<Cell> surroundingCells = getSurroundingCells(cl);
                int minesLeft = effectiveSurroundingMines(cl);

//                MultiCell multiC = multiCell;

                if (multiCell.nextToCell(cl)) {

//                    System.out.println("Accepted cell: " + cl);

                    if (multiCell.containsMarks()) {
                        minesLeft -= multiCell.markCount()+1;
                    } else {
                        minesLeft--;
                    }

//                    System.out.println("mines left: " + minesLeft);

                    surroundingCells.removeAll(multiCell.getCells());

                    if (!surroundingCells.isEmpty()) {

                        if (minesLeft == 0) {

                            for (Cell cll : surroundingCells) {

//                                System.out.println("Opening empty cell at: " + cll.toString() + " " + cl.toString() + " " + multiCell.toString());

                                botLeftClick(cll);
                            }

                        } else if (minesLeft == 1 && surroundingCells.size() == 1) {

                            Cell a = surroundingCells.get(0);

                            if (!a.isMarked()) {

//                                System.out.println("Marking a mine at: " + a.toString() + " " + cl.toString() + " " + multiCell.toString());

                                botRightClick(a);
                            }
                        }
                    }
                }
            }
        }
//        System.out.println("done looking at multicells");
    }

//    private void checkOneTwoRule() {
//
//        for(Cell cl: clickedCells) {
//
//            if( (effectiveSurroundingMines(cl) == 1) && getSurroundingCells(cl).size() == 3) {
//
//                if(isCellNextToTwo(cl) ) {
//
//
//
//                }
//
//            }
//        }
//    }

//    private boolean isCellNextToTwo(Cell cl) {
//
//        int x = cl.getX();
//        int y = cl.getY();
//
//        if( (board.getCellType(x - 1, y) - getAdjacentMarkedCells(cl) == 2) && (getSurroundingCells(x - 1, y).size() == 3) ) {
//            return true;
//        } else if((board.getCellType(x + 1, y) - getAdjacentMarkedCells(cl) == 2) && (getSurroundingCells(x + 1, y).size() == 3) ) {
//            return true;
//        } else if( (board.getCellType(x,y - 1) - getAdjacentMarkedCells(cl) == 2) && (getSurroundingCells(x , y - 1).size() == 3) ) {
//            return true;
//        } else return (board.getCellType(x, y + 1) - getAdjacentMarkedCells(cl) == 2) && (getSurroundingCells(x, y + 1).size() == 3);
//    }

    private int effectiveSurroundingMines(Cell cl) {
        return cl.getType() - getAdjacentMarkedCells(cl);
    }

    private void clickRandomCell() {

        Random rng = new Random();

//        int s = rng.nextInt(3);

        updateCellLists();

        while(true) {
//
//            int x = rng.nextInt(board.getSizeX());
//            int y = rng.nextInt(board.getSizeY());

            if(clickableCells.isEmpty()){
                break;
            }

            int x = rng.nextInt(clickableCells.size());

            Cell rngCell = clickableCells.get(x);

            if(rngCell.getType() == 10 && !rngCell.isMarked()) {

                botLeftClick(rngCell);
//                System.out.println("Bot clicking randomly at: "+ board.getCell(x,y));

                if(board.isGameLost()) {
                    System.out.println("FEK! Bot accidentally the whole mine");
                    System.out.println("-> bot into kys");
                    stop();
                }

                break;
            }
        }

    }

    private void openSafeCells() {

        Iterator<Cell> iterator = this.clickedCells.iterator();

        while(iterator.hasNext()) {

            Cell cl = iterator.next();

//            only check cell if it is not cover or empty
            if(cl.getType() !=10 && cl.getType() !=0) {

                List<Cell> lst = this.getSurroundingCells(cl);

//                open all surrounding covered cells if cell has the same amount of neighboring marked cells as mines
                if(getAdjacentMarkedCells(cl) == cl.getType()) {

                    for(Cell cell: lst) {

                        botLeftClick(cell);

                    }
                    iterator.remove();
                }
            }
        }
    }
    
    private void markAllCertainMines() {

        Iterator<Cell> iterator = this.clickedCells.iterator();

        while(iterator.hasNext()) {
            Cell cl = iterator.next();

            if(cl.getType() != 10) {

                List<Cell> lst = this.getSurroundingCells(cl);

                if(cl.getType() - getAdjacentMarkedCells(cl) == lst.size()) {

                    for(Cell cell: lst) {

                        botRightClick(cell);
                    }
                    iterator.remove();
                }
            }
        }

    }

    private void getAllClickedCells() {

        clickedCells.clear();

        for(Cell cl: avaibleCells) {

            if(cl.getType() !=10 && !cl.isMarked() && !this.getSurroundingCells(cl).isEmpty()) {
                this.clickedCells.add(cl);
            }
        }
    }

    private void getAllClickableCells() {

        clickableCells.clear();

        for(Cell cl: avaibleCells) {

            if(cl.getType() ==10 && !cl.isMarked()) {
                clickableCells.add(cl);
            }
        }
    }

    private void updateCellLists() {

//        System.out.println("updating bot cell-lists");

        avaibleCells.clear();

        for( int i=0;i< board.getSizeX();i++) {
            for(int j=0;j<board.getSizeY();j++) {
                this.avaibleCells.add(board.getCell(i,j));
            }
        }
        getAllClickedCells();
        getAllClickableCells();
    }
    
    private ArrayList<Cell> getSurroundingCells(Cell cell) {

        int x = cell.getX();
        int y = cell.getY();

        return  getSurroundingCells(x,y);
    }

    private ArrayList<Cell> getSurroundingCells(int x, int y) {
        ArrayList<Cell> list = new ArrayList<>();

        //check 3x3 area around given coordinates
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {

                // check that checked coordinates are inside of the array
                if (x + i >= 0 && x + i < board.getSizeX() && y + j >= 0 && y + j < board.getSizeY()) {

                    //don't check the original cell
                    if (!(x + i == x && y + j == y)) {

                        Cell cl = board.getCell(x+i,y+j);

                        // if cell is a untouched cell
                        if (cl.getType() == 10 && !cl.isMarked()) {
                            list.add(cl);
                        }
                    }
                }
            }
        }
        return list;
    }
    
    private int getAdjacentMarkedCells(Cell cell) {
        int lkm = 0;

        int x = cell.getX();
        int y = cell.getY();
        
        //check 3x3 area around given coordinates
        for (int i = -1; i < 2; i++) { 
            for (int j = -1; j < 2; j++) { 
                
                // check that checked coordinates are inside of the array
                if (x + i >= 0 && x + i < board.getSizeX() && y + j >= 0 && y + j < board.getSizeY()) {
                    
                    //don't check the original cell
                    if (!(x + i == x && y + j == y)) {
                        
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

    private void botRightClick(Cell cl) {
        board.markCell(cl.getX(), cl.getY());
    }

//    private void botRightClick(int x, int y) {
//        board.markCell(x, y);
//    }

    private void botLeftClick(Cell cl) {
        board.clickCell(cl.getX(), cl.getY());
    }

//    private void botLeftClick(int x, int y) {
//        board.clickCell(x, y);
//    }

    public void deActivate() {
        this.isBotActive = false;
        this.isBotOn = false;
    }

    public boolean isBotActive() {
        return isBotActive;
    }

    public void start(){
        this.isBotOn=true;
        this.isBotActive=true;
        System.out.println("bot has been started");
    }
    
    public void stop(){
        this.isBotOn=false;
        this.clickedCells.clear();
        this.multiCells.clear();
        System.out.println("stopping bot");
    }

}
