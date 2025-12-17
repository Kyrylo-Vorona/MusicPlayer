package dk.easv.mytunes.gui;

import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.bll.MusicFunctions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;

public class DeleteSongController {
    private Song song;
    private MyTunesController mainController;

    // Sets song to delete
    public void setSong(Song song) {
        this.song = song;
    }

    // Sets the main controller reference for refreshing the main view
    public void setMainController(MyTunesController controller) {
        this.mainController = controller;
    }

    // Deletes song from database and refreshes the main view
    public void btnDeleteSongFromDatabase(ActionEvent actionEvent) {
        MusicFunctions.getInstance().deleteSong(song);
        mainController.refreshTable();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onCancelButtonClick(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
