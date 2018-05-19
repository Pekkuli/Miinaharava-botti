
package miinaharavaBot;

import miinaharava.logic.Board;
import miinaharava.ui.MiinaHaravaUI;

public class MiinaHaravaBot {
    
    private int[][] board;
    private MiinaHaravaUI UI;
    
    
    
    public MiinaHaravaBot(MiinaHaravaUI ui) {
        
        this.UI = ui;
        Board brd = ui.getBoard();
        this.board = new int[brd.getSizeX()][brd.getSizeY()];
    }
    
    public void readBoard(Board board) {
        
        for (int i=0;i<board.getSizeX();i++) {
            for (int j=0;j<board.getSizeY();j++) {
                
                this.board[i][j] = board.getRuutuType(i, j);
                System.out.print(board.getRuutuType(i, j) + " ");
            }
            System.out.println("");
        }
    }
    
    
    
}
