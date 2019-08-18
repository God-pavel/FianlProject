package springBoot.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import springBoot.entity.Check;
import springBoot.exception.CheckCantBeDeleted;
import springBoot.service.CheckService;
import springBoot.service.ReportService;

@Log4j2
@Controller
@RequestMapping("/main")
public class MainController {

    private final CheckService checkService;
    private final ReportService reportService;

    public MainController(CheckService checkService, ReportService reportService) {
        this.checkService = checkService;
        this.reportService = reportService;
    }

    @GetMapping
    public String checkPage(@ModelAttribute("message") String message,
                            Model model,
                            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Check> page = checkService.getAllChecks(pageable);
        model.addAttribute("page", page);
        model.addAttribute("url", "/main");
        model.addAttribute("message", message);
        return "main";
    }

    @PostMapping("/createCheck")
    public String createCheck(RedirectAttributes ra) {
        if (reportService.getTodayZReport()) {
            ra.addFlashAttribute("message", "You can't create new check because today shift was closed");
            return "redirect:/main";
        }
        return "redirect:/createCheck";
    }

    @PostMapping("/deleteCheck")
    public String deleteCheck(Long checkId, RedirectAttributes ra) {
        try {
            checkService.deleteCheck(checkId);
        } catch (CheckCantBeDeleted e) {
            log.warn(e.getMessage());
            ra.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/main";
    }

    @PostMapping("/deleteProduct")
    public String deleteProduct(Long checkId, String productName, RedirectAttributes ra) {
        try {
            checkService.deleteProductFromCheck(checkId, productName);
        } catch (CheckCantBeDeleted e) {
            log.warn(e.getMessage());
            ra.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/main";
    }
}