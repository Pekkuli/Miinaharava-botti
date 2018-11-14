package miinaharava.ui;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class WinStage extends genericStage {

    private final Text winText;

    public WinStage(Stage primaryStage) {
        super(primaryStage);

        ImageView winImg = new ImageView();
        winImg.setImage(new Image("Images/"  + "WINSCREEN.png"));
        super.setCenter(winImg);
        BorderPane.setAlignment(winImg, Pos.CENTER);

        Media win = new Media(this.getClass().getResource("/Sounds/winsound.mp3").toString());
        setMedia(new MediaPlayer(win));
        getMedia().volumeProperty().set(0.15);

        winText = new Text();
        super.setTop(winText);
        BorderPane.setAlignment(winText, Pos.TOP_CENTER);
    }

    public void setWinText(String text) {
        winText.setText(text);
    }
}
