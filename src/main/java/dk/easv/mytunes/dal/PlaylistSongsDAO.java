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
            String sql = "INSERT INTO PlaylistSongs (Position, SongId, PlaylistId) VALUES (?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, position);
            pstmt.setInt(2, song.getId());
            pstmt.setInt(3, playlist.getId());
            pstmt.executeUpdate();
        }
        catch (SQLException e)  {
            throw new RuntimeException(e);
        }
    }

    public void deleteSongFromPlaylist(int position, Playlist playlist, Song song) {
        try (Connection con = cm.getConnection()) {
            String sql = "DELETE FROM PlaylistSongs WHERE position = ? AND PlaylistId = ? AND SongId = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, position);
            pstmt.setInt(2, playlist.getId());
            pstmt.setInt(3, song.getId());
            pstmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
