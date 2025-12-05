package dk.easv.mytunes.gui;

import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.bll.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MyTunesController implements Initializable {
    @FXML
    private Label lblName;
    @FXML
    private Button btnPlay;
    @FXML
    private Label lblDuration;
    @FXML
    private Slider sliderVolume;
    @FXML
    private TableColumn columnTitle;
    @FXML
    private TableColumn columnArtist;
    @FXML
    private TableColumn columnCategory;
    @FXML
    private TableColumn columnTime;
    @FXML
    private TableView<Song> tableSongs;
    private ObservableList<Song> songList;
    @FXML
    MusicFunctions musicFunctions = MusicFunctions.getInstance();
    private Song currentSong;
    private boolean isEditing;


    public void btnPlayOnClick(ActionEvent actionEvent) {
        Song selected = tableSongs.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }

        if (currentSong == null || !selected.equals(currentSong)) {
            musicFunctions.playSong(selected);
            currentSong = selected;
            btnPlay.setText("||");
            lblName.setText(selected.getTitle());
            lblDuration.setText(musicFunctions.getDuration());
            return;
        }

        if (musicFunctions.getStatus().equals("PLAYING")) {
            musicFunctions.pauseMusic();
            btnPlay.setText("\u25B6");
        } else if (musicFunctions.getStatus().equals("PAUSED") || musicFunctions.getStatus().equals("STOPPED")) {
            musicFunctions.playMusic();
            btnPlay.setText("||");
            lblName.setText(selected.getTitle());
        }
    }


    public void btnDeleteSongOnClick(ActionEvent actionEvent) {
        try {
            Song selected = tableSongs.getSelectionModel().getSelectedItem();
            if (selected == null) {
                return;
            }

            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("DeleteSong.fxml"));
            Parent root = loader.load();
            DeleteSongController controller = loader.getController();
            controller.setSong(selected);
            controller.setMainController(this);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Delete");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void btnNewSongOnClick(ActionEvent actionEvent) {
        isEditing = false;
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("NewSong.fxml"));
            Parent root = loader.load();
            NewSongController controller = loader.getController();
            controller.setMainController(this);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Add");
            stage.setScene(scene);
            stage.show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void btnEditSongOnClick(ActionEvent actionEvent) {
        Song selected = tableSongs.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }
        isEditing = true;
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("NewSong.fxml"));
            Parent root = loader.load();
            NewSongController controller = loader.getController();
            controller.setMainController(this);
            controller.setSongToEdit(selected);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Edit");
            stage.setScene(scene);
            stage.show();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean getIsEditing() {
        return isEditing;
    }

    public void sliderOnClick(MouseEvent mouseEvent) {

        musicFunctions.setVolume(sliderVolume.getValue());
    }

    public void sliderOnMouseDrag(MouseEvent mouseEvent) {
        musicFunctions.setVolume(sliderVolume.getValue());
    }

    public void btnBckOnClick(ActionEvent actionEvent) {
        /**
         * !!!!!!TODO!!!!!!!!!
         * First click restarts the music
         * and the second one goes back one track
         * if there is none then nothing happens or goes the last element of the playlist
         */
        musicFunctions.restartMusic();

    }





    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        readDataIntoList();
    }

    private void readDataIntoList() {
        songList = FXCollections.observableArrayList();
        songList.addAll(MusicFunctions.getInstance().getAllSongs());

        columnTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        columnArtist.setCellValueFactory(new PropertyValueFactory<>("artist"));
        columnCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        columnTime.setCellValueFactory(new PropertyValueFactory<>("time"));

        tableSongs.setItems(songList);
    }

    public void refreshTable() {
        songList.setAll(MusicFunctions.getInstance().getAllSongs());
    }

    @FXML
    public void onCloseButtonClick(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

}
