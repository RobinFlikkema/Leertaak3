package com.example.leertaakt_two;

import com.mysql.cj.jdbc.MysqlDataSource;
import com.mysql.cj.jdbc.PreparedStatement;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main (String[] args)
            throws IOException {
        ServerSocket serversocket = new ServerSocket();
        serversocket.bind(new InetSocketAddress("0.0.0.0", 7789));
        Socket socket = serversocket.accept();
        while(true) {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            System.out.println(oos.toString());
        }



//        MysqlDataSource dataSource = new MysqlDataSource();
//        dataSource.setUser("leertaak2");
//        dataSource.setPassword("Wyup&960");
//        dataSource.setServerName("controlpanel.bennink.me");
//
//        try (Connection connection = dataSource.getConnection()){
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
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