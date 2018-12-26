package cn.nulladev.test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by 绿叶风暴 on 2018/12/25.
 */
public class MyUtils2 {

    public static boolean isUsernameUsed(String username) {
        String sql = String.format("select * from user_data where name = '%s'", username);
        PreparedStatement pstmt;
        try {
            pstmt = (PreparedStatement)MyUtils.CONN.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static int getID(String username) {
        String sql = String.format("select * from user_data where name = '%s'", username);
        PreparedStatement pstmt;
        try {
            pstmt = (PreparedStatement)MyUtils.CONN.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static String getName(int ID) {
        String sql = String.format("select * from user_data where id = '%d'", ID);
        PreparedStatement pstmt;
        try {
            pstmt = (PreparedStatement)MyUtils.CONN.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("name");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getEncryptedPassword(String username) {
        if (!isUsernameUsed(username)) {
            return "";
        }
        String sql = String.format("select * from user_data where name = '%s'", username);
        PreparedStatement pstmt;
        try {
            pstmt = (PreparedStatement)MyUtils.CONN.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("encryptedpassword");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getEncryptedPassword(int id) {
        return getEncryptedPassword(getName(id));
    }

        public static boolean getIsFrozen(String username) {
        if (!isUsernameUsed(username)) {
            return false;
        }
        String sql = String.format("select * from user_data where name = '%s'", username);
        PreparedStatement pstmt;
        try {
            pstmt = (PreparedStatement)MyUtils.CONN.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getBoolean("isFrozen");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public static boolean getIsFrozen(int id) {
        return getIsFrozen(getName(id));
    }

}
