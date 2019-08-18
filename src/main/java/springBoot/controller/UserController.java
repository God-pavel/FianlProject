package springBoot.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springBoot.entity.User;
import springBoot.entity.enums.Role;
import springBoot.service.UserService;
import springBoot.validator.Result;
import springBoot.validator.UsernameValidator;

import java.util.Map;


@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping
    public String allUsersPage(Map<String, Object> model) {
        model.put("users", userService.getAllUsers());
        return "all_users";
    }

    @GetMapping("{user}")
    public String userEditPage(@PathVariable User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "user_edit";
    }

    @PostMapping
    public String userEdit(
            @RequestParam("userId") User user,
            @RequestParam Map<String, String> form,
            @RequestParam String username,
            Model model
    ) {

        Result checkUsername = new UsernameValidator().validate(username);


        if (!checkUsername.isValid()) {
            model.addAttribute("user", user);
            model.addAttribute("roles", Role.values());
            return "user_edit";
        }

        userService.userEdit(user, form, username);

        return "redirect:/user";
    }
}

