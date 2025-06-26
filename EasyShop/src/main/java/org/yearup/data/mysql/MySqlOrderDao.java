package org.yearup.data.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yearup.data.OrderDao;
import org.yearup.models.Order;
import org.yearup.models.OrderLineItem;

import javax.sql.DataSource;
import java.sql.*;

@Component
public class MySqlOrderDao implements OrderDao {
    private final DataSource dataSource;

    @Autowired
    public MySqlOrderDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public int createOrder(Order order) {

        String sql = "INSERT INTO orders (user_id, date, address, city, state, zip, shipping_amount) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, order.getUserId());
            preparedStatement.setDate(2, Date.valueOf(order.getDate()));
            preparedStatement.setString(3,order.getAddress());
            preparedStatement.setString(4,order.getCity());
            preparedStatement.setString(5,order.getState());
            preparedStatement.setString(6,order.getZip());
            preparedStatement.setInt(7,order.getShipping_amount());
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next())
                return resultSet.getInt(1);
            else throw new SQLException("No ID obtained.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addOrderLineItem(OrderLineItem item) {
        String sql = "INSERT INTO order_line_items (order_id, product_id, sales_price, quantity, discount) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, item.getOrderId());
            preparedStatement.setInt(2, item.getProductId());
            preparedStatement.setBigDecimal(3, item.getSalesPrice());
            preparedStatement.setInt(4, item.getQuantity());
            preparedStatement.setInt(5, item.getDiscount());
            preparedStatement.executeUpdate();

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
