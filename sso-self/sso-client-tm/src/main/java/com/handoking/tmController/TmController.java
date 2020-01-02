package com.handoking.tmController;

import com.handoking.utils.SSOClientUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

/**
 * @ClassName TMController
 * @Description TODO
 * @Author Handoking
 * @Date 2019/12/28 15:00
 **/
@Controller
public class TmController {
    @RequestMapping("/tianmao")
    public String index(Model model){
        model.addAttribute("serverLogoutUrl", SSOClientUtil.getServerLogOutUrl());
        return "tmall";
    }
    @RequestMapping("/logOut")
    public void logOut(HttpSession session){
        session.invalidate();
    }
}
