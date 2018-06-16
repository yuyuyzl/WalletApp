package com.yuyuyzl.WalletApp.Dubbo;

import buaa.jj.accountservice.api.AccountService;
import buaa.jj.accountservice.exceptions.*;

import java.util.*;

public class AccountServiceTestWrapper implements AccountService {
    private AccountService accountService;
    public AccountServiceTestWrapper(AccountService accountService){
        this.accountService=accountService;
    }

    public int userLogin(String user_name, String user_passwd) throws UserNotExistException, UserFrozenException {

        if(accountService!=null)return accountService.userLogin(user_name, user_passwd);
        return 9;
    }

    public int agencyLogin(String agency_name, String agency_passwd) {
        return accountService.agencyLogin(agency_name,agency_passwd);
    }

    public int userRegister(String user_name, String user_passwd, String user_realname, String user_tel, String user_email, String user_identity, int under_agency_id) throws NameDuplicateException, UserAgencyDuplicateException, AgencyNotExistException {
        return accountService.userRegister( user_name,  user_passwd,  user_realname,  user_tel,  user_email,  user_identity,  under_agency_id);
    }

    public int userRegister(String user_name, String user_passwd, String user_realname, String user_tel, String user_email, String user_identity, String under_agency_name) throws NameDuplicateException, UserAgencyDuplicateException, AgencyNotExistException {
        return accountService.userRegister( user_name,  user_passwd,  user_realname,  user_tel,  user_email,  user_identity,  under_agency_name);
    }

    public boolean userPasswdChanging(int user_id, String old_passwd, String new_passwd) throws UserNotExistException, UserFrozenException {
        return accountService.userPasswdChanging(user_id, old_passwd, new_passwd);
    }

    public Map agencyInformation(int agency_id) {
        return accountService.agencyInformation(agency_id);
    }

    public List<Integer> agencyAllUser(int agency_id) {
        return accountService.agencyAllUser(agency_id);
    }

    public Map userInformation(int user_id) {
        if(accountService!=null)return accountService.userInformation(user_id);
        Map p=new HashMap();
        p.put("userRealName","YZL");
        return p;
    }

    public int freezeUnfreeze(int user_id, boolean if_freeze) throws UserNotExistException {
        return accountService.freezeUnfreeze(user_id, if_freeze);
    }

    public boolean foundPasswd(String user_name, String user_identity, String new_passwd) throws UserNotExistException, UserFrozenException {
        return accountService.foundPasswd(user_name, user_identity, new_passwd);
    }

    public List<Map<String, String>> agencyTradeInformation(int agency_id, String start_date, String end_date, int trade_type) {
        return accountService.agencyTradeInformation(agency_id, start_date, end_date, trade_type);
    }

    public List<Map<String, String>> userTradeInformation(int user_id, String start_date, String end_date, int trade_type) {
        List<Map<String, String>> l= accountService.userTradeInformation(user_id, start_date, end_date, trade_type);
        if(l==null){
            System.out.println("NULL RESPONSE FROM AS");
            Random random=new Random();
            l=new ArrayList<Map<String, String>>();
            for (int i=0;i<5;i++){
                Map<String,String>map=new HashMap<String, String>();

                switch (trade_type){
                    case 0:
                        map.put("UT"+String.valueOf(i+20),"{\"date_time\":\"2018-06-04 12:00:00\",\"institution_id\":\"1\",\"sum\":\"100.00\",\"type\":\"true\",\"user_id\":\""+String.valueOf(user_id)+"\"}");
                        break;
                    case 1:
            map.put("UT"+String.valueOf(i+30),"{\"date_time\":\"2018-06-04 12:00:00\",\"institution_id\":\"1\",\"sum\":\"100.00\",\"type\":\"true\",\"user_id\":\""+String.valueOf(user_id)+"\"}");
            break;
            case 2:
                if (i%2==0)
                    map.put("UT"+String.valueOf(i),"{\"collection_institution_id\":\"1\",\"collection_user_id\":\""+String.valueOf(user_id)+"\",\"date_time\":\"2018-06-04 12:0"+i+":00\",\"payment_institution_id\":\"1\",\"payment_user_id\":\"3\",\"sum\":\"23.33\",\"type\":\"true\"}");
                else map.put("UT"+String.valueOf(i+10),"{\"collection_institution_id\":\"1\",\"collection_user_id\":\"3\",\"date_time\":\"2018-06-04 12:0"+(9-i)+":00\",\"payment_institution_id\":\"1\",\"payment_user_id\":\""+String.valueOf(user_id)+"\",\"sum\":\"23.33\",\"type\":\"true\"}");
                break;
        }
        l.add(map);
    }

        }
        return l;
    }

    public boolean transferConsume(int pay_user_id, int get_user_id, double amount, boolean trade_type) throws UserNotExistException, UserFrozenException {
        return accountService.transferConsume(pay_user_id, get_user_id, amount, trade_type);
    }

    public boolean reCharge(int user_id, double amount, boolean recharge_platform) throws UserNotExistException {
        return accountService.reCharge(user_id, amount, recharge_platform);
    }

    public boolean drawMoney(int user_id, double amount, boolean draw_platform) throws UserNotExistException {
        return accountService.drawMoney(user_id, amount, draw_platform);
    }

    public int getID(String name, boolean type) {
        return accountService.getID(name, type);
    }

    public void CSSystemReady() {
        accountService.CSSystemReady();
    }

    public void BlockChainServiceReady() {
        accountService.BlockChainServiceReady();
    }

    public void CSSystemClosing() {
        accountService.CSSystemClosing();
    }

    public void BlockChainServiceClosing() {
        accountService.BlockChainServiceClosing();
    }
}
