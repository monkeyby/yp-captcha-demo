package yp.sdk;


import yp.sdk.utils.HttpClient4Utils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import com.alibaba.fastjson.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Random;
/**
 * 二次验证
 * Created by 
 */
public class YPCaptchaVerifier {
    public static final String VERIFY_APIURL = "https://captcha.yunpian.com/v1/api/authenticate"; // verify接口地址
    public static final String REQ_TOKEN = "token"; // 二次验证带过来的token
    public static final String REQ_VATHENTICATE = "authenticate"; // 二次验证带过来的authenticate
    
    private static final String VERSION = "1.0";
    private String captchaId = ""; // 验证码id
    private YPSecretPair secretPair = null; // 密钥对

    public YPCaptchaVerifier(String captchaId, YPSecretPair secretPair) {
        Validate.notBlank(captchaId, "captchaId为空");
        Validate.notNull(secretPair, "secret为null");
        Validate.notBlank(secretPair.secretId, "secretId为空");
        Validate.notBlank(secretPair.secretKey, "secretKey为空");
        this.captchaId = captchaId;
        this.secretPair = secretPair;
    }

    /**
     * 二次验证
     *
     * @param validate 验证码组件提交上来的NECaptchaValidate值
     * @param user     用户
     * @return
     * @throws UnsupportedEncodingException 
     */
    public boolean verify(String token,String authenticate , String user) throws UnsupportedEncodingException {
        if (StringUtils.isEmpty(token) || StringUtils.equals(token, "null") || StringUtils.isEmpty(authenticate) || StringUtils.equals(authenticate, "null")) {
            return false;
        }
        user = (user == null) ? "" : user; // bugfix:如果user为null会出现签名错误的问题
        Map<String, String> params = new HashMap<String, String>();
        params.put("captchaId", captchaId);
        params.put("token", token);
        params.put("authenticate", authenticate);
//        params.put("user", user);
        // 公共参数
        params.put("secretId", secretPair.secretId);
        params.put("version", VERSION);
        params.put("timestamp", String.valueOf(System.currentTimeMillis()));
        params.put("nonce", String.valueOf(ThreadLocalRandom.current().nextInt(1,99999)));
        // 计算请求参数签名信息
        String signature = genSignature(secretPair.secretKey, params);
        params.put("signature", signature);

        String resp = HttpClient4Utils.sendPost(VERIFY_APIURL, params);
        
        System.out.println("resp = " + resp);
        return verifyRet(resp);
    }

    /**
     * 生成签名信息
     *
     * @param secretKey 验证码私钥
     * @param params    接口请求参数名和参数值map，不包括signature参数名
     * @return
     */
    
    public static String genSignature(String secretKey, Map<String, String> params) throws UnsupportedEncodingException{
        String[] keys = params.keySet().toArray(new String[0]);
        Arrays.sort(keys);

        StringBuilder sb = new StringBuilder();
        for (String key : keys) {
            sb.append(key).append(params.get(key));
        }
        sb.append(secretKey);
        return DigestUtils.md5Hex(sb.toString().getBytes("UTF-8"));
    }    

    /**
     * 验证返回结果
     *
     * @param resp
     * @return
     */
    private boolean verifyRet(String resp) {
        if (StringUtils.isEmpty(resp)) {
            return false;
        }
        try {
            JSONObject j = JSONObject.parseObject(resp);
             if(j.getIntValue("code") == 0) {
            	 return true;
             }else {
            	 return false;
             }
        } catch (Exception e) {
            return false;
        }
    }
}
