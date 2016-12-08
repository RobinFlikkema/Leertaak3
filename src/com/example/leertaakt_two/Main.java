package com.example.leertaakt_two;

import com.mysql.cj.jdbc.MysqlDataSource;
import com.mysql.cj.jdbc.PreparedStatement;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main (String[] args){
            MysqlDataSource dataSource = new MysqlDataSource();
            dataSource.setUser("leertaak2");
            dataSource.setPassword("Wyup&960");
            dataSource.setServerName("controlpanel.bennink.me");

            try (Connection connection = dataSource.getConnection()){

            } catch (SQLException e) {
                e.printStackTrace();
            }
    }
}



//    public List<User> getUser(int userId) {
//        try (Connection con = DriverManager.getConnection(myConnectionURL);
//             PreparedStatement ps = createPreparedStatement(con, userId);
//             ResultSet rs = ps.executeQuery()) {
//
//            // process the resultset here, all resources will be cleaned up
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private PreparedStatement createPreparedStatement(Connection con, int userId) throws SQLException {
//        String sql = "SELECT id, username FROM users WHERE id = ?";
//        PreparedStatement ps = con.prepareStatement(sql);
//        ps.setInt(1, userId);
//        return ps;
//    }