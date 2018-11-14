package miinaharava.ui;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class genericStage {

    private Stage stage;
    private Node Top;
    private Node Center;
    private Node Bottom;
    private MediaPlayer player;

    public genericStage(Stage primaryStage) {
        stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setResizable(false);
        stage.initOwner(primaryStage);
        stage.setAlwaysOnTop(true);
        stage.centerOnScreen();
    }

    public void setTop(Node node) {
        Top = node;
    }

    public void setCenter(Node node) {
        Center = node;
    }

    public void setBottom(Node node){
        Bottom = node;
    }

    public void initStage(){
        BorderPane br = new BorderPane();
        if(Top != null){
            br.setTop(Top);
        }
        if(Center != null){
            br.setCenter(Center);
        }
        if(Bottom != null){
            br.setBottom(Bottom);
        }
        br.setPadding(new Insets(10,10,10,10));
        stage.setScene(new Scene(br));
    }

    public Stage getStage() {
        initStage();
        return stage;
    }

    public void show(){
        initStage();
        stage.show();
    }

    public void hide(){
        stage.hide();
    }

    public void setMedia(MediaPlayer mdpl) {
        player = mdpl;
    }

    public MediaPlayer getMedia() {
        return player;
    }

    public void playSound() {
        player.play();
    }

    public void stopSound() {
        player.stop();
    }
}
