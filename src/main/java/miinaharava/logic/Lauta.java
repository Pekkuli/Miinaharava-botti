
package miinaharava.logic;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Stack;
import javafx.scene.image.Image;

public final class Lauta {
    private final int kokox;
    private final int kokoy;
    private final int miinalkm;
    private boolean aloitettu;
    
    private final Image COVER;
    private final Image  EMPTY;
    private final Image  ONE;
    private final Image  TWO;
    private final Image  THREE;
    private final Image  FOUR;
    private final Image  FIVE;
    private final Image  SIX;
    private final Image  SEVEN;
    private final Image  EIGHT;
    private final Image  MINE;
    private final Image  MARKED;
    
    private Ruutu[][] lauta;
    private int[][] miinat;
    private Stack<Ruutu> paivitetytruudut;
    private  boolean showmines;
    
    
    public Lauta(int x, int y, int miinat) throws IOException{
        
        this.COVER = new Image("Images/"  +"COVER.png");
        this.EMPTY = new Image("Images/"  +"EMPTY.png");
        this.ONE = new Image("Images/"  +"1.png");
        this.TWO = new Image("Images/"  +"2.png");
        this.THREE = new Image("Images/"  +"3.png");
        this.FOUR = new Image("Images/"  +"4.png");
        this.FIVE = new Image("Images/"  +"5.png");
        this.SIX = new Image("Images/"  +"6.png");
        this.SEVEN = new Image("Images/"  +"7.png");
        this.EIGHT = new Image("Images/"  +"8.png");
        this.MINE = new Image("Images/"  +"MINE.png");
        this.MARKED = new Image("Images/"  +"MARKED.png");
        
        this.aloitettu=false;
        this.kokox=x;
        this.kokoy=y;
        this.miinalkm=miinat;
        this.lauta = new Ruutu[x][y];
        this.miinat = new int[x][y];
        this.showmines = false;
        this.paivitetytruudut = new Stack();
        
        for(int i=0;i<kokox;i++){
            for(int j=0;j<kokoy;j++){
                putRuutu(i,j,10,false,false);
            }
        }
    }
    
    public void aloita(int x, int y) throws IOException{
        AsetaMiinat(x,y);
        this.aloitettu=true;
    }
    
    public boolean onkoAloitettu(){
        return this.aloitettu;
    }
    
    public void AsetaMiinat(int hiirix, int hiiriy) throws IOException {
        Random rng = new Random();
        
        int i=0;
        while(i<this.miinalkm){
            int x =rng.nextInt(kokox);
            int y = rng.nextInt(kokoy);
            
            if(x == hiirix && y == hiiriy){
            } else if(miinat[x][y] != 1){
                putRuutu(x,y,9,false,false);
                miinat[x][y] = 1;
                i++;
            } else {
            }
        }
    }
    
    public boolean showMines() {
        return this.showmines;
    }
    
    public Stack<Ruutu> getPaivitetytRuudut() {
        return this.paivitetytruudut;
    }
    
    public void resetPaivitetyt() {
        this.paivitetytruudut = new Stack();
    }
    
    public Image getRuutuIcon(int x, int y) {
        Ruutu ruutu = getRuutu(x,y);
        if(ruutu.isMarked()){
            return this.MARKED;
        } else {
            switch(ruutu.getType()){
                case 0: return this.EMPTY;
                case 1: return this.ONE;
                case 2: return this.TWO;
                case 3: return this.THREE;
                case 4: return this.FOUR;
                case 5: return this.FIVE;
                case 6: return this.SIX;
                case 7: return this.SEVEN;
                case 8: return this.EIGHT;
                case 9: return this.MINE;
                case 10: return this.COVER;
            default:
                throw new AssertionError(ruutu.getType());
            }
        }
    }
    
    public void paivitaRuutu(Ruutu ruutu){
        
        if(!this.paivitetytruudut.contains(ruutu)){
            this.paivitetytruudut.add(ruutu);
        }
    }
    
