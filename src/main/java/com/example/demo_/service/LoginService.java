package com.example.demo_.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    private UserService userService;

    public String login(String email, String password) {
        String pageStr;
        if(email.equals("admin@mail.com") & password.equals("admin")){ pageStr="redirect:/profile/admin"; }
        else { if(!userService.findByEmailAndPassword(email, password).isEmpty()) { pageStr=("redirect:/profile/" +email); }
        else { pageStr="redirect:/login"; } }
        return pageStr;
    }
}
