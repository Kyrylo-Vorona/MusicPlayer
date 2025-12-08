package dk.easv.mytunes.dal;

import dk.easv.mytunes.be.Song;

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

    public List<Song> getSongsInPlaylist(int playlistId) {
        List<Song> songsInPlaylist = new ArrayList<>();

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
            pstmt.setInt(1, playlistId);              // ← передаём ID плейлиста!

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Song song = new Song(
                        rs.getInt("Id"),
                        rs.getString("Title"),
                        null,                   // Artist и Category у тебя не нужны сейчас
                        null,                   // можешь убрать это поле из конструктора, если мешает
                        0,                      // Time тоже не извлекаем
                        rs.getString("File")
                );
                songsInPlaylist.add(song);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return songsInPlaylist;
    }

}
