package dk.easv.mytunes.be;

public class Song {

    private int id;
    private String title;
    private String artist;
    private Category category;
    private int seconds;
    private String path;

    // Constructor
    public Song(int id, String title, String artist, Category category, int seconds,  String path) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.category = category;
        this.seconds = seconds;
        this.path = path;
    }
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public Category getCategory() {
        return category;
    }

    public int getSeconds() {
        return seconds;
    }

    public String getPath() {
        return path;
    }

    public String getFormattedTime() {
        int min = seconds / 60;
        int sec = seconds % 60;
        return String.format("%d:%02d", min, sec);
    }

    public String getTime() {
        return getFormattedTime();
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void setArtist(String artist) {
        this.artist = artist;
    }
    public void setCategory(Category category) {
        this.category = category;
    }
    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }
    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return title;
    }
}
