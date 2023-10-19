package tr.com.meteor.crm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Formula;
import tr.com.meteor.crm.utils.idgenerator.IdType;
import tr.com.meteor.crm.utils.metadata.EntityMetadataAnn;
import tr.com.meteor.crm.utils.metadata.FieldMetadataAnn;
import tr.com.meteor.crm.utils.validate.AttributeValueValidate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Entity
@IdType(idType = IdType.IdTypeEnum.UUID)
@EntityMetadataAnn(
    apiName = "customers", displayField = "instanceName", title = "Tedarikçi", pluralTitle = "Tedarikçiler",
    masterPath = "owner.id", ownerPath = "owner.id"
    //masterPath = "owner.id", segmentPath = "segment.id", ownerPath = "owner.id"
) //@Index(columnList = "fleetCode")
@Table(indexes = {@Index(columnList = "search")})
public class Customer extends IdNameAuditingEntity<UUID> {

    @FieldMetadataAnn(required = true, title = "Tedarikçi Unvanı", search = true, display = true, priority = 20, filterable = true)
    private String name;

    @FieldMetadataAnn(title = "Açıklama")
    @Column(length = 2048)
    private String description;

    @ManyToOne
    @FieldMetadataAnn(title = "Tedarikçi Sahibi", display = true, priority = 10, filterable = true)
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User owner;

    @FieldMetadataAnn(title = "Mail", type = "Email", search = true)
    private String email;

    @FieldMetadataAnn(title = "Telefon", type = "Phone", search = true, display = true, priority = 30, filterable = true)
    private String phone;

    @FieldMetadataAnn(title = "Web Sitesi", search = true)
    private String website;

    @FieldMetadataAnn(title = "Vergi Dairesi", priority = 45)
    private String taxOffice;

    @FieldMetadataAnn(title = "Vergi Numarası", search = true, display = true, priority = 40, filterable = true)
    private String taxNumber;

    @FieldMetadataAnn(title = "Borç")
    private BigDecimal loan = BigDecimal.ZERO;

    @FieldMetadataAnn(title = "Alacak")
    private BigDecimal receive = BigDecimal.ZERO;

    @FieldMetadataAnn(title = "Bakiye")
    private BigDecimal balance = BigDecimal.ZERO;

    /*@FieldMetadataAnn(title = "Ortalama Tüketim(LT)", active = false)
    private BigDecimal fuelLt;

    @FieldMetadataAnn(title = "Ortalama Tüketim(TL)", active = false)
    private BigDecimal fuelTl;*/

    //@FieldMetadataAnn(title = "Araç Sayısı")
    //private Integer vehicleCount;

    @OneToMany(mappedBy = "customer")
    @FieldMetadataAnn(title = "Adresler")
    @JsonIgnoreProperties("customer")
    private List<Address> addresses;

    /*@ManyToOne
    //@AttributeValueValidate(attributeId = "Müş_Seg")
    //@FieldMetadataAnn(title = "Bölüm")
    private AttributeValue segment;*/

    @ManyToOne
    @AttributeValueValidate(attributeId = "Müş_Sek")
    @FieldMetadataAnn(title = "Sektör")
    private AttributeValue sector;

    @ManyToOne
    @AttributeValueValidate(attributeId = "Müş_Dur")
    @FieldMetadataAnn(title = "Durum", display = true, defaultValue = "Müş_Dur_Yeni", priority = 50, filterable = true)
    private AttributeValue status;

    /*@ManyToOne
    @FieldMetadataAnn(title = "Üst Müşteri")
    @JsonIgnoreProperties("customer")
    private Customer parent;*/

    //@FieldMetadataAnn(title = "Filo Kodu")
    //private Integer fleetCode;

    @FieldMetadataAnn(title = "Ticari Unvan", search = true)
    private String commercialTitle;

    /*@ManyToOne
    @AttributeValueValidate(attributeId = "Müş_Kay")
    @FieldMetadataAnn(title = "Kaynak")
    private AttributeValue source;*/

    /*@FieldMetadataAnn(title = "Eski Filo Kodu", active = false)
    private String oldFleetCode;*/

    @Formula("name")
    @FieldMetadataAnn(readOnly = true, title = "Başlık", active = false)
    private String instanceName;

