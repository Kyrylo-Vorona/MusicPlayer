package dk.easv.mytunes.gui;

import dk.easv.mytunes.be.Playlist;
import dk.easv.mytunes.bll.MusicFunctions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class NewPlaylistController {
    @FXML
    private TextField nameTextField;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;
    private MyTunesController mainController;
    private MusicFunctions mf = MusicFunctions.getInstance();
    private Playlist playlistToEdit;

    public void setPlaylistToEdit(Playlist playlistToEdit) {
        this.playlistToEdit = playlistToEdit;
        nameTextField.setText(playlistToEdit.getName());
    }

    public void setMainController(MyTunesController controller) {
        this.mainController = controller;
    }

    @FXML
    private void onAddPlaylistButtonClick(ActionEvent event) {
        if (mainController.getPlaylistIsEditing()) {
            if (playlistToEdit != null) {
                playlistToEdit.setName(nameTextField.getText());
                mf.editPlaylist(playlistToEdit);
                mainController.refreshTable();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.close();
            }
        }
        else {
            String name = nameTextField.getText();
            int songs = 0;
            int seconds = 0;
            mf.addPlaylist(name, songs, seconds);
            mainController.refreshTable();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    private void onCancelButtonClick(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
