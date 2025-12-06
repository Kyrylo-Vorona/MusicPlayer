package dk.easv.mytunes.dal;

import dk.easv.mytunes.be.Category;
import dk.easv.mytunes.be.Song;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DALManager {
    private SongDAO songDAO;
    private CategoryDAO categoryDAO;
    private PlaylistDAO playlistDAO;
    private static DALManager instance;

    public static DALManager getInstance() {
        if (instance == null) {
            instance = new DALManager();
        }
        return instance;
    }

    private final ConnectionManager cm;

    private DALManager() {
        cm = new ConnectionManager();
    }

    public SongDAO getSongDAO() {
        if (songDAO == null) songDAO = new SongDAO();
        return songDAO;
    }

    public CategoryDAO getCategoryDAO() {
        if (categoryDAO == null) categoryDAO = new CategoryDAO();
        return categoryDAO;
    }

    public PlaylistDAO getPlaylistDAO() {
        if (playlistDAO == null) playlistDAO = new PlaylistDAO();
        return playlistDAO;
    }
}
