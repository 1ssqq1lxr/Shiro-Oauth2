package com.it.uaa.web.controller;

import com.hjimi.uaa.service.dto.LoginDto;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Auther: lxr
 * @Date: 2018/9/29 13:47
 * @Description:  ShiroController 类封装
 */
@Controller
public class ShiroController {

    private static final Logger LOG = LoggerFactory.getLogger(ShiroController.class);


    @RequestMapping("index")
    public String index() {
        return "index";
    }

    @RequestMapping("unauthorized")
    public String unauthorized() {
        return "unauthorized";
    }

    /*
     * Logout
     */
//    @RequestMapping("logout")
//    public String logout() {
//        final Subject subject = SecurityUtils.getSubject();
//        LOG.debug("{} is logout", subject.getPrincipal());
//        subject.logout();
//        return "redirect:/";
//    }

    /*
     * Go login page
     */
    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String login(Model model) {
        final LoginDto loginDto = new LoginDto();
        //TODO: Just testing
        loginDto.setUsername("test");

        model.addAttribute("formDto", loginDto);
        return "login";
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String login(@ModelAttribute("formDto") LoginDto formDto, BindingResult errors) {

        UsernamePasswordToken token = formDto.token();
        token.setRememberMe(false);

        try {
            SecurityUtils.getSubject().login(token);
        } catch (Exception e) {
            LOG.debug("Error authenticating.", e);
            errors.rejectValue("username", null, "The username or password was not correct.");
            return "login";
        }

        return "redirect:index";
    }


}
