package miinaharava.logic;

import java.util.ArrayList;

public class MultiCell {

    private ArrayList<Cell> cells;

    public MultiCell(ArrayList<Cell> clls) {

        cells = new ArrayList<>();

        for(Cell cl: clls) {

            if(cl != null) {
                cells.add(cl);
            }
        }

    }

    public ArrayList<Cell> getCells() {
        return this.cells;
    }

    private boolean cellsNextTo(Cell multiC, Cell insp) {

        int Dx = Math.abs(multiC.getX() - insp.getX());
        int Dy = Math.abs(multiC.getY() - insp.getY());

        return Dx <= 1 && Dy <= 1;
    }

    public boolean nextToCell(Cell cell) {

        boolean nextTo = true;

        for (Cell cl: this.cells) {

            if(!cellsNextTo(cl, cell)) {
                nextTo = false;
            }
        }

        return nextTo;
    }

    public boolean contains(Cell cell) {

        return this.cells.contains(cell);

    }

    public int markCount() {

        int marks = 0;

        for(Cell cl: cells) {
            if(cl.isMarked()) {
                marks++;
            }
        }

        return marks;
    }

    public boolean containsMarks() {

        return markCount() > 0;
    }

    public Cell getCell(int index) {
        return this.cells.get(index);
    }

    @Override
    public String toString() {

        String text = "MultiCell { ";

        for (Cell cl: cells) {
                text += cl.toString();
                text += ", ";
        }
        text += "}";

        return text;
//        return "MultiCell{ "
//                + aX +"," + aY +
//                "; " + bX + "," + bY +
//                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MultiCell)) return false;
        MultiCell that = (MultiCell) o;

        return this.toString().equals(that.toString());

//        return Objects.equals(getA().toString(), that.getA().toString()) &&
//                Objects.equals(getB().toString(), that.getB().toString());
    }

    @Override
    public int hashCode() {

//        String str = String.valueOf(aX)+String.valueOf(aY) + String.valueOf(bX) + String.valueOf(bY);

        int str = 0;

        for(Cell cl: cells) {
            str += cl.hashCode();
        }

        return Long.hashCode(str);
    }
}
