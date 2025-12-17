package dk.easv.mytunes.bll;
import dk.easv.mytunes.be.Category;
import dk.easv.mytunes.be.Playlist;
import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.be.SongInPlaylist;
import dk.easv.mytunes.dal.CategoryDAO;
import dk.easv.mytunes.dal.DALManager;
import dk.easv.mytunes.dal.PlaylistSongsDAO;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.util.List;

// This class handles song and playlist operations
public class MusicFunctions {

    public void pauseMusic() {
        mediaPlayer.pause();
    }

    private MediaPlayer mediaPlayer;
    String currentSongPath;
    private Runnable onSongFinished;

    private PlaylistSongsDAO playlistSongsDAO;

    // Sets a callback to be executed when the current song finishes playing
    public void setOnSongFinished(Runnable callback) {
        this.onSongFinished = callback;
    }

    // Plays the selected song. If the song is the same as the currently loaded one, it resumes playback
    public void playSong(Song song){
        if (song.getPath().equals(currentSongPath)) {
            mediaPlayer.play();
        }
        else {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
            }
            String path = song.getPath();
            if (path != null && !path.isEmpty() && path.charAt(0) != '/') {
                Media hit = new Media(new File(song.getPath()).toURI().toString());
                mediaPlayer = new MediaPlayer(hit);
                mediaPlayer.play();
                currentSongPath = song.getPath();

            } else {
                Media hit = new Media(getClass().getResource(song.getPath()).toExternalForm());
                mediaPlayer = new MediaPlayer(hit);
                mediaPlayer.play();
                currentSongPath = song.getPath();

                // Set an action to be performed when the current song finishes playing
                mediaPlayer.setOnEndOfMedia(() -> {
                    if (onSongFinished != null) {
                        onSongFinished.run();
                    }
                });
            }
        }
    }

    // Returns status of media player
    public String getStatus() {
        if (mediaPlayer == null) {
            return "STOPPED";
        } else {
            return mediaPlayer.getStatus().toString();
        }
    }

    public void setVolume(double volume) {
        double rounded = Math.round(volume * 10) / 10.0;
        mediaPlayer.setVolume(volume);
    }

    public void playMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }

    private static MusicFunctions instance;

    // Private constructor
    private MusicFunctions() {
        playlistSongsDAO = DALManager.getInstance().getSongsInPlaylistDAO();
    }

    // Provides access to the single instance of MusicFunctions
    public static MusicFunctions getInstance()
    {
        if (instance == null)
        {
            instance = new MusicFunctions();
        }

        return instance;
    }

    public void deleteSong(Song song){
        DALManager.getInstance().getSongDAO().deleteSong(song);
    }

    public void addSong(String title, String artist, int seconds, String file, Category category) {
        DALManager.getInstance().getSongDAO().addSong(title, artist, seconds, file, category);
    }

    public void addSongToPlaylist(int position, Song song, Playlist playlist) {
        DALManager.getInstance().getSongsInPlaylistDAO().addSongToPlaylist(position, song, playlist);
    }

    public void deleteSongInPlaylist(int position, Playlist playlist, Song song) {
        DALManager.getInstance().getSongsInPlaylistDAO().deleteSongFromPlaylist(position, playlist, song);
    }

    public void moveUp(int playlistId, int position) {
        DALManager.getInstance().getSongsInPlaylistDAO().moveUp(playlistId, position);
    }

    public void moveDown(int playlistId, int position) {
        DALManager.getInstance().getSongsInPlaylistDAO().moveDown(playlistId, position);
    }

    public void editSong(Song song) {
        DALManager.getInstance().getSongDAO().editSong(song);
    }

    public void editPlaylist(Playlist playlist) {
        DALManager.getInstance().getPlaylistDAO().editPlaylist(playlist);
    }

    public void addPlaylist(String name) {
        DALManager.getInstance().getPlaylistDAO().addPlaylist(name);
    }

    public void deletePlaylist(Playlist playlist) {
        DALManager.getInstance().getPlaylistDAO().deletePlaylist(playlist);
    }

    public Category addCategory(String name) {
        return  new CategoryDAO().addCategory(name);
    }

    public List<Song> getAllSongs()
    {
        return DALManager.getInstance().getSongDAO().getAllSongs();
    }

    public List<Category>  getAllCategories()
    {
        return DALManager.getInstance().getCategoryDAO().getAllCategories();
    }

    public List<Playlist> getAllPlaylists()
    {
        return DALManager.getInstance().getPlaylistDAO().getAllPlaylists();
    }

    public List<SongInPlaylist> getSongsInPlaylist(int playlistId) {
        return DALManager.getInstance().getSongsInPlaylistDAO().getSongsInPlaylist(playlistId);
    }

    // Returns amount of song in playlist
    public int getNumberOfSongsInPlaylist(Playlist playlist) {
        return playlistSongsDAO.getSongsInPlaylist(playlist.getId()).size();
    }

    // Returns total time of playlist in seconds
    public int getTotalTimeOfPlaylist(Playlist playlist) {
        return playlistSongsDAO.getSongsInPlaylist(playlist.getId())
                .stream()
                .mapToInt(s -> s.getSong().getSeconds())
                .sum();
    }

    // Returns total formatted time
    public String formatTime(int totalSeconds) {
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;

        if (hours > 0) {
            return String.format("%d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format("%02d:%02d", minutes, seconds);
        }
    }

}
