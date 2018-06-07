// Dom7
var $$ = Dom7;
var currentUser = {
  id: -1,
};
var tradeInfo;
resetTradeInfo=function(){
    console.log("reset trade info start")
    tradeInfo=[];
    for (var tradetype=0;tradetype<=2;tradetype++){
        var tradeInfoRes=JSON.parse($.ajax({url: "Account?Action=6&tradetype="+tradetype, async: false}).responseText);
        //console.log(tradeInfoRes);
        tradeInfo=tradeInfo.concat(tradeInfoRes);
    }
    tradeInfo=tradeInfo.sort(function (a,b) {
        return -a['date_time'].localeCompare(b['date_time']);
    });
    console.log(tradeInfo);
    if(homeView!=null)homeView.router.refreshPage();
};

getTradeInfo=function () {
    // Must return an object
    return {
        tradeInfo: function(){
            console.log("GTI CALLED");
            return tradeInfo;
        }
    }
}
// Framework7 App main instance
var app = new Framework7({
  root: '#app', // App root element
  id: 'io.framework7.testapp', // App bundle ID
  name: 'Framework7', // App name
  theme: 'md', // Automatic theme detection
  // App root data
  data: function () {
    return {
      user: {
        firstName: 'John',
        lastName: 'Doe',
      },
      // Demo products for Catalog section
      products: [
        {
          id: '1',
          title: 'Apple iPhone 8',
          description: 'Lorem ipsum dolor sit amet, consectetur adipisicing elit. Nisi tempora similique reiciendis, error nesciunt vero, blanditiis pariatur dolor, minima sed sapiente rerum, dolorem corrupti hic modi praesentium unde saepe perspiciatis.'
        },
        {
          id: '2',
          title: 'Apple iPhone 8 Plus',
          description: 'Velit odit autem modi saepe ratione totam minus, aperiam, labore quia provident temporibus quasi est ut aliquid blanditiis beatae suscipit odio vel! Nostrum porro sunt sint eveniet maiores, dolorem itaque!'
        },
        {
          id: '3',
          title: 'Apple iPhone X',
          description: 'Expedita sequi perferendis quod illum pariatur aliquam, alias laboriosam! Vero blanditiis placeat, mollitia necessitatibus reprehenderit. Labore dolores amet quos, accusamus earum asperiores officiis assumenda optio architecto quia neque, quae eum.'
        },
      ]
    };
  },
  // App root methods
  methods: {
    helloWorld: function () {
      app.dialog.alert('Hello World!');
    },
  },
  // App routes
  routes: routes,

  dialog: {
    title: 'WalletApp',
    buttonOk: '确定',
    buttonCancel: '取消',
  },
  alert: {}
});

