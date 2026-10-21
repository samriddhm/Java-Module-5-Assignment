/** Q18: methods using @Schedule once and several times. */
public class BackupJobs {

    @Schedule(day = "Monday")
    @Schedule(day = "Wednesday", hour = 14)
    @Schedule(day = "Friday", hour = 18)
    public void fullBackup() { }

    @Schedule(day = "Sunday", hour = 2)       // used only once
    public void cleanTempFiles() { }

    // Before Java 8 you had to write the container yourself, like this:
    @Schedules({@Schedule(day = "Tuesday"), @Schedule(day = "Thursday")})
    public void oldStyleReport() { }
}
