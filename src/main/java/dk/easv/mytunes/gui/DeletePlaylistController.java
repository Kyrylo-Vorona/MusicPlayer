package dk.easv.mytunes.gui;

import dk.easv.mytunes.be.Playlist;
import dk.easv.mytunes.bll.MusicFunctions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;

public class DeletePlaylistController {
    private MyTunesController mainController;
    private Playlist playlist;

    // Sets the playlist to delete
    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    // Sets the main controller reference for refreshing the main view
    public void setMainController(MyTunesController mainController) {
        this.mainController = mainController;
    }

    // Deletes the playlist from database and refreshes the main view
    public void btnDeletePlaylistFromDatabase(ActionEvent actionEvent) {
        MusicFunctions.getInstance().deletePlaylist(playlist);
        mainController.clearCurrentPlaylist();
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
