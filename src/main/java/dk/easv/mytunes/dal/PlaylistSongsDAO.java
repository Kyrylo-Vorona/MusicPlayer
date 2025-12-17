package dk.easv.mytunes.dal;

import dk.easv.mytunes.be.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class PlaylistSongsDAO {
    private final ConnectionManager cm;

    // Creates a PlaylistSongsDAO and initializes the database connection manager
    public PlaylistSongsDAO () {
        cm = new ConnectionManager();
    }

    // Loads all songs in the selected playlist and their positions
    // Returns list of SongInPlaylist in selected playlist
    public List<SongInPlaylist> getSongsInPlaylist(int playlistId) {
        List<SongInPlaylist> songsInPlaylist = new ArrayList<>();

        try (Connection con = cm.getConnection()) {
            String sql = "SELECT\n" +
                    "  ps.Position,\n" +
                    "  s.Id,\n" +
                    "  s.Title,\n" +
                    "  s.Artist,\n" +
                    "  s.Time,\n" +
                    "  s.[File],\n" +
                    "  c.Id   AS CategoryId,\n" +
                    "  c.Name AS CategoryName\n" +
                    "FROM PlaylistSongs ps\n" +
                    "JOIN Songs s ON ps.SongId = s.Id\n" +
                    "LEFT JOIN Categories c ON s.CategoryId = c.Id\n" +
                    "WHERE ps.PlaylistId = ?\n" +
                    "ORDER BY ps.Position;\n";

            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, playlistId);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Song song = new Song(
                        rs.getInt("Id"),
                        rs.getString("Title"),
                        rs.getString("Artist"),
                        new Category(
                                rs.getInt("CategoryId"),
                                rs.getString("CategoryName")
                        ),
                        rs.getInt("Time"),
                        rs.getString("File")
                );


                songsInPlaylist.add(new SongInPlaylist(
                        song,
                        rs.getInt("position")
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return songsInPlaylist;
    }

    // Adds song to playlist
    public void addSongToPlaylist(int position, Song song, Playlist playlist) {
        try (Connection con = cm.getConnection()) {
            String insertSql =
                    "INSERT INTO PlaylistSongs (Position, SongId, PlaylistId) VALUES (?, ?, ?)";
            PreparedStatement insert = con.prepareStatement(insertSql);
            insert.setInt(1, position);
            insert.setInt(2, song.getId());
            insert.setInt(3, playlist.getId());
            insert.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Deletes selected song from playlist and fixes positions
    public void deleteSongFromPlaylist(int position, Playlist playlist, Song song) {
        try (Connection con = cm.getConnection()) {

            String deleteSql = "DELETE FROM PlaylistSongs WHERE Position = ? AND PlaylistId = ? AND SongId = ?";
            PreparedStatement delete = con.prepareStatement(deleteSql);
            delete.setInt(1, position);
            delete.setInt(2, playlist.getId());
            delete.setInt(3, song.getId());
            delete.executeUpdate();

            fixPositions(playlist.getId());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Selects songs from selected playlist and sets new positions for them
    public void fixPositions(int playlistId) {
        try (Connection con = cm.getConnection()) {

            String selectSql = "SELECT SongId FROM PlaylistSongs WHERE PlaylistId = ? ORDER BY Position ASC";
            PreparedStatement select = con.prepareStatement(selectSql);
            select.setInt(1, playlistId);

            ResultSet rs = select.executeQuery();

            int newPos = 1;
            String updateSql = "UPDATE PlaylistSongs SET Position = ? WHERE PlaylistId = ? AND SongId = ?";
            PreparedStatement update = con.prepareStatement(updateSql);

            while (rs.next()) {
                int songId = rs.getInt("SongId");

                update.setInt(1, newPos);
                update.setInt(2, playlistId);
                update.setInt(3, songId);
                update.executeUpdate();

                newPos++;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Swaps the position of the selected song with the previous song in the playlist
    public void moveUp(int playlistId, int position) {
        if (position <= 1) return;

        String sql = "UPDATE PlaylistSongs SET Position = CASE WHEN Position = ? THEN ? WHEN Position = ? THEN ? END WHERE PlaylistId = ? AND Position IN (?, ?)";

        try (Connection con = cm.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, position);
            stmt.setInt(2, position - 1);

            stmt.setInt(3, position - 1);
            stmt.setInt(4, position);

            stmt.setInt(5, playlistId);

            stmt.setInt(6, position);
            stmt.setInt(7, position - 1);

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Swaps the position of the selected song with the next song in the playlist
    public void moveDown(int playlistId, int position) {

        String sql = "UPDATE PlaylistSongs SET Position = CASE WHEN Position = ? THEN ? WHEN Position = ? THEN ? END WHERE PlaylistId = ? AND Position IN (?, ?)";

        try (Connection con = cm.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, position);
            stmt.setInt(2, position + 1);

            stmt.setInt(3, position + 1);
            stmt.setInt(4, position);

            stmt.setInt(5, playlistId);

            stmt.setInt(6, position);
            stmt.setInt(7, position + 1);

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
