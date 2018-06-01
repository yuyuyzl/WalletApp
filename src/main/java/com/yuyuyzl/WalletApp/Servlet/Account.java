package com.yuyuyzl.WalletApp.Servlet;

import buaa.jj.accountservice.Encrypt;
import buaa.jj.accountservice.exceptions.AgencyNotExistException;
import buaa.jj.accountservice.exceptions.NameDuplicateException;
import buaa.jj.accountservice.exceptions.UserAgencyDuplicateException;
import com.google.gson.Gson;
import com.yuyuyzl.WalletApp.Dubbo.DubboHandler;
import com.yuyuyzl.WalletApp.Login.LoginHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;

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

        if(LoginHandler.getUID(request.getSession().getId())<0)return;

        if(request.getParameter("Action")!=null) {
            int action = Integer.valueOf(request.getParameter("Action"));
            switch (action) {
                case 1://findUser
                {
                    String username = request.getParameter("Username");

                    out.print(DubboHandler.INSTANCE.accountService.getID(username, true));
                }
                    break;
                case 2://transfer
                {
                    int uid = Integer.valueOf(request.getParameter("uid"));
                    String amount = request.getParameter("amount");
                    //TODO 检查余额是否足够
                    if (DubboHandler.INSTANCE.accountService.transferConsume(LoginHandler.getUID(request.getSession().getId()), uid, Double.valueOf(amount), false)) {
                        out.print(1);
                    } else out.print(-1);
                }
                    break;
                case 3://userinfo
                {
                    int uid = Integer.valueOf(request.getParameter("uid"));
                    System.out.println("query_info("+uid+")");
                    Map m=DubboHandler.INSTANCE.accountService.userInformation(uid);
                    Gson g=new Gson();
//                    System.out.println("information : "+g.toJson(m));
                    out.print(g.toJson(m));
                }

            }
        }
        //out.print(-1);
    }
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request,response);
    }
}
