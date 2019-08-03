package springBoot.controller;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import springBoot.dto.UserDTO;
import springBoot.service.UserService;

import java.util.Map;

@Controller
@RequestMapping("/registration")
public class RegistrationController {

    private final UserService userService;
    private final MessageSource messageSource;

    public RegistrationController(UserService userService, MessageSource messageSource) {
        this.userService = userService;
        this.messageSource = messageSource;
    }

    @GetMapping
    public String registration() {
        return "registration";
    }


    @PostMapping
    public String addUser(UserDTO userdto, Map<String, Object> model) {
        if (!userService.saveNewUser(userdto)) {

            model.put("message", messageSource.getMessage("message.exist.user", null, LocaleContextHolder.getLocale()));
            return "registration";
        }


        return "redirect:/login";
    }
}
