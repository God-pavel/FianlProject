package springBoot.controller;


import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import springBoot.entity.User;
import springBoot.exception.ZReportAlreadyCreatedException;
import springBoot.service.ReportService;

@Log4j2
@Controller
@RequestMapping("/report")
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping
    public String getAllReports(Model model, @ModelAttribute("message") String message) {
        model.addAttribute("reports", reportService.getAllReports());
        model.addAttribute("message", message);
        return "report";
    }

    @PostMapping("/createXReport")
    public String createXReport(@AuthenticationPrincipal User user) {
        reportService.createXReport(user);
        return "redirect:/report";
    }

    @PostMapping("/createZReport")
    public String createZReport(@AuthenticationPrincipal User user,
                                RedirectAttributes ra) {
        try {
            reportService.createZReport(user);
        } catch (ZReportAlreadyCreatedException e) {
            log.warn(e.getMessage());
            ra.addFlashAttribute("message", e.getMessage());
        }

        return "redirect:/report";
    }
}
