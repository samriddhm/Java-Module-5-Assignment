/** Q16: three services using ServiceConfig in different ways. */
public class Services {
}

/** Only the required element - everything else uses defaults. */
@ServiceConfig(name = "EmailService")
class EmailService { }

/** Overrides some defaults. */
@ServiceConfig(name = "PaymentService", timeoutSeconds = 5, environment = ServiceConfig.Env.PROD,
               tags = {"critical", "finance"})
class PaymentService { }

/** Overrides every default. */
@ServiceConfig(name = "ReportService", timeoutSeconds = 120, cacheEnabled = false,
               environment = ServiceConfig.Env.TEST, tags = "batch", fallback = EmailService.class)
class ReportService { }
