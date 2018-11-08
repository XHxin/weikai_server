package cc.modules.util;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;

import java.io.InputStream;

/**
 * @auther xinye
 * @create 2018 04 10
 */
public class AliyunOOSUtil {

    private static final String endpoint = "http://oss-cn-shenzhen.aliyuncs.com";
    private static final String accessKeyId = "LTAIkVx7aZAixeQr";
    private static final String accessKeySecret = "m0xhopLagcRE147CP2R0iU4nVmgHQb";

    public static boolean upload(String key, InputStream in) {
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        try {
            ossClient.putObject("cvc-certmap", key, in);
            return true;
        } catch (OSSException oe) {
            oe.printStackTrace();
        } finally {
            ossClient.shutdown();
        }
        return false;
    }

}
