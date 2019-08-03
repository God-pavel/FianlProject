package springBoot.exception;

public class ZReportAlreadyCreatedException extends RuntimeException {
    public ZReportAlreadyCreatedException(String message) {
        super(message);
    }
}
