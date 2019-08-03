package springBoot.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import springBoot.entity.Check;
import springBoot.entity.Report;
import springBoot.entity.User;
import springBoot.entity.enums.ReportType;
import springBoot.exception.ZReportAlreadyCreatedException;
import springBoot.repository.ReportRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Log4j2
@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final CheckService checkService;

    public ReportService(ReportRepository reportRepository, CheckService checkService) {
        this.reportRepository = reportRepository;
        this.checkService = checkService;
    }

    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    private Double getCheckTotal(Check check) {
        return check.getTotal().doubleValue();
    }

    private Set<Check> getTodayChecks() {
        return checkService.getAllChecks().stream()
                .filter(check -> check.getTime().
                        getDayOfYear() == LocalDate.now().getDayOfYear())
                .collect(Collectors.toSet());
    }

    private BigDecimal calcTotalSum(Set<Check> checks) {
        List<Double> totals = checks.stream()
                .map(this::getCheckTotal)
                .collect(Collectors.toList());

        return new BigDecimal(totals.stream().reduce(0.0, (x, y) -> x + y))
                .setScale(2, RoundingMode.HALF_UP);
    }

    public boolean getTodayZReport() {
        Set<Report> todayReports = reportRepository.findByDate(LocalDate.now());
        Set<Report> todayZReports = todayReports.stream()
                .filter(report -> report.getReportType() == ReportType.ZReport)
                .collect(Collectors.toSet());
        return !todayZReports.isEmpty();
    }

    public void createXReport(User user) {
        log.info("start create method");
        Set<Check> todayChecks = getTodayChecks();
        log.info("today checks: " + todayChecks);
        BigDecimal totalSum = calcTotalSum(todayChecks);
        log.info("total:  " + totalSum);

        Report report = Report.builder()
                .checks(todayChecks)
                .reportType(ReportType.XReport)
                .user(user)
                .total(totalSum)
                .date(LocalDate.now())
                .build();
        log.info("new report:  " + report);
        finishReport(report, todayChecks);
        log.info("Xreport was saved. Report id: " + report.getId());

    }

    public void createZReport(User user) {
        if (getTodayZReport()) {
            throw new ZReportAlreadyCreatedException("ZReport was already created today!");
        }

        Set<Check> todayChecks = getTodayChecks();
        BigDecimal totalSum = calcTotalSum(todayChecks);

        Report report = Report.builder()
                .checks(todayChecks)
                .reportType(ReportType.ZReport)
                .user(user)
                .total(totalSum)
                .date(LocalDate.now())
                .build();
        finishReport(report, todayChecks);

        log.info("Zreport was saved. Report id: " + report.getId());
    }

    private void finishReport(Report report, Set<Check> checks) {
        log.info("in finish method");
        checks.forEach(check -> {
            log.info("check: " + check);
            check.setToDelete(false);
            log.info("check: " + check);
            checkService.saveCheck(check);
            report.getChecks().add(check);
        });
        log.info("report: " + report);
        reportRepository.save(report);
    }


}