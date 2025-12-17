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
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class NewSongController implements Initializable {

    @FXML
    private Label lblError;
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

    private MyTunesController mainController;
    private ObservableList<Category> categories;
    private MusicFunctions mf = MusicFunctions.getInstance();
    private Song songToEdit;

    // Sets the main controller reference for refreshing the main view
    public void setMainController(MyTunesController controller) {
        this.mainController = controller;
    }

    // Initializes the controller and loads categories into the ComboBox
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        readDataIntoComboBox();
    }

    private void readDataIntoComboBox() {
        categories = FXCollections.observableArrayList();
        categories.addAll(mf.getAllCategories());
        categoryComboBox.setItems(categories);
    }

    // Sets the song to edit and fills the text fields with songs data
    public void setSongToEdit(Song song) {
        this.songToEdit = song;
        titleTextField.setText(song.getTitle());
        artistTextField.setText(song.getArtist());
        timeTextField.setText(song.getSeconds() / 60 + ":" + song.getSeconds() % 60);
        filePathTextField.setText(song.getPath());
        categoryComboBox.setValue(song.getCategory());
    }

    // Handles adding a new song or editing an existing one
    // Validates input, parses time format (mm:ss)
    @FXML
    private void onAddSongButtonClick(ActionEvent event) {
        if (mainController.getSongIsEditing()) {
            if (songToEdit != null) {
                if (titleTextField.getText().trim().isEmpty() || artistTextField.getText().trim().isEmpty() || timeTextField.getText().trim().isEmpty() || filePathTextField.getText().trim().isEmpty() || categoryComboBox.getValue() == null) {
                    lblError.setText("Please fill all the fields");
                    return;
                }
                lblError.setText("");
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
            if (titleTextField.getText().trim().isEmpty() || artistTextField.getText().trim().isEmpty() || timeTextField.getText().trim().isEmpty() || filePathTextField.getText().trim().isEmpty() || categoryComboBox.getValue() == null) {
                lblError.setText("Please fill all the fields");
                return;
            }
            lblError.setText("");
            String title = titleTextField.getText();
            String artist = artistTextField.getText();
            int time;

            try {
                String[] parts = timeTextField.getText().split(":");
                time = Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
            } catch (Exception ex) {
                lblError.setText("Invalid time format");
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

    // Closes the NewSong.fxml without saving changes
    @FXML
    private void onCancelButtonClick(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    // Opens a dialog for choosing song file
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

    // Opens a dialog for adding a new category
    @FXML
    private void onAddCategoryMoreClick() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add New Category");
        dialog.setHeaderText("Add a new category");
        dialog.setContentText("Category name:");

        dialog.showAndWait().ifPresent(name -> {
            if (!name.isBlank() && categories.stream().noneMatch(c -> c.getName().equalsIgnoreCase(name))) {
                try {
                    Category newCategory = mf.addCategory(name);
                    categories.add(newCategory);
                    categoryComboBox.setItems(categories);
                    categoryComboBox.setValue(newCategory);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}

