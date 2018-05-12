
package miinaharava.logic;

import java.io.IOException;

public class Ruutu {
    private int type;
    private boolean clicked;
    private boolean marked;
    
    public Ruutu(int type, Boolean clicked, boolean marked) throws IOException {
        this.clicked=clicked;
        this.marked=marked;
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
//        if(this.type == 9){
//            return 9;
//        } else {
//            return this.type;
//        }
    }
    
    public boolean isMarked(){
        return this.marked;
    }
    
    public int GetTrueType(){
        return this.type;
    }
}
