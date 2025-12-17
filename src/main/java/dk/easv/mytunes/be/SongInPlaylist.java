package dk.easv.mytunes.be;

public class SongInPlaylist {
    private Song song;
    private int position;

    // Constructor
    public SongInPlaylist(Song song, int position) {
        this.song = song;
        this.position = position;
    }

    public Song getSong() { return song; }
    public int getPosition() { return position; }

    @Override
    public String toString() {
        return position + ": " + song.getTitle();
    }
}

