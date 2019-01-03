<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
  <meta name="referrer" content="no-referrer">
  <title>验证码示例-dialog模式</title>
  <link href='//cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.css' rel='stylesheet'>
</head>
<body>
<form style="max-width: 320px; margin: 120px auto;">
    <h2 class="form-signin-heading">云片验证码</h2>
    <input type="text" class="form-control" name="username" placeholder="账号" />
    <input type="password" class="form-control" name="password" placeholder="密码" />
    <div id="cbox"></div>
    <button class="btn btn-lg btn-primary btn-block" type="submit" id="submit-btn">登录</button>
</form>
<script src="https://www.yunpian.com/static/official/js/libs/riddler-sdk-0.2.1.js"></script>
<script src="http://code.jquery.com/jquery-2.1.4.min.js"></script>
<script>
    window.onload = function () {
    reqdata = {token:"",authenticate:""}
      // 初始化
      new YpRiddler({
        expired: 10,
        mode: 'dialog',
        container: document.getElementById('cbox'),
        appId: '6a964d3946584546b46f7bf83a775904',
        version: 'v1',
        onError: function (param) {
          if(param.code == 429) {
            alert('请求过于频繁，请稍后再试！')
            return
          }
          // 异常回调
          console.error('验证服务异常')
        },
        onSuccess: function (validInfo, close) {
          // 成功回调
          console.log(`验证通过！token=${validInfo.token}, authenticate=${validInfo.authenticate}`)
          reqdata.token = validInfo.token;
          reqdata.authenticate = validInfo.authenticate;

        close()
        },
        onFail: function (code, msg, retry) {
          // 失败回调
          alert('出错啦：' + msg + ' code: ' + code) 
          retry()
        },
        beforeStart: function (next) {
         console.log('验证马上开始')
         next()
        },
        onExit: function() {
          // 退出验证 （仅限dialog模式有效）
          console.log('退出验证')
        }
      })
    }
  // 监听button的点击事件，进行验证
  document.getElementById('submit-btn').addEventListener('click', function (event) {
        if(reqdata.token == "" || reqdata.authenticate == ""){
          alert('请先通过验证码!')
          return ;
        }
        event.preventDefault();
        var data = {
          username: $('#username').val(),
          password: $('#password').val(),
          token:reqdata.token,
          authenticate:reqdata.authenticate,
        }
        console.log('data',data)
        $.ajax({      
        url: '/login',      
        datatype: "json",
        data: data,
        type: 'post',      
        success: function (data) {      
        //成功后回调      
            console.log("回调函数成功了");
            window.location.href="/success.jsp";       
         },      
        error: function(e){      
        //失败后回调      
            console.log("服务器请求失败");
            window.location.href="/fail.jsp";       
        },      
        beforeSend: function(){      
        //发送请求前调用，可以放一些"正在加载"之类额话      
            // console.log("正在加载");           
        }}) 
  });
</script>
</body>
</html>