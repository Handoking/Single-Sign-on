package com.handoking.controller;

import com.handoking.db.UserDB;
import com.handoking.pojo.ClientInfoVo;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @ClassName SsoServerController
 * @Description TODO
 * @Author Handoking
 * @Date 2019/12/17 17:12
 **/

@Controller
public class SsoServerController {
    @RequestMapping("/index")
    public String index(){
        return "login";
    }
    @RequestMapping("/login")
    public String login(String userName,String passwd,HttpSession session,Model model,String redirectUrl){
        System.out.println("====================================");
        System.out.println("userName:"+userName+"\tpasswd:"+passwd);

        if("handoking".equals(userName)&&"123456".equals(passwd)){
            //用户存在，验证成功
            System.out.println("登陆校验成功");
            //生成令牌
            String token = UUID.randomUUID().toString();
            System.out.println("成功生成token："+token);
            //存到服务器本地
            session.setAttribute("token",token);
            UserDB.tokenSet.add(token);
            //返回给用户 model和？token=传参作用一样,重定向到来的地方
            model.addAttribute("token",token);
            return "redirect:"+redirectUrl;
        }
        //用户名或者密码错误，重新登陆，但需要带上从哪里来url,登陆成功后重定向到此url
        System.out.println("用户名或密码错误");
        model.addAttribute("redirectUrl",redirectUrl);
        return "login";

//        if (UserDB.userMap.containsKey(userName)) {
//            if (UserDB.userMap.get(userName).equals(passwd)){
//                System.out.println("登陆校验成功");
//                //生成令牌
//                String token = UUID.randomUUID().toString();
//                System.out.println("成功生成token"+token);
//                //存到服务器本地
//                session.setAttribute("token",token);
//                UserDB.tokenSet.add(token);
//                //返回给用户 model和？token=传参作用一样,重定向到来的地方
//                model.addAttribute("token",token);
//                return "redirectUrl:"+redirectUrl;
//            }else{
//                //密码错误，重新登陆，但需要带上从哪里来url,登陆成功后重定向到此url
//                System.out.println("密码错误");
//                model.addAttribute("redirectUrl",redirectUrl);
//                return "login";
//            }
//        } else{
//            //用户名不存在
//            System.out.println("用户不存在，直接注册成功，请再次登陆");
//            UserDB.userMap.put(userName,passwd);
//            model.addAttribute("redirectUrl",redirectUrl);
//            return "login";
//        }
    }
    @RequestMapping("/checkLogin")
    public String checkLogin(HttpSession session, String redirectUrl, Model model){
        //1.判断有没有全局会话 token是服务器生成
        String token = (String) session.getAttribute("token");

        if(StringUtils.isEmpty(token)){
            //如果全局会话不存在，返回登陆界面
            model.addAttribute("redirectUrl", redirectUrl);
            return "login";
        }else{
            //如果全局会话存在，那么拿到token,重定向到来的url
            System.out.println(System.currentTimeMillis()+" 会话存在");
            model.addAttribute("token",token);
            return "redirect:"+redirectUrl;
        }
    }
    /**
     *@params  [token, clientUrl, jessionId]
     *@return  java.lang.String
     *@author  Handoking
     *@date  2019/12/30 16:12
     * 验证token的同时，将退出登录的clientUrl和当前session id存到认证中心
     */
    @RequestMapping("/verify")
    @ResponseBody
    public String verify(String token,String clientUrl,String jessionId){
        if (UserDB.tokenSet.contains(token)){
            System.out.println(System.currentTimeMillis()+" 服务器存在当前token,验证成功");
            //将clientUrl和sessionId等用户信息存入认证中心
            List<ClientInfoVo> clientList = UserDB.clientInfo.computeIfAbsent(token, k -> new ArrayList<>());
      //      以上写法等同于下面的写法
//            List<ClientInfoVo> clientList = UserDB.clientInfo.get(token);
//            if (clientList == null){
//                //第一次判断一定为空
//                clientList = new ArrayList<>();
//                UserDB.clientInfo.put(token,clientList);
//            }

            ClientInfoVo vo = new ClientInfoVo();
            vo.setClientUrl(clientUrl);
            vo.setJessionId(jessionId);
            clientList.add(vo);
            System.out.println("客户端的登出地址和jession已经添加到list");
            return "true";
        }
        return "false";
    }
    /**
     *@params  [session]
     *@return  java.lang.String
     *@author  Handoking
     *@date  2019/12/30 19:32
     * 服务器端注销，使用监听器
     */
    @RequestMapping("/logOut")
    public String logOut(HttpSession session){
        //手动销毁全局会话，前提是session还没有自动注销的。
        session.invalidate();
        //通知子系统注销，调用所有的子系统，并销毁子系统的session
        //使用监听器，监听session是否过期
        //销毁session时调用监听器
        return "login";
    }


}
