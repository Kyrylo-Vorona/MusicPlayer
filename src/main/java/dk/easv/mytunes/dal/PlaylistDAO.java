package dk.easv.mytunes.dal;

import dk.easv.mytunes.be.Category;
import dk.easv.mytunes.be.Playlist;
import dk.easv.mytunes.be.Song;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlaylistDAO {
    private final ConnectionManager cm;

    public PlaylistDAO () {
        cm = new ConnectionManager();
    }

    public void deletePlaylist(Playlist playlist) {
        try (Connection con = cm.getConnection()) {
            String sql = "DELETE FROM PlaylistSongs WHERE playlistId=?" +
                         "DELETE FROM Playlists WHERE id=?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, playlist.getId());
            pstmt.setInt(2, playlist.getId());
            pstmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addPlaylist(String name, int songs, int seconds) {
        try (Connection con = cm.getConnection()) {
            String sql = "INSERT INTO Playlists (Name, Songs, Time) VALUES (?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setInt(2, songs);
            pstmt.setInt(3, seconds);
            pstmt.executeUpdate();
        }
        catch (SQLException e)  {
            throw new RuntimeException(e);
        }
    }

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

    public List<Playlist> getAllPlaylists() {
        List<Playlist> playlists = new ArrayList<>();

        try (Connection con = cm.getConnection()) {
            String sql = "SELECT * FROM Playlists";
            PreparedStatement pstmt = con.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                playlists.add(new Playlist(
                        rs.getInt("Id"),
                        rs.getString("Name"),
                        rs.getInt("Songs"),
                        rs.getInt("Time")
                ));
            }

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return playlists;
    }
}
