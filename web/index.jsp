<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.yuyuyzl.WalletApp.Login.LoginHandler" %>
<%@ page import="com.yuyuyzl.WalletApp.Dubbo.DubboHandler" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <!--
  Customize this policy to fit your own app's needs. For more guidance, see:
      https://github.com/apache/cordova-plugin-whitelist/blob/master/README.md#content-security-policy
  Some notes:
      * gap: is required only on iOS (when using UIWebView) and is needed for JS->native communication
      * https://ssl.gstatic.com is required only on Android and is needed for TalkBack to function properly
      * Disables use of inline scripts in order to mitigate risk of XSS vulnerabilities. To change this:
          * Enable inline JS: add 'unsafe-inline' to default-src
  -->
  <meta http-equiv="Content-Security-Policy"
        content="default-src * 'self' 'unsafe-inline' 'unsafe-eval' data: gap: content:">
  <meta name="viewport"
        content="width=device-width, initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no, minimal-ui, viewport-fit=cover">
  <meta name="apple-mobile-web-app-capable" content="yes">
  <meta name="apple-mobile-web-app-status-bar-style" content="default">
  <meta name="theme-color" content="#2196f3">
  <meta name="format-detection" content="telephone=no">
  <meta name="msapplication-tap-highlight" content="no">
  <title>My App</title>


  <link rel="stylesheet" href="css/framework7.min.css">
  <link rel="stylesheet" href="css/icons.css">
  <link rel="stylesheet" href="css/app.css">
