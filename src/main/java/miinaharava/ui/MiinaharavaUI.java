
package miinaharava.ui;

import java.io.IOException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import miinaharava.logic.Lauta;

public class MiinaharavaUI extends Application{
    
    private Lauta lauta;
    private int h;
    private int w;
    
    @Override
    public void start(Stage stage) throws Exception {
        
        w=100;
        h=100;
        
        this.lauta=new Lauta(w,h,4000);
        
        stage.setTitle("Miinaharava med bot bier");
        
        Canvas tausta = new Canvas(w*25,h*25);
        
        final GraphicsContext graphicsContext = tausta.getGraphicsContext2D();
        BorderPane panel = new BorderPane();
        
        drawMineField(tausta);
        panel.setCenter(tausta);
        Scene scene = new Scene(panel);
        
        Button reset = new Button("Reset game");
        
        Button showmine = new Button("Show mines");
        
        ToolBar toolbar = new ToolBar();
        toolbar.getItems().addAll(reset,showmine);
        toolbar.setOrientation(Orientation.HORIZONTAL);
        toolbar.setPrefHeight(25);
        
        panel.setTop(toolbar);
        
        reset.setOnAction((ActionEvent event) -> {
            stage.close();
            restart();
        });
        
        showmine.setOnAction((ActionEvent event) -> {
            if(lauta.onkoAloitettu()){
                try {
                    lauta.naytaMiinat();
                    drawMineField(tausta);
                } catch (IOException ex) {
                }
            }
        });
        
        scene.setOnMouseClicked((MouseEvent event) -> {
            
            int x =(int) event.getSceneX()/25;
            int y =(int) (event.getSceneY() -35)/25;
            
            if(null != event.getButton())switch (event.getButton()) {
                case PRIMARY:
                    if(!lauta.onkoAloitettu()){
                        try {
                            lauta.aloita(x,y);
                        } catch (IOException ex) {
                        }
                    }
                    try {
                        lauta.clickRuutu(x,y);
                        drawMineField(tausta);
                    } catch (IOException ex) {
                    }   break;
                case SECONDARY: {
                    try {
                        lauta.merkkaaRuutu(x,y);
                        drawMineField(tausta);
                    } catch (IOException ex) {
                    }
                }   break;
                default:
                    break;
            }
        });
        panel.layout();
        stage.setScene(scene);
        
        stage.setResizable(false);
        stage.sizeToScene();
        stage.show();
    }
    
    private void restart() {
        try {
            start(new Stage());
        } catch (Exception e) {
        }
    }
    
//    public void drawMineField(Canvas canvas, int x, int y) throws IOException {
//        GraphicsContext gc = canvas.getGraphicsContext2D();
//        gc.clearRect(x-1, y-1, 3, 3);
//        for(int i=x-1;i<x+2;i++) {
//            for(int j=y-1;j<y+2;j++) {
//                if(this.lauta.getRuutuIcon(i, j) != null){
//                    gc.drawImage(this.lauta.getRuutuIcon(i, j), x*25, y*25);
//                }
//            }
//        }
//    }
    
    public void drawMineField(Canvas canvas) throws IOException {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, w*24+35, h*24);
        for(int i=0;i<h;i++) {
            for(int j=0;j<w;j++) {
                gc.drawImage(this.lauta.getRuutuIcon(i, j), i*25, j*25);
            }
        }
    }
}