// Init/Create views
var homeView = app.views.create('#view-home', {
  url: '/',
  on: {
    pageInit: function (page) {
      console.log("pageInit " + page.name);
      console.log(page);
      currentUser.id = parseInt($.ajax({url: "User", async: false}).responseText);
      console.log(currentUser.id);

      if (currentUser.id <= 0) {
        app.loginScreen.open('#my-login-screen', true);
      }
      if(currentUser.id>=0 && page.name=="home"){
        resetTradeInfo();
        var htiHtml="";
        for(var i=0;i<=2;i++){
            if(tradeInfo.length<=i)break;
            var trade=tradeInfo[i];
            htiHtml+=
                "<li>\n" +
                "    <a href=\"/tradeInfo/"+trade['trade_id']+"/\" class=\"item-link item-content\">\n" +
                "        <div class=\"item-inner\">\n" +
                "            <div class=\"item-title\">\n" ;
            if(trade['trade_type']==2){
                if(parseInt(trade['collection_user_id'])==currentUser.id)
                    htiHtml+="<div class=\"item-header\">收款自</div>"+getUserInfo(trade['payment_user_id'])['userName'];
                else
                    htiHtml+="<div class=\"item-header\">转账给</div>"+getUserInfo(trade['collection_user_id'])['userName'];
            }else {
                if (trade['trade_type'] == 1)
                    htiHtml += "<div class=\"item-header\">提现到</div>";
                else htiHtml += "<div class=\"item-header\">充值自</div>";
                htiHtml += (trade['type'] == 'true') ? '支付宝' : '微信';
            }
            htiHtml+=
                "                <div class=\"item-footer\">"+trade['date_time']+"</div>\n" +
                "            </div>\n" +
                "            <div class=\"item-after\">¥<span>"+trade['sum']+"</span></div>\n" +
                "        </div>\n" +
                "    </a>\n" +
                "</li>";

        }
        $$('#homeTradeInfo').html(htiHtml);
      }
      if (page.name == "transfertoaccount") {
        //TODO 迷之特性研究
        $$("#transfer-next-button").off("click");
        $$("#transfer-next-button").on("click", function () {
          var username = $$('#targetUsername').val();
          console.log(username);
          var id = $.ajax({
            type: 'POST',
            url: 'Account',
            data: {
              Action: "1",
              Username: username,
            },
            async: false
          }).responseText;
          if (parseInt(id) == currentUser.id) {
            app.dialog.create({
              title: '错误',
              text: '不能给自己转账',
              buttons: [
                {
                  text: 'OK',
                }]
            }).open();
            return;
          }
          if (parseInt(id) == -1) {
            app.dialog.create({
              title: '错误',
              text: '找不到这个用户，请检查输入',
              buttons: [
                {
                  text: 'OK',
                }]
            }).open();
            return;
          }
          page.router.navigate({url: "/transfertoaccount2/?" + id});
        });
      }
      if (page.name == "transfertoaccount2") {
        console.log(page.route.url.split('?')[1]);

        var userInfo = getUserInfo(page.route.url.split('?')[1]);
        console.log(userInfo);
        $$("#p-userName")[0].innerHTML = userInfo['userName'] + ' (' + shadeStr(userInfo['userRealName']) + ')';
        $$("#p-mobile")[0].innerHTML = shadeStr(userInfo['userTel']);
        $$("#transfer-2-confirm").off("click");
        $$("#transfer-2-confirm").on("click", function () {
          app.dialog.password('请输入密码', function (password) {
            let username = $$('.userName').text();
            console.log("transfermoney" + username + " " + password);
            $.ajax({
              type: 'POST',
              url: 'User',
              data: {
                Action: "6",
                Username: username,
                Password: password
              },
              success: function (data, textStatus, jqXHR) {
                console.log("transfer-login" + " " + data);
                if (parseInt(data) !== -1) {
                  actualtransfer();
                } else {
                  alert_OK("提现失败", "密码错误");
                }
              }
            });
          });
        });

        function actualtransfer() {
          var amount = $$('#transferAmount').val();
          if (parseFloat(amount) > 0) {
            var res = $.ajax({
              type: 'POST',
              url: 'Account',
              data: {
                Action: "2",
                uid: page.route.url.split('?')[1],
                amount: amount,
              },
              async: false
            }).responseText;
            if (res == '1') {
              app.dialog.create({
                title: '提示',
                text: '转账成功',
                buttons: [
                  {
                    text: 'OK',
                  }]
              }).open();
                setTimeout(function () {
                    resetTradeInfo();
                }, 3000);
              page.router.navigate({url: "/"});
            } else if (res == '-2') {
              app.dialog.create({
                title: '错误',
                text: '余额不足',
                buttons: [
                  {
                    text: 'OK',
                  }]
              }).open();
            } else if (res === '-1') {
              alert_OK("错误", "输入金额有误");
            } else {
              app.dialog.create({
                title: '错误',
                text: '转账失败',
                buttons: [
                  {
                    text: 'OK',
                  }]
              }).open();
            }
          } else {
            app.dialog.create({
              title: '错误',
              text: '输入金额有误',
              buttons: [
                {
                  text: 'OK',
                }]
            }).open();
            return;
          }
        };
      }

      if (page.name === "recharge") {
        $$("#rechargeAmount").on("input", function () {
          this.value = limitstrlength(this.value, 15);
        });
        $$("#submit-recharge").on("click", function () {
          let money = $$("#rechargeAmount").val();
          let way = $$("input[type='radio']:checked").val();
          $.ajax({
            type: 'POST',
            url: 'Account',
            data: {
              Action: "4",
              Money: money,
              uid: currentUser.id,
              Way: way,
            },
            success: function (data, textStatus, jqXHR) {
              console.log("recharge" + data + " " + money + ' way: ' + way);
              switch (parseInt(data)) {
                case 0:
                case -1:
                  alert_OK("输入不合法", "请输入正确的钱数");
                  break;
                case -2:
                  alert_OK("充值失败", "没有此用户");
                  break;
                case -3:
                  alert_OK("输入不合法", "请不要反向充钱");
                  break;
                case -4:
                  alert_OK("输入不合法", "土豪我们做朋友吧");
                  break;
                case -5:
                  alert_OK("充值失败", "出现了未知错误");
                  break;
                default:
                  alert_OK("充值成功", "充值成功");
                    setTimeout(function () {
                        resetTradeInfo();
                    }, 3000);
                  homeView.router.back();
                  break;
              }
            }
          });
        });
      }
      if (page.name === "drawmoney") {
        updateUserInfo();//这里是刷新钱数
        $$("#drawAmount").on("input", function () {
          this.value = limitstrlength(this.value, 15);
        });
        $$("#submit-drawmoney").on("click", function () {
          app.dialog.password('请输入密码', function (password) {
            let username = $$('.userName').text();
            console.log("drawmoney" + username + " " + password);
            $.ajax({
              type: 'POST',
              url: 'User',
              data: {
                Action: "6",
                Username: username,
                Password: password
              },
              success: function (data, textStatus, jqXHR) {
                console.log("draw-login" + " " + data);
                if (parseInt(data) !== -1) {
                  actualdrawmoney();
                } else {
                  alert_OK("提现失败", "密码错误");
                }
              }
            });
          });

          function actualdrawmoney() {
            let money = $$("#drawAmount").val();
            let way = $$("input[type='radio']:checked").val();
            $.ajax({
              type: 'POST',
              url: 'Account',
              data: {
                Action: "5",
                Money: money,
                uid: currentUser.id,
                Way: way,
              },
              success: function (data, textStatus, jqXHR) {
                console.log("draw" + data + " " + money + ' way: ' + way);
                switch (parseInt(data)) {
                  case 0:
                  case -1:
                    alert_OK("输入不合法", "请输入正确的钱数");
                    break;
                  case -2:
                    alert_OK("提现失败", "没有此用户");
                    break;
                  case -3:
                    alert_OK("输入不合法", "请不要反向提现");
                    break;
                  case -4:
                    alert_OK("输入不合法", "超出单次提现限额");
                    break;
                  case -5:
                    alert_OK("提现失败", "出现了未知错误");
                    break;
                  case -6:
                    alert_OK("提现失败", "余额不足");
                    break;
                  default:
                    alert_OK("提现成功", "提现成功");
                      setTimeout(function () {
                          resetTradeInfo();
                      }, 3000);
                    homeView.router.back();
                    break;
                }
              }
            });
          }
        });
      }
    },
    pageBeforeIn(page) {
      if (page.name === 'home') updateUserInfo();
    }
  }
});

