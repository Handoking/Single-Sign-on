package com.handoking.listener;

import com.handoking.db.UserDB;
import com.handoking.pojo.ClientInfoVo;
import com.handoking.utils.HttpUtil;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.List;

/**
 * @ClassName MySessionListener
 * @Description TODO
 * @Author Handoking
 * @Date 2019/12/30 19:40
 * 实现HttpSessionListener方法后，创建和销毁session时会调用监听器
 **/
public class MySessionListener implements HttpSessionListener {
    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {

    }

    /**
     * 自动注销， 主动注销
     */
    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        HttpSession session = httpSessionEvent.getSession();
        String token = (String) session.getAttribute("token");
        UserDB.tokenSet.remove(token);
        List<ClientInfoVo> clientinfo = UserDB.clientInfo.remove(token);
        //获取所有的子系统，并通知注销
        for (ClientInfoVo vo : clientinfo) {
            try {
                //访问的url是客户端的登出地址，在登陆时从客户端携带至参数，认证中心验证时将登出地址和sessionid存入UserDB，
                HttpUtil.sendHttpRequest(vo.getClientUrl(),vo.getJessionId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
