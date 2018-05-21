package com.bookStore.utils;

import java.io.FileWriter;
import java.io.IOException;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *修改日期：2017-04-05
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipayConfig {
	
//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

	// 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
	public static String app_id = "2016091400512964";
	
	// 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCnyysLnamUvfxw3r+jSXhzvfQSrIBDE6oE3SxFUyP8SonGC+kLFbJWmA3hWm+GPYgvwY7yu7yfZxwi0gVdiQJHzubMlcVZM+B20GFpXbVfuF9Yl8qpRVVp24waKSuXtsa744qFdSeRkm1JbUIdmJEr1TGySlvtzBgZFcXrPiLP96mfz9nGlg0TI+M1U07jwauQA0543A2TdKoQxe+xDQM+p5l5PXJqLL+B0/VsaK9vmyVUWdOG+8sd99OVumDuJo27SBRE3AoAUpfXuzvQfZhZMACjClFwsBW4KA0FR4CS46GEgXVKXcEjzvLLH5pPTvOVYF5jYO7EJjhXUj4S3egTAgMBAAECggEAUZddqxxTZdyscEYgcco8JCvuD1aKWBOrzQmviVZQXVHxhLgt7hHy0mDF/xr2uBQbsP0yAZ6ArhJ5gAUYTs9cqIZXfIwnlqS/NYdGlvHWTGFfG7lT9tOcIQl9KwtyAygBo0OWDCdXwlWthtL2H2Yc0W+t5sB5yzw/S5Nm1mk0jGNcG/l0Im2Stu5DdTrqip+nbbEQuXPHa4FWicFA+yWc3ZG4987hU9grq8K9IsptvDQ+1Zx/i/7QBHV1Xugi6DUp3Ndms0jnQwNW1QXhmKdM4FNHtopmAEK3buITN9zIo/fc0YZ7m7uPQgBiy3Oa2lwRIuozjMhhMXFaikuH8A77AQKBgQDUdx77JTsp1wWyW7FPordEO8NsKeCRUIYcSkUt4CQQGfm5kmfW5mSQfA5UsrJX6lNdBD5HQ/Hws+RcMog6k0p/XXIptU2NjOoklT3pUA2nWWFPaDWvOCStAN1SZdeOBMkWmQHJMeiE/668a93sAckH9Mqb8v+NgOPPDvmbksOhMwKBgQDKLMqTJuJZag9DGwDi5pods9899mOOQuz+uoTpj5BLxXtXhNTTwckmzBMf1FqI85NVdM7vQh6sysBCr+qTeWrFsuFb71jN6FFEkXcyMCm4MSEtHkep8YafrpJmWw8pA4MQQkMgG739uifSR9AKqwmaiG04eFFUdZc1jVFzhqJdoQKBgC50EREGNeZS/greI2lvATTpADRQ/6ml+nyK/I8oN5pRo5kgWpXQ7+HrTdLU80tnj2yB9f/s6iQfoOlLnx93fs1UKBbYbilpyvPOsaemeiXB1BXhigNSeipdSwat/7Asea74KzzLLbSOlqzZxL7KMSMNMZNhUOCmcqid9JNARoejAoGAbQRoG58tEL/zCOFnwfJNClUyvMu+dTi7aMQU5YiHa4RKdS/oP2NQXz5N1a0BQLXvLXZ2G2fQ+KW/tHX9jJY9L+u8P4G5phZutAWoe4SjCnJbjGO2ivn0+Of+g3j4nMoLA1pnrcmn0khuLqKQmSAPZqfIFVozqNJbvgG8+QmRReECgYBFvluP5FAJ6WMysSdz87K63ONMFG/s63Jd2AmVP/FbTy76gye5bj5rZ1wrSBF19Xk72KOl3Deb49zqtfbasZllyKPZzSySYNn7V9mGT2Z3W6vTQjbq7zQMD2CETGRZGshIlBGGwI/NHGZug9TYqVDNHk7gbwzZDixmeZl+HiR/EA==";
	
	// 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA6E6gKI+96msGXHYEym4RY4Q5mcjmVk02CxMBdgTj8Xk3JKQ0HyUKYYyb6Q0Fc0Bh5s0brwo7u0Kd4VIztv9w8QTUz69tZF+M8gbKnWALd1m/kEOSzMhG9OnfJXEuZsr3bpPp8a3O37s5w9+Wdgfg5V/CZpAPdu4yaF5atvk+rvk8wMc65dnr8JhclXsd167YGVqqSS5Wqr4EDwjiUK0I+7J9gb7Nn3lmqU0Qx9qAq55Wc931+jjFasd+dBANoCEiG83Kd75DzSvtCbEsmNbsPuC3Bp6RmwEq3nEYN0/e+d7tswVG8vKitKS+QjPE7Tj460VinsVSWnyuILXcp43nkQIDAQAB";

	// 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String notify_url = "http://localhost:8080/alipay.trade.page.pay-JAVA-UTF-8/notify_url.jsp";

	// 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String return_url = "http://localhost:8080/bookstore01/client/cart/paysuccess.do";

	// 签名方式
	public static String sign_type = "RSA2";
	
	// 字符编码格式
	public static String charset = "utf-8";
	
	// 支付宝网关
	public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";
	
	// 支付宝网关
	public static String log_path = "C:\\";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /** 
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis()+".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

