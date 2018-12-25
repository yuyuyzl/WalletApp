package com.yuyuyzl.WalletApp.Dubbo;

import buaa.jj.accountservice.api.AccountService;

public class DubboHandler {
    public static DubboHandler INSTANCE;
    public static void init(){
        INSTANCE=new DubboHandler();
    }
    public AccountService accountService;
    private DubboHandler(){

        accountService=new AccountServiceTestWrapper(accountService);
    }
}
