package com.yuyuyzl.WalletApp.Servlet;

import buaa.jj.accountservice.Encrypt;
import buaa.jj.accountservice.exceptions.AgencyNotExistException;
import buaa.jj.accountservice.exceptions.NameDuplicateException;
import buaa.jj.accountservice.exceptions.UserAgencyDuplicateException;
import buaa.jj.accountservice.exceptions.UserNotExistException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.yuyuyzl.WalletApp.Dubbo.DubboHandler;
import com.yuyuyzl.WalletApp.Login.LoginHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.*;

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
                case 5://drawMoney
                {
                    int uid = Integer.valueOf(request.getParameter("uid"));
                    String moneyString = request.getParameter("Money");
                    String way = request.getParameter("Way");
                    if (moneyString.length() > 20) {// 长度过长,可能会传输不过去
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
                        System.out.println("change - " + moneyString + " money : " + money);
                        if (action == 4) {
                            boolean okay=DubboHandler.INSTANCE.accountService.reCharge(uid, money, rechargePlatform);
                            if (!okay){
                                out.print("-5");
                                return;
                            }
                        } else {
                            if (((BigDecimal) DubboHandler.INSTANCE.accountService.userInformation(LoginHandler.getUID(request.getSession().getId())).get("availableBalance")).doubleValue() < money) {
                                out.print("-6");
                                return;
                            }
                            boolean okay=DubboHandler.INSTANCE.accountService.drawMoney(uid, money, rechargePlatform);
                            if (!okay){
                                out.print("-5");
                                return;
                            }
                        }
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
                case 6://userTradeInfo
                    // http://localhost:8080/Account?Action=6&uid=9&tradetype=2&startdate=2018-05-01%2000:00:00&enddate=2018-07-01%2000:00:00
                {
                    Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
                    int userID=Integer.valueOf(request.getParameter("uid"));
                    String startDate=request.getParameter("startdate");
                    String endDate=request.getParameter("enddate");
                    int tradeType=Integer.valueOf(request.getParameter("tradetype"));
                    List<Map<String, String>> l = DubboHandler.INSTANCE.accountService.userTradeInformation(userID, startDate, endDate, tradeType);
                    List<Map<String,String>> res=new ArrayList<Map<String, String>>();
                    for (Map<String,String> map:l) {

                        Map.Entry<String,String> entry=map.entrySet().iterator().next();


                        String jsonString = entry.getValue();
                        Type type = new TypeToken<Map<String, String>>() {}.getType();
                        Map<String, String> map2 = gson.fromJson(jsonString, type);
                        map2.put("trade_id",entry.getKey());
                        res.add(map2);
                        //System.out.println(map2.toString());
                    }
                    out.print(gson.toJson(res));

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
