package tr.com.meteor.crm.service.dto;
public class PaymentOrderDTO {

    private String company;

    private String customer;

    private String amount;

    private String okeyFirst;

    private String okeySecond;

    private String status;

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getOkeyFirst() {
        return okeyFirst;
    }

    public void setOkeyFirst(String okeyFirst) {
        this.okeyFirst = okeyFirst;
    }

    public String getOkeySecond() {
        return okeySecond;
    }

    public void setOkeySecond(String okeySecond) {
        this.okeySecond = okeySecond;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
