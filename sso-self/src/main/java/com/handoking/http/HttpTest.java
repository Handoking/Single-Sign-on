package com.handoking.http;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName HttpTest
 * @Description TODO
 * @Author Handoking
 * @Date 2019/12/13 11:03
 **/
//https://way.jd.com/jisuapi/weather?city=嵩县&cityid=111&citycode=101260301&appkey=a553d77138b35588225f0fc2b4d4d95f
//https://way.jd.com/jisuapi/query4?shouji=13456755448&appkey=a553d77138b35588225f0fc2b4d4d95f
public class HttpTest {
    public static void main(String[] args) throws Exception {
        Map<String, String> map = new HashMap<String, String>(6);
//        map.put("city","嵩县");
//        map.put("appkey","a553d77138b35588225f0fc2b4d4d95f");
//        String result = HttpUtil.sendHttpRequest("http://way.jd.com/jisuapi/weather",map);

        map.put("shouji","13282806373");
        map.put("appkey","a553d77138b35588225f0fc2b4d4d95f");
        String result = HttpUtil.sendHttpRequest("http://way.jd.com/jisuapi/query4",map);
        System.out.println(result);
    }
}
