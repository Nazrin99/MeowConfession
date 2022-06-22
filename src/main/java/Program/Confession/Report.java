package Program.Confession;

public class Report {
    private String reportCategory;
    private String reportContent;
    private String reportAgainstID;

    public Report(String reportCategory, String reportContent, String reportAgainstID) {
        this.reportCategory = reportCategory;
        this.reportContent = reportContent;
        this.reportAgainstID = reportAgainstID;
    }

    public String getReportCategory() {
        return reportCategory;
    }

    public void setReportCategory(String reportCategory) {
        this.reportCategory = reportCategory;
    }

    public String getReportContent() {
        return reportContent;
    }

    public void setReportContent(String reportContent) {
        this.reportContent = reportContent;
    }

    public String getReportAgainstID() {
        return reportAgainstID;
    }

    public void setReportAgainstID(String reportAgainstID) {
        this.reportAgainstID = reportAgainstID;
    }
}
