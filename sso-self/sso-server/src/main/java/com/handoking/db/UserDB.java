package com.handoking.db;

import com.handoking.pojo.ClientInfoVo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * @ClassName UserDB
 * @Description TODO
 * @Author Handoking
 * @Date 2019/12/27 10:00
 **/
public class UserDB {

       public  static HashSet<String> tokenSet = new HashSet<>();
       /**
        * 用户登出地址和sessionid。map中key为token,值为多个客户端的登出地址和sessionid组成的列表，比如天猫，淘宝等
        */
       public static Map<String,List<ClientInfoVo>> clientInfo=
              new HashMap<>();
}