function updateUserInfo() {
  //todo: 检测输出是否合法
  console.log("reload user-information");
  $.ajax({
    type: 'POST',
    url: 'Account',
    data: {
      Action: "3",
      uid: currentUser.id,
    },
    success: function (data, textStatus, jqXHR) {
      // console.log('查询账户 : ' + data);
      if (data === '') {
        console.log('not found');
        return;
      }
      //这里有bug, 数字过大精度会损失, 会变成实数(?) 但现在无法复现bug
      let info = JSON.parse(data);
      console.log(info);
      $$('.userRealName').text(info.userRealName);
      $$('.userName').text(info.userName);
      $$('.agency').text(info.agency);
      $$('.amountMoney').text(info.availableBalance.toFixed(2));
    }
  });
}

var settingsView = app.views.create('#view-settings', {
  url: '/account/',
  on: {
    pageInit: function (page) {
      console.log("account.pageInit : " + page.name);
      if (page.name === 'settings') {
        $$('.logout-button').on('click', function () {
          $.ajax({url: "User?Action=2", async: false});
          location.reload();
        });
      }
      if (page.name === "change-password") {
        $$(".submit-password").on('click', function () {
          let initialPassword = $$('#my-password-changing [name="initialPassword"]').val();
          let password = $$('#my-password-changing [name="password"]').val();
          let repeatPassword = $$('#my-password-changing [name="repeatPassword"]').val();
          if (initialPassword === '') {
            alert_OK("输入有误", "初始密码不能为空");
            return;
          } else if (password === '') {
            alert_OK("输入有误", "更改后的密码不能为空");
            return;
          } else if (password === initialPassword) {
            alert_OK("输入有误", "更改的密码和原密码相同");
            return;
          } else if (repeatPassword === '') {
            alert_OK("输入有误", "请重复输入一遍更改后的密码");
            return;
          } else if (repeatPassword !== password) {
            alert_OK("输入有误", "两次新输入的密码不匹配");
            return;
          }
          console.log('submit new password : ' + initialPassword + ' -> ' + password);
          $.ajax({
            type: 'POST',
            url: 'User',
            data: {
              Action: "4",
              InitialPassword: initialPassword,
              Password: password
            },
            success: function (data, textStatus, jqXHR) {
              console.log('修改密码ret : ' + data);
              switch (parseInt(data)) {
                case 0:
                case -1:
                  alert_OK("修改密码失败", "服务器异常:没有uid");
                  break;
                case -2:
                  alert_OK("修改密码失败", "服务器异常:不存在此用户");
                  break;
                case -3:
                  alert_OK("修改密码失败", "服务器异常:用户被冻结");
                  break;
                case -4:
                  alert_OK("修改密码失败", "初始密码错误");
                  break;
                case -5:
                  alert_OK("修改密码失败", "出现了其他的奇怪的问题,请联系管理员");
                  break;
                default:
                  alert_OK("修改密码成功", "修改密码成功");
                  settingsView.router.back();
              }
            }
          });
        });
      }
    },
    pageBeforeIn(page) {
      console.log("setting.pageBeforeIn : " + page.name + "  uid=" + currentUser.id);
      if (page.name === 'settings') updateUserInfo();
    }
  }
});
var transferView = app.views.create('#view-transfer', {
  url: '/transfer/',
  removeElements: false
});
var transfertoaccountView = app.views.create('#view-transfertoaccount', {
  url: '/transfertoaccount/',
  removeElements: false
});
var transfertoaccount2View = app.views.create('#view-transfertoaccount2', {
  url: '/transfertoaccount2/',
  removeElements: false
});

