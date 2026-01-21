package server;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository {

    //Connection connection = DBConnection.getConnection();

    // 1. insert(String name, int price, int qty)
    public void insert(String name, int price, int qty) {
        String sql = "INSERT INTO product(name, price, qty) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setInt(2, price);
            ps.setInt(3, qty);

            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("SQL Error");
        }
    }


    // 2. deleteById(int id)
    public int deleteById(int id){
        String sql = "DELETE FROM product WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate();


        }catch (Exception e){
            throw new RuntimeException("SQL Error");
        }
    }

    // 3. findById(int id)
    public Product findById(int id) {
        String sql = "SELECT id, name, price, qty FROM product WHERE ID = ?";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, id);

            try(ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Product(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getInt("price"),
                            rs.getInt("qty")
                    );
                }
                return null;
            }
        }catch (Exception e) {
            throw new RuntimeException("SQL Error");
        }
    }

    // 4. findAll()
    public List<Product> findAll() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT id, name, price, qty FROM product";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){

            while (rs.next()){
                Product p = new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("price"),
                        rs.getInt("qty")
                );
                list.add(p);
            }
            return list;
        }catch (Exception e){
            throw new RuntimeException("SQL Error");
        }
    }
}
