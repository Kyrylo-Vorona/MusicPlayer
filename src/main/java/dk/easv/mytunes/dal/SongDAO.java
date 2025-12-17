package dk.easv.mytunes.dal;

import dk.easv.mytunes.be.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SongDAO {
    private final ConnectionManager cm;

    // Creates a SongDAO and initializes the database connection manager
    public SongDAO() {
        cm = new ConnectionManager();
    }

    // Selects all playlists which contain the song to delete
    // Deletes from PlaylistSongs all lines with song to delete
    // Updates PlaylistSongs positions
    // Deletes selected song from Songs table in database
    public void deleteSong(Song song) {
        try (Connection con = cm.getConnection()) {
            con.setAutoCommit(false);

            String selectSql =
                    "SELECT PlaylistId, Position " +
                            "FROM PlaylistSongs WHERE SongId = ?";

            PreparedStatement select = con.prepareStatement(selectSql);
            select.setInt(1, song.getId());
            ResultSet rs = select.executeQuery();

            while (rs.next()) {
                int playlistId = rs.getInt("PlaylistId");
                int position = rs.getInt("Position");

                String deleteSql =
                        "DELETE FROM PlaylistSongs " +
                                "WHERE PlaylistId = ? AND SongId = ?";

                PreparedStatement delete = con.prepareStatement(deleteSql);
                delete.setInt(1, playlistId);
                delete.setInt(2, song.getId());
                delete.executeUpdate();

                String shiftSql =
                        "UPDATE PlaylistSongs " +
                                "SET Position = Position - 1 " +
                                "WHERE PlaylistId = ? AND Position > ?";

                PreparedStatement shift = con.prepareStatement(shiftSql);
                shift.setInt(1, playlistId);
                shift.setInt(2, position);
                shift.executeUpdate();
            }

            String deleteSongSql = "DELETE FROM Songs WHERE Id = ?";
            PreparedStatement deleteSong = con.prepareStatement(deleteSongSql);
            deleteSong.setInt(1, song.getId());
            deleteSong.executeUpdate();

            con.commit();
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    // Adds a new song to the database
    public void addSong(String title, String artist, int seconds, String file, Category category) {
        try (Connection con = cm.getConnection()) {
            String sql = "INSERT INTO Songs (Title, Artist, Time, [File], CategoryId) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, title);
            pstmt.setString(2, artist);
            pstmt.setInt(3, seconds);
            pstmt.setString(4, file);
            pstmt.setInt(5, category.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Updates selected song data
    public void editSong(Song song) {
        try (Connection con = cm.getConnection()) {
            String sql = "UPDATE Songs SET Title = ?, Artist = ?, Time = ?, [File] = ?, CategoryId = ? WHERE Id = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, song.getTitle());
            pstmt.setString(2, song.getArtist());
            pstmt.setInt(3, song.getSeconds());
            pstmt.setString(4, song.getPath());
            pstmt.setInt(5, song.getCategory().getId());
            pstmt.setInt(6, song.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Returns all songs in database
    public List<Song> getAllSongs() {
        List<Song> songs = new ArrayList<>();
        try (Connection con = cm.getConnection()) {
            String sql = "SELECT s.Id, s.Title, s.Artist, s.Time, s.[File], " +
                    "c.Id AS CategoryId, c.Name AS CategoryName " +
                    "FROM Songs AS s " +
                    "LEFT JOIN Categories AS c ON s.CategoryId = c.Id";
            PreparedStatement pstmt = con.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Category category = new Category(rs.getInt("CategoryId"), rs.getString("CategoryName"));
                songs.add(new Song(
                        rs.getInt("Id"),
                        rs.getString("Title"),
                        rs.getString("Artist"),
                        category,
                        rs.getInt("Time"),
                        rs.getString("File")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return songs;
    }
}

