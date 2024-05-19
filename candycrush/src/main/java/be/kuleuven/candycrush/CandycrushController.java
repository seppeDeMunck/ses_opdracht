package be.kuleuven.candycrush;

import java.net.URL;
import java.util.ResourceBundle;

import be.kuleuven.candycrush.model.CandycrushModel;
import be.kuleuven.candycrush.model.Position;
import be.kuleuven.candycrush.view.CandycrushView;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class CandycrushController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label Label;

    @FXML
    private Label tip;

    @FXML
    private Button btn;

    @FXML
    private AnchorPane paneel;

    @FXML
    private AnchorPane speelbord;

    @FXML
    private TextField textInput;

    private CandycrushModel model;
    private CandycrushView view;
    boolean ingame = false;

    @FXML
    void initialize() {
        assert Label != null : "fx:id=\"Label\" was not injected: check your FXML file 'candycrush-view.fxml'.";
        assert btn != null : "fx:id=\"btn\" was not injected: check your FXML file 'candycrush-view.fxml'.";
        assert paneel != null : "fx:id=\"paneel\" was not injected: check your FXML file 'candycrush-view.fxml'.";
        assert speelbord != null : "fx:id=\"speelbord\" was not injected: check your FXML file 'candycrush-view.fxml'.";
        assert textInput != null : "fx:id=\"textInput\" was not injected: check your FXML file 'candycrush-view.fxml'.";
        assert tip != null :  "fx:id=\"tip\" was not injected: check your FXML file 'candycrush-view.fxml'.";
        model = new CandycrushModel("Test");
        view = new CandycrushView(model);
        speelbord.getChildren().add(view);
        view.setOnMouseClicked(this::onCandyClicked);
        btn.setOnMouseClicked(this::update2);

    }
    public void update2(MouseEvent me){
        if(ingame) {
            model.setPunten(0);
            Label.setText("score = "+model.getPunten());

        }
        else {
            if (textInput.getCharacters().toString().equals("seppe")) {
                update();
                paneel.setStyle("-fx-background-color: #00c4ff; ");
                tip.setText(" ");
                btn.setText("reset");
                textInput.setText("");
                ingame = true;
                model.updateBoard();
            } else {
                paneel.setStyle("-fx-background-color: #ff0000; ");
                tip.setStyle("-fx-font-size:30");
                textInput.setText("");
            }
        }



    }
    public void update(){
        view.update();
        if (model.getPunten()>50){
            paneel.setStyle("-fx-background-color: #00ffd0; ");
        }
        if (model.getPunten()>100){
            paneel.setStyle("-fx-background-color: #00ff6f; ");
        }
        if (model.getPunten()>150){
            paneel.setStyle("-fx-background-color: #2fff00; ");
        }
    }

    public void onCandyClicked(MouseEvent me){
        Position position = view.getIndexOfClicked(me);
        model.candyWithIndexSelected(position);
        update();
        Label.setText("score = "+model.getPunten());

    }

}
