package dk.easv.mytunes.dal;

import dk.easv.mytunes.be.Playlist;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlaylistDAO {
    private final ConnectionManager cm;

    // Creates a PlaylistDAO and initializes the database connection manager
    public PlaylistDAO () {
        cm = new ConnectionManager();
    }

    // Deletes the selected playlist and all its song relations from the database
    public void deletePlaylist(Playlist playlist) {
        try (Connection con = cm.getConnection()) {
            String sql1 = "DELETE FROM PlaylistSongs WHERE PlaylistId = ?";
            PreparedStatement pstmt1 = con.prepareStatement(sql1);
            pstmt1.setInt(1, playlist.getId());
            pstmt1.executeUpdate();

            String sql2 = "DELETE FROM Playlists WHERE Id = ?";
            PreparedStatement pstmt2 = con.prepareStatement(sql2);
            pstmt2.setInt(1, playlist.getId());
            pstmt2.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Adds new playlist
    public void addPlaylist(String name) {
        try (Connection con = cm.getConnection()) {
            String sql = "INSERT INTO Playlists (Name) VALUES (?)";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.executeUpdate();
        }
        catch (SQLException e)  {
            throw new RuntimeException(e);
        }
    }

    // Updates the name of the selected playlist in the database
    public void editPlaylist(Playlist playlist) {
        try (Connection con = cm.getConnection()) {
            String sql = "UPDATE Playlists SET Name = ? WHERE Id = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, playlist.getName());
            pstmt.setInt(2, playlist.getId());
            pstmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Returns a list with all playlists
    public List<Playlist> getAllPlaylists() {
        List<Playlist> playlists = new ArrayList<>();

        try (Connection con = cm.getConnection()) {
            String sql = "SELECT * FROM Playlists";
            PreparedStatement pstmt = con.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                playlists.add(new Playlist(
                        rs.getInt("Id"),
                        rs.getString("Name")
                ));
            }

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return playlists;
    }
}
