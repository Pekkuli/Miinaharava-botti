
package miinaharava.logic;

import java.io.IOException;

public class Ruutu {

    private final int x;
    private final int y;
    
    private int type;
    private boolean clicked;
    private boolean marked;
    
    enum Type {
        ONE,
        TWO,
        THREE,
        FOUR,
        FIVE,
        SIX,
        SEVEN,
        EIGHT,
        COVER,
        EMPTY,
        MARKED,
        MINE
    }
    
    public Ruutu(int x, int y, int type, Boolean clicked, boolean marked) throws IOException {
        this.clicked=clicked;
        this.marked=marked;
        this.x = x;
        this.y = y;
        this.type=type;
    }
    
    public boolean isClicked(){
        return this.clicked;
    }
    
    public int getType(){
        if(this.clicked){
            return this.type;
        } else {
            return 10;
        }
    }
    
    public void setClicked() {
        this.clicked=true;
    }
    
    public void setMarked(Boolean marked) {
        this.marked = marked;
    }
    
    public Boolean isMarked() {
        return this.marked;
    }
    
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
    public int GetTrueType(){
        return this.type;
    }
}
