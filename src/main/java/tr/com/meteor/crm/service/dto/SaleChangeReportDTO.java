package tr.com.meteor.crm.service.dto;

public class SaleChangeReportDTO {
    private Integer fleetCode;
    private String customerName;
    private double avgVolumeLastYear;
    private double avgVolumeLast3Months;
    private double avgVolumeLastMonth;
    private double diffLastYear;
    private double percentageDiffLastYear;
    private double diffLast3Months;
    private double percentageDiffLast3Months;
    private double score;

    public Integer getFleetCode() {
        return fleetCode;
    }

    public void setFleetCode(Integer fleetCode) {
        this.fleetCode = fleetCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public double getAvgVolumeLastYear() {
        return avgVolumeLastYear;
    }

    public void setAvgVolumeLastYear(double avgVolumeLastYear) {
        this.avgVolumeLastYear = avgVolumeLastYear;
    }

    public double getAvgVolumeLast3Months() {
        return avgVolumeLast3Months;
    }

    public void setAvgVolumeLast3Months(double avgVolumeLast3Months) {
        this.avgVolumeLast3Months = avgVolumeLast3Months;
    }

    public double getAvgVolumeLastMonth() {
        return avgVolumeLastMonth;
    }

    public void setAvgVolumeLastMonth(double avgVolumeLastMonth) {
        this.avgVolumeLastMonth = avgVolumeLastMonth;
    }

    public double getDiffLastYear() {
        return diffLastYear;
    }

    public double getPercentageDiffLastYear() {
        return percentageDiffLastYear;
    }

    public double getDiffLast3Months() {
        return diffLast3Months;
    }

    public double getPercentageDiffLast3Months() {
        return percentageDiffLast3Months;
    }

    public double getScore() {
        return score;
    }

    public void calculate() {
        diffLastYear = avgVolumeLastMonth - avgVolumeLastYear;
        diffLast3Months = avgVolumeLastMonth - avgVolumeLast3Months;

        if(diffLastYear == 0 || avgVolumeLastYear == 0) {
            percentageDiffLastYear = 0;
        } else {
            percentageDiffLastYear = diffLastYear / avgVolumeLastYear;
        }

        if(diffLast3Months == 0 || avgVolumeLast3Months == 0) {
            percentageDiffLast3Months = 0;
        } else {
            percentageDiffLast3Months = diffLast3Months / avgVolumeLast3Months;
        }

        score = percentageDiffLast3Months * Math.abs(percentageDiffLast3Months) * avgVolumeLast3Months * 0.001;
    }
}
