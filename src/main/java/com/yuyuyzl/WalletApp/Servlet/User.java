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
public class User extends HttpServlet {

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
                case 1: //Login
                {
                    String username = request.getParameter("Username");
                    String password = request.getParameter("Password");
                    System.out.print(username + " - " + password + " @ " + request.getSession().getId());
                    int uid = -1;
                    try {
                        uid = DubboHandler.INSTANCE.accountService.userLogin(username, Encrypt.SHA256(password));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (uid != -1) {
                        LoginHandler.login(request.getSession().getId(), uid);
                    }
                }
                    break;

                case 2: //logout
                    LoginHandler.logout(request.getSession().getId());
                    break;

                case 3: //register
                {
                    String username = request.getParameter("Username");
                    String password = request.getParameter("Password");
                    int agencyID = Integer.valueOf(request.getParameter("agencyID"));
                    String mobile = request.getParameter("mobile");
                    String ID = request.getParameter("ID");
                    String email=request.getParameter("email");
                    String realname = request.getParameter("realname");
                    System.out.print(username + " - " + password + " @ " + request.getSession().getId());
                    int uid=-1;
                    try{
                        uid=DubboHandler.INSTANCE.accountService.userRegister(
                                username,
                                Encrypt.SHA256(password),
                                realname,
                                mobile,
                                email,
                                ID,
                                agencyID
                        );
                    }
                    catch (NameDuplicateException e){
                        out.print(-2);
                        return;
                    }
                    catch(UserAgencyDuplicateException e){
                        out.print(-3);
                        return;
                    }
                    catch (AgencyNotExistException e){
                        out.print(-4);
                        return;
                    }
                    out.print(uid);
                    return;
                }
            }
        }
        out.print(LoginHandler.getUID(request.getSession().getId()));
    }
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request,response);
    }
}
