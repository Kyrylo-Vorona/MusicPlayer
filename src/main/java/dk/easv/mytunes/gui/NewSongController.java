package dk.easv.mytunes.gui;

import dk.easv.mytunes.be.Category;
import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.bll.MusicFunctions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class NewSongController implements Initializable {

    @FXML
    private ComboBox<Category> categoryComboBox;
    @FXML
    private TextField titleTextField;
    @FXML
    private TextField artistTextField;
    @FXML
    private TextField timeTextField;
    @FXML
    private TextField filePathTextField;
    @FXML
    private Button chooseButton;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;

    private MyTunesController mainController;
    private ObservableList<Category> categories;
    private MusicFunctions mf = MusicFunctions.getInstance();
    private Song songToEdit;

    public void setMainController(MyTunesController controller) {
        this.mainController = controller;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        readDataIntoComboBox();
    }

    private void readDataIntoComboBox() {
        categories = FXCollections.observableArrayList();
        categories.addAll(mf.getAllCategories());
        categoryComboBox.setItems(categories);
    }

    public void setSongToEdit(Song song) {
        this.songToEdit = song;
        titleTextField.setText(song.getTitle());
        artistTextField.setText(song.getArtist());
        timeTextField.setText(song.getSeconds() / 60 + ":" + song.getSeconds() % 60);
        filePathTextField.setText(song.getPath());
        categoryComboBox.setValue(song.getCategory());
    }


    @FXML
    private void onAddSongButtonClick(ActionEvent event) {
        if (mainController.getIsEditing()) {
            if (songToEdit != null) {
                songToEdit.setTitle(titleTextField.getText());
                songToEdit.setArtist(artistTextField.getText());
                int time;
                try {
                    String[] parts = timeTextField.getText().split(":");
                    time = Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
                } catch (Exception ex) {
                    System.out.println("Invalid time format");
                    return;
                }
                songToEdit.setSeconds(time);
                songToEdit.setPath(filePathTextField.getText());
                songToEdit.setCategory(categoryComboBox.getValue());
                mf.editSong(songToEdit);
                mainController.refreshTable();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.close();

            }
        }
        else {
            String title = titleTextField.getText();
            String artist = artistTextField.getText();
            int time;

            try {
                String[] parts = timeTextField.getText().split(":");
                time = Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
            } catch (Exception ex) {
                System.out.println("Invalid time format");
                return;
            }

            String file = filePathTextField.getText();
            Category selectedCategory = categoryComboBox.getValue();

            mf.addSong(title, artist, time, file, selectedCategory);
            mainController.refreshTable();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    private void chooseSongButtonClick(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                filePathTextField.setText(file.getAbsolutePath());
            }
            catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}

