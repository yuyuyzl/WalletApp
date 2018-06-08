package com.yuyuyzl.WalletApp.Servlet;

import buaa.jj.accountservice.Encrypt;
import buaa.jj.accountservice.exceptions.*;
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


        if (request.getParameter("Action") != null) {
            int action = Integer.valueOf(request.getParameter("Action"));
            switch (action) {
                case 1: //Login
                {
                    String username = request.getParameter("Username");
                    String password = request.getParameter("Password");
                    System.out.println(username + " - " + password + " @ " + request.getSession().getId());
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
                    String ID = 'Y'+request.getParameter("ID");
                    String email = request.getParameter("email");
                    String realname = request.getParameter("realname");
                    System.out.println(username + " - " + password + " @ " + request.getSession().getId());
                    int uid = -1;
                    try {
                        uid = DubboHandler.INSTANCE.accountService.userRegister(
                                username,
                                Encrypt.SHA256(password),
                                realname,
                                mobile,
                                email,
                                ID,
                                agencyID
                        );
                    } catch (NameDuplicateException e) {
                        out.print(-2);
                        return;
                    } catch (UserAgencyDuplicateException e) {
                        out.print(-3);
                        return;
                    } catch (AgencyNotExistException e) {
                        out.print(-4);
                        return;
                    }
                    out.print(uid);
                    return;
                }

                case 4: //change-password
                {
                    try {
                        int uid = LoginHandler.getUID(request.getSession().getId());
                        if (uid <= 0) {
                            out.print(-1);
                            return;
                        }
                        String initialPassword = request.getParameter("InitialPassword");
                        String password = request.getParameter("Password");
                        System.out.println(uid + " change : " + initialPassword + " to " + password);
//                        System.out.println(uid + " from   : " + initialPassword + " @ " + Encrypt.SHA256(initialPassword));
                        boolean okay = DubboHandler.INSTANCE.accountService.userPasswdChanging(
                                uid,
                                Encrypt.SHA256(initialPassword),
                                Encrypt.SHA256(password)
                        );
                        System.out.println("change : okay");
                        if (!okay) out.print(-4);
                        else out.print(uid);
                        return;
                    } catch (UserNotExistException e) {
                        out.print(-2);
                        return;
                    } catch (UserFrozenException e) {
                        out.print(-3);
                        return;
                    } catch (Exception e) {
                        out.print(-5);
                        return;
                    }
                }

                case 5: //forget-password
                {
                    String username = request.getParameter("Username");
                    String userIdentity = 'Y'+request.getParameter("userIdentity");
                    String password = request.getParameter("Password");
                    try {
                        boolean okay = DubboHandler.INSTANCE.accountService.foundPasswd(
                                username,
                                userIdentity,
                                Encrypt.SHA256(password)
                        );
                        System.out.println("forget - " + username + " - " + userIdentity + " to " + password + okay);
                        if (!okay) {
                            out.print(-3);
                            return;
                        } else {
                            //这里是为了返回一个true的值
                            out.print(Integer.MAX_VALUE);
                            return;
                        }
                    } catch (UserNotExistException e) {
                        out.print(-1);
                        return;
                    } catch (UserFrozenException e) {
                        out.print(-2);
                        return;
                    }
                }

                case 6: //check-password
                {
                    String username = request.getParameter("Username");
                    String password = request.getParameter("Password");
                    System.out.println("check : " + username + " " + password);
                    try {
                        int uid = DubboHandler.INSTANCE.accountService.userLogin(username, Encrypt.SHA256(password));
                        out.print(uid);
                        return;
                    } catch (Exception e) {
                        out.print(Integer.MAX_VALUE);//假装OK, 下一层check
                        return;
                    }
                }
            }
        }
        out.print(LoginHandler.getUID(request.getSession().getId()));
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
