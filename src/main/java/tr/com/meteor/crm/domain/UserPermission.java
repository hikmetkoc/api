package tr.com.meteor.crm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Formula;
import tr.com.meteor.crm.utils.idgenerator.IdType;
import tr.com.meteor.crm.utils.metadata.EntityMetadataAnn;
import tr.com.meteor.crm.utils.metadata.FieldMetadataAnn;
import tr.com.meteor.crm.utils.request.SortOrder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Entity
@IdType(idType = IdType.IdTypeEnum.UUID)
@EntityMetadataAnn(
    apiName = "user_permissions", displayField = "instanceName", sortBy = "id", sortOrder = SortOrder.DESC,
    title = "Kullanıcı İzni", pluralTitle = "Kullanıcı İzinleri",
    ownerPath = "owner.id"
)
@Table(indexes = {@Index(columnList = "search")})
public class UserPermission extends IdNameAuditingEntity<UUID> {
    @FieldMetadataAnn(title = "İşlem Yapan", display = true, priority = 1, filterable = true)
    @ManyToOne
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User owner;
    @ManyToOne
    @FieldMetadataAnn(title = "Kullanıcı", priority = 0)
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User user;

    @FieldMetadataAnn(title = "Mavi Yaka İzin Görüntüsü", display = true, priority = 2)
    private Boolean holidayView = false;

    @FieldMetadataAnn(title = "Ödeme Talimatı Oluşturma", display = true, priority = 3)
    private Boolean createPayment = false;

    @FieldMetadataAnn(title = "Fatura Atama", display = true, priority = 4)
    private Boolean sendInvoice = false;

    @FieldMetadataAnn(title = "Ödeme Yapma", display = true, priority = 5)
    private Boolean spendInvoice = false;

    @FieldMetadataAnn(title = "Personel Oluşturma", display = true, priority = 6)
    private Boolean createUser = false;
    @FieldMetadataAnn(title = "Konu", search = true, readOnly = true, active = false, display = false)
    private String subject;
    @Formula("subject")
    @FieldMetadataAnn(readOnly = true, title = "Başlık", active = false)
    private String instanceName;
    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String getInstanceName() {
        return instanceName;
    }

    @PreUpdate
    public void preUpdate() {
        generateSubject();
    }

    @PrePersist
    public void postUpdate() {
        generateSubject();
    }

    private void generateSubject() {
        List<String> parts = new ArrayList<>();
    }

    @Override
    public void updateSearchPre() {
        super.updateSearchPre();

        if (user != null) {
            search += " " + user.getFullName();
        }

        if (StringUtils.isNotBlank(instanceName)) {
            search += " " + user;
        }

        search = search.toLowerCase(new Locale("tr", "TR"));
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Boolean getHolidayView() {
        return holidayView;
    }

    public void setHolidayView(Boolean holidayView) {
        this.holidayView = holidayView;
    }

    public Boolean getCreatePayment() {
        return createPayment;
    }

    public void setCreatePayment(Boolean createPayment) {
        this.createPayment = createPayment;
    }

    public Boolean getSendInvoice() {
        return sendInvoice;
    }

    public void setSendInvoice(Boolean sendInvoice) {
        this.sendInvoice = sendInvoice;
    }

    public Boolean getSpendInvoice() {
        return spendInvoice;
    }

    public void setSpendInvoice(Boolean spendInvoice) {
        this.spendInvoice = spendInvoice;
    }

    public Boolean getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Boolean createUser) {
        this.createUser = createUser;
    }
}
