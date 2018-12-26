package cn.nulladev.test;

import java.sql.Connection;
import java.sql.DriverManager;
/**
 * Created by 绿叶风暴 on 2018/12/25.
 */
public class MyUtils {
    public static final String DRIVER = "com.mysql.jdbc.Driver";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "970317";

    public static final String URL = "jdbc:mysql://localhost:3306/wallet?characterEncoding=utf8&useSSL=true&serverTimezone=GMT%2B8";
    public static final Connection CONN = getConn(URL, USERNAME, PASSWORD);

    private static Connection getConn(String url, String username, String password) {
        Connection conn = null;
        try {
            Class.forName(DRIVER);
            conn = (Connection) DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
}
