package dk.easv.mytunes.gui;

import dk.easv.mytunes.be.Playlist;
import dk.easv.mytunes.be.PlaylistSongs;
import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.be.SongInPlaylist;
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
    private Button btnFilter;
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
    private ObservableList<Song> filteredSongs;
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
    private ListView<SongInPlaylist> ListSongsInPlaylist;
    private ObservableList<SongInPlaylist> songsInPlaylist;
    @FXML
    MusicFunctions musicFunctions = MusicFunctions.getInstance();
    @FXML
    private TextField txtFilter;
    private Song currentSong;
    private boolean songIsEditing;
    private boolean playlistIsEditing;
    private Song selected;
    private Playlist currentPlaylist;
    private boolean filterActive = false;

    public void btnFilter(ActionEvent actionEvent) {
        if (!filterActive) {

            String query = txtFilter.getText();
            if (query == null || query.isBlank()) {
                return;
            }

            String q = query.toLowerCase();

            filteredSongs = FXCollections.observableArrayList();

            for (Song s : songList) {
                boolean matchTitle = s.getTitle().toLowerCase().contains(q);
                boolean matchArtist = s.getArtist().toLowerCase().contains(q);
                boolean matchCategory = s.getCategory().getName().toLowerCase().contains(q);

                if (matchTitle || matchArtist || matchCategory) {
                    filteredSongs.add(s);
                }
            }

            tableSongs.setItems(filteredSongs);
            filterActive = true;
            btnFilter.setText("Clear");
        }

        else {
            tableSongs.setItems(songList);
            txtFilter.clear();
            filterActive = false;
            btnFilter.setText("Filter");
        }
    }

    public void btnPlayOnClick(ActionEvent actionEvent) {
        if (selected == null) {
            return;
        }


        if (currentSong == null || !selected.equals(currentSong)) {
            musicFunctions.playSong(selected);
            currentSong = selected;
            btnPlay.setText("||");
            lblName.setText(currentSong.getTitle());
            lblDuration.setText(musicFunctions.getDuration());
            return;
        }

        if (musicFunctions.getStatus().equals("PLAYING")) {
            musicFunctions.pauseMusic();
            btnPlay.setText("\u25B6");
        } else if (musicFunctions.getStatus().equals("PAUSED") || musicFunctions.getStatus().equals("STOPPED")) {
            musicFunctions.playMusic();
            btnPlay.setText("||");
            lblName.setText(currentSong.getTitle());
        }
    }

    public void playFromPlaylist(MouseEvent mouseEvent) {
        SongInPlaylist sip = ListSongsInPlaylist.getSelectionModel().getSelectedItem();
        if (sip != null) {
            selected = sip.getSong();
        }
    }


    public void playFromMainList(MouseEvent mouseEvent) {
        selected = tableSongs.getSelectionModel().getSelectedItem();
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
        boolean isInPlaylist = ListSongsInPlaylist.getItems().stream()
                .anyMatch(sip -> sip.getSong().equals(currentSong));

        if (tableSongs.getItems().contains(currentSong)) {
            int index = tableSongs.getSelectionModel().getSelectedIndex();
            if (index <= 0) {
                return;
            }
            tableSongs.getSelectionModel().selectPrevious();
            selected =  tableSongs.getSelectionModel().getSelectedItem();
            btnPlayOnClick(actionEvent);
            }

        else if (isInPlaylist) {
            int index = ListSongsInPlaylist.getSelectionModel().getSelectedIndex();
            if (index <= 0) {
                return;
            }
            ListSongsInPlaylist.getSelectionModel().selectPrevious();
            SongInPlaylist sip = ListSongsInPlaylist.getSelectionModel().getSelectedItem();
            if (sip != null) selected = sip.getSong();
            btnPlayOnClick(actionEvent);
        }
    }

    public void btnNxtOnClick(ActionEvent actionEvent) {
        boolean isInPlaylist = ListSongsInPlaylist.getItems().stream()
                .anyMatch(sip -> sip.getSong().equals(currentSong));

        if (tableSongs.getItems().contains(currentSong)) {
            int index = tableSongs.getSelectionModel().getSelectedIndex();
            if (index == tableSongs.getItems().size() - 1) {
                return;
            }
            tableSongs.getSelectionModel().selectNext();
            selected =  tableSongs.getSelectionModel().getSelectedItem();
            btnPlayOnClick(actionEvent);
        }
        else if (isInPlaylist) {
            int index = ListSongsInPlaylist.getSelectionModel().getSelectedIndex();
            if (index == ListSongsInPlaylist.getItems().size() - 1) {
                return;
            }
            ListSongsInPlaylist.getSelectionModel().selectNext();
            SongInPlaylist sip = ListSongsInPlaylist.getSelectionModel().getSelectedItem();
            if (sip != null) selected = sip.getSong();
            btnPlayOnClick(actionEvent);
        }
    }

    private void playNext() {
        boolean isInPlaylist = ListSongsInPlaylist.getItems().stream()
                .anyMatch(sip -> sip.getSong().equals(currentSong));

        if (isInPlaylist) {
            int index = ListSongsInPlaylist.getSelectionModel().getSelectedIndex();
            if (index < ListSongsInPlaylist.getItems().size() - 1) {
                ListSongsInPlaylist.getSelectionModel().select(index + 1);

                SongInPlaylist next = ListSongsInPlaylist.getSelectionModel().getSelectedItem();
                selected = next.getSong();
                btnPlayOnClick(null);
            }
        }
        else {
            int index = tableSongs.getSelectionModel().getSelectedIndex();
            if (index < tableSongs.getItems().size() - 1) {
                tableSongs.getSelectionModel().select(index + 1);

                selected = tableSongs.getSelectionModel().getSelectedItem();
                btnPlayOnClick(null);
            }
        }
    }



    @FXML
    public void addSongToPlaylist(ActionEvent actionEvent) {
        Song selectedSong = tableSongs.getSelectionModel().getSelectedItem();
        if (currentPlaylist == null || selectedSong == null) return;

        int position = ListSongsInPlaylist.getItems().size() + 1;
        musicFunctions.addSongToPlaylist(position, selectedSong, currentPlaylist);

        updateSongsInPlaylistView(currentPlaylist);
        refreshTable();
    }

    @FXML
    public void deleteSongfromPlaylist(ActionEvent actionEvent) {
        SongInPlaylist selectedSongInPlaylist = ListSongsInPlaylist.getSelectionModel().getSelectedItem();
        if (currentPlaylist == null || selectedSongInPlaylist == null) return;

        musicFunctions.deleteSongInPlaylist(
                selectedSongInPlaylist.getPosition(),
                currentPlaylist,
                selectedSongInPlaylist.getSong()
        );

        updateSongsInPlaylistView(currentPlaylist);
        refreshTable();
    }

    @FXML
    public void moveUp(ActionEvent actionEvent) {
        SongInPlaylist selectedSongInPlaylist = ListSongsInPlaylist.getSelectionModel().getSelectedItem();

        if (currentPlaylist == null || selectedSongInPlaylist == null) return;

        if (selectedSongInPlaylist.getPosition() <= 1) {
            return;
        }
        MusicFunctions.getInstance().moveUp(currentPlaylist.getId(), selectedSongInPlaylist.getPosition());
        updateSongsInPlaylistView(currentPlaylist);
    }

    @FXML
    public void moveDown(ActionEvent actionEvent) {
        SongInPlaylist selectedSongInPlaylist = ListSongsInPlaylist.getSelectionModel().getSelectedItem();

        if (currentPlaylist == null || selectedSongInPlaylist == null) return;

        int maxPosition = ListSongsInPlaylist.getItems().size();

        if (selectedSongInPlaylist.getPosition() >= maxPosition) return;
        MusicFunctions.getInstance().moveDown(currentPlaylist.getId(), selectedSongInPlaylist.getPosition());
        updateSongsInPlaylistView(currentPlaylist);
    }

    private void updateSongsInPlaylistView(Playlist playlist) {
        ObservableList<SongInPlaylist> songs = FXCollections.observableArrayList(
                musicFunctions.getSongsInPlaylist(playlist.getId())
        );
        ListSongsInPlaylist.setItems(songs);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        readDataIntoList();
        tablePlaylists.getSelectionModel().selectedItemProperty().addListener((obs, oldP, newP) -> {
            if (newP != null) {
                currentPlaylist = newP;
                updateSongsInPlaylistView(currentPlaylist);
            }
        });
        tablePlaylists.getSelectionModel().selectedItemProperty().addListener((obs, oldPlaylist, newPlaylist) -> {
            readDataIntoPlaylist();
        });

        musicFunctions.setOnSongFinished(() -> {
            playNext();
        });
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

    private void readDataIntoPlaylist() {

        if (currentPlaylist != null) {
            if (songsInPlaylist == null) {
                songsInPlaylist = FXCollections.observableArrayList();
                ListSongsInPlaylist.setItems(songsInPlaylist);
            }
            songsInPlaylist.setAll(musicFunctions.getSongsInPlaylist(currentPlaylist.getId()));
        }
    }

    public void clearCurrentPlaylist() {
        currentPlaylist = null;
        if (songsInPlaylist != null) {
            songsInPlaylist.clear();
        }
    }


    public void refreshTable() {
        songList.setAll(musicFunctions.getAllSongs());
        playlistList.setAll(musicFunctions.getAllPlaylists());

        if (currentPlaylist != null) {
            if (songsInPlaylist == null) {
                songsInPlaylist = FXCollections.observableArrayList();
                ListSongsInPlaylist.setItems(songsInPlaylist);
            }
            songsInPlaylist.setAll(musicFunctions.getSongsInPlaylist(currentPlaylist.getId()));
        }
        else {
            if (songsInPlaylist != null) {
                songsInPlaylist.clear();
            }
            ListSongsInPlaylist.setItems(FXCollections.observableArrayList());
        }
    }




    @FXML
    public void onCloseButtonClick(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

}
