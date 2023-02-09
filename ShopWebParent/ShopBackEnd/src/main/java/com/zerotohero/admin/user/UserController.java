package com.zerotohero.admin.user;

import com.zerotohero.entities.Role;
import com.zerotohero.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping("/users")
    public String listAll(Model model){
        List<User> userList = service.listAll();
        model.addAttribute("userList", userList);
        return "users";
    }

    @GetMapping("/users/new")
    public String newUser(Model model){
        List<Role> roleList = service.listRoles();
        User user = new User();
        model.addAttribute("user", user);
        model.addAttribute("roleList", roleList);
        model.addAttribute("pageTitle", "Create New User");
        return "user_form";
    }

    @PostMapping("/users/save")
    public String saveUser(User user, RedirectAttributes redirectAttributes){
        System.out.println(user);
        service.saveUser(user);

        redirectAttributes.addFlashAttribute("message", "The user has been saved successfully.");
        return "redirect:/users";
    }

    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes, Model model){
        try {
            User user = service.get(id);
            List<Role> roleList = service.listRoles();
            model.addAttribute("user",user);
            model.addAttribute("pageTitle", "Edit User ID: "+id);
            model.addAttribute("roleList",roleList);

            return "user_form";
        }catch (UserNotFoundException e){
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/users";
        }
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes, Model model){
        try {
            service.delete(id);
            redirectAttributes.addFlashAttribute("message", "The User ID: "+id+" has been deleted successfully");
        }catch (UserNotFoundException e){
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/users";
    }
}