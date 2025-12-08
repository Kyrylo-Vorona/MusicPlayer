package dk.easv.mytunes.be;

public class PlaylistSongs {
    private int position;
    private Song song;
    private Playlist playlist;
    public PlaylistSongs(int position, Song song, Playlist playlist) {
        this.position = position;
        this.song = song;
        this.playlist = playlist;
    }

    public int getPosition() {
        return position;
    }

    public Song getSong() {
        return song;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }
}
