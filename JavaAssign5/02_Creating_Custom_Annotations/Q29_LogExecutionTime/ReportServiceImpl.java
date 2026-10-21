/** Q29: the real business code - it contains NO timing code at all, just annotations. */
public class ReportServiceImpl implements ReportService {

    @Override
    @LogExecutionTime("Build report")
    public String buildReport(int rows) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows; i++) sb.append("row").append(i).append('\n');
        pause(120);
        return "report with " + rows + " rows";
    }

    @Override
    public int countUsers() {          // NOT annotated -> not timed
        return 42;
    }

    @Override
    @LogExecutionTime(value = "Export to CSV", warnAboveMs = 200)
    public String slowExport() {
        pause(300);
        return "export.csv";
    }

    private static void pause(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
