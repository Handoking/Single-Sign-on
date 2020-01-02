package com.handoking.tbController;

import com.handoking.util.SSOClientUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

/**
 * @ClassName TBController
 * @Description TODO
 * @Author Handoking
 * @Date 2019/12/17 20:01
 **/
@Controller
public class TBController {
    @RequestMapping("/taobao")
    public String index(Model model){
        model.addAttribute("serverLogoutUrl", SSOClientUtil.getServerLogOutUrl());
        return "taobao";
    }
    @RequestMapping("/logOut")
    public void logOut(HttpSession session){
        session.invalidate();
    }
}
