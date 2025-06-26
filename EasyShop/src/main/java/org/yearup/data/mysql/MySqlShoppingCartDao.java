package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MySqlShoppingCartDao implements ShoppingCartDao {

    private final DataSource dataSource;

    public MySqlShoppingCartDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ShoppingCart getByUserId(int userId) {
        String sql = "SELECT ci.product_id, ci.quantity, p.name, p.price, p.description " +
                "FROM shopping_cart ci " +
                "JOIN products p ON ci.product_id = p.product_id " +
                "WHERE ci.user_id = ?";

        Map<Integer, ShoppingCartItem> items = new HashMap<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    ShoppingCartItem item = new ShoppingCartItem();

                    Product product = new Product();
                    product.setProductId(rs.getInt("product_id"));
                    product.setName(rs.getString("name"));
                    product.setPrice(rs.getBigDecimal("price"));
                    product.setDescription(rs.getString("description"));

                    item.setProduct(product);
                    item.setQuantity(rs.getInt("quantity"));

                    items.put(product.getProductId(), item);
                }
            }

            ShoppingCart cart = new ShoppingCart();
            cart.setUserId(userId);
            cart.setItems(items);

            return cart;

        } catch (SQLException e) {
            System.out.println(e);
            throw new RuntimeException("Error getting shopping cart for user " + userId, e);
        }
    }

    @Override
    public void addProduct(int userId, int productId) {
        String sql = "INSERT INTO shopping_cart (user_id, product_id, quantity) VALUES (?, ?, 1) " +
                "ON DUPLICATE KEY UPDATE quantity = quantity + 1";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setInt(2, productId);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error adding product to cart", e);
        }
    }

    @Override
    public void updateQuantity(int userId, int productId, int quantity) {
        String sql = "UPDATE shopping_cart SET quantity = ? WHERE user_id = ? AND product_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, quantity);
            statement.setInt(2, userId);
            statement.setInt(3, productId);

            int rows = statement.executeUpdate();
            if (rows == 0) {
                throw new RuntimeException("No cart item found to update");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error updating cart item quantity", e);
        }
    }

    @Override
    public void clearCart(int userId) {
        String sql = "DELETE FROM shopping_cart WHERE user_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e);
            throw new RuntimeException("Error clearing cart", e);
        }
    }

    @Override
    public List<ShoppingCartItem> getCartItems(int userId) {
        String sql = "SELECT ci.product_id, ci.quantity, p.name, p.description, p.price " +
                "FROM shopping_cart ci JOIN products p ON ci.product_id = p.product_id WHERE ci.user_id = ?";

        List<ShoppingCartItem> items = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ShoppingCartItem item = new ShoppingCartItem();

                    Product product = new Product();
                    product.setProductId(rs.getInt("product_id"));
                    product.setName(rs.getString("name"));
                    product.setDescription(rs.getString("description"));
                    product.setPrice(rs.getBigDecimal("price"));

                    item.setProduct(product);
                    item.setQuantity(rs.getInt("quantity"));

                    items.add(item);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching cart items", e);
        }

        return items;
    }


}
