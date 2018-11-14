package miinaharava.ui;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


public class CustomGameStage extends genericStage {

    private TextField width,height,mines;
    private Label gameDiffEstimate;
    private GridPane grid;

    public CustomGameStage(Stage primaryStage) {
        super(primaryStage);

        grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));

        width = new TextField("9");
        height = new TextField("9");
        mines = new TextField("10");

        width.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                width.setText(newValue.replaceAll("[\\D]", ""));
            }
            calcDifficulty();
        });

        height.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                height.setText(newValue.replaceAll("[\\D]", ""));
            }
            calcDifficulty();
        });

        mines.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                mines.setText(newValue.replaceAll("[\\D]", ""));
            }
            if(Integer.valueOf(newValue) >= getCurrentX()*getCurrentY()){
                mines.setText(String.valueOf(getCurrentX()*getCurrentY()-1));
            }
            calcDifficulty();
        });

        Button reset = new Button("Reset");
        Button play = new Button("PLay!");

        Button closeBtn = new Button("Exit");
        closeBtn.setOnAction((ActionEvent event) -> System.exit(0));

        reset.setOnMouseClicked((Event) -> {
            width.setText("9");
            height.setText("9");
            mines.setText("10");
        });

        gameDiffEstimate = new Label();
        calcDifficulty();

        grid.add(new Label("Width:"),0,0);
        grid.add(width,1,0);
        grid.add(new Label("Height:"),0,1);
        grid.add(height,1,1);
        grid.add(new Label("Mines:"),0,2);
        grid.add(mines,1,2);
        grid.add(gameDiffEstimate,0, 3);
        grid.add(reset,0,4);
        grid.add(play,1,4);

        super.setTop(new Label("Make your own game!"));
        super.setCenter(grid);
    }

    public int getCurrentX() {
        return  Integer.valueOf(width.getText());
    }

    public int getCurrentY(){
        return Integer.valueOf(height.getText());
    }

    public int getCurrentMines(){
        return Integer.valueOf(mines.getText());
    }

    public void setPlayButton(Button btn){
        grid.add(btn,1,4);
    }

    public void reset() {
        width.setText("9");
        height.setText("9");
        mines.setText("10");
    }

    private void calcDifficulty(){

        String str = "Game difficulty: ";

        int mineslkm = Integer.valueOf(mines.getText());
        int widthlkm = Integer.valueOf(width.getText());
        int heightlkm = Integer.valueOf(height.getText());

        if(mineslkm == 0) {mineslkm = 1;}
        if(widthlkm == 0) {widthlkm = 1;}
        if(heightlkm == 0) {heightlkm = 1;}

        double diff = (double) mineslkm / (widthlkm * heightlkm);
        diff = 1/diff;
//
//        System.out.println(mineslkm);
//        System.out.println(widthlkm);
//        System.out.println(heightlkm);
//        System.out.println(diff);

        if(diff <= 4.85) {
            str+="HARD";
        } else if (diff <= 6.4) {
            str+="MEDIUM";
        } else {
            str+="EASY";
        }
        gameDiffEstimate.setText(str);
    }
}
