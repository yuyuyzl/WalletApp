package buaa.jj.accountservice.api;

import buaa.jj.accountservice.exceptions.*;

import java.util.List;
import java.util.Map;

public interface AccountService {
    int userLogin(String user_name,String user_passwd) throws UserNotExistException,UserFrozenException;
    int agencyLogin(String agency_name,String agency_passwd);
    int userRegister(String user_name,String user_passwd,String user_realname,String user_tel,String user_email,String user_identity,int under_agency_id) throws NameDuplicateException,UserAgencyDuplicateException,AgencyNotExistException;
    int userRegister(String user_name,String user_passwd,String user_realname,String user_tel,String user_email,String user_identity,String under_agency_name) throws NameDuplicateException,UserAgencyDuplicateException,AgencyNotExistException;
    boolean userPasswdChanging(int user_id,String old_passwd,String new_passwd) throws UserNotExistException,UserFrozenException;
    Map agencyInformation(int agency_id);
    List<Integer> agencyAllUser(int agency_id);
    Map userInformation(int user_id);
    int freezeUnfreeze(int user_id,boolean if_freeze) throws UserNotExistException;
    boolean foundPasswd(String user_name,String user_identity,String new_passwd) throws UserNotExistException,UserFrozenException;
    List<Map<String,String>> agencyTradeInformation(int agency_id, String start_date, String end_date, int trade_type);
    List<Map<String,String>> userTradeInformation(int user_id,String start_date,String end_date,int trade_type);
    boolean transferConsume(int pay_user_id,int get_user_id,double amount,boolean trade_type) throws UserNotExistException,UserFrozenException;
    boolean reCharge(int user_id,double amount,boolean recharge_platform) throws UserNotExistException;
    boolean drawMoney(int user_id,double amount,boolean draw_platform) throws UserNotExistException,UserFrozenException;
    int getID(String name,boolean type);
    void CSSystemReady();
    void BlockChainServiceReady();
    void CSSystemClosing();
    void BlockChainServiceClosing();
}
