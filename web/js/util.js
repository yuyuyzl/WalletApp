var UserInfoCache={};
var getUserInfoUncached=function (uid) {
    uid=uid.toString();
    var res= JSON.parse($.ajax({
        type: 'POST',
        url: 'Account',
        data: {
            Action: "3",
            uid: uid,
        },
        async: false
    }).responseText);
    var cacheUI={
        data:res,
        cacheTime:new Date(),
    }
    UserInfoCache[uid]=cacheUI;
    return res;
};

var getUserInfo=function (uid) {
    uid=uid.toString();
    if(UserInfoCache.hasOwnProperty(uid))return UserInfoCache[uid].data;
    return getUserInfoUncached(uid);
}
