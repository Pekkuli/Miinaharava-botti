
package miinaharava.ui;

import java.io.IOException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import miinaharava.logic.Board;

public class MiinaharavaUI extends Application {
    
    private Board lauta;
    private int seconds;
    
    
    private Text time;
    private Text wintext;
    private Text markcounter;
    
    Timeline timer;
    
    private Scene startscene;
    private Scene gameScene;
    
    private GridPane lautanodes;
    
    private Stage deathscreen;
    private Stage winscreen;
    
    private MediaPlayer deathsound;
    
    
    public ImageView createCellNode(int x, int y) {
        
        Image img = lauta.getRuutuIcon(x, y);
        ImageView imgv = new ImageView();
        imgv.setImage(img);
        imgv.setX(x);
        imgv.setY(y);
        
        // set node left and right click features e.g. right click -> mark cell, left click -> click cell, and then perform appropiate logic
        imgv.setOnMouseClicked((MouseEvent event) -> {
            markcounter.setText(lauta.getMarkcount());
            switch (event.getButton()) {
                
                case PRIMARY: // if mine is clicked end game 
                    
                    if(!lauta.getCell(x, y).isMarked()) {
                        
                        timer.play();
                        lauta.clickCell(x, y);
                        this.drawLauta();
                        
                        if (lauta.getCell(x, y).getTrueType() == 9) {
                            
                            lauta.revealMines();
                            seconds = 0;
                            this.drawLauta();
                            timer.stop();
                            deathsound.play();
                            deathscreen.show();
                            
                        } else {
                            if (this.lauta.checkIfGameIsWon()) {
                                
                                timer.stop();
                                wintext.setText("Completed in: " + formatTime(seconds));
                                seconds=0;
                                this.drawLauta();
                                winscreen.show();

                            } else { 
                            
                            }
                        }  
                    }
                    break;
                case SECONDARY:
                    
                    lauta.markCell(x, y);
//                    markcounter.setText(lauta.getMarkcount());
                    this.drawLauta();
                    
                    if(this.lauta.checkIfGameIsWon()) {
                        timer.stop();
                        wintext.setText("Completed in: " + formatTime(seconds));
                        seconds=0;
//                        this.lauta.revealMines();
                        this.drawLauta();
                        winscreen.show();
                        
                    } 
                    break;
                default:
                    break;
            }
        });
        
        return imgv;
    }
    
    public void drawLauta() { // clear existing nodes from gridpane and then add the new ones in
        lautanodes.getChildren().clear();
        for (int i = 0; i < lauta.getSizeX(); i++) {
            for (int j = 0; j < lauta.getSizeY(); j++) {
                lautanodes.add(createCellNode(i, j), i, j);
            }
        }
        
    }
    
//    public void reDrawLauta() {
//        
//        for (int i = 0; i < lauta.getSizeX(); i++) {
//            for (int j = 0; j < lauta.getSizeY(); j++) {
//                ImageView img = (ImageView) lautanodes.getChildren().get(lauta.getSizeX() * i + j);
//                lautanodes.getChildren().remove(img);
//                lautanodes.add(createCellNode(i, j), i, j);
//            }
//        }
//        
//    }
    
