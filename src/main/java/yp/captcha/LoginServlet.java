package yp.captcha;

import yp.sdk.YPCaptchaVerifier;
import yp.sdk.YPSecretPair;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by captcha_dev on 16-10-9.
 */
public class LoginServlet extends HttpServlet {
//    private static final long serialVersionUID = -3185301474503659058L;
    private static final String captchaId = ""; // 验证码id
    private static final String secretId = ""; // 密钥对id
    private static final String secretKey = ""; // 密钥对key

    private final YPCaptchaVerifier verifier = new YPCaptchaVerifier(captchaId, new YPSecretPair(secretId, secretKey));

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = request.getParameter(YPCaptchaVerifier.REQ_TOKEN); // 从请求体里获得验证码validate数据
        String authenticate = request.getParameter(YPCaptchaVerifier.REQ_VATHENTICATE); // 从请求体里获得验证码validate数据

        boolean isValid = verifier.verify(token, authenticate); // 发起二次校验

        System.out.println("token = " + token + " , authenticate = " + authenticate + ", isValid = " + isValid);
        
        if (isValid) {
            response.sendRedirect("/success.jsp");
        } else {
            response.sendRedirect("/fail.jsp");
        }
    }
}
