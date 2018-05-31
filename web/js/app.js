// Dom7
var $$ = Dom7;
var currentUser={
    id:-1,
};
// Framework7 App main instance
var app  = new Framework7({
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

});

// Init/Create views
var homeView = app.views.create('#view-home', {
  url: '/',
    on:{
      pageInit:function (page) {
          console.log("pageInit "+page.name);
          console.log(page);
          currentUser.id = parseInt($.ajax({url: "/User", async: false}).responseText);
          console.log(currentUser.id);

          if (currentUser.id <=0) {
              app.loginScreen.open('#my-login-screen', true);
          }


            if(page.name=="transfertoaccount") {
                //TODO 迷之特性研究
                $$("#transfer-next-button").off("click");
                $$("#transfer-next-button").on("click", function () {
                    var username = $$('#targetUsername').val();
                    console.log(username);
                    var id = $.ajax({
                        type: 'POST',
                        url: '/Account',
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
          if(page.name=="transfertoaccount2") {
              console.log(page.route.url.split('?')[1]);
              $$("#transfer-2-confirm").off("click");
              $$("#transfer-2-confirm").on("click", function () {

      }
    }
});
var settingsView = app.views.create('#view-settings', {
    url: '/account/',
    on: {
        pageInit: function () {
            console.log("account.pageInit");
            $$('.logout-button').on('click', function () {
                $.ajax({url: "/User?Action=2", async: false});
                location.reload();
            });
            $$(".submit-password").on('click', function () {
                var initialPassword = $$('#my-password-changing [name="initialPassword"]').val();
                var password = $$('#my-password-changing [name="password"]').val();
                var repeatPassword = $$('#my-password-changing [name="repeatPassword"]').val();
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
                    url: '/User',
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
                                alert_OK("修改密码失败","出现了其他的奇怪的问题,请联系管理员");
                                break;
                            default:
                                alert_OK("修改密码成功", "修改密码成功");
                                settingsView.router.back();
                        }
                    }
                });
            });
        }
    }
});
var transferView = app.views.create('#view-transfer', {
    url: '/transfer/',
});
var transfertoaccountView = app.views.create('#view-transfertoaccount', {
    url: '/transfertoaccount/',

});
var transfertoaccount2View = app.views.create('#view-transfertoaccount2', {
    url: '/transfertoaccount2/',
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

var passwdchangingView = app.views.create('#view-passwdchanging', {
    url: '/passwdchanging/',
});


// Login Screen Demo
$$('#my-login-screen .login-button').on('click', function () {
  var username = $$('#my-login-screen [name="username"]').val();
  var password = $$('#my-login-screen [name="password"]').val();

    $.ajax({
        type: 'POST',
        url: '/User',
        data: {
            Action:"1",
            Username:username,
            Password:password
        },
        success: function (data, textStatus, jqXHR) {
            console.log(data);
            if(parseInt(data)!=-1) {
                // Close login screen
                app.loginScreen.close('#my-login-screen');
                location.reload();
            }else {
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

$$("#register-screen .back").on('click',function () {
    app.loginScreen.close('#register-screen');
});

$$("#register-screen .login-button").on('click',function () {
    var username = $$('#register-screen [name="username"]').val();
    var password = $$('#register-screen [name="password"]').val();
    var agencyID = $$('#register-screen [name="agencyID"]').val();
    var mobile = $$('#register-screen [name="mobile"]').val();
    var email = $$('#register-screen [name="email"]').val();
    var realname = $$('#register-screen [name="realname"]').val();
    var repeatPassword = $$('#register-screen [name="repeatPassword"]').val();
    var ID = $$('#register-screen [name="ID"]').val();
    if(password!=repeatPassword) {
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
        url: '/User',
        data: {
            Action:"3",
            Username:username,
            Password:password,
            agencyID:parseInt(agencyID),
            mobile:mobile,
            email:email,
            realname:realname,
            ID:ID
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

