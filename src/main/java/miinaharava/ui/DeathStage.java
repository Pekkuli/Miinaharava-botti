package miinaharava.ui;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class DeathStage extends genericStage {

    public DeathStage(Stage primaryStage) {
        super(primaryStage);

        ImageView loseWindowImg = new ImageView();
        loseWindowImg.setImage(new Image("Images/"  + "YOUDIED.png"));
        super.setCenter(loseWindowImg);
        BorderPane.setAlignment(loseWindowImg, Pos.CENTER);

        Media death = new Media(this.getClass().getResource("/Sounds/YOUDIED.mp3").toString());
        setMedia(new MediaPlayer(death));
        getMedia().volumeProperty().set(0.25);
    }
}
