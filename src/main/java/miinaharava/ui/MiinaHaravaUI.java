
package miinaharava.ui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import miinaharava.logic.Board;
import miinaharavaBot.MiinaHaravaBot;

public class MiinaHaravaUI extends Application {
    
    private Board lauta;
    private int seconds;
    private double timerScaling =0.2;
    
    
    private Text time;
    private Text winText;
    private Text markCounter;
    
    private Timeline timer;
    
    private Scene startScene;
    private Scene gameScene;
    
    private GridPane lautaNodes;
    
    private Stage deathScreen;
    private Stage winScreen;
    
    private MediaPlayer deathSound;
    private MiinaHaravaBot bot;


    private ImageView createCellNode(int x, int y) { // each imageview is its own cell in the game with built-in clicking actions
        
        Image img = lauta.getRuutuIcon(x, y);
        ImageView imgv = new ImageView();
        imgv.setImage(img);
        imgv.setX(x);
        imgv.setY(y);
        
        // set node left and right click features e.g. right click -> mark cell, left click -> click cell, and then perform appropiate logic
        imgv.setOnMouseClicked((MouseEvent event) -> {
            switch (event.getButton()) {
                
                case PRIMARY:
                    
                    if(!lauta.getCell(x, y).isMarked() && !lauta.getCell(x, y).isClicked()) {
                        
                        timer.play();
                        lauta.clickCell(x, y);
                        this.drawLauta();
                        
                        if (lauta.getCell(x, y).getTrueType() == 9) {
                            
                            seconds = 0;
                            timer.stop();
                            deathSound.play();
                            deathScreen.show();
                            break;
                            
                        } else {
                            WinConditionCheck();
                            break;
                        }  
                    }
                    break;
                    
                case SECONDARY:
                    
                    lauta.markCell(x, y);
                    imgv.setImage(lauta.getRuutuIcon(x, y));
                    this.drawLauta();

                    WinConditionCheck();
                    break;
                default:
                    break;
            }
        });
        
        return imgv;
    }

    private void WinConditionCheck() {
        if (this.lauta.checkIfGameIsWon()) {

            bot.stop();
            timer.stop();
            winText.setText("Completed in: " + formatTime(seconds));
            seconds=0;
            this.drawLauta();
            winScreen.show();
        }
    }

    private void drawLauta() { // clear existing nodes from gridpane and then add the new ones in
        lautaNodes.getChildren().clear();
        for (int i = 0; i < lauta.getSizeX(); i++) {
            for (int j = 0; j < lauta.getSizeY(); j++) {
                lautaNodes.add(createCellNode(i, j), i, j);
            }
        }
        
    }
    
    @Override
    public void start(Stage primaryStage) {
        
//        primaryStage.setTitle("Miinaharava med bot bier");
        
        //########################################## deathscene setup ################################
        
        deathScreen = new Stage();
        BorderPane deathpane = new BorderPane();
        deathpane.setPadding(new Insets(10,10,10,10));
        ImageView loseimg = new ImageView();
        loseimg.setImage(new Image("Images/"  + "YOUDIED.png"));
        deathpane.setCenter(loseimg);
        Scene popscene = new Scene(deathpane);
        Media sound = new Media(this.getClass().getResource("/YOUDIED.mp3").toString());
        deathSound = new MediaPlayer(sound);
        deathSound.volumeProperty().set(0.25);
        
        deathScreen.centerOnScreen();
        
        deathScreen.initModality(Modality.WINDOW_MODAL);
        deathScreen.initOwner(primaryStage);
        deathScreen.setAlwaysOnTop(true);
        
        deathScreen.setOnShown(event -> time.setText(formatTime(seconds)));
        
        Button resetBtn = new Button("Reset game");
        resetBtn.setOnAction((ActionEvent event) -> {
            deathSound.stop();
            deathScreen.hide();
            winScreen.hide();
            primaryStage.setScene(gameScene);
            primaryStage.centerOnScreen();
            lautaNodes.getChildren().clear();
            markCounter.setText(lauta.getMineCount());
            timer.stop();
            lauta.reset();
            time.setText(formatTime(0));
            seconds=0;
            this.drawLauta();
        });

        deathpane.setBottom(resetBtn);
        
        deathScreen.addEventHandler(MouseEvent.MOUSE_CLICKED, (mouseEvent) -> {
            deathSound.stop();
            deathScreen.hide();
            primaryStage.setScene(startScene);
            primaryStage.centerOnScreen();
        });
        
        deathScreen.setOnCloseRequest((event) -> {
            deathSound.stop();
            primaryStage.setScene(startScene);
            primaryStage.centerOnScreen();
        });
        
        deathScreen.setScene(popscene);
        
        
        
        //####################################### winscene setup ##################################
        
        
        
        winScreen = new Stage();
        BorderPane winPane = new BorderPane();
        winPane.setPadding(new Insets(10,10,10,10));
        
        ImageView winimg = new ImageView();
        winimg.setImage(new Image("Images/"  + "WINSCREEN.png"));
        winPane.setCenter(winimg);
        BorderPane.setAlignment(winimg, Pos.CENTER);
        
        winText = new Text();
        winPane.setTop(winText);
        winPane.setBottom(resetBtn);
        BorderPane.setAlignment(winText, Pos.TOP_CENTER);
        
        Scene winscene = new Scene(winPane);
        
        winScreen.initModality(Modality.WINDOW_MODAL);
        winScreen.initOwner(primaryStage);
        winScreen.setAlwaysOnTop(true);
        winScreen.centerOnScreen();
        
        winScreen.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent) -> {
            time.setText(formatTime(0));
            deathSound.stop();
            winScreen.hide();
            primaryStage.setScene(startScene);
            primaryStage.centerOnScreen();
        });
        
        winScreen.setOnCloseRequest((event) -> {
            time.setText(formatTime(0));
            deathSound.stop();
            primaryStage.setScene(startScene);
            primaryStage.centerOnScreen();
        });
        winScreen.setScene(winscene);
        
        
        
        //###################################### startUpScene setup ################################
        
        
        
