
package miinaharava.ui;

import java.io.IOException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import miinaharava.logic.Lauta;

public class MiinaharavaUI extends Application{
    
    private Lauta lauta;
    private int seconds;
    
    Timeline timer;
    
    private Scene startscene;
    private Scene gamescene;
    
    private GridPane lautanodes;
    
    private Stage popup;
    
    private MediaPlayer mediap;
    
    public ImageView createRuutuNode(int x, int y) {
        Image img = lauta.getRuutuIcon(x, y);
        ImageView imgv =new ImageView();
        imgv.setImage(img);
        imgv.setX(x);
        imgv.setY(y);
        
        imgv.setOnMouseClicked((MouseEvent event) -> {
            switch (event.getButton()) {
                case PRIMARY:
                    
                    if(lauta.getRuutu(x, y).GetTrueType() ==9) {
                        
                        try {
                            lauta.showMines();
                        } catch (IOException ex) {
                        }
                        this.drawLauta();
                        timer.stop();
                        mediap.play();
                        popup.show();
                    } else {
                        try {
                            timer.play();
                            lauta.clickRuutu(x,y);
                            this.drawLauta();

                        } catch (IOException ex) {
                        }
                    }
                    
                case SECONDARY: {
                    
                    try {
                        
                        lauta.merkkaaRuutu(x,y);
                        this.drawLauta();
                        
                    } catch (IOException ex) {
                    }
                }   break;
                default:
                    break;
            }
        });
        
        return imgv;
    }
    
    public void drawLauta() {
        lautanodes.getChildren().clear();
        for(int i=0;i<lauta.getKokox();i++) {
            for(int j=0;j<lauta.getKokoy();j++) {
                lautanodes.add(createRuutuNode(i,j),i,j);
            }
        }
        
    }
    
    public void reDrawLauta() {
        
        for(int i=0;i<lauta.getKokox();i++) {
            for(int j=0;j<lauta.getKokoy();j++) {
                ImageView img = (ImageView) lautanodes.getChildren().get(lauta.getKokox()*i+j);
                lautanodes.getChildren().remove(img);
                lautanodes.add(createRuutuNode(i,j),i,j);
            }
        }
        
    }
    
    @Override
    public void start(Stage primarystage) throws Exception {
        
        primarystage.setTitle("Miinaharava med bot bier");
        
        //########################### popupscene setup ################################
        
        popup = new Stage();
        BorderPane poppane = new BorderPane();
        ImageView imgv = new ImageView();
        imgv.setImage(new Image("Images/"  +"YOUDIED.png"));
        poppane.setCenter(imgv);
        Scene popscene = new Scene(poppane);
        Media sound = new Media(this.getClass().getResource("/YOUDIED.mp3").toString());
        mediap = new MediaPlayer(sound);
        mediap.volumeProperty().set(0.25);
        
        popup.centerOnScreen();
        
        popup.initModality(Modality.WINDOW_MODAL);
        popup.initOwner(primarystage);
        popup.setAlwaysOnTop(true);
        popup.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent) -> {
            mediap.stop();
            popup.hide();
            primarystage.setScene(startscene);
        });
        
        popup.setOnCloseRequest((WindowEvent event) -> {
            mediap.stop();
            primarystage.setScene(startscene);
        });
        
        popup.setScene(popscene);
        
        
        
        
        //########################### startupscene setup ################################
        
        
        
        lauta = new Lauta(0,0,0);

        BorderPane startpane = new BorderPane();
        
        ToolBar vaikeusasteet = new ToolBar();
        
        Button easy = new Button("start easy game");
        easy.setMinSize(100, 40);
        
        easy.setOnAction((ActionEvent event) -> {
            try {
                
                lauta = new Lauta(8,8,10);
                
            } catch (IOException ex) {
            }
            this.drawLauta();
            primarystage.setScene(gamescene);
            primarystage.sizeToScene();
            
        });
        
        Button medium = new Button("start medium game");
        medium.setMinSize(100, 40);
        
        medium.setOnAction((ActionEvent event) -> {
            try {
                
                lauta = new Lauta(16,16,40);
                
            } catch (IOException ex) {
            }
            this.drawLauta();
            primarystage.setScene(gamescene);
            primarystage.sizeToScene();
        });
        
        Button hard = new Button("start hard game");
        hard.setMinSize(100, 40);
        
        hard.setOnAction((ActionEvent event) -> {
            try {
                
                lauta = new Lauta(30,24,200);
                
            } catch (IOException ex) {
            }
            this.drawLauta();
            primarystage.setScene(gamescene);
            primarystage.sizeToScene();
            
        });
        vaikeusasteet.getItems().addAll(easy,medium,hard);
        vaikeusasteet.setOrientation(Orientation.VERTICAL);
        startpane.setLeft(vaikeusasteet);
        startpane.setMinSize(300, 150);
        
        startscene = new Scene(startpane);
        
        
        
        //######################## gamescene setup #######################################
        
        seconds = 0;
        
        BorderPane gameborder = new BorderPane();
        gamescene = new Scene(gameborder);
        
        Button reset = new Button("Reset game");
        
        Button showmine = new Button("Show mines");
        showmine.setMaxSize(80, 25);
        
        Text text = new Text(FormatTime(seconds));
        
        timer = new Timeline(new KeyFrame(Duration.seconds(1), (ActionEvent t) -> {
            seconds++;
            text.setText(FormatTime(seconds));
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        
        
        ToolBar gamebar = new ToolBar();
        gamebar.setOrientation(Orientation.HORIZONTAL);
        gamebar.setPrefHeight(25);
        
        BorderPane.setAlignment(text, Pos.TOP_RIGHT);
        BorderPane.setAlignment(gamebar, Pos.TOP_LEFT);
        gamebar.getItems().addAll(reset,showmine,text);
        
        
        reset.setOnAction((ActionEvent event) -> {
            lautanodes.getChildren().clear();
            timer.stop();
            try {
                lauta.reset();
                text.setText(FormatTime(0));
                this.drawLauta();
            } catch (IOException ex) {
            }
        });
        
        showmine.setOnAction((ActionEvent event) -> {
            if(lauta.onkoAloitettu() && !lauta.minesShown()){
                try {
                    lauta.naytaMiinat();
                    this.drawLauta();
                } catch (IOException ex) {
                }
            }
        });
        
        lautanodes = new GridPane();
        this.drawLauta();

        gameborder.setTop(gamebar);
        gameborder.setCenter(lautanodes);
        
        
        
        //################################## mainscene setup ########################################
        
        primarystage.setScene(startscene);
        primarystage.setResizable(false);
        primarystage.sizeToScene();
        primarystage.centerOnScreen();
        primarystage.show();
    }
    
    private void restart() {
        try {
            start(new Stage());
        } catch (Exception e) {
        }
    }
    
    private static String FormatTime(int time) {
        return String.format("%02d:%02d", time / 60, time % 60);
    }
}
