package dk.easv.mytunes.gui;

import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.bll.MusicFunctions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class DeleteSongController {
    private Song song;
    private MyTunesController mainController;
    public void setSong(Song song) {
        this.song = song;
    }

    public void setMainController(MyTunesController controller) {
        this.mainController = controller;
    }

    public void btnDeleteSongFromDatabase(ActionEvent actionEvent) {
        MusicFunctions.getInstance().deleteSong(song);
        mainController.refreshTable();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }
}
