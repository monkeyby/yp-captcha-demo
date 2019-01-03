package yp.sdk;

/**
 * 密钥对
 * Created by captcha_dev on 16-11-10.
 */
public class YPSecretPair {
    public final String secretId;
    public final String secretKey;

    /**
     * 构造函数
     * @param secretId 密钥对id
     * @param secretKey 密钥对key
     */
    public YPSecretPair(String secretId, String secretKey) {
        this.secretId = secretId;
        this.secretKey = secretKey;
    }
}
