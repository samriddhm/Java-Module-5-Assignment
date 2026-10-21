/** Q9 - STEP 4: apply the annotation to code. */
@Review(reviewer = "Alice", status = Review.Status.APPROVED, comment = "Clean design")
public class ReportGenerator {

    @Review(reviewer = "Bob")   // status and comment use their default values
    public void generatePdf() {
        System.out.println("    (generating PDF...)");
    }

    @Review(reviewer = "Carol", status = Review.Status.REJECTED, comment = "Needs unit tests")
    public void generateCsv() {
        System.out.println("    (generating CSV...)");
    }

    public void helper() {
        // not annotated
    }
}
