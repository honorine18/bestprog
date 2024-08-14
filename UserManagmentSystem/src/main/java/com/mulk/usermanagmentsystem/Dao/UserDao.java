package com.mulk.usermanagmentsystem.Dao;

import com.mulk.usermanagmentsystem.Model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.*;
import java.util.*;

public class UserDao {

    private static final Logger logger = LogManager.getLogger(UserDao.class);

    static {
        try {
            Class.forName("org.postgresql.Driver");
            logger.info("PostgreSQL JDBC Driver loaded successfully.");
        } catch (ClassNotFoundException e) {
            logger.error("Failed to load PostgreSQL JDBC driver.", e);
            throw new RuntimeException("Failed to load PostgreSQL JDBC driver.", e);
        }
    }

    private final String jdbcURL = "jdbc:postgresql://localhost:5432/user_management_db";
    private final String jdbcUserName = "postgres";
    private final String jdbcPasswd = "Admin1";

    //Insert user
    public Integer registerUSer(User userObj) {
        //CRUD Queries
        String insertUserQuery = "INSERT INTO USERS (fullName,email,country) VALUES(?,?,?)";
        try (Connection conn = DriverManager.getConnection(jdbcURL, jdbcUserName, jdbcPasswd);
             PreparedStatement stm = conn.prepareStatement(insertUserQuery)) {

            stm.setString(1, userObj.getFullName());
            stm.setString(2, userObj.getEmail());
            stm.setString(3, userObj.getCountry());

            int num = stm.executeUpdate();
            logger.debug("Inserted user: {}", userObj);
            return num;
        } catch (Exception e) {
            logger.error("Error inserting user: {}", userObj, e);
        }
        return null;
    }

    //updateUser
    public Integer updateUser(User userObj) {
        try (Connection conn = DriverManager.getConnection(jdbcURL, jdbcUserName, jdbcPasswd)) {
            String query1 = "SELECT userId FROM USERS WHERE userId = ?";
            PreparedStatement stm1 = conn.prepareStatement(query1);
            stm1.setInt(1, userObj.getUserId());

            ResultSet rs = stm1.executeQuery();
            if (rs.next()) {
                String updateUserQuery = "UPDATE USERS SET fullName = ?, email = ?, country = ? WHERE userId = ?";
                try (PreparedStatement stm = conn.prepareStatement(updateUserQuery)) {
                    stm.setString(1, userObj.getFullName());
                    stm.setString(2, userObj.getEmail());
                    stm.setString(3, userObj.getCountry());
                    stm.setInt(4, userObj.getUserId());
                    int num = stm.executeUpdate();
                    logger.debug("Updated user: {}", userObj);
                    return num;
                }
            } else {
                logger.warn("User not found: {}", userObj.getUserId());
                return 0;
            }
        } catch (SQLException e) {
            logger.error("Error updating user: {}", userObj, e);
        }
        return 0;
    }

    //Retrieve User by ID
    public User findUserById(User userObj) {
        String selectUserQuery = "SELECT userId, fullName, email, country FROM USERS WHERE userId = ?";
        try (Connection conn = DriverManager.getConnection(jdbcURL, jdbcUserName, jdbcPasswd);
             PreparedStatement stm = conn.prepareStatement(selectUserQuery)) {

            stm.setInt(1, userObj.getUserId());
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                String fullName = rs.getString("fullName");
                String email = rs.getString("email");
                String country = rs.getString("country");
                logger.debug("Found user by ID: {}", userObj.getUserId());
                return new User(userObj.getUserId(), fullName, email, country);
            }
        } catch (SQLException e) {
            logger.error("Error finding user by ID: {}", userObj.getUserId(), e);
        }
        return null;
    }

    //Retrieve all Users
    public List<User> retrieveAllUser() {
        List<User> users = new ArrayList<>();
        String selectAllUserQuery = "SELECT * FROM USERS";
        try (Connection conn = DriverManager.getConnection(jdbcURL, jdbcUserName, jdbcPasswd);
             PreparedStatement stm = conn.prepareStatement(selectAllUserQuery)) {

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                int userId = rs.getInt("userId");
                String fullName = rs.getString("fullName");
                String email = rs.getString("email");
                String country = rs.getString("country");
                users.add(new User(userId, fullName, email, country));
            }
            logger.debug("Retrieved all users: {}", users.size());
        } catch (SQLException e) {
            logger.error("Error retrieving all users", e);
        }
        return users;
    }

    //Delete user
    public Integer deleteUser(User userObj) {
        String deleteUserQuery = "DELETE FROM USERS WHERE userId = ?";
        try (Connection conn = DriverManager.getConnection(jdbcURL, jdbcUserName, jdbcPasswd);
             PreparedStatement stm = conn.prepareStatement(deleteUserQuery)) {

            stm.setInt(1, userObj.getUserId());
            int num = stm.executeUpdate();
            logger.debug("Deleted user: {}", userObj.getUserId());
            return num;
        } catch (SQLException e) {
            logger.error("Error deleting user: {}", userObj.getUserId(), e);
        }
        return null;
    }
}
