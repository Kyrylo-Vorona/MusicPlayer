package dk.easv.mytunes.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/dk/easv/mytunes/gui/MyTunesGUI.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("My tunes");
        stage.setScene(scene);
        stage.show();
    }
}
