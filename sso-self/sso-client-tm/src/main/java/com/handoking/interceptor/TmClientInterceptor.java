package com.handoking.interceptor;


import com.handoking.utils.HttpUtil;
import com.handoking.utils.SSOClientUtil;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName TmClientInterceptor
 * @Description TODO
 * @Author Handoking
 * @Date 2019/12/28 15:48
 **/
public class TmClientInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        //1.判断本地是否存在会话
        Boolean isLogin = (Boolean) session.getAttribute("isLogin");
        if (isLogin!=null&&isLogin){
            System.out.println(System.currentTimeMillis()+":再次登陆天猫，直接验证成功");
            return true;
        }
        //2.判断本地是否有token
        String token = request.getParameter("token");
        System.out.println("令牌："+token);
        if (!StringUtils.isEmpty(token)){
            String httpUrl = SSOClientUtil.SERVER_URL_PREFIX+"/verify";
            Map<String, String> params = new HashMap<>(10);
            params.put("token",token);
            params.put("clientUrl",SSOClientUtil.getClientLogOutUrl());
            params.put("jessionId",session.getId());
            String verigfyBool = HttpUtil.sendHttpRequest(httpUrl, params);
            if ("true".equals(verigfyBool)) {
                //服务器验证成功
                System.out.println(System.currentTimeMillis()+":访问天猫-服务器验证成功");
                session.setAttribute("isLogin",true);
                return true;
            }
        }
        //3.不存在会话、token,那么携带来时的url即redirectUrl，跳转到服务器判断是否有其他账号互通的客户端登陆
        System.out.println(System.currentTimeMillis()+" 本地不存在会话，也不存在token");
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
