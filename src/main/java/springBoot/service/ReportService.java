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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service class realizing reports logic
 *
 * @author Pavlo Pankratov
 * @version 1.0
 */
@Log4j2
@Service
public class ReportService {

    /**
     * Report repository field
     */
    private final ReportRepository reportRepository;
    /**
     * Check service field
     */
    private final CheckService checkService;

    /**
     * Constructor - creating new object
     *
     * @param checkService     - check serviced
     * @param reportRepository - report repository
     */
    public ReportService(ReportRepository reportRepository, CheckService checkService) {
        this.reportRepository = reportRepository;
        this.checkService = checkService;
    }

    /**
     * Method to get all reports
     *
     * @return returns list of reports
     */
    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    /**
     * Method to get check total value
     *
     * @param check certain date
     * @return returns double total
     */
    private Double getCheckTotal(Check check) {
        return check.getTotal().doubleValue();
    }

    /**
     * Method to get all today checks
     *
     * @return returns set of checks
     */
    private Set<Check> getTodayChecks() {
        return checkService.getAllChecks().stream()
                .filter(check -> check.getTime().
                        getDayOfYear() == LocalDate.now().getDayOfYear())
                .collect(Collectors.toSet());
    }

    /**
     * Method to calculate report total value
     *
     * @param checks set of report checks
     * @return returns BigDecimal total
     */
    private BigDecimal calcTotalSum(Set<Check> checks) {
        List<Double> totals = checks.stream()
                .map(this::getCheckTotal)
                .collect(Collectors.toList());

        return new BigDecimal(totals.stream().reduce(0.0, (x, y) -> x + y))
                .setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Method to check if today was already created z-report
     *
     * @return returns boolean value
     */
    public boolean getTodayZReport() {
        Set<Report> todayReports = reportRepository.findByDate(LocalDate.now());
        Set<Report> todayZReports = todayReports.stream()
                .filter(report -> report.getReportType() == ReportType.ZReport)
                .collect(Collectors.toSet());
        return !todayZReports.isEmpty();
    }

    /**
     * Method to create x-report
     *
     * @param user user that create report
     */
    public void createXReport(User user) {
        Set<Check> todayChecks = getTodayChecks();
        BigDecimal totalSum = calcTotalSum(todayChecks);

        Report report = Report.builder()
                .checks(new HashSet<>())
                .reportType(ReportType.XReport)
                .user(user)
                .total(totalSum)
                .date(LocalDate.now())
                .build();
        finishReport(report, todayChecks);
        log.info("X-report was saved. Report id: " + report.getId());
    }

    /**
     * Method to create z-report
     *
     * @param user user that create report
     */
    public void createZReport(User user) {
        if (getTodayZReport()) {
            throw new ZReportAlreadyCreatedException("ZReport was already created today!");
        }
        checkService.deleteUncompletedChecks();
        Set<Check> todayChecks = getTodayChecks();
        BigDecimal totalSum = calcTotalSum(todayChecks);

        Report report = Report.builder()
                .checks(new HashSet<>())
                .reportType(ReportType.ZReport)
                .user(user)
                .total(totalSum)
                .date(LocalDate.now())
                .build();
        finishReport(report, todayChecks);

        log.info("Z-report was saved. Report id: " + report.getId());
    }


    /**
     * Method to calculate report total value
     *
     * @param report report needs to be finished
     * @param checks report checks
     */
    private void finishReport(Report report, Set<Check> checks) {
        checks.forEach(check -> {
            check.setToDelete(false);
            checkService.saveCheck(check);
            report.getChecks().add(check);
        });
        reportRepository.save(report);
    }


}