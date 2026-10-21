/**
 * Q29 demo: calling methods through the timing proxy.
 * (Exact milliseconds will differ slightly on each run.)
 *
 * Run:  javac *.java && java LogExecutionTimeDemo
 */
public class LogExecutionTimeDemo {

    public static void main(String[] args) {
        ReportService service = TimingProxy.wrap(new ReportServiceImpl(), ReportService.class);

        System.out.println("Calling buildReport(1000)...");
        System.out.println("  result: " + service.buildReport(1000));

        System.out.println("Calling countUsers() (not annotated, so no timer line)...");
        System.out.println("  result: " + service.countUsers());

        System.out.println("Calling slowExport()...");
        System.out.println("  result: " + service.slowExport());
    }
}
