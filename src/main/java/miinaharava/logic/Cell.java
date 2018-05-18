
package miinaharava.logic;

public class Cell {

    private final int x;
    private final int y;
    
    private int type;
    private boolean clicked;
    private boolean marked;
    
//    enum Type {
//        ONE,
//        TWO,
//        THREE,
//        FOUR,
//        FIVE,
//        SIX,
//        SEVEN,
//        EIGHT,
//        COVER,
//        EMPTY,
//        MARKED,
//        MINE
//    }
    
    public Cell(int x, int y, int type, Boolean clicked, boolean marked) {
        this.clicked = clicked;
        this.marked = marked;
        this.x = x;
        this.y = y;
        this.type = type;
    }
    
    public Cell(int x, int y, int type) {
        this.clicked = false;
        this.marked = false;
        this.x = x;
        this.y = y;
        this.type = type;
    }
    
    public boolean isClicked() {
        return this.clicked;
    }
    
    public int getType() {
        if (this.clicked) {
            return this.type;
        } else {
            return 10;
        }
    }
    
//    public void setClicked() {
//        this.clicked = true;
//    }
    
    public Boolean isMarked() {
        return this.marked;
    }
    
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
    public int getTrueType() {
        return this.type;
    }
}
