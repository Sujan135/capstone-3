package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.models.Profile;
import org.yearup.data.ProfileDao;

import javax.sql.DataSource;
import java.sql.*;

@Component
public class MySqlProfileDao extends MySqlDaoBase implements ProfileDao
{
    public MySqlProfileDao(DataSource dataSource)
    {
        super(dataSource);
    }

    @Override
    public Profile create(Profile profile)
    {
        String sql = "INSERT INTO profiles (user_id, first_name, last_name, phone, address, city, state, email, zip) " +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try(Connection connection = getConnection())
        {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, profile.getUserId());
            preparedStatement.setString(2, profile.getFirstName());
            preparedStatement.setString(3, profile.getLastName());
            preparedStatement.setString(4, profile.getPhone());
            preparedStatement.setString(5, profile.getAddress());
            preparedStatement.setString(6, profile.getCity());
            preparedStatement.setString(7, profile.getState());
            preparedStatement.setString(8, profile.getEmail());
            preparedStatement.setString(9, profile.getZip());

            preparedStatement.executeUpdate();

            return profile;
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Profile getByUserId(int userId)
    {
        String sql = "SELECT * FROM profiles WHERE user_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next())
            {
                return new Profile(
                        resultSet.getInt("user_id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("phone"),
                        resultSet.getString("address"),
                        resultSet.getString("city"),
                        resultSet.getString("state"),
                        resultSet.getString("email"),
                        resultSet.getString("zip")
                );
            }
            return null;
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Profile profile)
    {
        String sql = "UPDATE profiles SET first_name = ?, last_name = ?, phone = ?, email = ?, " +
                "address = ?, city = ?, state = ?, zip = ? WHERE user_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {
            preparedStatement.setString(1, profile.getFirstName());
            preparedStatement.setString(2, profile.getLastName());
            preparedStatement.setString(3, profile.getPhone());
            preparedStatement.setString(4, profile.getAddress());
            preparedStatement.setString(5, profile.getCity());
            preparedStatement.setString(6, profile.getState());
            preparedStatement.setString(7, profile.getZip());
            preparedStatement.setString(8, profile.getEmail());
            preparedStatement.setInt(9, profile.getUserId());

            preparedStatement.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

}
