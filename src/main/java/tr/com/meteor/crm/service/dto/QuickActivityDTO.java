package tr.com.meteor.crm.service.dto;

import tr.com.meteor.crm.domain.*;

public class QuickActivityDTO {

    private Customer customer;
    private Address address;
    private boolean customerSite;
    private Contact contact;
    private Activity activity;
    private Quote quote;
    private boolean createContract;
    private Task task;
    private Lead lead;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public boolean getCustomerSite() {
        return customerSite;
    }

    public void setCustomerSite(boolean customerSite) {
        this.customerSite = customerSite;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Quote getQuote() {
        return quote;
    }

    public void setQuote(Quote quote) {
        this.quote = quote;
    }

    public boolean isCreateContract() {
        return createContract;
    }

    public void setCreateContract(boolean createContract) {
        this.createContract = createContract;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Lead getLead() {
        return lead;
    }

    public void setLead(Lead lead) {
        this.lead = lead;
    }
}
