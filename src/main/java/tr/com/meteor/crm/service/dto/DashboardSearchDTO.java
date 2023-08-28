package tr.com.meteor.crm.service.dto;

import tr.com.meteor.crm.domain.Campaign;
import tr.com.meteor.crm.domain.Contact;
import tr.com.meteor.crm.domain.Customer;
import tr.com.meteor.crm.domain.Task;

import java.util.ArrayList;
import java.util.List;

public class DashboardSearchDTO {
    private List<Customer> customerList = new ArrayList<>();
    private List<Contact> contactList = new ArrayList<>();
    private List<Campaign> campaignList = new ArrayList<>();

    private List<Task> taskList = new ArrayList<>();

    public List<Customer> getCustomerList() {
        return customerList;
    }

    public void setCustomerList(List<Customer> customerList) {
        this.customerList = customerList;
    }

    public List<Contact> getContactList() {
        return contactList;
    }

    public void setContactList(List<Contact> contactList) {
        this.contactList = contactList;
    }

    public List<Campaign> getCampaignList() {
        return campaignList;
    }

    public void setCampaignList(List<Campaign> campaignList) {
        this.campaignList = campaignList;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }
}
