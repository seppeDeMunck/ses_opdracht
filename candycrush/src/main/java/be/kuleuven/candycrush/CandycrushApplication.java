package be.kuleuven.candycrush;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CandycrushApplication extends Application {
    FXMLLoader fxmlLoader= new FXMLLoader();
    @Override
    public void start(Stage stage) throws IOException {
        fxmlLoader = new FXMLLoader(CandycrushApplication.class.getResource("candycrush-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1920/4, 1080/2.5);
        stage.setTitle("login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }


}