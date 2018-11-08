package cc.modules.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

public class GetJsonData {  
	
	/**
	 * 发送Post请求，过去Json数据
	 */
    public static String getJsonData(JSONObject jsonParam,String urls) {  
        StringBuffer sb=new StringBuffer();  
        try {  
            ;  
            // 创建url资源  
            URL url = new URL(urls);  
            // 建立http连接  
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
            // 设置允许输出  
            conn.setDoOutput(true);  
            // 设置允许输入  
            conn.setDoInput(true);  
            // 设置不用缓存  
            conn.setUseCaches(false);  
            // 设置传递方式  
            conn.setRequestMethod("POST");  
            // 设置维持长连接  
            conn.setRequestProperty("Connection", "Keep-Alive");  
            // 设置文件字符集:  
            conn.setRequestProperty("Charset", "UTF-8");  
            // 转换为字节数组  
            byte[] data = (jsonParam.toString()).getBytes();  
            // 设置文件长度  
            conn.setRequestProperty("Content-Length", String.valueOf(data.length));  
            // 设置文件类型:  
            conn.setRequestProperty("contentType", "application/json");  
            // 开始连接请求  
            conn.connect();        
            OutputStream out = new DataOutputStream(conn.getOutputStream()) ;  
            // 写入请求的字符串  
            out.write((jsonParam.toString()).getBytes("utf-8"));
            out.flush();  
            out.close();  
  
            //System.out.println(conn.getResponseCode());  
              
            // 请求返回的状态  
            if (HttpURLConnection.HTTP_OK == conn.getResponseCode()){  
                //System.out.println("连接成功");  
                // 请求返回的数据  
                InputStream in1 = conn.getInputStream();  
                try {  
                      String readLine=new String();  
                      BufferedReader responseReader=new BufferedReader(new InputStreamReader(in1,"gbk"));
                      while((readLine=responseReader.readLine())!=null){  
                        sb.append(readLine);  
                      }  
                      responseReader.close();  
                      //System.out.println(sb.toString());  
                      
                } catch (Exception e1) {  
                    e1.printStackTrace();  
                }  
            } else {  
                System.out.println("error++");  
                  
            }  
  
        } catch (Exception e) {  
  
        }
        return sb.toString();
  
    }
    
    /**
     * 发送Post请求，以xml形式提交参数
     */
    public static String getXmlData(String xmlParam,String urls) {  
        StringBuffer sb=new StringBuffer();  
        try {  
            ;  
            // 创建url资源  
            URL url = new URL(urls);  
            // 建立http连接  
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
            // 设置允许输出  
            conn.setDoOutput(true);  
            // 设置允许输入  
            conn.setDoInput(true);  
            // 设置不用缓存  
            conn.setUseCaches(false);  
            // 设置传递方式  
            conn.setRequestMethod("POST");  
            // 设置维持长连接  
            conn.setRequestProperty("Connection", "Keep-Alive");  
            // 设置文件字符集:  
            conn.setRequestProperty("Charset", "UTF-8");  
            // 转换为字节数组  
            byte[] data = (xmlParam).getBytes();  
            // 设置文件长度  
            conn.setRequestProperty("Content-Length", String.valueOf(data.length));  
            // 设置文件类型:  
            conn.setRequestProperty("contentType", "text/xml");  
            // 开始连接请求  
            conn.connect();        
            OutputStream out = new DataOutputStream(conn.getOutputStream()) ;  
            // 写入请求的字符串  
            out.write((xmlParam).getBytes("UTF-8"));  
            out.flush();
            out.close();  
  
            //System.out.println(conn.getResponseCode());  
              
            // 请求返回的状态  
            if (HttpURLConnection.HTTP_OK == conn.getResponseCode()){  
                //System.out.println("连接成功");  
                // 请求返回的数据  
                InputStream in1 = conn.getInputStream();  
                try {  
                      String readLine=new String();  
                      BufferedReader responseReader=new BufferedReader(new InputStreamReader(in1,"UTF-8"));  
                      while((readLine=responseReader.readLine())!=null){  
                        sb.append(readLine);  
                      }  
                      responseReader.close();  
                      //System.out.println(sb.toString());  
                      
                } catch (Exception e1) {  
                    e1.printStackTrace();  
                }  
            } else {  
                System.out.println("error++");  
                  
            }  
  
        } catch (Exception e) {  
  
        }  
          
        return sb.toString();  
  
    }  
    
    public static String sendGet(String url, String param) {  
        String result = "";  
        BufferedReader in = null;  
        try {  
            String urlNameString = url + "?" + param;
            System.out.println(urlNameString);
            URL realUrl = new URL(urlNameString);  
            // 打开和URL之间的连接  
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();

            // 建立实际的连接  
            connection.connect();  

            // 定义 BufferedReader输入流来读取URL的响应  
            in = new BufferedReader(new InputStreamReader(  
            		connection.getInputStream(), "UTF-8"));  
            String line;  
            while ((line = in.readLine()) != null) {  
                result += line;  
            }  
        } catch (Exception e) {  
            System.out.println("发送GET请求出现异常！" + e);  
            e.printStackTrace();  
        }  
        // 使用finally块来关闭输入流  
        finally {  
            try {  
                if (in != null) {  
                    in.close();  
                }  
            } catch (Exception e2) {  
                e2.printStackTrace();  
            }  
        }  
        return result;  
    } 
    
