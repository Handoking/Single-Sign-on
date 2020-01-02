package com.handoking.interceptor;


import com.handoking.util.HttpUtil;
import com.handoking.util.SSOClientUtil;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName SsoClientInterceptor
 * @Description TODO
 * @Author Handoking
 * @Date 2019/12/21 9:55
 **/
public class SsoClientInterceptor implements HandlerInterceptor {
    //false:被拦截
    //true：放行
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断是否存在会话 isLogin
        HttpSession session = request.getSession();
        Boolean isLogin = (Boolean) session.getAttribute("isLogin");
        if (isLogin!=null && isLogin){
            System.out.println(System.currentTimeMillis()+"：再次访问淘宝，验证直接通过");
            //登陆可以放行
            return true;
        }
        //判断token
        String token = request.getParameter("token");
        System.out.println(token);
        if(!StringUtils.isEmpty(token)){
            //如果token存在，去服务器中验证https://www.sso.com:8003/verify 验证token信息
            String serverUrl = SSOClientUtil.SERVER_URL_PREFIX +"/verify";
            Map<String,String> params = new HashMap<>(10);
            params.put("token",token);
            //将登出地址和当前sessionId发送给认证中心
            params.put("clientUrl",SSOClientUtil.getClientLogOutUrl());
            params.put("jessionId",session.getId());
            try{
                String isVerify = HttpUtil.sendHttpRequest(serverUrl,params);
                if("true".equals(isVerify)){
                    System.out.println(System.currentTimeMillis()+"服务器令牌验证通过");
                    session.setAttribute("isLogin",true);
                    return true;
                }
            }catch (Exception e){
                System.out.println("HTTP通信失败");
                e.printStackTrace();
            }
        }
        SSOClientUtil.redirectToSSOURL(request,response);
        return false;

    }
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
