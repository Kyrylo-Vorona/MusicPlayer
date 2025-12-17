package dk.easv.mytunes.gui;

import dk.easv.mytunes.be.Playlist;
import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.be.SongInPlaylist;
import dk.easv.mytunes.bll.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
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
    private Label lblError;
    @FXML
    private Button btnPlay;
    @FXML
    private Button btnFilter;
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
    private TableColumn<Playlist, String> columnName;
    @FXML
    private TableColumn<Playlist, Integer> columnSongs;
    @FXML
    private TableColumn<Playlist, String> columnTotalTime;
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

    // Method for filtering the main list of songs
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
        // Checks if song selected
        if (selected == null) {
            lblError.setText("No song selected");
            return;
        }
        if (currentSong == null || !selected.equals(currentSong)) {
            musicFunctions.playSong(selected);
            currentSong = selected;
            btnPlay.setText("||");
            lblName.setText(currentSong.getTitle());
            lblError.setText("");
            return;
        }

        //Pauses the music
        if (musicFunctions.getStatus().equals("PLAYING")) {
            musicFunctions.pauseMusic();
            btnPlay.setText("\u25B6");
        }

        else if (musicFunctions.getStatus().equals("PAUSED") || musicFunctions.getStatus().equals("STOPPED")) {
            musicFunctions.playMusic();
            btnPlay.setText("||");
            lblName.setText(currentSong.getTitle());
            lblError.setText("");
        }
    }

    // Selects song from playlist
    public void playFromPlaylist(MouseEvent mouseEvent) {
        SongInPlaylist sip = ListSongsInPlaylist.getSelectionModel().getSelectedItem();
        if (sip != null) {
            selected = sip.getSong();
            lblError.setText("");
        }
    }

    // Selects song from main list
    public void playFromMainList(MouseEvent mouseEvent) {
        selected = tableSongs.getSelectionModel().getSelectedItem();
        lblError.setText("");
    }

    public void btnDeleteSongOnClick(ActionEvent actionEvent) {
        try {
            Song selected = tableSongs.getSelectionModel().getSelectedItem();
            if (selected == null) {
                lblError.setText("No song selected");
                return;
            }
            lblError.setText("");
            // Opens DeleteSong.fxml
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
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void btnNewSongOnClick(ActionEvent actionEvent) {
        // Changes the boolean so NewSong.fxml will be used for creating a new song
        songIsEditing = false;
        lblError.setText("");
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
            lblError.setText("No song selected");
            return;
        }
        // Changes the boolean so NewSong.fxml will be used for editing selected song, and will fill text fields
        songIsEditing = true;
        lblError.setText("");
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
        // Changes the boolean so NewPlaylist.fxml will be used for creating a playlist. Same thing was used for NewSong.fxml
        playlistIsEditing = false;
        lblError.setText("");
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
            lblError.setText("No playlist selected");
            return;
        }
        // Changes the boolean so name text field will be filled
        playlistIsEditing = true;
        lblError.setText("");
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
                lblError.setText("No playlist selected");
                return;
            }
            lblError.setText("");
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

    // Sets the volume if user clicks on slider
    public void sliderOnClick(MouseEvent mouseEvent) {

        musicFunctions.setVolume(sliderVolume.getValue());
    }

    // Sets the volume if user drags the slider
    public void sliderOnMouseDrag(MouseEvent mouseEvent) {
        musicFunctions.setVolume(sliderVolume.getValue());
    }

    // Plays previous song in playlist or main list
    public void btnBckOnClick(ActionEvent actionEvent) {
        // checks if song is in playlist
        boolean isInPlaylist = ListSongsInPlaylist.getItems().stream()
                .anyMatch(sip -> sip.getSong().equals(currentSong));

        if (tableSongs.getItems().contains(currentSong)) {
            int index = tableSongs.getSelectionModel().getSelectedIndex();
            // Returns if its first song in the list
            if (index <= 0) {
                return;
            }
            tableSongs.getSelectionModel().selectPrevious();
            // Changes selected song so the label which shows the playing song also changes
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
        // Checks if song is in playlist
        boolean isInPlaylist = ListSongsInPlaylist.getItems().stream()
                .anyMatch(sip -> sip.getSong().equals(currentSong));

        if (tableSongs.getItems().contains(currentSong)) {
            int index = tableSongs.getSelectionModel().getSelectedIndex();
            // Returns if it is the last song in main list
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

    // Autoplay method
    private void playNext() {
        // Checks if song is in playlist
        boolean isInPlaylist = ListSongsInPlaylist.getItems().stream()
                .anyMatch(sip -> sip.getSong().equals(currentSong));

        if (isInPlaylist) {
            // Select the next song in the playlist (if not the last one)
            int index = ListSongsInPlaylist.getSelectionModel().getSelectedIndex();
            if (index < ListSongsInPlaylist.getItems().size() - 1) {
                ListSongsInPlaylist.getSelectionModel().select(index + 1);

                // Set it as the currently selected song
                SongInPlaylist next = ListSongsInPlaylist.getSelectionModel().getSelectedItem();
                selected = next.getSong();
                // Play the selected song
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

    // Adds song to playlist
    @FXML
    public void addSongToPlaylist(ActionEvent actionEvent) {
        Song selectedSong = tableSongs.getSelectionModel().getSelectedItem();

        // Checks if song and playlist are selected
        if (currentPlaylist == null || selectedSong == null) {
            lblError.setText("No song or playlist selected");
            return;
        }
        lblError.setText("");

        // Creates a new position for added song
        int position = ListSongsInPlaylist.getItems().size() + 1;
        musicFunctions.addSongToPlaylist(position, selectedSong, currentPlaylist);

        // Updates list of playlists and list of songs in playlist
        updateSongsInPlaylistView(currentPlaylist);
        refreshTable();
    }

    // Deletes song from selected playlist
    @FXML
    public void deleteSongfromPlaylist(ActionEvent actionEvent) {
        SongInPlaylist selectedSongInPlaylist = ListSongsInPlaylist.getSelectionModel().getSelectedItem();

        // Checks if song and playlist are selected
        if (currentPlaylist == null || selectedSongInPlaylist == null) {
            lblError.setText("No song or playlist selected");
            return;
        }
        lblError.setText("");

        // Deletes song from playlist
        musicFunctions.deleteSongInPlaylist(
                selectedSongInPlaylist.getPosition(),
                currentPlaylist,
                selectedSongInPlaylist.getSong()
        );

        // Updates list of playlists and list of songs in playlist
        updateSongsInPlaylistView(currentPlaylist);
        refreshTable();
    }

    // Moves selected song in playlist
    @FXML
    public void moveUp(ActionEvent actionEvent) {
        SongInPlaylist selectedSongInPlaylist = ListSongsInPlaylist.getSelectionModel().getSelectedItem();

        // Checks if song selected
        if (currentPlaylist == null || selectedSongInPlaylist == null) {
            lblError.setText("No song or playlist selected");
            return;
        }
        lblError.setText("");

        // Returns if song is first in playlist
        if (selectedSongInPlaylist.getPosition() <= 1) {
            return;
        }

        // Moves song and updates list of songs in playlist
        MusicFunctions.getInstance().moveUp(currentPlaylist.getId(), selectedSongInPlaylist.getPosition());
        updateSongsInPlaylistView(currentPlaylist);
    }

    // Moves song down in playlist
    @FXML
    public void moveDown(ActionEvent actionEvent) {
        SongInPlaylist selectedSongInPlaylist = ListSongsInPlaylist.getSelectionModel().getSelectedItem();

        if (currentPlaylist == null || selectedSongInPlaylist == null) {
            lblError.setText("No song or playlist selected");
            return;
        }
        lblError.setText("");
        int maxPosition = ListSongsInPlaylist.getItems().size();

        if (selectedSongInPlaylist.getPosition() >= maxPosition) return;
        MusicFunctions.getInstance().moveDown(currentPlaylist.getId(), selectedSongInPlaylist.getPosition());
        updateSongsInPlaylistView(currentPlaylist);
    }

    // Updates list of songs in playlist
    private void updateSongsInPlaylistView(Playlist playlist) {
        ObservableList<SongInPlaylist> songs = FXCollections.observableArrayList(
                musicFunctions.getSongsInPlaylist(playlist.getId())
        );
        ListSongsInPlaylist.setItems(songs);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // Loads all songs and playlists
        readDataIntoList();

        // Adds a listener to the playlists table to update the song list when a new playlist is selected
        tablePlaylists.getSelectionModel().selectedItemProperty().addListener((obs, oldP, newP) -> {
            if (newP != null) {
                currentPlaylist = newP;
                updateSongsInPlaylistView(currentPlaylist);
            }
        });

        // Adds another listener to refresh the playlist view
        tablePlaylists.getSelectionModel().selectedItemProperty().addListener((obs, oldPlaylist, newPlaylist) -> {
            readDataIntoPlaylist();
        });

        // Sets a callback in MusicFunctions to automatically play the next song when the current song finishes
        musicFunctions.setOnSongFinished(() -> {
            playNext();
        });
    }

    private void readDataIntoList() {
        // Loads lists of songs and playlists
        songList = FXCollections.observableArrayList();
        songList.addAll(MusicFunctions.getInstance().getAllSongs());
        playlistList = FXCollections.observableArrayList();
        playlistList.addAll(MusicFunctions.getInstance().getAllPlaylists());

        // Loads song data into columns
        columnTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        columnArtist.setCellValueFactory(new PropertyValueFactory<>("artist"));
        columnCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        columnTime.setCellValueFactory(new PropertyValueFactory<>("time"));

        // Loads playlist names into the column
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));

        // Loads amount of song in playlist into the column
        columnSongs.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(MusicFunctions.getInstance().getNumberOfSongsInPlaylist(cellData.getValue()))
        );

        // Loads total time of playlist into the column
        columnTotalTime.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(
                        MusicFunctions.getInstance().formatTime(
                                MusicFunctions.getInstance().getTotalTimeOfPlaylist(cellData.getValue())
                        )
                )
        );
        tableSongs.setItems(songList);
        tablePlaylists.setItems(playlistList);
    }

    // Loads songs in the list when playlist is selected
    private void readDataIntoPlaylist() {
        // Checks if playlist selected
        if (currentPlaylist != null) {
            // If observableArrayList was not created, creates it
            if (songsInPlaylist == null) {
                songsInPlaylist = FXCollections.observableArrayList();
                ListSongsInPlaylist.setItems(songsInPlaylist);
            }
            songsInPlaylist.setAll(musicFunctions.getSongsInPlaylist(currentPlaylist.getId()));
        }
    }

    // Clears the list of songs in playlist
    public void clearCurrentPlaylist() {
        currentPlaylist = null;
        if (songsInPlaylist != null) {
            songsInPlaylist.clear();
        }
    }

    // Reloads all lists
    public void refreshTable() {
        songList.setAll(musicFunctions.getAllSongs());
        playlistList.setAll(musicFunctions.getAllPlaylists());

        if (currentPlaylist != null) {
            // Update the ObservableList for the currently selected playlist
            ListSongsInPlaylist.setItems(null);
            songsInPlaylist = FXCollections.observableArrayList(
                    musicFunctions.getSongsInPlaylist(currentPlaylist.getId())
            );
            ListSongsInPlaylist.setItems(songsInPlaylist);
        }

        else {
            // Clear the playlist song view if no playlist is selected
            if (songsInPlaylist != null) {
                songsInPlaylist.clear();
            }
            ListSongsInPlaylist.setItems(FXCollections.observableArrayList());
        }
    }

    // Closes the application
    @FXML
    public void onCloseButtonClick(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

}
