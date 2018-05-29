package com.yuyuyzl.WalletApp.Login;

import com.yuyuyzl.WalletApp.Dubbo.DubboHandler;

public class LoginHandler {
    public static int getUID(String sessionID){
        if(LoginDB.getElement(sessionID)!=-1)return LoginDB.getElement(sessionID);
        return -1;
    }
    public static void login(String sessionID,int uid){

        LoginDB.insertElement(sessionID,uid);
    }
    public static void logout(String sessionID){
        LoginDB.removeElement(sessionID);
    }
}