var rechargeView = app.views.create('#view-recharge', {
  url: '/recharge/',
  removeElements: false
});
var rechargeView = app.views.create('#view-drawmoney', {
  url: '/drawmoney/',
  removeElements: false
});
var tradeinfolistView = app.views.create('#view-tradeInfoList', {
  url: '/tradeinfolist/',
  removeElements: false,
});

function alert_OK(title, text) {
  app.dialog.create({
    title: title,
    text: text,
    buttons: [
      {
        text: 'OK',
      }]
  }).open();
}

function shadeStr(str) {
  var l = str.length;
  var sstart = Math.floor(l / 3);
  var send = Math.ceil(l * 2 / 3);
  var s = "";
  for (var i = sstart; i < send; i++) {
    s = s + '*';
  }
  s = str.substring(0, sstart) + s + str.substring(send);
  return s;
}

function limitstrlength(str, limit) {
  if (str.length > limit) return str.substr(0, limit);
  return str;
}

var passwdchangingView = app.views.create('#view-passwdchanging', {
  url: '/passwdchanging/',
});


$$('#my-login-screen .login-button').on('click', function () {
  var username = $$('#my-login-screen [name="username"]').val();
  var password = $$('#my-login-screen [name="password"]').val();

  $.ajax({
    type: 'POST',
    url: 'User',
    data: {
      Action: "1",
      Username: username,
      Password: password
    },
    success: function (data, textStatus, jqXHR) {
      console.log(data);
      if (parseInt(data) != -1) {
        // Close login screen
        //app.loginScreen.close('#my-login-screen');
        location.reload();
      } else {
        $$('#my-login-screen [name="password"]').val('');
        app.dialog.create({
          title: '登录失败',
          text: '请检查您的凭据',
          buttons: [
            {
              text: 'OK',
            }]
        }).open();
      }
    }
  });

});

