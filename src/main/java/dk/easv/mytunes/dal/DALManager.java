package dk.easv.mytunes.dal;

// This class ensures that each DAO is created only when needed and that only one instance of each DAO exists
public class DALManager {
    private SongDAO songDAO;
    private CategoryDAO categoryDAO;
    private PlaylistDAO playlistDAO;
    private PlaylistSongsDAO  playlistSongsDAO;
    private static DALManager instance;

    // Returns the singleton instance of DALManager
    public static DALManager getInstance() {
        if (instance == null) {
            instance = new DALManager();
        }
        return instance;
    }

    private final ConnectionManager cm;

    // Private constructor
    private DALManager() {
        cm = new ConnectionManager();
    }

    // Returns the SongDAO instance
    public SongDAO getSongDAO() {
        if (songDAO == null) songDAO = new SongDAO();
        return songDAO;
    }

    // Returns the CategoryDAO instance
    public CategoryDAO getCategoryDAO() {
        if (categoryDAO == null) categoryDAO = new CategoryDAO();
        return categoryDAO;
    }

    // Returns the PlaylistDAO instance
    public PlaylistDAO getPlaylistDAO() {
        if (playlistDAO == null) playlistDAO = new PlaylistDAO();
        return playlistDAO;
    }

    // Returns the PlaylistSongsDAO instance
    public PlaylistSongsDAO getSongsInPlaylistDAO() {
        if (playlistSongsDAO == null) playlistSongsDAO = new PlaylistSongsDAO();
        return playlistSongsDAO;
    }
}
