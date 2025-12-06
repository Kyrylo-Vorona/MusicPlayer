package dk.easv.mytunes.bll;
import dk.easv.mytunes.be.Category;
import dk.easv.mytunes.be.Playlist;
import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.dal.DALManager;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.List;

public class MusicFunctions {

    public void pauseMusic() {
        mediaPlayer.pause();
    }

    private MediaPlayer mediaPlayer;
    private String fullDuration;
    private String status;


    String currentSongPath;
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
            }
        }
    }

    public String getStatus() {
        if (mediaPlayer == null) {
            return "STOPPED";
        } else {
            return mediaPlayer.getStatus().toString();
        }
    }

    public void setStatus(String newStatus) {
        if (mediaPlayer != null) {
            status = newStatus;
        } else {
            status = "STOPPED";
        }
    }


    public void setVolume(double volume) {
        double rounded = Math.round(volume * 10) / 10.0;
        mediaPlayer.setVolume(volume);
    }
    public String getDuration() {
        /**Duration in in ms and must be converted to string to cut of the "ms" part
         * then into double because the duration of the last song was x.0 so it cannot be converted into int from string
         * therefor it must be double and then integer
         */
        String duration = mediaPlayer.getTotalDuration().toString().replace("ms", "").trim();
        double durationDouble = Double.parseDouble(duration);
        int durationInt = (int) durationDouble;
        int totalseconds = durationInt / 1000;
        int minutes = totalseconds / 60;
        int seconds = totalseconds % 60;
        String fullDuration = String.format("%02d:%02d", minutes, seconds);

        return fullDuration;
    }



    public void playMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }
    private static MusicFunctions instance;

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

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void editSong(Song song) {
        DALManager.getInstance().getSongDAO().editSong(song);
    }

    public void editPlaylist(Playlist playlist) {
        DALManager.getInstance().getPlaylistDAO().editPlaylist(playlist);
    }

    public void addPlaylist(String name, int songs, int seconds) {
        DALManager.getInstance().getPlaylistDAO().addPlaylist(name, songs, seconds);
    }

    public void deletePlaylist(Playlist playlist) {
        DALManager.getInstance().getPlaylistDAO().deletePlaylist(playlist);
    }

    private MusicFunctions() {}

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
}