    @Override
    public void start(Stage primarystage) throws Exception {
        
        primarystage.setTitle("Miinaharava med bot bier");
        
        //########################### popupscene setup ################################
        
        deathscreen = new Stage();
        BorderPane popPane = new BorderPane();
        popPane.setPadding(new Insets(10,10,10,10));
        ImageView loseimg = new ImageView();
        loseimg.setImage(new Image("Images/"  + "YOUDIED.png"));
        popPane.setCenter(loseimg);
        Scene popscene = new Scene(popPane);
        Media sound = new Media(this.getClass().getResource("/YOUDIED.mp3").toString());
        deathsound = new MediaPlayer(sound);
        deathsound.volumeProperty().set(0.25);
        
        deathscreen.centerOnScreen();
        
        deathscreen.initModality(Modality.WINDOW_MODAL);
        deathscreen.initOwner(primarystage);
        deathscreen.setAlwaysOnTop(true);
        
        deathscreen.setOnShown(event -> time.setText(formatTime(seconds)));
        
        deathscreen.addEventHandler(MouseEvent.MOUSE_CLICKED, (mouseEvent) -> {
            deathsound.stop();
            deathscreen.hide();
            primarystage.setScene(startscene);
        });
        
        deathscreen.setOnCloseRequest((event) -> {
            deathsound.stop();
            primarystage.setScene(startscene);
        });
        
        deathscreen.setScene(popscene);
        
        
        
        //########################## winscene setup ##################################
        
        
        
        winscreen = new Stage();
        BorderPane winPane = new BorderPane();
        winPane.setPadding(new Insets(10,10,10,10));
        
        ImageView winimg = new ImageView();
        winimg.setImage(new Image("Images/"  + "WINSCREEN.png"));
        winPane.setCenter(winimg);
        BorderPane.setAlignment(winimg, Pos.CENTER);
        
        wintext = new Text();
        winPane.setTop(wintext);
        BorderPane.setAlignment(wintext, Pos.TOP_CENTER);
        
        Scene winscene = new Scene(winPane);
        
        winscreen.initModality(Modality.WINDOW_MODAL);
        winscreen.initOwner(primarystage);
        winscreen.setAlwaysOnTop(true);
        winscreen.centerOnScreen();
        
        winscreen.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent) -> {
            time.setText(formatTime(0));
            deathsound.stop();
            winscreen.hide();
            primarystage.setScene(startscene);
        });
        
        winscreen.setOnCloseRequest((event) -> {
            time.setText(formatTime(0));
            deathsound.stop();
            primarystage.setScene(startscene);
        });
        winscreen.setScene(winscene);
        
        
        
        //########################### startUpScene setup ################################
        
        
        
        lauta = new Board(0, 0, 0);

        BorderPane startPane = new BorderPane();
        startPane.setPadding(new Insets(10,10,10,10));
        
        ToolBar vaikeusasteet = new ToolBar();
        
        Button easy = new Button("start easy game");
        easy.setMinSize(100, 40);
        
        easy.setOnAction((ActionEvent event) -> {
            try {
                
                lauta = new Board(8, 8, 10);
                
            } catch (IOException ex) {
            }
            markcounter.setText("10");
            this.drawLauta();
            primarystage.setScene(gameScene);
            primarystage.sizeToScene();
            
        });
        
        Button medium = new Button("start medium game");
        medium.setMinSize(100, 40);
        
        medium.setOnAction((ActionEvent event) -> {
            try {
                
                lauta = new Board(16, 16, 40);
                
            } catch (IOException ex) {
            }
            markcounter.setText("40");
            this.drawLauta();
            primarystage.setScene(gameScene);
            primarystage.sizeToScene();
        });
        
        Button hard = new Button("start hard game");
        hard.setMinSize(100, 40);
        
        hard.setOnAction((ActionEvent event) -> {
            try {
                
                lauta = new Board(30, 16, 99);
                
            } catch (IOException ex) {
            }
            markcounter.setText("99");
            this.drawLauta();
            primarystage.setScene(gameScene);
            primarystage.sizeToScene();
            
        });
        
        vaikeusasteet.getItems().addAll(easy, medium, hard);
        vaikeusasteet.setOrientation(Orientation.VERTICAL);
        vaikeusasteet.setPrefHeight(140);
        
        startPane.setLeft(vaikeusasteet);
        startPane.setMinSize(300, 140);
        
        startscene = new Scene(startPane);
        
        
        
        //######################## gamescene setup #######################################
        
        seconds = 0;
        
        BorderPane gameborder = new BorderPane();
        gameborder.setPadding(new Insets(10,10,10,10));
        gameScene = new Scene(gameborder);
        
        
        time = new Text(formatTime(seconds));
        
        timer = new Timeline(new KeyFrame(Duration.seconds(1), (ActionEvent t) -> {
            seconds++;
            time.setText(formatTime(seconds));
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        
        markcounter = new Text();
        markcounter.setText(lauta.getMarkcount());
        
        Button reset = new Button("Reset game");
        
        reset.setOnAction((ActionEvent event) -> {
            lautanodes.getChildren().clear();
            timer.stop();
            lauta.reset();
            time.setText(formatTime(0));
            seconds=0;
            this.drawLauta();
        });
        
        Button showmine = new Button("Show mines");
        
        showmine.setOnAction((ActionEvent event) -> {
            if (lauta.isGameStarted() && !lauta.minesShown()) {
                lauta.revealMines();
                timer.stop();
                this.drawLauta();
            }
        });
        
        Button difficulty = new Button("Difficulty selection");
        
        difficulty.setOnAction((event) -> {
            markcounter.setText("0");
            time.setText(formatTime(0));
            seconds=0;
            primarystage.setScene(startscene);
        });

        VBox keski = new VBox();
        keski.getChildren().addAll(difficulty, reset);
        keski.setAlignment(Pos.CENTER);

        final Pane leftSpacer = new Pane();
        HBox.setHgrow(
                leftSpacer,
                Priority.SOMETIMES
        );
        
        final Pane rightSpacer = new Pane();
        HBox.setHgrow(
                rightSpacer,
                Priority.SOMETIMES
        );

        ToolBar gamebar = new ToolBar(
                markcounter,
                leftSpacer,
                keski,
                rightSpacer,
                this.time
        );
        
        gamebar.setOrientation(Orientation.HORIZONTAL);
        
        lautanodes = new GridPane();
        this.drawLauta();
        
        gameborder.setTop(gamebar);
        gameborder.setCenter(lautanodes);
        BorderPane.setAlignment(lautanodes, Pos.CENTER);
        
        
        
        //################################## mainscene setup ########################################
        
        primarystage.setScene(startscene);
        primarystage.setResizable(false);
        primarystage.sizeToScene();
        primarystage.centerOnScreen();
        primarystage.show();
    }
    
//    private void restart() {
//        try {
//            start(new Stage());
//        } catch (Exception e) {
//        }
//    }
    
    private static String formatTime(int time) {
        return String.format("%02d:%02d", time / 60, time % 60);
    }
}
