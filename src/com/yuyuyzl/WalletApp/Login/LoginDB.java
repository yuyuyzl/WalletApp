package com.yuyuyzl.WalletApp.Login;

import java.util.HashMap;

public class LoginDB {
    private static HashMap<String,Integer> loginMap=new HashMap<String, Integer>();
    public static int getElement(String key){
        if(loginMap.containsKey(key))return loginMap.get(key);
        return -1;
    }
    public static void insertElement(String key,int value){
        loginMap.put(key,value);
    }
    public static void removeElement(String key){
        loginMap.remove(key);
    }
}
