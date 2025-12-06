package dk.easv.mytunes.be;

public class Playlist {
    private int id;
    private String name;
    private int songs;
    private int seconds;

    public Playlist(int id, String name, int songs, int seconds) {
        this.id = id;
        this.name = name;
        this.songs = songs;
        this.seconds = seconds;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getSongs() {
        return songs;
    }

    public int getSeconds() {
        return seconds;
    }

    public String getFormattedTime() {
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int sec = seconds % 60;

        if (hours > 0) {
            return String.format("%d:%02d:%02d", hours, minutes, sec);
        } else {
            return String.format("%d:%02d", minutes, sec);
        }
    }


    public String getTime() {
        return getFormattedTime();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSongs(int songs) {
        this.songs = songs;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }
}
