package dk.easv.mytunes.dal;

import dk.easv.mytunes.be.Playlist;
import dk.easv.mytunes.be.PlaylistSongs;
import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.be.SongInPlaylist;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class PlaylistSongsDAO {
    private final ConnectionManager cm;

    public PlaylistSongsDAO () {
        cm = new ConnectionManager();
    }

    public List<SongInPlaylist> getSongsInPlaylist(int playlistId) {
        List<SongInPlaylist> songsInPlaylist = new ArrayList<>();

        try (Connection con = cm.getConnection()) {
            String sql = "SELECT " +
                    "ps.Position, " +
                    "s.Id, " +
                    "s.Title, " +
                    "s.[File] " +
                    "FROM PlaylistSongs AS ps " +
                    "JOIN Songs AS s ON ps.SongId = s.Id " +
                    "WHERE ps.PlaylistId = ? " +
                    "ORDER BY ps.Position;";

            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, playlistId);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Song song = new Song(
                        rs.getInt("Id"),
                        rs.getString("Title"),
                        null,
                        null,
                        0,
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

    public void addSongToPlaylist(int position, Song song, Playlist playlist) {
        try (Connection con = cm.getConnection()) {
            String insertSql = "INSERT INTO PlaylistSongs (Position, SongId, PlaylistId) VALUES (?, ?, ?)";
            String updateSql = "UPDATE Playlists SET Songs = ?, Time = ? WHERE Id = ?";
            PreparedStatement insert = con.prepareStatement(insertSql);
            insert.setInt(1, position);
            insert.setInt(2, song.getId());
            insert.setInt(3, playlist.getId());
            insert.executeUpdate();
            playlist.setSongs(playlist.getSongs() + 1);
            playlist.setSeconds(playlist.getSeconds() + song.getSeconds());

            PreparedStatement update = con.prepareStatement(updateSql);
            update.setInt(1, playlist.getSongs());
            update.setInt(2, playlist.getSeconds());
            update.setInt(3, playlist.getId());
            update.executeUpdate();

        }
        catch (SQLException e)  {
            throw new RuntimeException(e);
        }
    }

    public void deleteSongFromPlaylist(int position, Playlist playlist, Song song) {
        try (Connection con = cm.getConnection()) {

            String deleteSql =
                    "DELETE FROM PlaylistSongs " +
                            "WHERE Position = ? AND PlaylistId = ? AND SongId = ?";

            PreparedStatement delete = con.prepareStatement(deleteSql);
            delete.setInt(1, position);
            delete.setInt(2, playlist.getId());
            delete.setInt(3, song.getId());
            delete.executeUpdate();

            String calcSql =
                    "SELECT COUNT(*) AS cnt, COALESCE(SUM(s.Time), 0) AS total " +
                            "FROM PlaylistSongs ps " +
                            "JOIN Songs s ON ps.SongId = s.Id " +
                            "WHERE ps.PlaylistId = ?";

            PreparedStatement calc = con.prepareStatement(calcSql);
            calc.setInt(1, playlist.getId());
            ResultSet rs = calc.executeQuery();

            int count = 0;
            int totalSeconds = 0;

            if (rs.next()) {
                count = rs.getInt("cnt");
                totalSeconds = rs.getInt("total");
            }

            String updateSql =
                    "UPDATE Playlists SET Songs = ?, Time = ? WHERE Id = ?";

            PreparedStatement update = con.prepareStatement(updateSql);
            update.setInt(1, count);
            update.setInt(2, totalSeconds);
            update.setInt(3, playlist.getId());
            update.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