$$('#my-login-screen .register-button').on('click', function () {
  app.loginScreen.open('#register-screen', true);
});

$$("#register-screen .back").on('click', function () {
  app.loginScreen.close('#register-screen');
});

$$("#register-screen .login-button").on('click', function () {
  var username = $$('#register-screen [name="username"]').val();
  var password = $$('#register-screen [name="password"]').val();
  var agencyID = $$('#register-screen [name="agencyID"]').val();
  var mobile = $$('#register-screen [name="mobile"]').val();
  var email = $$('#register-screen [name="email"]').val();
  var realname = $$('#register-screen [name="realname"]').val();
  var repeatPassword = $$('#register-screen [name="repeatPassword"]').val();
  var ID = $$('#register-screen [name="ID"]').val();
  if (password !== repeatPassword) {
    app.dialog.create({
      title: '注册失败',
      text: '确认密码不匹配',
      buttons: [
        {
          text: 'OK',
        }]
    }).open();
    return;
  }
  $.ajax({
    type: 'POST',
    url: 'User',
    data: {
      Action: "3",
      Username: username,
      Password: password,
      agencyID: parseInt(agencyID),
      mobile: mobile,
      email: email,
      realname: realname,
      ID: ID
    },
    success: function (data, textStatus, jqXHR) {
      console.log(parseInt(data));
      switch (parseInt(data)) {
        case 0:
        case -1:
          app.dialog.create({
            title: '注册失败',
            text: '不知道发生了什么',
            buttons: [
              {
                text: 'OK',
              }]
          }).open();
          break;
        case -2:
          app.dialog.create({
            title: '注册失败',
            text: '重复的用户名',
            buttons: [
              {
                text: 'OK',
              }]
          }).open();
          break;
        case -3:
          app.dialog.create({
            title: '注册失败',
            text: 'UserAgencyDuplicateException',
            buttons: [
              {
                text: 'OK',
              }]
          }).open();
          break;

        case -4:
          app.dialog.create({
            title: '注册失败',
            text: '该机构不存在',
            buttons: [
              {
                text: 'OK',
              }]
          }).open();
          break;
        default:
          app.loginScreen.close('#register-screen');
          app.dialog.create({
            title: '注册成功',
            text: '现在登录吧',
            buttons: [
              {
                text: 'OK',
              }]
          }).open();
          var username = $$('#register-screen [name="username"]').val("");
          var password = $$('#register-screen [name="password"]').val("");
          var agencyID = $$('#register-screen [name="agencyID"]').val("");
          var mobile = $$('#register-screen [name="mobile"]').val("");
          var email = $$('#register-screen [name="email"]').val("");
          var realname = $$('#register-screen [name="realname"]').val("");
          var repeatPassword = $$('#register-screen [name="repeatPassword"]').val("");
          var ID = $$('#register-screen [name="ID"]').val("");
          break;
      }

    }
  });

});


