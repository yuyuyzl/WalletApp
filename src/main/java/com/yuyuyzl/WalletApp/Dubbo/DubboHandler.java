package com.yuyuyzl.WalletApp.Dubbo;

import buaa.jj.accountservice.api.AccountService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DubboHandler {
    public static DubboHandler INSTANCE;
    public static void init(){
        INSTANCE=new DubboHandler();
    }
    public AccountService accountService;
    private DubboHandler(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        context.start();
        accountService = (AccountService) context.getBean(AccountService.class);
    }
}
