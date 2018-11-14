
package miinaharava.ui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import miinaharava.logic.Board;
import miinaharavaBot.MiinaHaravaBot;
import java.util.HashMap;
import java.util.Set;

public class MiinaHaravaUI extends Application {
    
    private Board board;
    private int seconds;
    private double timerScaling =0.2;
    
    private Text time;
    private Text mineCounter;
    
    private Timeline timer;
    
    private Scene startScene;
    private Scene gameScene;
    private DeathStage deathScreen;
    private WinStage winScreen;
    
    private GridPane lautaNodes;
    private ImageView[][] lautaNodeReferences;

    private MiinaHaravaBot bot;

    private ImageView createCellNode(int x, int y) { // each imageview is its own cell in the game with built-in clicking actions
        
        Image img = board.getRuutuIcon(x, y);
        ImageView imgv = new ImageView();
        imgv.setImage(img);
        imgv.setX(x);
        imgv.setY(y);
        
        // set node left and right click features e.g. right click -> mark cell, left click -> click cell, and then perform appropiate logic
        imgv.setOnMouseClicked((MouseEvent event) -> {
            switch (event.getButton()) {

                case NONE:
                    break;
                case PRIMARY:
                    
                    if(!board.getCell(x, y).isMarked() && !board.getCell(x, y).isClicked()) {
                        
                        timer.play();
                        board.clickCell(x, y);

                        updateGameScreen();

//                        imgv.setImage(board.getRuutuIcon(x,y));
//                        drawLauta();
                        
                        if (board.getCell(x, y).getType() == 9) {
                            loseGame();
                        } else {
                            WinGameCheck();
                        }  
                    }
                    break;

                case MIDDLE:

                    break;

                case SECONDARY:
                    
                    board.markCell(x, y);
//                    imgv.setImage(board.getRuutuIcon(x, y));
                    updateGameScreen();
//                    drawLauta();
                    WinGameCheck();
                    break;

                default:
                    break;
            }
        });
        
        return imgv;
    }

    private void updateGameScreen() {
        HashMap<Integer, Set<Integer>> cells = board.getUpdatedCells();
        if(!cells.keySet().isEmpty()) {
            for(int i:cells.keySet()) {
                for(int j: cells.get(i)) {
                    lautaNodeReferences[i][j].setImage(board.getRuutuIcon(i,j));
                }
            }
            board.clearChangedCells();
            mineCounter.setText(board.getRemainingMines());
        }
    }

    private void loseGame() {
        System.out.println("game lost!");
        if (bot.isBotOn()) {
            resetGame();
        } else {
            seconds = 0;
            timer.stop();
//            bot.stop();
//            board.revealMines();
            updateGameScreen();
            deathScreen.playSound();
            deathScreen.show();
        }
    }

    private void WinGameCheck() {
        if (this.board.isGameWon()) {
            System.out.println("game won!!!!");
            bot.stop();
            timer.stop();
            winScreen.setWinText("Completed in: " + formatTime(seconds));
            seconds=0;
            winScreen.playSound();
            winScreen.show();
        }
    }

    private void drawLauta() { // clear existing nodes from gridpane and then add the new ones in
        lautaNodes.getChildren().clear();
        for (int i = 0; i < board.getSizeX(); i++) {
            for (int j = 0; j < board.getSizeY(); j++) {
                ImageView imgv = createCellNode(i, j);
                lautaNodeReferences[i][j] = imgv;
                lautaNodes.add(imgv, i, j);
            }
        }
    }

    private void botPlei() {

        if(bot.isBotOn()) {
            bot.plei();
            updateGameScreen();
            WinGameCheck();
        } else if(board.isGameStarted()){
            System.out.println("Resetting game with bot");
            resetGame();
            bot.start();
        }
        time.setText(formatTime(seconds));
    }

    private void initBoard() {
        bot = new MiinaHaravaBot(this.board);
        lautaNodeReferences = new ImageView[board.getSizeX()][board.getSizeY()];
        mineCounter.setText(board.getRemainingMines());
        drawLauta();
        timer.play();
    }