//        lauta = new Board(0, 0, 0);

        BorderPane startPane = new BorderPane();
        startPane.setMinSize(200, 100);
        startPane.setPadding(new Insets(40,10,10,10));
        
        
        lautaNodes = new GridPane();
        
//        ToolBar vaikeusasteet = new ToolBar();
        
        HBox menuitems = new HBox();
        
//        ChoiceBox choices = new ChoiceBox();
//        choices.getItems().add("Easy");
//        choices.getItems().add("Medium");
//        choices.getItems().add("Hard");
//
//        HBox menu = new HBox(new Label("Choose difficulty"),choices);
        
//        startPane.setCenter(menu);
        
        MenuItem easy = new MenuItem("easy");
        MenuItem medium = new MenuItem("medium");
        MenuItem hard = new MenuItem("hard");
        
        easy.setOnAction((ActionEvent event) -> {
            lauta = new Board(8, 8, 10);
            this.bot = new MiinaHaravaBot(this.lauta);
            markCounter.setText("10");
            this.drawLauta();
            primaryStage.setScene(gameScene);
            primaryStage.sizeToScene();
            primaryStage.centerOnScreen();
        });

        medium.setOnAction((ActionEvent event) -> {
            lauta = new Board(16, 16, 40);
            this.bot = new MiinaHaravaBot(this.lauta);
            markCounter.setText("40");
            this.drawLauta();
            primaryStage.setScene(gameScene);
            primaryStage.sizeToScene();
            primaryStage.centerOnScreen();
        });
        
        hard.setOnAction((ActionEvent event) -> {
            lauta = new Board(30, 16, 99);
            this.bot = new MiinaHaravaBot(this.lauta);
            markCounter.setText("99");
            this.drawLauta();
            primaryStage.setScene(gameScene);
            primaryStage.sizeToScene();
            primaryStage.centerOnScreen();
        });
        
        MenuButton menu = new MenuButton("Choose difficulty",null, easy,medium,hard);
//        startPane.setLeft(menu);

        
        
        Button closeBtn = new Button("Exit");
        closeBtn.setOnAction((ActionEvent event) -> System.exit(0));
        
        menuitems.getChildren().addAll(menu,closeBtn);
        menuitems.setSpacing(10);
        
        startPane.setCenter(menuitems);

//        ChoiceDialog dialog = new ChoiceDialog((Object)"Easy", choices);
//        dialog.setTitle("");
//        dialog.setHeaderText(null);
//        dialog.setContentText("Choose difficulty:");
//
//        Button button = (Button)dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
//        button.setText("Exit");
//        Optional result = dialog.showAndWait();
//        
//        
//        if(result.isPresent()) {
//            switch ((String)result.get()) {
//                case "Easy" : {
//                    try {
//                        lauta = new Board(8, 8, 10);
//                        System.out.println("board init successfull (easy)");
//                    } catch (IOException ex) {
//                    }
//                    markCounter.setText("10");
//                    break;
//                }
//                case "Medium" : {
//                    try {
//                        lauta = new Board(16, 16, 40);
//                        System.out.println("board init successfull (medium)");
//                    } catch (IOException ex) {
//                    }
//                    markCounter.setText("40");
//                    break;
//                }
//                case "Hard" : {
//                    try {
//                        lauta = new Board(30, 16, 99);
//                        System.out.println("board init successfull (hard)");
//                    } catch (IOException ex) {
//                    }
//                    markCounter.setText("99");
//                    break;
//                }
//            }
//            this.drawLauta();
//            primaryStage.setScene(gameScene);
//            primaryStage.sizeToScene();
//        } else {
//            System.exit(0);
//        }
        
        
        
