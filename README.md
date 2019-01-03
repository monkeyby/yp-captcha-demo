# yp-captcha-demo
云片验证码java演示

# demo运行步骤
* 修改index.html
```
  new YpRiddler({
    expired: 10,
    mode: 'dialog',
    container: document.getElementById('cbox'),
    appId: '',  // <-- 这里填入在云片官网申请的验证码id
    version: 'v1',
    onError: function (param) {
    },
    onSuccess: function (validInfo, close) {
      // 成功回调
      console.log(`验证通过！token=${validInfo.token}, authenticate=${validInfo.authenticate}`)  // <-- 这里获取到二次验证数据
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
```

* 修改 LoginServlet.java
```
    private static final String captchaId = "YOUR_CAPTCHA_ID"; // 验证码id
    private static final String secretId = "YOUR_SECRET_ID"; // 密钥对id
    private static final String secretKey = "YOUR_SECRET_KEY"; // 密钥对key
```

* `mvn tomcat7:run`
* 浏览器访问 http://localhost:8181/ 查看演示