</head>
<body class="framework7-root">
<div id="app">
  <!-- Status bar overlay for fullscreen mode-->
  <div class="statusbar"></div>


  <!-- Views/Tabs container -->
  <div class="views tabs ios-edges">
    <!-- Tabbar for switching views-tabs -->
    <div class="toolbar tabbar-labels toolbar-bottom-md">
      <div class="toolbar-inner">
        <a href="#view-home" class="tab-link tab-link-active">
          <i class="icon f7-icons ios-only">home</i>
          <i class="icon f7-icons ios-only icon-ios-fill">home_fill</i>
          <i class="icon material-icons md-only">home</i>
          <span class="tabbar-label">Home</span>
        </a>
        <a href="#view-settings" class="tab-link">
          <i class="icon f7-icons ios-only">settings</i>
          <i class="icon f7-icons ios-only icon-ios-fill">settings_fill</i>
          <i class="icon material-icons md-only">settings</i>
          <span class="tabbar-label">Settings</span>
        </a>
      </div>
    </div>
    <!-- Your main view/tab, should have "view-main" class. It also has "tab-active" class -->
    <div id="view-home" class="view view-main tab tab-active">
      <!-- Page, data-name contains page name which can be used in page callbacks -->
      <div class="page" data-name="home">
        <!-- Top Navbar -->
        <div class="navbar">
          <div class="navbar-inner">
            <div class="title sliding">Wallet App</div>
          </div>
        </div>

        <!-- Scrollable page content-->
        <div class="page-content">

          <div class="block block-strong">
            <p>您好,
              <%
                if (LoginHandler.getUID(request.getSession().getId()) > 0)
                  out.print(DubboHandler.INSTANCE.accountService.userInformation(LoginHandler.getUID(request.getSession().getId())).get("userRealName"));
              %>
            </p>
            <p>没有新的通知。</p>
          </div>
          <div>
            <div class="row no-gap">
              <!-- Each "cell" has col-[widht in percents] class -->

              <a class="col button home-fw transfer-button" href="/transfer/">
                <div class="home-braket"></div>
                <div class="home-button">
                  <i class="f7-icons size-50">forward_fill</i>
                  <p>转账</p>
                </div>
              </a>

              <a class="col button home-fw draw-money-button" href="/drawmoney/">
                <div class="home-braket"></div>
                <div class="home-button">
                  <i class="f7-icons size-50">money_yen</i>
                  <p>提现</p>
                </div>
              </a>
              <a class="col button home-fw recharge-button" href="/recharge/"
                 style="border-right-style: none!important;">
                <div class="home-braket" style="margin-top: calc(100% - 1px)!important;"></div>
                <div class="home-button">
                  <i class="f7-icons size-50">login_fill</i>
                  <p>充值</p>
                </div>
              </a>
            </div>

          </div>


        </div>
      </div>
    </div>


    <div id="view-settings" class="view tab">
    </div>
    <div id="view-transfer" class="view tab">
    </div>
    <div id="view-transfertoaccount" class="view tab">
    </div>
    <div id="view-transfertoaccount2" class="view tab">
    </div>
    <div id="view-passwdchanging" class="view tab">
    </div>
    <div id="view-recharge" class="view tab">
    </div>
    <div id="view-drawmoney" class="view tab">
    </div>
  </div>


  <!-- Login Screen -->
  <div class="login-screen" id="my-login-screen">
    <div class="view">
      <div class="page">
        <div class="page-content login-screen-content">
          <div class="login-screen-title">WalletApp</div>
          <div class="list">
            <ul>
              <li class="item-content item-input input-login">
                <div class="item-inner">
                  <div class="item-title item-label">&nbsp;</div>
                  <div class="item-input-wrap">
                    <input type="text" name="username" placeholder="用户名">
                  </div>
                </div>
              </li>
              <li class="item-content item-input input-login">
                <div class="item-inner">
                  <div class="item-title item-label">&nbsp;</div>
                  <div class="item-input-wrap">
                    <input type="password" name="password" placeholder="密码">
                  </div>
                </div>
              </li>
              <li class="item-content item-input">
                <div class="item-inner">
                  <div class="item-title item-label">&nbsp;</div>
                </div>
              </li>
            </ul>
          </div>
          <div class="list">
            <ul>
              <li>
                <a href="#" class="item-link button button-big button-fill login-button">登录</a>
              </li>
            </ul>
            <div class="block-footer"><a href="#" class="link register-button">现在注册</a>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;<a href="#" class="link foundPasswd-button">找回密码</a></div>
          </div>
        </div>
      </div>
    </div>
  </div>

  <div class="login-screen" id="register-screen">
    <div class="view">
      <div class="page">
        <div class="navbar ">
          <div class="navbar-inner sliding">
            <div class="left">
              <a href="#" class="link back">
                <i class="icon icon-back"></i>
                <span class="ios-only">Back</span>
              </a>
            </div>
          </div>
        </div>
        <div class="page-content login-screen-content">

          <div class="login-screen-title">用户注册</div>
          <div class="list">
            <ul>
              <li class="item-content item-input input-login">
                <div class="item-inner">
                  <div class="item-title item-label">机构编号</div>
                  <div class="item-input-wrap">
                    <input type="text" name="agencyID" placeholder="">
                  </div>
                </div>
              </li>
              <li class="item-content item-input input-login">
                <div class="item-inner">
                  <div class="item-title item-label">用户名</div>
                  <div class="item-input-wrap">
                    <input type="text" name="username" placeholder="">
                  </div>
                </div>
              </li>
              <li class="item-content item-input input-login">
                <div class="item-inner">
                  <div class="item-title item-label">密码</div>
                  <div class="item-input-wrap">
                    <input type="password" name="password" placeholder="">
                  </div>
                </div>
              </li>
              <li class="item-content item-input input-login">
                <div class="item-inner">
                  <div class="item-title item-label">重复密码</div>
                  <div class="item-input-wrap">
                    <input type="password" name="repeatPassword" placeholder="">
                  </div>
                </div>
              </li>
              <li class="item-content item-input input-login">
                <div class="item-inner">
                  <div class="item-title item-label">手机号码</div>
                  <div class="item-input-wrap">
                    <input type="text" name="mobile" placeholder="">
                  </div>
                </div>
              </li>

              <li class="item-content item-input input-login">
                <div class="item-inner">
                  <div class="item-title item-label">电子邮件</div>
                  <div class="item-input-wrap">
                    <input type="text" name="email" placeholder="">
                  </div>
                </div>
              </li>
              <li class="item-content item-input input-login">
                <div class="item-inner">
                  <div class="item-title item-label">真实姓名</div>
                  <div class="item-input-wrap">
                    <input type="text" name="realname" placeholder="">
                  </div>
                </div>
              </li>
              <li class="item-content item-input input-login">
                <div class="item-inner">
                  <div class="item-title item-label">证件号码</div>
                  <div class="item-input-wrap">
                    <input type="text" name="ID" placeholder="">
                  </div>
                </div>
              </li>
              <li class="item-content item-input">
                <div class="item-inner">
                  <div class="item-title item-label">&nbsp;</div>
                </div>
              </li>
            </ul>
          </div>
          <div class="list">
            <ul>
              <li>
                <a href="#" class="item-link button button-big button-fill login-button">注册</a>
              </li>
            </ul>
            <div class="block-footer">单击注册按钮表明您同意我们并不存在的<br>最终用户许可协议。</div>
          </div>
        </div>
      </div>
    </div>
  </div>

  <div class="login-screen" id="foundPasswd-screen">
    <div class="view">
      <div class="page">
        <div class="navbar ">
          <div class="navbar-inner sliding">
            <div class="left">
              <a href="#" class="link back">
                <i class="icon icon-back"></i>
                <span class="ios-only">Back</span>
              </a>
            </div>
          </div>
        </div>
        <div class="page-content login-screen-content">

          <div class="login-screen-title">找回密码</div>
          <div class="list">
            <ul>
              <li class="item-content item-input input-login">
                <div class="item-inner">
                  <div class="item-title item-label">用户名</div>
                  <div class="item-input-wrap">
                    <input type="text" name="username" placeholder="">
                  </div>
                </div>
              </li>

              <li class="item-content item-input input-login">
                <div class="item-inner">
                  <div class="item-title item-label">证件号码</div>
                  <div class="item-input-wrap">
                    <input type="text" name="ID" placeholder="">
                  </div>
                </div>
              </li>
              <li class="item-content item-input">
                <div class="item-inner">
                  <div class="item-title item-label">&nbsp;</div>
                </div>
              </li>
            </ul>
          </div>
          <div class="list">
            <ul>
              <li>
                <a href="#" class="item-link button button-big button-fill login-button">确认</a>
              </li>
            </ul>
            <!--<div class="block-footer">您的真实(?)信息我们保证<br/>不会泄露给他人</div>-->
          </div>
        </div>
      </div>
    </div>
  </div>

  <div class="login-screen" id="foundPasswd-screen2">
    <div class="view">
      <div class="page">
        <div class="navbar ">
          <div class="navbar-inner sliding">
            <div class="left">
              <a href="#" class="link back">
                <i class="icon icon-back"></i>
                <span class="ios-only">Back</span>
              </a>
            </div>
          </div>
        </div>
        <div class="page-content login-screen-content">
          <div class="login-screen-title">找回密码</div>
          <div class="list">
            <ul>
              <li class="item-content item-input input-login">
                <div class="item-inner">
                  <div class="item-title item-label">密码</div>
                  <div class="item-input-wrap">
                    <input type="password" name="password" placeholder="">
                  </div>
                </div>
              </li>
              <li class="item-content item-input input-login">
                <div class="item-inner">
                  <div class="item-title item-label">重复密码</div>
                  <div class="item-input-wrap">
                    <input type="password" name="repeatPassword" placeholder="">
                  </div>
                </div>
              </li>
              <li class="item-content item-input">
                <div class="item-inner">
                  <div class="item-title item-label">&nbsp;</div>
                </div>
              </li>
            </ul>
          </div>
          <div class="list">
            <ul>
              <li>
                <a href="#" class="item-link button button-big button-fill login-button">确认</a>
              </li>
            </ul>
            <!--<div class="block-footer">您的真实(?)信息我们保证<br/>不会泄露给他人</div>-->
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
<!-- Cordova -->
<!--
<script src="cordova.js"></script>
-->

<!-- Framework7 library -->
<script src="javascript/framework7.min.js"></script>
<script src="javascript/jquery.min.js"></script>
<!-- App routes -->
<script src="js/routes.js"></script>
<script src="js/util.js"></script>
<!-- Your custom app scripts -->
<script src="js/app.js"></script>

</body>
</html>
