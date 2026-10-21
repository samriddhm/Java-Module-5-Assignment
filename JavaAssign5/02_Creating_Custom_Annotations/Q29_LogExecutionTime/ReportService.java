/** Q29: the interface the proxy will implement. (JDK dynamic proxies need an interface.) */
public interface ReportService {
    String buildReport(int rows);
    int countUsers();
    String slowExport();
}
