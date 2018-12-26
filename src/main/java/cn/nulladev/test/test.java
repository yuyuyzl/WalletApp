package cn.nulladev.test;

import buaa.jj.accountservice.api.AccountService;
import buaa.jj.accountservice.exceptions.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 绿叶风暴 on 2018/12/25.
 */
public class test implements AccountService {
    public int userLogin(String username, String password) throws UserNotExistException, UserFrozenException {
        if (!MyUtils2.isUsernameUsed(username)) {
            throw new UserNotExistException();
        }
        if (MyUtils2.getIsFrozen(username)) {
            throw new UserFrozenException();
        }
        if (password.equals(MyUtils2.getEncryptedPassword(username))) {
            return MyUtils2.getID(username);
        }
        return -1;
    }

    public int agencyLogin(String agency_name, String agency_passwd) {
        return 0;
    }

    public int userRegister(String username, String password, String realname, String tel, String email, String identity, int under_agency_id) throws NameDuplicateException, UserAgencyDuplicateException, AgencyNotExistException {
        if (MyUtils2.isUsernameUsed(username)) {
            throw new NameDuplicateException();
        }

        String encryptedPassword = password;
        String sql = String.format(
                "insert into user_data(name, encryptedpassword, realname, tel, email, identity,available,frozen,isFrozen) values('%s', '%s', '%s', '%s', '%s', '%s',0,0,false)",
                username, encryptedPassword, realname, tel, email, identity);
        PreparedStatement pstmt;
        try {
            pstmt = (PreparedStatement)MyUtils.CONN.prepareStatement(sql);
            pstmt.execute();
            return MyUtils2.getID(username);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int userRegister(String username, String password, String realname, String tel, String email, String identity, String under_agency_id) throws NameDuplicateException, UserAgencyDuplicateException, AgencyNotExistException {
        return userRegister(username, password, realname, tel, email, identity, -1);
    }

    public boolean userPasswdChanging(int id, String old_password, String new_password) throws UserNotExistException, UserFrozenException {
        if (MyUtils2.isUsernameUsed(MyUtils2.getName(id))) {
            throw new UserNotExistException();
        }
        if (MyUtils2.getIsFrozen(id)) {
            throw new UserFrozenException();
        }
        if (old_password.equals(MyUtils2.getEncryptedPassword(id))) {
            String encryptedPassword2 = new_password;
            String sql = String.format("UPDATE Person SET encryptedpassword = '%s' WHERE id = '%d'", encryptedPassword2, id);
            PreparedStatement pstmt;
            try {
                pstmt = (PreparedStatement)MyUtils.CONN.prepareStatement(sql);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            //是不是应该报个错
            return false;
        }
        return false;
    }

    public Map agencyInformation(int agency_id) {
        return null;
    }

    public List<Integer> agencyAllUser(int agency_id) {
        return null;
    }

    public Map userInformation(int id) {
        Map<String, Object> info = new HashMap();
        String sql = String.format("select * from user_data where id = '%s'", id);
        PreparedStatement pstmt;
        try {
            pstmt = (PreparedStatement)MyUtils.CONN.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                info.put("userID", rs.getInt("id"));
                info.put("userName", rs.getString("name"));
                info.put("userTel", rs.getString("tel"));
                info.put("userEmail", rs.getString("email"));
                info.put("userIdentity", rs.getString("identity"));
                info.put("agency", rs.getInt("agency"));
                info.put("availableBalance", rs.getInt("available"));
                info.put("frozenBalance", rs.getInt("frozen"));
                info.put("isFrozen", rs.getBoolean("isFrozen"));
                info.put("userRealName", rs.getString("realname"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public int freezeUnfreeze(int id, boolean if_freeze) throws UserNotExistException {
        if (MyUtils2.getIsFrozen(id) == if_freeze) {
            return 0;
        }
        if (if_freeze) {
            String sql = String.format("UPDATE Person SET isFrozen = 1 WHERE id = '%d'", id);
            PreparedStatement pstmt;
            try {
                pstmt = (PreparedStatement)MyUtils.CONN.prepareStatement(sql);
                return 1;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            String sql = String.format("UPDATE Person SET isFrozen = 0 WHERE id = '%d'", id);
            PreparedStatement pstmt;
            try {
                pstmt = (PreparedStatement)MyUtils.CONN.prepareStatement(sql);
                return 2;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public boolean foundPasswd(String username, String identity, String new_password) throws UserNotExistException, UserFrozenException {
        if (MyUtils2.isUsernameUsed(username)) {
            throw new UserNotExistException();
        }
        if (MyUtils2.getIsFrozen(username)) {
            throw new UserFrozenException();
        }
        String identity1 = "";
        String encryptedPassword = new_password;
        String sql1 = String.format("select * from user_data where name = '%s'", username);
        String sql2 = String.format("UPDATE Person SET encryptedpassword = '%s' WHERE name = '%s'", encryptedPassword, username);
        PreparedStatement pstmt;
        try {
            pstmt = (PreparedStatement)MyUtils.CONN.prepareStatement(sql1);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                identity1 =  rs.getString("identity");
            }
            if (identity1.equals(identity)) {
                pstmt = (PreparedStatement)MyUtils.CONN.prepareStatement(sql2);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Map<String, String>> agencyTradeInformation(int agency_id, String start_date, String end_date, int trade_type) {
        return null;
    }

    public List<Map<String, String>> userTradeInformation(int user_id, String start_date, String end_date, int trade_type) {
        return null;
    }

    public boolean transferConsume(int pay_user_id, int get_user_id, double amount, boolean trade_type) throws UserNotExistException, UserFrozenException {
        if (MyUtils2.isUsernameUsed(MyUtils2.getName(pay_user_id)) || MyUtils2.isUsernameUsed(MyUtils2.getName(get_user_id))) {
            throw new UserNotExistException();
        }
        if (MyUtils2.getIsFrozen(pay_user_id) || MyUtils2.getIsFrozen(get_user_id)) {
            throw new UserFrozenException();
        }
        int value1 = 0;
        int value2 = 0;
        String sql1 = String.format("select * from user_data where id = '%d'", pay_user_id);
        String sql2 = String.format("select * from user_data where id = '%d'", get_user_id);
        PreparedStatement pstmt;
        try {
            pstmt = (PreparedStatement)MyUtils.CONN.prepareStatement(sql1);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                value1 =  rs.getInt("available");
            }
            pstmt = (PreparedStatement)MyUtils.CONN.prepareStatement(sql2);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                value2 =  rs.getInt("available");
            }
            if (value1 >= amount) {
                String sql3 = String.format("UPDATE Person SET available = '%s' WHERE username = '%d'", value1 - amount, pay_user_id);
                String sql4 = String.format("UPDATE Person SET available = '%s' WHERE username = '%d'", value1 + amount, get_user_id);
                //TODO 记录账单
                pstmt = (PreparedStatement)MyUtils.CONN.prepareStatement(sql3);
                pstmt = (PreparedStatement)MyUtils.CONN.prepareStatement(sql4);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean reCharge(int id, double amount, boolean recharge_platform) throws UserNotExistException {
        if (MyUtils2.isUsernameUsed(MyUtils2.getName(id))) {
            throw new UserNotExistException();
        }
        int value = 0;
        String sql1 = String.format("select * from user_data where id = '%d'", id);
        PreparedStatement pstmt;
        try {
            pstmt = (PreparedStatement)MyUtils.CONN.prepareStatement(sql1);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                value =  rs.getInt("available");
            }
            String sql2 = String.format("UPDATE Person SET available = '%s' WHERE username = '%d'", value + amount, id);
            pstmt = (PreparedStatement)MyUtils.CONN.prepareStatement(sql2);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean drawMoney(int id, double amount, boolean draw_platform) throws UserNotExistException, UserFrozenException {
        if (MyUtils2.isUsernameUsed(MyUtils2.getName(id))) {
            throw new UserNotExistException();
        }
        if (MyUtils2.getIsFrozen(MyUtils2.getName(id))) {
            throw new UserFrozenException();
        }
        int value = 0;
        String sql1 = String.format("select * from user_data where id = '%d'", id);
        PreparedStatement pstmt;
        try {
            pstmt = (PreparedStatement)MyUtils.CONN.prepareStatement(sql1);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                value =  rs.getInt("available");
            }
            if (value >= amount) {
                String sql2 = String.format("UPDATE Person SET available = '%s' WHERE username = '%d'", value - amount, id);
                pstmt = (PreparedStatement)MyUtils.CONN.prepareStatement(sql2);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getID(String name, boolean type) {
        return 0;
    }

    public void CSSystemReady() {

    }

    public void BlockChainServiceReady() {

    }

    public void CSSystemClosing() {

    }

    public void BlockChainServiceClosing() {

    }
}
