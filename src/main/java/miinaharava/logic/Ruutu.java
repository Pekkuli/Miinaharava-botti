
package miinaharava.logic;

import java.io.File;
import java.io.IOException;
import javafx.scene.image.Image;

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
    
    public Image getIcon() {
        if(marked){
            return new Image(new File("Images/"  +"MARKED.png").toURI().toString());
        } else {
            switch(type){
                case 0: return new Image(new File("Images/EMPTY.png").toURI().toString());
                case 1: return new Image(new File("Images/1.png").toURI().toString());
                case 2: return new Image(new File("Images/2.png").toURI().toString());
                case 3: return new Image(new File("Images/3.png").toURI().toString());
                case 4: return new Image(new File("Images/4.png").toURI().toString());
                case 5: return new Image(new File("Images/5.png").toURI().toString());
                case 6: return new Image(new File("Images/6.png").toURI().toString());
                case 7: return new Image(new File("Images/7.png").toURI().toString());
                case 8: return new Image(new File("Images/8.png").toURI().toString());
                case 9: return new Image(new File("Images/MINE.png").toURI().toString());
                case 10: return new Image(new File("Images/COVER.png").toURI().toString());
            default:
                throw new AssertionError(type);
            }
        }
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