//        Button easy = new Button("start easy game");
//        easy.setMinSize(100, 40);
//        
//        easy.setOnAction((ActionEvent event) -> {
//            try {
//                
//                lauta = new Board(8, 8, 10);
//                
//            } catch (IOException ex) {
//            }
//            markCounter.setText("10");
//            this.drawLauta();
//            primaryStage.setScene(gameScene);
//            primaryStage.sizeToScene();
//            
//        });
//        
//        Button medium = new Button("start medium game");
//        medium.setMinSize(100, 40);
//        
//        medium.setOnAction((ActionEvent event) -> {
//            try {
//                
//                lauta = new Board(16, 16, 40);
//                
//            } catch (IOException ex) {
//            }
//            markCounter.setText("40");
//            this.drawLauta();
//            primaryStage.setScene(gameScene);
//            primaryStage.sizeToScene();
//        });
//        
//        Button hard = new Button("start hard game");
//        hard.setMinSize(100, 40);
//        
//        hard.setOnAction((ActionEvent event) -> {
//            try {
//                
//                lauta = new Board(30, 16, 99);
//                
//            } catch (IOException ex) {
//            }
//            markCounter.setText("99");
//            this.drawLauta();
//            primaryStage.setScene(gameScene);
//            primaryStage.sizeToScene();
//            
//        });
//        
//        vaikeusasteet.getItems().addAll(easy, medium, hard);
//        vaikeusasteet.setOrientation(Orientation.VERTICAL);
//        vaikeusasteet.setPrefHeight(140);
//        
//        startPane.setLeft(vaikeusasteet);
//        startPane.setMinSize(300, 140);
        
        startScene = new Scene(startPane);
//        startScene = new Scene(menu,200,100);
        
        
        
        //################################### gamescene setup #######################################
        
        seconds = 0;
        
        BorderPane gameborder = new BorderPane();
//        gameborder.setPadding(new Insets(10,10,10,10));
        gameScene = new Scene(gameborder);
        
        
        
        time = new Text(formatTime(seconds));
        
        timer = new Timeline(new KeyFrame(Duration.seconds(this.timerScaling), (ActionEvent t) -> {

            seconds++;
            time.setText(formatTime(seconds));
            markCounter.setText(lauta.getMarkCount());

            if(bot.isBotOn()){

                bot.plei();
                this.drawLauta();
                this.WinConditionCheck();

            }
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        
        markCounter = new Text();
        
        Button reset = new Button("Reset game");
        
        reset.setOnAction((ActionEvent event) -> {
            lautaNodes.getChildren().clear();
            timer.stop();
            lauta.reset();
            time.setText(formatTime(0));
            markCounter.setText(lauta.getMineCount());
            seconds=0;
            bot.stop();
            this.drawLauta();
            winScreen.hide();
        });

        Button botBtn = new Button("Activate bot");
        
        botBtn.setOnAction((ActionEvent event) -> {
            timer.play();
            bot.start();
        });
        
        Button showmine = new Button("Show mines");
        
        showmine.setOnAction((ActionEvent event) -> {
            if (lauta.isGameStarted() && !lauta.minesShown()) {
//                lauta.revealMines();
                timer.stop();
                this.drawLauta();
            }
        });
        
        Button difficulty = new Button("Difficulty selection");
        
        difficulty.setOnAction((event) -> {
            time.setText(formatTime(0));
            seconds=0;
            primaryStage.setScene(startScene);
            primaryStage.centerOnScreen();
        });

        VBox keski = new VBox();
        keski.getChildren().addAll(botBtn, difficulty, reset);
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
                markCounter,
                leftSpacer,
                keski,
                rightSpacer,
                this.time
        );
        
        gamebar.setOrientation(Orientation.HORIZONTAL);
        
        
//        this.drawLauta();
        
        gameborder.setTop(gamebar);
        gameborder.setCenter(lautaNodes);
        BorderPane.setAlignment(lautaNodes, Pos.CENTER);
        
        
        
        //######################################### mainscene setup ########################################
        
        primaryStage.setScene(startScene);
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    private String formatTime(int time) {
        return String.format("%02d:%02d", (int)(time*this.timerScaling) / 60,  (int)(time*this.timerScaling) % 60);
    }
    
//    public Board getBoard() {
//        return this.lauta;
//    }
}