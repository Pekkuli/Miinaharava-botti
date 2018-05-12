
package miinaharava.ui;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import miinaharava.logic.Lauta;
import miinaharava.logic.Ruutu;

public class MiinaharavaUI extends Application{
    
    private Lauta lauta;
    private int h;
    private int w;
    private int seconds;
    
    @Override
    public void start(Stage stage) throws Exception {
        
        
        w=16;
        h=16;
        seconds = 0;
        
        this.lauta=new Lauta(w,h,40);
        
        stage.setTitle("Miinaharava med bot bier");
        
        Canvas tausta = new Canvas(w*25,h*25);
        
        final GraphicsContext graphicsContext = tausta.getGraphicsContext2D();
        BorderPane panel = new BorderPane();
        
        drawMineField(tausta);
        panel.setCenter(tausta);
        Scene scene = new Scene(panel);
        
        Button reset = new Button("Reset game");
        
        Button showmine = new Button("Show mines");
        
        Text text = new Text(getTime(seconds));
        
        Timeline timer = new Timeline(new KeyFrame(Duration.seconds(1), (ActionEvent t) -> {
            seconds++;
            text.setText(getTime(seconds));
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        
        
        ToolBar toolbar = new ToolBar();
        toolbar.getItems().addAll(reset,showmine,text);
        toolbar.setOrientation(Orientation.HORIZONTAL);
        toolbar.setPrefHeight(25);
        
        panel.setTop(toolbar);
        
        reset.setOnAction((ActionEvent event) -> {
            stage.close();
            restart();
        });
        
        showmine.setOnAction((ActionEvent event) -> {
            if(lauta.onkoAloitettu() && !lauta.showMines()){
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
                            timer.play();
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
    
    public void drawMineField(Canvas canvas) throws IOException {
        GraphicsContext gc = canvas.getGraphicsContext2D();
//        gc.clearRect(0, 0, w*24+35, h*24);
        if(!lauta.onkoAloitettu()) {
            for(int i=0;i<h;i++) {
                for(int j=0;j<w;j++) {
                    gc.drawImage(this.lauta.getRuutuIcon(i, j), i*25, j*25);
                    
                }
            }
        } else {
            System.out.println(lauta.getPaivitetytRuudut().size());
            for (Ruutu ruutu : this.lauta.getPaivitetytRuudut()) {
                gc.clearRect(ruutu.getX()*25+1, ruutu.getY()*25+1, 23, 23);
                gc.drawImage(ruutu.getIcon(),ruutu.getX()*25, ruutu.getY()*25);
            }
            this.lauta.resetPaivitetyt();
        }
    }
    
    private static String getTime(int time) {
        return String.format("%02d:%02d", time / 60, time % 60);
    }
}
