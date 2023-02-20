package com.zerotohero.admin.user;

import com.zerotohero.admin.security.ShopUserDetails;
import com.zerotohero.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccountController {
    @Autowired
    private UserService service;

    @GetMapping("/account")
    public String viewDetails(@AuthenticationPrincipal ShopUserDetails loggedUser, Model model){
        String email = loggedUser.getUsername();
        User user = service.getUserBuEmail(email);

        model.addAttribute("user",user);

        return "account_form";
    }

}
