package springBoot.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.MessageSource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import springBoot.entity.TemporaryCheck;
import springBoot.entity.User;
import springBoot.exception.NotEnoughProductsException;
import springBoot.service.CheckService;

@Log4j2
@Controller
public class CheckFormController {

    private final CheckService checkService;
    private final MessageSource messageSource;

    public CheckFormController(CheckService checkService, MessageSource messageSource) {
        this.checkService = checkService;
        this.messageSource = messageSource;
    }

    @GetMapping("/createCheck")
    public String showCheck(@AuthenticationPrincipal User user, Model model) {
        TemporaryCheck check = checkService.createCheck(user);
        model.addAttribute("check", check);
        model.addAttribute("message", "");

        return "check_form";
    }


    @GetMapping("/createCheck/{checkId}")
    public String userEditPage(@PathVariable Long checkId,
                               @ModelAttribute("message") String message, Model model) {
        TemporaryCheck check = checkService.getTemporaryCheckById(checkId);
        model.addAttribute("check", check);
        model.addAttribute("message", message);
        return "check_form";
    }


    @PostMapping("/createCheck/addByName/{checkId}")
    public String addByName(@PathVariable Long checkId, String name,
                            RedirectAttributes ra,
                            Long amount) {
        try {
            checkService.addProductToCheckByName(checkId, name, amount);
        } catch (NotEnoughProductsException | IllegalArgumentException e) {
            ra.addFlashAttribute("message", e.getMessage());
            log.warn(e.getMessage());
        }

        return "redirect:/createCheck/" + checkId;
    }

    @PostMapping("/createCheck/addById/{checkId}")
    public String addById(@PathVariable Long checkId, Long id,
                          RedirectAttributes ra,
                          Long amount) {
        try {
            checkService.addProductToCheckById(checkId, id, amount);
        } catch (NotEnoughProductsException | IllegalArgumentException e) {
            ra.addFlashAttribute("message", e.getMessage());
            log.warn(e.getMessage());
        }

        return "redirect:/createCheck/" + checkId;

    }

    @PostMapping("/createCheck/closeCheck/{checkId}")
    public String closeCheck(@PathVariable Long checkId,
                             RedirectAttributes ra) {
        try {
            checkService.closeCheck(checkId);
            log.info("Check was closed");
        } catch (Exception e) {
            log.warn(e.getMessage());
            ra.addFlashAttribute(e.getMessage());
        }
        return "redirect:/main";
    }
}
