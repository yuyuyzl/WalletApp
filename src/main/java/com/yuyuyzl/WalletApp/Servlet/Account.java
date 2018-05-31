package com.yuyuyzl.WalletApp.Servlet;

import buaa.jj.accountservice.Encrypt;
import buaa.jj.accountservice.exceptions.AgencyNotExistException;
import buaa.jj.accountservice.exceptions.NameDuplicateException;
import buaa.jj.accountservice.exceptions.UserAgencyDuplicateException;
import com.yuyuyzl.WalletApp.Dubbo.DubboHandler;
import com.yuyuyzl.WalletApp.Login.LoginHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class Account extends HttpServlet {
        @Override
    public void init() throws ServletException {
        super.init();
        DubboHandler.init();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {


        response.setContentType("text/html");
        response.setCharacterEncoding("utf-8");
        PrintWriter out = response.getWriter();


        if(request.getParameter("Action")!=null) {
            int action = Integer.valueOf(request.getParameter("Action"));
            switch (action) {
                case 1://findUser
                    String username = request.getParameter("Username");

                    out.print(DubboHandler.INSTANCE.accountService.getID(username,true));

                    break;
                case 2://findUser
                    String uid = request.getParameter("uid");
                    String amount = request.getParameter("amount");
                    //TODO 检查余额是否足够
                    if(DubboHandler.INSTANCE.accountService.transferConsume(LoginHandler.getUID(request.getSession().getId()),Integer.valueOf(uid),Double.valueOf(amount),false)){
                        out.print(1);
                    }else out.print(-1);

                    break;

            }
        }
        //out.print(-1);
    }
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request,response);
    }
}
