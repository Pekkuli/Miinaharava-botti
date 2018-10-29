
package miinaharava.logic;

public class Cell {

    private final int x;
    private final int y;
    
    private int type;
    private boolean clicked;
    private boolean marked;
    
    Cell(int x, int y, int type, Boolean clicked, boolean marked) {
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
    
    public void mark() {
        this.marked = !(this.marked);
    }
    
    public void click() {
        this.clicked = true;
    }

    public boolean cellCoordsMatch(Cell cl) {

        int x = cl.getX();
        int y = cl.getY();

        return x == this.x && y == this.y;

    }

    public String getCoordsAsString() {
        return this.x + "," + this.y;
    }

    @Override
    public String toString() {
        return "Cell: " + x +
                "," + y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return this.toString().equals(cell.toString());
//                x == cell.x &&
//                y == cell.y;
    }

    @Override
    public int hashCode() {

        String asd = "";
        asd += this.x;
        asd += this.y;

        return Integer.parseInt(asd);

    }
}
