package dk.easv.mytunes.gui;

import dk.easv.mytunes.be.Playlist;
import dk.easv.mytunes.bll.MusicFunctions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class NewPlaylistController {
    @FXML
    private TextField nameTextField;
    @FXML
    private Label lblError;
    private MyTunesController mainController;
    private MusicFunctions mf = MusicFunctions.getInstance();
    private Playlist playlistToEdit;

    // Sets the playlist to edit and fills the text field with its name
    public void setPlaylistToEdit(Playlist playlistToEdit) {
        this.playlistToEdit = playlistToEdit;
        nameTextField.setText(playlistToEdit.getName());
    }

    // Sets the main controller reference for refreshing the main view
    public void setMainController(MyTunesController controller) {
        this.mainController = controller;
    }

    @FXML
    private void onAddPlaylistButtonClick(ActionEvent event) {
        if (mainController.getPlaylistIsEditing()) {
            // If user pressed the "Edit" button
            if (playlistToEdit != null) {
                if (nameTextField.getText().trim().isEmpty()) {
                    lblError.setText("Please enter a playlist name");
                    return;
                }
                playlistToEdit.setName(nameTextField.getText());
                mf.editPlaylist(playlistToEdit);
                // Refreshes tables and closes this window
                mainController.refreshTable();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.close();
            }
        }
        else {
            if (nameTextField.getText().trim().isEmpty()) {
                lblError.setText("Please enter a playlist name");
                return;
            }
            lblError.setText("");
            String name = nameTextField.getText();
            mf.addPlaylist(name);
            mainController.refreshTable();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }

    // Closes the NewPlaylist.fxml without saving changes
    @FXML
    private void onCancelButtonClick(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
