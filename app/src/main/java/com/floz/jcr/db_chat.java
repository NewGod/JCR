package com.floz.jcr;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

class db_chat {
    private static Connection getConn() {
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://123.206.87.179:3306/JCR?useUnicode=true&characterEncoding=UTF-8";
        String username = "JCR";
        String password = "123456";
        Connection conn = null;
        try {
            Class.forName(driver); //classLoader,加载对应驱动
            conn = (Connection) DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    static int creat_room(String room_name) {
        Connection conn = getConn();

        int i = 0;
        String sql = "insert into room_name (room) values(?)";
        PreparedStatement pstmt;
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            pstmt.setString(1, room_name);
            i = pstmt.executeUpdate();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return i;
    }

    static void getRoomName(List<String> room){
        Connection conn = getConn();
        String sql = "select * from room_name";
        PreparedStatement pstmt;
        try{
            pstmt = (PreparedStatement)conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) room.add(rs.getString(1));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static List<ChatEntity> chat_info(String room_name) throws SQLException{
        synchronized (db_chat.class) {
            Connection conn = getConn();
            String sql = "select * from chat_info WHERE room_info = '" + room_name + "' ORDER BY time DESC";
            //System.out.println(sql);
            PreparedStatement pstmt;
            try {
                pstmt = (PreparedStatement) conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery();
                List<ChatEntity> ans = new ArrayList<ChatEntity>();

                for (int cnt = 0; cnt < 10 && rs.next(); cnt++){
                    ChatEntity info=new ChatEntity();
                    info.setContent(rs.getString(3));
                    info.setChatTime(rs.getString(2));
                    info.setUser(rs.getString(1));
                    ans.add(info);
                }
            //    Collections.reverse(ans);
                pstmt.close();
                conn.close();
                return ans;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    static int insert(String user_name,String info,String room_name) {
        synchronized (db_chat.class) {
            Connection conn = getConn();
            int i = 0;
            String sql = "insert into chat_info (user_name,info,room_info) values(?,?,?)";
            PreparedStatement pstmt;
            if (conn==null) {
                return 0;
            }
            try {
                pstmt = (PreparedStatement) conn.prepareStatement(sql);
                pstmt.setString(1, user_name);
                pstmt.setString(2, info);
                pstmt.setString(3, room_name);
                i = pstmt.executeUpdate();
                pstmt.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return i;
        }
    }
}