    public void clickRuutu(int x, int y) throws IOException{
        Ruutu ruutu = getRuutu(x, y);
        
        if(!ruutu.isMarked() && !ruutu.isClicked()){ //only click unmarked and unclicked cells
            if(ruutu.GetTrueType() == 9) { // ruutu is mine reveal the mine
                
                putRuutu(x, y,9,true,false);
                paivitaRuutu(new Ruutu(x,y,9,true,false));
                
            } else if (vierekkäisetMiinat(x,y) ==0){
                
                putRuutu(x,y,0,true,false);
                paivitaRuutu(new Ruutu(x,y,0,true,false));
                avaaVierekkäiset(x,y);
                
            } else {
                
                putRuutu(x,y,vierekkäisetMiinat(x,y),true,false);
                paivitaRuutu(new Ruutu(x,y,vierekkäisetMiinat(x,y),true,false));
            }
        }
    }
    
    public void merkkaaRuutu(int x, int y) throws IOException {
        Ruutu ruutu = getRuutu(x,y);
        
        if(!ruutu.isClicked() && !this.aloitettu) { // only mark unclicked cell
        } else {
            if(!ruutu.isClicked()) {
                if(ruutu.isMarked()){ // unmark marked cell

                    putRuutu(x,y,ruutu.GetTrueType(),false,false);
                    paivitaRuutu(new Ruutu(x,y,10,false,false));

                } else { // mark the cell

                    putRuutu(x,y,ruutu.GetTrueType(),false,true);
                    paivitaRuutu(new Ruutu(x,y,10,false,true));

                }
            }
        }
    }
    
    public int vierekkäisetMiinat(int x, int y) {
        
        int lkm =0;
        
        //check 3x3 area around given coordinates
        for(int i=-1;i<2;i++){ 
            for(int j=-1;j<2;j++){ 
                
                // check that checked coordinates are inside of the array
                if((x + i >= 0 && x + i < this.kokox && y + j >= 0 && y + j < this.kokoy)) { 
                    
                    //dont check the original cell
                    if(((x+i) == x && (y+j) ==y) == false){ 
                        
                        // if cell is a mine
                        if(getRuutu(x+i,y+j).GetTrueType()==9){ 
                            
                            lkm++;
                        }
                    }
                }
            }
        }
        return lkm;
    }
    
    public void avaaVierekkäiset(int x, int y) throws IOException{
        
        //check 3x3 area around given coordinates
        for(int i=-1;i<2;i++){
            for(int j=-1;j<2;j++){
                
                // check that given coordinates are inside of the array
                if((x + i >= 0 && x + i < this.kokox && y + j >= 0 && y + j < this.kokoy)) { 
                    
                    //dont check the original cell
                    if(((x+i) == x && (y+j) ==y) == false){ 
                        
                        clickRuutu(x+i,y+j);
                    }
                }
            }
        }
    }
    
    public Ruutu getRuutu(int x,int y){
        return this.lauta[x][y];
    }
    
    public void putRuutu(int x,int y,int type, Boolean clicked, Boolean marked) throws IOException {
        lauta[x][y] = new Ruutu(x,y,type,clicked,marked);
    }
    
    public void updateRuutu(int x,int y,int type, Boolean clicked, Boolean marked) throws IOException {
        Ruutu ruutu = new Ruutu(x,y,type,clicked,marked);
        lauta[x][y] = ruutu;
        
        if(!this.paivitetytruudut.contains(ruutu)){
            this.paivitetytruudut.add(ruutu);
        }
    }
    
    public void naytaMiinat() throws IOException{
        
        for(int i=0;i<this.kokox;i++){
            for(int j=0;j< this.kokoy;j++){
                
                if(this.miinat[i][j] ==1){
                    
                    putRuutu(i,j,9,true,false);
                    paivitaRuutu(new Ruutu(i,j,9,true,false));
                    
                }
            }
        }
        this.showmines=true;
    }
}
