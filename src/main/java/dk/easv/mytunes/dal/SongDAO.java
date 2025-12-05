package dk.easv.mytunes.dal;

import dk.easv.mytunes.be.Category;
import dk.easv.mytunes.be.Song;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SongDAO {
    private final ConnectionManager cm;

    public SongDAO () {
        cm = new ConnectionManager();
    }

    public void deleteSong(Song song) {
        try (Connection con = cm.getConnection()) {
            String sql = "DELETE FROM Songs WHERE id=?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, song.getId());
            pstmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

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
        }
        catch (SQLException e)  {
            throw new RuntimeException(e);
        }
    }

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
            pstmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Song> getAllSongs() {
        List<Song> songs = new ArrayList<>();
        try (Connection con = cm.getConnection()) {
            String sql = "SELECT \n" +
                    "    s.Id,\n" +
                    "    s.Title,\n" +
                    "    s.Artist,\n" +
                    "    s.Time,\n" +
                    "    s.[File],\n" +
                    "    c.Id AS CategoryId,\n" +
                    "    c.Name AS CategoryName\n" +
                    "FROM Songs AS s\n" +
                    "LEFT JOIN Categories AS c\n" +
                    "    ON s.CategoryId = c.Id;\n";
            PreparedStatement pstmt = con.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Category category = new Category(
                        rs.getInt("CategoryId"),
                        rs.getString("CategoryName")
                );
                songs.add(new Song(
                        rs.getInt("id"),
                        rs.getString("Title"),
                        rs.getString("Artist"),
                        category,
                        rs.getInt("Time"),
                        rs.getString("File")
                ));
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return songs;
    }
}