$$('#my-login-screen .foundPasswd-button').on('click', function () {
  app.loginScreen.open('#foundPasswd-screen', true);
});


$$("#foundPasswd-screen .back").on('click', function () {
  app.loginScreen.close('#foundPasswd-screen');
});

$$("#foundPasswd-screen .login-button").on('click', function () {
  let username = $$('#foundPasswd-screen [name="username"]').val();
  let userIdentity = $$('#foundPasswd-screen [name="ID"]').val();
  let password = '123456';
  if (username === '') {
    alert_OK("查询失败", "未输入用户名");
    return;
  } else if (userIdentity === '') {
    alert_OK("查询失败", "未输入证件号");
    return;
  }
  console.log("check-user:" + username + "; " + userIdentity + "; " + password);
  $.ajax({
    type: 'POST',
    url: 'User',
    data: {
      Action: "5",
      userIdentity: userIdentity,
      Username: username,
      Password: password
    },
    success: function (data, textStatus, jqXHR) {
      console.log(data);
      switch (parseInt(data)) {
        case 0:
        case -1:
          alert_OK("查询失败", "不存在此用户名");
          break;
        case -2:
          alert_OK("查询失败", "用户被冻结");
          break;
        case -3:
          alert_OK("查询失败", "证件号码不匹配");
          break;
        default:
          alert_OK("查询成功", "已将密码更新为123456, 请更改密码:");
          app.loginScreen.open("#foundPasswd-screen2");
      }
    }
  });
});

$$("#foundPasswd-screen2 .back").on('click', function () {
  app.loginScreen.close('#foundPasswd-screen');
  app.loginScreen.close('#foundPasswd-screen2');
});

$$("#foundPasswd-screen2 .login-button").on('click', function () {
  let password = $$('#foundPasswd-screen2 [name="password"]').val();
  let repeatPassword = $$('#foundPasswd-screen2 [name="repeatPassword"]').val();
  if (password === '') {
    alert_OK("更改密码失败", "未输入新密码");
    return;
  } else if (repeatPassword === '') {
    alert_OK("更改密码失败", "请重复一遍刚刚输入的密码");
    return;
  } else if (password !== repeatPassword) {
    alert_OK("更改密码失败", "两次输入的密码不同");
    return;
  }
  let username = $$('#foundPasswd-screen [name="username"]').val();
  let userIdentity = $$('#foundPasswd-screen [name="ID"]').val();
  console.log("change-password:" + username + "; " + userIdentity + "; " + password);
  $.ajax({
    type: 'POST',
    url: 'User',
    data: {
      Action: "5",
      userIdentity: userIdentity,
      Username: username,
      Password: password
    },
    success: function (data, textStatus, jqXHR) {
      console.log(data);
      switch (parseInt(data)) {
        case 0:
        case -1:
          alert_OK("更改密码失败", "不存在此用户名");
          break;
        case -2:
          alert_OK("更改密码失败", "用户被冻结");
          break;
        case -3:
          alert_OK("更改密码失败", "密码不匹配");
          break;
        default:
          alert_OK("修改成功", "请使用新的密码登录");
          app.loginScreen.close("#foundPasswd-screen");
          app.loginScreen.close("#foundPasswd-screen2");
          $$('#foundPasswd-screen2 [name="repeatPassword"]').val("");
          $$('#foundPasswd-screen2 [name="password"]').val("");
          $$('#foundPasswd-screen [name="username"]').val("");
          $$('#foundPasswd-screen [name="ID"]').val("");
      }
    }
  });
});

