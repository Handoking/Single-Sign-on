package com.handoking.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.util.StreamUtils;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;


/**
 * @ClassName HttpUtil
 * @Description TODO
 * @Author Handoking
 * @Date 2019/12/13 11:54
 **/
public class HttpUtil {
    public static String sendHttpRequest(String url, Map<String,String>params) throws Exception {
        //定义需要访问的地址
        URL u = new URL(url);
        //连接URL
        HttpURLConnection connection = (HttpURLConnection)u.openConnection();
        //请求方式
        connection.setRequestMethod("POST");
        //携带参数
        connection.setDoOutput(true);
        if(params!=null&& params.size()>0){
            StringBuilder param = new StringBuilder();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                param.append("&").append(entry.getKey()).append("=").append(entry.getValue());
            }
            //去除第一个&
            connection.getOutputStream().write( param.substring(1).getBytes("UTF-8"));
        }

        //发起请求
        connection.connect();
        //接受返回值
        String response = StreamUtils.copyToString(connection.getInputStream(), Charset.forName("UTF-8"));
        JSONObject jb = JSON.parseObject(response);
        String result = jb.getString("result");
//        JSONObject jo = JSON.parseObject(result);
//        String city= jo.getString("city");
//        String  date = jo.getString("date");
//        String week = jo.getString("week");
//        String weather = jo.getString("weather");
//        String temp = jo.getString("templow")+"-"+jo.getString("temphigh");
        return result;
    }
}