    /*@FieldMetadataAnn(readOnly = true, title = "Aktif Olma Zamanı")
    private Instant activationTime;*/

    /*@OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private List<SummaryOpetSale> summaryOpetSales;*/

    //@FieldMetadataAnn(title = "DBS Limiti")
    //private BigDecimal dbsLimit;

    /*@ManyToOne
    //@AttributeValueValidate(attributeId = "Müş_Rnk")
    //@FieldMetadataAnn(title = "Renk")
    private AttributeValue color;*/

    @Override
    public String getInstanceName() {
        return instanceName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getTaxOffice() {
        return taxOffice;
    }

    public void setTaxOffice(String taxOffice) {
        this.taxOffice = taxOffice;
    }

    public String getTaxNumber() {
        return taxNumber;
    }

    public void setTaxNumber(String taxNumber) {
        this.taxNumber = taxNumber;
    }

    /*public BigDecimal getFuelLt() {
        return fuelLt;
    }

    public void setFuelLt(BigDecimal fuelLt) {
        this.fuelLt = fuelLt;
    }

    public BigDecimal getFuelTl() {
        return fuelTl;
    }

    public void setFuelTl(BigDecimal fuelTl) {
        this.fuelTl = fuelTl;
    }*/

   /* public Integer getVehicleCount() {
        return vehicleCount;
    }

    public void setVehicleCount(Integer vehicleCount) {
        this.vehicleCount = vehicleCount;
    }*/

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    /*public AttributeValue getSegment() {
        return segment;
    }

    public void setSegment(AttributeValue segment) {
        this.segment = segment;
    }*/

    public AttributeValue getSector() {
        return sector;
    }

    public void setSector(AttributeValue sector) {
        this.sector = sector;
    }

    public AttributeValue getStatus() {
        return status;
    }

    public void setStatus(AttributeValue status) {
        this.status = status;
    }

    /*public Customer getParent() {
        return parent;
    }

    public void setParent(Customer parent) {
        this.parent = parent;
    }

    public Integer getFleetCode() {
        return fleetCode;
    }

    public void setFleetCode(Integer fleetCode) {
        this.fleetCode = fleetCode;
    }*/

    public String getCommercialTitle() {
        return commercialTitle;
    }

    public void setCommercialTitle(String commercialTitle) {
        this.commercialTitle = commercialTitle;
    }

    /*public AttributeValue getSource() {
        return source;
    }

    public void setSource(AttributeValue source) {
        this.source = source;
    }

    public String getOldFleetCode() {
        return oldFleetCode;
    }

    public void setOldFleetCode(String oldFleetCode) {
        this.oldFleetCode = oldFleetCode;
    }

    public Instant getActivationTime() {
        return activationTime;
    }

    public void setActivationTime(Instant activationTime) {
        this.activationTime = activationTime;
    }
*/

    /*public BigDecimal getDbsLimit() {
        return dbsLimit;
    }

    public void setDbsLimit(BigDecimal dbsLimit) {
        this.dbsLimit = dbsLimit;
    }

    public AttributeValue getColor() {
        return color;
    }

    public void setColor(AttributeValue color) {
        this.color = color;
    }
*/
    @Override
    public void updateSearchPre() {
        super.updateSearchPre();

        if (StringUtils.isNotBlank(name)) {
            search += " " + name;
        }

        /*if (fleetCode != null) {
            search += " " + fleetCode;
        }

        if (StringUtils.isNotBlank(oldFleetCode)) {
            search += " " + oldFleetCode;
        }*/

        if (StringUtils.isNotBlank(commercialTitle)) {
            search += " " + commercialTitle;
        }

        if (StringUtils.isNotBlank(phone)) {
            search += " " + phone;
        }

        if (StringUtils.isNotBlank(email)) {
            search += " " + email;
        }

        if (StringUtils.isNotBlank(website)) {
            search += " " + website;
        }

        if (StringUtils.isNotBlank(taxNumber)) {
            search += " " + taxNumber;
        }

        search = search.toLowerCase(new Locale("tr", "TR"));
    }
    public BigDecimal getLoan() {
        return loan;
    }

    public void setLoan(BigDecimal loan) {
        this.loan = loan;
    }

    public BigDecimal getReceive() {
        return receive;
    }

    public void setReceive(BigDecimal receive) {
        this.receive = receive;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
