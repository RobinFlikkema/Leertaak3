package com.example.leertaakt_two;

import java.io.IOException;
import java.net.ServerSocket;

public class Main {
    public static void main(String[] args)
            throws IOException {
        ServerSocket serverSocket = new ServerSocket(7789);
        new WeatherdataReceiver(serverSocket);
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