package dk.easv.mytunes.gui;

import dk.easv.mytunes.be.Playlist;
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
    private TableView<Playlist> tablePlaylists;
    @FXML
    private TableColumn columnName;
    @FXML
    private TableColumn columnSongs;
    @FXML
    private TableColumn columnTotalTime;
    private ObservableList<Playlist> playlistList;
    @FXML
    MusicFunctions musicFunctions = MusicFunctions.getInstance();
    private Song currentSong;
    private boolean songIsEditing;
    private boolean playlistIsEditing;


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
        songIsEditing = false;
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
        songIsEditing = true;
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

    public void btnNewPlaylistOnClick(ActionEvent actionEvent) {
        playlistIsEditing = false;
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("NewPlaylist.fxml"));
            Parent root = loader.load();
            NewPlaylistController controller = loader.getController();
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

    public void btnEditPlaylistOnClick(ActionEvent actionEvent) {
        Playlist selected = tablePlaylists.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }
        playlistIsEditing = true;
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("NewPlaylist.fxml"));
            Parent root = loader.load();
            NewPlaylistController controller = loader.getController();
            controller.setMainController(this);
            controller.setPlaylistToEdit(selected);
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

    public void btnDeletePlaylistOnClick(ActionEvent actionEvent) {
        try {
            Playlist selected = tablePlaylists.getSelectionModel().getSelectedItem();
            if (selected == null) {
                return;
            }

            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("DeletePlaylist.fxml"));
            Parent root = loader.load();
            DeletePlaylistController controller = loader.getController();
            controller.setPlaylist(selected);
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

    public boolean getSongIsEditing() {
        return songIsEditing;
    }

    public boolean getPlaylistIsEditing() {
        return playlistIsEditing;
    }

    public void sliderOnClick(MouseEvent mouseEvent) {

        musicFunctions.setVolume(sliderVolume.getValue());
    }

    public void sliderOnMouseDrag(MouseEvent mouseEvent) {
        musicFunctions.setVolume(sliderVolume.getValue());
    }

    public void btnBckOnClick(ActionEvent actionEvent) {
        int index = tableSongs.getSelectionModel().getSelectedIndex();
        if (index <= 0) {
            return;
        }
        tableSongs.getSelectionModel().selectPrevious();
        btnPlayOnClick(actionEvent);
    }

    public void btnNxtOnClick(ActionEvent actionEvent) {
        int index = tableSongs.getSelectionModel().getSelectedIndex();
        if (index == tableSongs.getItems().size() - 1) {
            return;
        }
        tableSongs.getSelectionModel().selectNext();
        btnPlayOnClick(actionEvent);
    }



    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        readDataIntoList();
    }

    private void readDataIntoList() {
        songList = FXCollections.observableArrayList();
        songList.addAll(MusicFunctions.getInstance().getAllSongs());
        playlistList = FXCollections.observableArrayList();
        playlistList.addAll(MusicFunctions.getInstance().getAllPlaylists());

        columnTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        columnArtist.setCellValueFactory(new PropertyValueFactory<>("artist"));
        columnCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        columnTime.setCellValueFactory(new PropertyValueFactory<>("time"));

        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnSongs.setCellValueFactory(new PropertyValueFactory<>("songs"));
        columnTotalTime.setCellValueFactory(new PropertyValueFactory<>("time"));

        tableSongs.setItems(songList);
        tablePlaylists.setItems(playlistList);
    }

    public void refreshTable() {
        songList.setAll(MusicFunctions.getInstance().getAllSongs());
        playlistList.setAll(MusicFunctions.getInstance().getAllPlaylists());
    }

    @FXML
    public void onCloseButtonClick(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

}