    private void resetGame() {
        deathScreen.stopSound();
        deathScreen.hide();
        winScreen.stopSound();
        winScreen.hide();
        lautaNodes.getChildren().clear();
        board.reset();
        mineCounter.setText(board.getRemainingMines());
        time.setText(formatTime(0));
        seconds=0;
        drawLauta();
    }

    @Override
    public void start(Stage primaryStage) {
        
        primaryStage.setTitle("Miinaharava med bot bier");
        
        //########################################## deathscene setup ################################
        
        deathScreen = new DeathStage(primaryStage);

        Button resetBtnDeath = new Button("Reset game");
        resetButton(primaryStage, resetBtnDeath);

        deathScreen.setBottom(resetBtnDeath);

        clickStage(deathScreen,primaryStage);
        
        deathScreen.getStage().setOnCloseRequest((event) -> {
            deathScreen.stopSound();
            primaryStage.setScene(startScene);
            primaryStage.centerOnScreen();
        });

        //####################################### winscene setup ##################################

        winScreen = new WinStage(primaryStage);

        Button resetBtnWin = new Button("Reset game");
        resetButton(primaryStage, resetBtnWin);

        winScreen.setBottom(resetBtnWin);

        clickStage(winScreen,primaryStage);
        
        winScreen.getStage().setOnCloseRequest((event) -> {
            winScreen.stopSound();
            primaryStage.setScene(startScene);
            primaryStage.centerOnScreen();
        });

        //###################################### startUpScene setup ################################

        BorderPane startPane = new BorderPane();
        startPane.setMinSize(300, 100);
        startPane.setPadding(new Insets(40,10,10,10));

        lautaNodes = new GridPane();

        CustomGameStage customGameStage = new CustomGameStage(primaryStage);
        
        HBox startMenu = new HBox();
        VBox difficulty_menu = new VBox();

        Button easyBtn = new Button("Easy difficulty");
        Button mediumBtn = new Button("Medium difficulty");
        Button hardBtn = new Button("Hard difficulty");
        Button customGameBtn = new Button("Custom difficulty");

        Label gameInfoText = new Label("Select difficulty\nHover on buttons for details");
        gameInfoText.setWrapText(true);
        gameInfoText.setMinHeight(50);

        easyBtn.setOnMouseEntered(e -> gameInfoText.setText("Easy difficulty \n8 x 8 with 10 mines"));
        mediumBtn.setOnMouseEntered(e -> gameInfoText.setText("Medium difficulty \n16 x 16 with 40 mines"));
        hardBtn.setOnMouseEntered(e -> gameInfoText.setText("Hard difficulty \n30 x 16 with 99 mines"));
        customGameBtn.setOnMouseEntered(e -> gameInfoText.setText("Custom difficulty \nmake your own field"));

        easyBtn.setOnMouseClicked((MouseEvent event) -> {
            board = new Board(9, 9, 10);
            initBoard();
            primaryStage.setScene(gameScene);
            primaryStage.sizeToScene();
            primaryStage.centerOnScreen();
        });

        mediumBtn.setOnMouseClicked((MouseEvent event) -> {
//            board = new Board(35, 35, 196);
            board = new Board(16, 16, 40);
//            board = new Board(100, 100, 1600);
//            board = new Board(150, 150, 3600);
            initBoard();
            primaryStage.setScene(gameScene);
            primaryStage.sizeToScene();
            primaryStage.centerOnScreen();
        });
        
        hardBtn.setOnMouseClicked((MouseEvent event) -> {
            board = new Board(30, 16, 99);
            initBoard();
            primaryStage.setScene(gameScene);
            primaryStage.sizeToScene();
            primaryStage.centerOnScreen();
        });

        customGameBtn.setOnMouseClicked((MouseEvent event) -> {
            customGameStage.reset();
            customGameStage.show();
        });

        Button play = new Button("PLay!");

        play.setOnMouseClicked((MouseEvent event) ->{
            int x = customGameStage.getCurrentX();
            int y = customGameStage.getCurrentY();
            int mines = customGameStage.getCurrentMines();
            board = new Board(x,y,mines);
            initBoard();
            primaryStage.setScene(gameScene);
            primaryStage.sizeToScene();
            primaryStage.centerOnScreen();
            customGameStage.hide();
        });

        customGameStage.setPlayButton(play);



        difficulty_menu.setPrefWidth(150);
        easyBtn.setMinWidth(difficulty_menu.getPrefWidth());
        mediumBtn.setMinWidth(difficulty_menu.getPrefWidth());
        hardBtn.setMinWidth(difficulty_menu.getPrefWidth());
        customGameBtn.setMinWidth(difficulty_menu.getPrefWidth());
        difficulty_menu.getChildren().addAll(easyBtn,mediumBtn,hardBtn,customGameBtn);
        
        Button closeBtn = new Button("Exit");
        closeBtn.setOnAction((ActionEvent event) -> System.exit(0));

        VBox options_exit = new VBox();
        options_exit.getChildren().addAll(gameInfoText,closeBtn);
        options_exit.setSpacing(10);
        options_exit.setMinWidth(150);

        startMenu.getChildren().addAll(difficulty_menu,options_exit);
        startMenu.setSpacing(10);

        startMenu.setAlignment(Pos.CENTER);
        startMenu.setMinHeight(150);
        
        startPane.setCenter(startMenu);
        
        startScene = new Scene(startPane);

        //################################### gamescene setup #######################################
        
        seconds = 0;
        
        BorderPane gameborder = new BorderPane();
        gameScene = new Scene(gameborder);

        time = new Text(formatTime(seconds));
        
        timer = new Timeline(new KeyFrame(Duration.seconds(timerScaling), (ActionEvent t) -> {

            if(bot.isBotActive()) {
                botPlei();
//                drawLauta();
            }

            if(board.isGameStarted()) {
                seconds++;
                time.setText(formatTime(seconds));
                mineCounter.setText(board.getRemainingMines());
            }

            if(!board.isGameWon()) {
                updateGameScreen();
            }
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        
        mineCounter = new Text();
        Button reset = new Button("Reset game");
        
        reset.setOnAction((ActionEvent event) -> {
            resetGame();
            timer.stop();
            bot.deActivate();
        });

        Button botBtn = new Button("Activate bot");
        botBtn.setOnAction((ActionEvent event) -> {
            timer.play();
            bot.start();
        });
        
        Button show_mine = new Button("Show mines");
        show_mine.setOnAction((ActionEvent event) -> {
            if (board.isGameStarted() && !board.minesShown()) {
//                board.revealMines();
                timer.stop();
//                drawLauta();
            }
        });
        
        Button difficulty_selection = new Button("Difficulty selection");
        
        difficulty_selection.setOnAction((event) -> {
            time.setText(formatTime(0));
            seconds=0;
            bot.deActivate();
            primaryStage.setScene(startScene);
            primaryStage.sizeToScene();
            primaryStage.centerOnScreen();
        });

        VBox gameButtonBox = new VBox();
        gameButtonBox.getChildren().addAll(botBtn, difficulty_selection, reset);
        gameButtonBox.setAlignment(Pos.CENTER);

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
                mineCounter,
                leftSpacer,
                gameButtonBox,
                rightSpacer,
                time
        );
        gamebar.setOrientation(Orientation.HORIZONTAL);

        gameborder.setTop(gamebar);
        gameborder.setCenter(lautaNodes);
        BorderPane.setAlignment(lautaNodes, Pos.CENTER);

        //######################################### mainscene setup ########################################

        primaryStage.getIcons().add(new Image("Images/Logo.png"));
        primaryStage.setScene(startScene);
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        primaryStage.centerOnScreen();
        primaryStage.show();

    }

    private void clickStage(genericStage stage, Stage primary){
        stage.getStage().addEventHandler(MouseEvent.MOUSE_CLICKED, (mouseEvent) -> {
            stage.stopSound();
            stage.hide();
            primary.setScene(startScene);
            primary.centerOnScreen();
        });
    }

    private void resetButton(Stage primaryStage, Button resetBtn) {
        resetBtn.setOnAction((ActionEvent event) -> {
            resetGame();
            timer.stop();
            bot.deActivate();
            primaryStage.setScene(gameScene);
            primaryStage.centerOnScreen();
        });
    }

    private String formatTime(int time) {
        return String.format("%02d:%02d", (int)(time*timerScaling) / 60,  (int)(time*timerScaling) % 60);
    }
}