package com.yuyuyzl.WalletApp.Servlet;

import buaa.jj.accountservice.Encrypt;
import buaa.jj.accountservice.exceptions.AgencyNotExistException;
import buaa.jj.accountservice.exceptions.NameDuplicateException;
import buaa.jj.accountservice.exceptions.UserAgencyDuplicateException;
import buaa.jj.accountservice.exceptions.UserNotExistException;
import com.google.gson.Gson;
import com.yuyuyzl.WalletApp.Dubbo.DubboHandler;
import com.yuyuyzl.WalletApp.Login.LoginHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
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

        if (LoginHandler.getUID(request.getSession().getId()) < 0) return;

        if (request.getParameter("Action") != null) {
            int action = Integer.valueOf(request.getParameter("Action"));
            switch (action) {
                case 1://findUser
                {
                    String username = request.getParameter("Username");

                    out.print(DubboHandler.INSTANCE.accountService.getID(username, true));
                    break;
                }
                case 2://transfer
                {
                    int uid = Integer.valueOf(request.getParameter("uid"));
                    String amount = request.getParameter("amount");
                    //TODO 检查余额是否足够
                    try {
                        double double_amount = Double.valueOf(amount);
                        if (((BigDecimal) DubboHandler.INSTANCE.accountService.userInformation(LoginHandler.getUID(request.getSession().getId())).get("availableBalance")).doubleValue() < double_amount) {
                            out.print(-2);
                            return;
                        }
                        if (DubboHandler.INSTANCE.accountService.transferConsume(LoginHandler.getUID(request.getSession().getId()), uid, Double.valueOf(amount), false)) {
                            out.print(1);
                        } else out.print(-1);
                    } catch (NumberFormatException e) {
                        out.print("-1");
                    }
                    break;
                }
                case 3://userinfo
                {
                    int uid = Integer.valueOf(request.getParameter("uid"));
                    System.out.println("query_info(" + uid + ")");
                    Map m = DubboHandler.INSTANCE.accountService.userInformation(uid);
                    Gson g = new Gson();
//                    System.out.println("information : "+g.toJson(m));
                    out.print(g.toJson(m));
                    break;
                }
                case 4://recharge
                {
                    int uid = Integer.valueOf(request.getParameter("uid"));
                    String moneyString = request.getParameter("Money");
                    String way = request.getParameter("Way");
                    if (moneyString.length() > 12) {
                        out.print("-4");
                        return;
                    }
                    boolean rechargePlatform;
                    if (way.equals("wechat")) rechargePlatform = false;
                    else rechargePlatform = true;
                    System.out.println("update(" + uid + ")  " + moneyString + way);
                    try {
                        double money = Double.valueOf(moneyString);
                        if (money < 0) {
                            out.print("-3");
                            return;
                        }
                        System.out.println("charge - " + moneyString + "to" + money);
                        DubboHandler.INSTANCE.accountService.reCharge(uid, money, rechargePlatform);
                    } catch (NumberFormatException e) {
                        out.print("-1");
                        return;
                    } catch (UserNotExistException e) {
                        out.print("-2");
                        return;
                    } catch (Exception e) {
                        out.print("-5");
                        return;
                    }
                    out.print(uid);
                    break;
                }

            }
        }
        //out.print(-1);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
