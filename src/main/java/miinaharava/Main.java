package miinaharava;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import miinaharava.ui.MiinaharavaUI;

public class Main {
    
    public static void main(String[] args) throws URISyntaxException, MalformedURLException, IOException {
        
        javafx.application.Application.launch(MiinaharavaUI.class);
//        Image img = new Image(new File("MINE.png").toURI().toString());
        
//        
//        Random rng = new Random();
//        int x = 16;
//        int y = 16;
//        int miinat = 40;
//         
//        Lauta lt = new Lauta(x,y, miinat);
//        lt.AsetaMiinat(rng.nextInt(x),rng.nextInt(y));
//        
//        
//        
//        for (int i=0;i < x;i++){
//            for (int j=0;j< y;j++){
//                Ruutu rt = lt.getRuutu(i, j);
//                if(i==5 && j==7){
//                    System.out.print("[O]");
//                } else {
//                    switch(rt.getType()) {
//                    case EMPTY:
//                        break; 
//                    case COVER: System.out.print("[ ]");
//                        break;
//                    case MINE: System.out.print("[X]");
//                        break;
//                    default:
//                        throw new AssertionError(rt.getType().name());
//                    
//                }
//                }
//                
//            }
//            System.out.println("");
//        }
        

//        SwingUtilities.invokeLater(() -> {
//            JFrame ex = new MiinaharavaUI(10,10,30);
//            ex.setVisible(true);
//        });
    }
}
