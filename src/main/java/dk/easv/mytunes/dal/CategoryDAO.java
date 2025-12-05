package dk.easv.mytunes.dal;

import dk.easv.mytunes.be.Category;
import dk.easv.mytunes.be.Song;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class CategoryDAO {
    private ConnectionManager cm;

    public CategoryDAO() {
        cm = new ConnectionManager();
    }

    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();

        try (Connection con = cm.getConnection()) {
            String sql = "SELECT * FROM Categories";
            PreparedStatement pstmt = con.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                categories.add(new Category(
                        rs.getInt("Id"),
                        rs.getString("Name")
                ));
            }

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return categories;
    }
}
