package dk.easv.mytunes.be;

public class Playlist {
    private int id;        // Unique identifier of the playlist
    private String name;   // Name of the playlist

    // Constructor
    public Playlist(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

