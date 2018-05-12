
package miinaharava.logic;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import javafx.scene.image.Image;

public final class Lauta {
    private final int kokox;
    private final int kokoy;
    private final int miinalkm;
    private boolean aloitettu;
    
    private Ruutu[][] lauta;
    private int[][] miinat;
    
    public Lauta(int x, int y, int miinat) throws IOException{
        this.aloitettu=false;
        this.kokox=x;
        this.kokoy=y;
        this.miinalkm=miinat;
        this.lauta = new Ruutu[x][y];
        this.miinat = new int[x][y];
        
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
            } else if(getRuutu(x,y).getType() == 10){
                putRuutu(x,y,9,false,false);
                miinat[x][y] = 1;
                i++;
            } else {
            }
        }
    }
    
    public Image getRuutuIcon(int x, int y) {
        Ruutu ruutu = getRuutu(x,y);
        if(ruutu.isMarked()){
            return new Image(new File("Images/"  +"MARKED.png").toURI().toString());
        } else {
            switch(ruutu.getType()){
                case 0: return new Image(new File("Images/"  +"EMPTY.png").toURI().toString());
                case 1: return new Image(new File("Images/"  +"1.png").toURI().toString());
                case 2: return new Image(new File("Images/"  +"2.png").toURI().toString());
                case 3: return new Image(new File("Images/"  +"3.png").toURI().toString());
                case 4: return new Image(new File("Images/"  +"4.png").toURI().toString());
                case 5: return new Image(new File("Images/"  +"5.png").toURI().toString());
                case 6: return new Image(new File("Images/"  +"6.png").toURI().toString());
                case 7: return new Image(new File("Images/"  +"7.png").toURI().toString());
                case 8: return new Image(new File("Images/"  +"8.png").toURI().toString());
                case 9: return new Image(new File("Images/"  +"MINE.png").toURI().toString());
                case 10: return new Image(new File("Images/"  +"COVER.png").toURI().toString());
            default:
                throw new AssertionError(ruutu.getType());
            }
        }
        
    }
    
    public void clickRuutu(int x, int y) throws IOException{
        Ruutu ruutu = getRuutu(x, y);
        
        if(!ruutu.isMarked() && !ruutu.isClicked()){ //only click unmarked and unclicked cells
            if(ruutu.GetTrueType() == 9) {
                
                putRuutu(x, y,9,true,false);
                
            } else if (vierekkäisetMiinat(x,y) ==0){
                
                putRuutu(x,y,0,true,false);
                avaaVierekkäiset(x,y);
                
            } else {
                
                putRuutu(x,y,vierekkäisetMiinat(x,y),true,false);
            }
        }
    }
    
    public void merkkaaRuutu(int x, int y) throws IOException {
        Ruutu ruutu = getRuutu(x,y);
        
        if(ruutu.isClicked() || !this.aloitettu) { // only mark unclicked cell
            
        } else if(getRuutu(x,y).isMarked()){ // unmark marked cell
            
            putRuutu(x,y,ruutu.GetTrueType(),false,false);
        } else { // mark the cell
            
            putRuutu(x,y,ruutu.GetTrueType(),false,true);
        }
    }
    
    public int vierekkäisetMiinat(int x, int y) {
        int lkm =0;
        for(int i=-1;i<2;i++){ 
            for(int j=-1;j<2;j++){ //check 3x3 area around given coordinates
                if((x + i >= 0 && x + i < this.kokox && y + j >= 0 && y + j < this.kokoy)) { // check that given coordinates are inside of the array
                    if(((x+i) == x && (y+j) ==y) == false){ //dont check the original cell
                        if(getRuutu(x+i,y+j).GetTrueType()==9){ // if cell is a mine
                            lkm++;
                        }
                    }
                }
            }
        }
        return lkm;
    }
    
    public void avaaVierekkäiset(int x, int y) throws IOException{
        Ruutu[][] avattavat = new Ruutu[this.kokox][this.kokoy];
        for(int i=-1;i<2;i++){
            for(int j=-1;j<2;j++){
                if((x + i >= 0 && x + i < this.kokox && y + j >= 0 && y + j < this.kokoy)) { // check that given coordinates are inside of the array
                    if(((x+i) == x && (y+j) ==y) == false){ //dont check the original cell
                        avattavat[x+i][y+j] = new Ruutu(vierekkäisetMiinat(x+i,y+j),true,false);
//                        putRuutu(x+i,y+j,vierekkäisetMiinat(x+i,y+j),true,false);
                    }
                }
            }
        }
    }
    
    public void vierekkäisetTyhjät(int x, int y){
        
    }
    
    public Ruutu getRuutu(int x,int y){
        return this.lauta[x][y];
    }
    
    public void putRuutu(int x,int y,int type, Boolean clicked, Boolean marked) throws IOException {
        lauta[x][y] = new Ruutu(type,clicked,marked);
    }
    
    public void naytaMiinat() throws IOException{
        for(int i=0;i<this.kokox;i++){
            for(int j=0;j< this.kokoy;j++){
                if(this.miinat[i][j] ==1){
                    this.lauta[i][j] = new Ruutu(9,true,false);
                }
            }
        }
    }
}
