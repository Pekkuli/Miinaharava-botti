package miinaharava.logic;

public class DoubleCell {

    private int aX;
    private int aY;

    private int bX;
    private int bY;

    public DoubleCell(Cell a, Cell b) {

        this.aX = a.getX();
        this.aY = a.getY();

        this.bX = b.getX();
        this.bY = b.getY();

    }

    public int getaX() {
        return aX;
    }

    public int getaY() {
        return aY;
    }

    public int getbX() {
        return bX;
    }

    public int getbY() {
        return bY;
    }

    public boolean isNextToCell(Cell cell) {

        int x=cell.getX();
        int y=cell.getY();

        int dxa =Math.abs(aX -x);
        int dya =Math.abs(aY -y);

        int dxb=Math.abs(bX -x);
        int dyb=Math.abs(bY -y);

        return dxa <= 1 && dya <= 1 && dxb <= 1 && dyb <= 1;
    }

    public boolean contains(Cell cell) {

        int x = cell.getX();
        int y = cell.getY();

        return (aX == x && aY == y) || (bX == x && bY == y);

//        return this.a == cell || this.b == cell;
    }

    @Override
    public String toString() {

        return "DoubleCell{ "
                + aX +"," + aY +
                "; " + bX + "," + bY +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DoubleCell)) return false;
        DoubleCell that = (DoubleCell) o;

        return (aX == that.aX && aY == that.aY) || (bX == that.bX && bY == that.bY);


//        return Objects.equals(getA().toString(), that.getA().toString()) &&
//                Objects.equals(getB().toString(), that.getB().toString());
    }

    //
    @Override
    public int hashCode() {

        String str = String.valueOf(aX)+String.valueOf(aY) + String.valueOf(bX) + String.valueOf(bY);

        return Integer.parseInt(str);


    }
}