    public static void main(String[] args) {  
//        JSONObject jsonParam = new JSONObject();  
//        jsonParam.put("id", "1401_1406");  
//        jsonParam.put("device_size", "480x720");  
//        String url="www.baidu.com";  
//        String data=GetJsonData.getJsonData(jsonParam,url);  
//        System.out.println(data);
//    	String str="openid=oASC10nhr7c-BmE9_stYRLYHc60g&openid=oASC10r6KArwREuajBX97OMOTxI8&openid=oASC10oh6yQiLDJn5sipn70LqU1s&openid=oASC10u3MOE5eBGTwPXAMIAYLIc8&openid=oASC10gBEKu4Nm7RtwLhkrp9o_DA&openid=oASC10jAE3sXxrPTWBUj3LXTY2lc&openid=oASC10rLaRVM3c9o4VvHdTvtK5q8&openid=oASC10lyB1l3D73GtuVODKV8BcWY&openid=oASC10nv9WL85pgOOdgq0ESpyww8&openid=oASC10kjnJ42s_YeDoYYZJZBGJbw&openid=oASC10tVrSiQ8QmG31uoCoKxPPrs&openid=oASC10hwaFH-muB9IyLvyRLyUzRA&openid=oASC10qBK5wUhJefR80WU7ISwdiQ&openid=oASC10kBHF25VtjumXrSwiZtnr6w&openid=oASC10kiHgKUyfGYvUL7TJOVQrR4&openid=oASC10te-R0kuKg4pZa9sMIdXkDM&openid=oASC10kllz88RVDeglV7EBlHXOXw&openid=oASC10uA49vFkQOqeEFN30OOVqAM&openid=oASC10lWbSC-raI8YiXEYSNWm9vc&openid=oASC10uha1GNp7C0pLb261NrN5V4&openid=oASC10l3Xff_yBSBvjphOY0pVGzQ&openid=oASC10t_sgaKgpxV31SiGjWrQRXQ&openid=oASC10heinNUwS42_pj0M32npKTE&openid=oASC10n9tTI19msNp7BPSU9YlXP0&openid=oASC10r7qhheXdQzrPfzLxAtuevg&openid=oASC10tYrVWLc6yfYcHbelpRu4Zg&openid=oASC10lF8lmIvonStAHDCJXkeV8g&openid=oASC10ksb97cO1dl4_ScNZHwa3ow&openid=oASC10j9aCZbEDH4EZohmVQ-timg&openid=oASC10lM6lgfqhR1kK-rYAZnGm4s&openid=oASC10q0tstqsHo0kDrnTmpW50ts&openid=oASC10p-nSXOqfMGv_NuLbKpQzn4&openid=oASC10sLfxbYWZJ2hKi7GBabbo4A&openid=oASC10ruIY_-Zd-O_rT2UIsMS8WI&openid=oASC10rD_uhYDOl2R1xqIYFIf034&openid=oASC10sfW_MiTARh8WQWeS0R6tDM&openid=oASC10qIh_EMtIsyc7051UgVL0hI&openid=oASC10np0yzCnnR3L6ogHAPqWp8U&openid=oASC10v8g0dSv0LLWcXl2PR1Ljm4&openid=oASC10lPZXjvOEwt_FCeD_B-alA4&openid=oASC10mOBhVBtczouRlYpSzvNv5E&openid=oASC10lV8M9gTGKCAjUwKdfQJsQY&openid=oASC10ktkRj1sr4cxa6OTtABfqUo&openid=oASC10uYPdtvDCsdj-4XNF8IQHJ8&openid=oASC10kYZgPqJ60hviDeN8Pdn13w&openid=oASC10ocVzHB39vV8MI36lJbE5-o&openid=oASC10r4yCYizWYIakOyh7IYNz5k&openid=oASC10t0_vEdb-RALGlR0SUAESdQ&openid=oASC10l6OQ9gKy9sKT5HaNCpvBX8&openid=oASC10rUGr42v_UZxYOWygY0Z8Qs&openid=oASC10vfLJfQQseYJGgb3AGJsPDo&openid=oASC10oXQWPjJX5h6dwviSEgaQPw&openid=oASC10qwwlN1Wgytx0YPLvPsypW4&openid=oASC10r1RuCtLm--LVyYy_TSvAZQ&openid=oASC10top_t0QCv5zLBWkGKVtC4s&openid=oASC10k44oJ0Q6AwfI0I4qVozxi4&openid=oASC10jO8GVz3An0fsouRrQ0afNw&openid=oASC10hwhuOY901Gpv964-7bp4kA";
    	String str="openid=oASC10nyGz5mlgiDGhm-wgQee-RQ";
    	System.out.println(GetJsonData.sendGet("http://mp.certmaptest.com/user", str));
    	
    }  
}  
