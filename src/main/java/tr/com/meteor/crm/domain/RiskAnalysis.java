package tr.com.meteor.crm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Formula;
import tr.com.meteor.crm.utils.idgenerator.IdType;
import tr.com.meteor.crm.utils.metadata.EntityMetadataAnn;
import tr.com.meteor.crm.utils.metadata.FieldMetadataAnn;
import tr.com.meteor.crm.utils.validate.AttributeValueValidate;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Locale;
import java.util.UUID;

@Entity
@IdType(idType = IdType.IdTypeEnum.UUID)
@EntityMetadataAnn(apiName = "risk_analysies", displayField = "instanceName", title = "Risk Analiz", pluralTitle = "Risk Analiz Carileri",
    ownerPath = "owner.id")
@Table(indexes = {@Index(columnList = "search")})
public class RiskAnalysis extends IdNameAuditingEntity<UUID> {

    @FieldMetadataAnn(title = "Cari", search = true, readOnly = true, active = true)
    private String name;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    @FieldMetadataAnn(title = "Cari Oluşturan", filterable = true)
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User owner;

    @ManyToOne
    @JoinColumn(name = "risk_commercial_type_id")
    @FieldMetadataAnn(title = "Cari Tipi", defaultValue = "Risk_Commercial_Type_Dbs", filterable = true)
    @AttributeValueValidate(attributeId = "Risk_Commercial_Type")
    private AttributeValue riskCommercialType;

    @FieldMetadataAnn(title = "Vergi No")
    @Column(length = 2048)
    private String taxNumber;

    @FieldMetadataAnn(title = "Yetkili")
    @Column(length = 2048)
    private String contact;

    @FieldMetadataAnn(title = "Cihaz Sayısı")
    private Integer deviceCount = 0;

    @FieldMetadataAnn(title = "Filo Kodu")
    @Column(length = 2048)
    private String fleetCode;

    @FieldMetadataAnn(title = "Cari Kodu")
    @Column(length = 2048)
    private String currencyCode;

    @FieldMetadataAnn(title = "Cari Unvan")
    private String currencyName;

    @FieldMetadataAnn(title = "Cari Kısa Unvan")
    private String currencyShortName;

    @ManyToOne
    @JoinColumn(name = "sales_owner_id")
    @FieldMetadataAnn(title = "Satış Temsilcisi", filterable = true)
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User salesOwner;

    @ManyToOne
    @JoinColumn(name = "currency_group_id")
    @FieldMetadataAnn(title = "Cari Grup", defaultValue = "Risk_Currency_Group_Dbs", filterable = true)
    @AttributeValueValidate(attributeId = "Risk_Currency_Group")
    private AttributeValue currenyGroup;

    @ManyToOne
    @JoinColumn(name = "tts_service_id")
    @FieldMetadataAnn(title = "TTS Servis", defaultValue = "Risk_Tts_Service_Bur", filterable = true)
    @AttributeValueValidate(attributeId = "Risk_Tts_Service")
    private AttributeValue ttsService;

    @FieldMetadataAnn(title = "Vergi Dairesi")
    private String taxApartment;

    @FieldMetadataAnn(title = "Teminat Tutarı")
    private BigDecimal guaranteeAmount;

    @ManyToOne
    @FieldMetadataAnn(required = true, title = "İl")
    private City city;

    @ManyToOne
    @FieldMetadataAnn(required = true, title = "İlçe")
    private District district;

    @FieldMetadataAnn(title = "Fatura Adresi")
    private String invoiceAddress;

    @FieldMetadataAnn(title = "Not")
    private String note;

    @FieldMetadataAnn(title = "Adres")
    private String address;

    @Email
    @Size(min = 5, max = 254)
    @Column(length = 254, unique = true)
    @FieldMetadataAnn(title = "Bilgilendirme E-Posta Adresi", search = true, type = "Email", required = true)
    private String infoEmail;

    @Email
    @Size(min = 5, max = 254)
    @Column(length = 254, unique = true)
    @FieldMetadataAnn(title = "Sisteme Giriş İçin Gerekli E-Posta Adresi", search = true, type = "Email", required = true)
    private String systemEmail;

    @FieldMetadataAnn(title = "Telefon", type="Phone")
    private String phone;

    @FieldMetadataAnn(title = "Cep Telefonu", type="Phone")
    private String userPhone;

    @FieldMetadataAnn(title = "Motorin İskonto Oranı")
    private Double dieselDiscountRate = 0.00;

    @FieldMetadataAnn(title = "Kurşunsuz İskonto Oranı")
    private Double gasolineDiscountRate = 0.00;

    @FieldMetadataAnn(title = "Sözleşme Başlangıç Tarihi", filterable = true)
    private Instant contractStartDate;

    @FieldMetadataAnn(title = "Sözleşme Bitiş Tarihi", filterable = true)
    private Instant contractEndDate;

    @FieldMetadataAnn(title = "Taahhüt Tutarı(Litre)")
    private BigDecimal commitmentAmount = BigDecimal.ZERO;

    @FieldMetadataAnn(title = "Tazminat Tutarı")
    private BigDecimal compensationAmount = BigDecimal.ZERO;

    @FieldMetadataAnn(title = "Bloke Limiti")
    private BigDecimal blockingLimit = BigDecimal.ZERO;

    @FieldMetadataAnn(title = "Cihaz Bedeli")
    private BigDecimal deviceFee;

    @ManyToOne
    @JoinColumn(name = "payment_method_id")
    @FieldMetadataAnn(title = "Ödeme Şekli", defaultValue = "Risk_Payment_Method_Dbs", filterable = true)
    @AttributeValueValidate(attributeId = "Risk_Payment_Method")
    private AttributeValue paymentMethod;

    @ManyToOne
    @JoinColumn(name = "collection_type_id")
    @FieldMetadataAnn(title = "Tahsilat Tipi", defaultValue = "Risk_Col_Type_Tek", filterable = true)
    @AttributeValueValidate(attributeId = "Risk_Col_Type")
    private AttributeValue collectionType;

    @FieldMetadataAnn(title = "Fatura Vadesi")
    private Integer invoiceMaturity = 0;

    @FieldMetadataAnn(title = "Motorin İskonto Oranı(Tanımlı İstasyon)")
    private Double dieselDrDefined = 0.00;

    @FieldMetadataAnn(title = "Kurşunsuz İskonto Oranı(Tanımlı İstasyon)")
    private Double gasolineDrDefined = 0.00;

    @FieldMetadataAnn(title = "Özel Oran İlk Ay (%)")
    private Double specialRate = 95.00;

    @ManyToOne
    @JoinColumn(name = "currency_status_id")
    @FieldMetadataAnn(title = "Cari Durum", defaultValue = "Risk_Cur_Sta_Aktif", filterable = true)
    @AttributeValueValidate(attributeId = "Risk_Cur_Sta")
    private AttributeValue currencyStatus;

    @FieldMetadataAnn(title = "Yakıt Kart Limitleme Oranı")
    private BigDecimal fuelCardLimRate = BigDecimal.ZERO;

    @FieldMetadataAnn(title = "Hediye Kart Limitleme Oranı")
    private BigDecimal giftCardLimRate = BigDecimal.ZERO;

    @FieldMetadataAnn(title = "Otomatik Limit Atansın")
    private Boolean autoLimit = false;

    @FieldMetadataAnn(title = "Giriş Ücretini Tahsil Et")
    private Boolean entranceFee = false;

    @FieldMetadataAnn(title = "Muhasebe Farkını Yansıt")
    private Boolean accountingDif = false;

    @FieldMetadataAnn(title = "E-Posta ve Sms Uyarısı Açik")
    private Boolean warningOn = false;

    @FieldMetadataAnn(title = "Özel Oran Uygula")
    private Boolean speRateDone = false;

    @FieldMetadataAnn(title = "Yakıt Kart Cari")
    private Boolean fuelcard = false;

    @FieldMetadataAnn(title = "Hediye Kart Cari")
    private Boolean giftcard = false;

    @FieldMetadataAnn(title = "Cift DBS Ödemeli Cari")
    private Boolean dubDbs = false;

    @FieldMetadataAnn(title = "E-Tahsilat Kullanabilir")
    private Boolean eCollection = false;

    @FieldMetadataAnn(title = "Bloke Miktarı Uygula")
    private Boolean blockAmount = false;

    @FieldMetadataAnn(title = "Cari Ekstre Gönder")
    private Boolean currentExtract = false;

    @FieldMetadataAnn(title = "Çatı Limit Uygula")
    private Boolean upperLimit = false;

    @FieldMetadataAnn(title = "Her Ay Aynı Limit Atansın")
    private Boolean mounthlyLimit = false;

    @FieldMetadataAnn(title = "Ön Provizyon Modeli Çalışacak")
    private Boolean preAuthModel = false;

    @Transient
    @Formula("name")
    @FieldMetadataAnn(readOnly = true, title = "Başlık", active = false)
    private String instanceName;

    @PostLoad
    private void populateInstanceName() {
        this.instanceName = this.name;
    }

    @Override
    public String getInstanceName() {
        return instanceName;
    }
    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public User getOwner() {
        return owner;
    }
    public void setOwner(User owner) {
        this.owner = owner;
    }

    public AttributeValue getRiskCommercialType() {
        return riskCommercialType;
    }

    public void setRiskCommercialType(AttributeValue riskCommercialType) {
        this.riskCommercialType = riskCommercialType;
    }

    public String getTaxNumber() {
        return taxNumber;
    }

    public void setTaxNumber(String taxNumber) {
        this.taxNumber = taxNumber;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Integer getDeviceCount() {
        return deviceCount;
    }

    public void setDeviceCount(Integer deviceCount) {
        this.deviceCount = deviceCount;
    }

    public String getFleetCode() {
        return fleetCode;
    }

    public void setFleetCode(String fleetCode) {
        this.fleetCode = fleetCode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getCurrencyShortName() {
        return currencyShortName;
    }

    public void setCurrencyShortName(String currencyShortName) {
        this.currencyShortName = currencyShortName;
    }

    public User getSalesOwner() {
        return salesOwner;
    }

    public void setSalesOwner(User salesOwner) {
        this.salesOwner = salesOwner;
    }

    public AttributeValue getCurrenyGroup() {
        return currenyGroup;
    }

    public void setCurrenyGroup(AttributeValue currenyGroup) {
        this.currenyGroup = currenyGroup;
    }

    public AttributeValue getTtsService() {
        return ttsService;
    }

    public void setTtsService(AttributeValue ttsService) {
        this.ttsService = ttsService;
    }

    public String getTaxApartment() {
        return taxApartment;
    }

    public void setTaxApartment(String taxApartment) {
        this.taxApartment = taxApartment;
    }

    public BigDecimal getGuaranteeAmount() {
        return guaranteeAmount;
    }

    public void setGuaranteeAmount(BigDecimal guaranteeAmount) {
        this.guaranteeAmount = guaranteeAmount;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public String getInvoiceAddress() {
        return invoiceAddress;
    }

    public void setInvoiceAddress(String invoiceAddress) {
        this.invoiceAddress = invoiceAddress;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getInfoEmail() {
        return infoEmail;
    }

    public void setInfoEmail(String infoEmail) {
        this.infoEmail = infoEmail;
    }

    public String getSystemEmail() {
        return systemEmail;
    }

    public void setSystemEmail(String systemEmail) {
        this.systemEmail = systemEmail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public Double getDieselDiscountRate() {
        return dieselDiscountRate;
    }

    public void setDieselDiscountRate(Double dieselDiscountRate) {
        this.dieselDiscountRate = dieselDiscountRate;
    }

    public Double getGasolineDiscountRate() {
        return gasolineDiscountRate;
    }

    public void setGasolineDiscountRate(Double gasolineDiscountRate) {
        this.gasolineDiscountRate = gasolineDiscountRate;
    }

    public Instant getContractStartDate() {
        return contractStartDate;
    }

    public void setContractStartDate(Instant contractStartDate) {
        this.contractStartDate = contractStartDate;
    }

    public Instant getContractEndDate() {
        return contractEndDate;
    }

    public void setContractEndDate(Instant contractEndDate) {
        this.contractEndDate = contractEndDate;
    }

    public BigDecimal getCommitmentAmount() {
        return commitmentAmount;
    }

    public void setCommitmentAmount(BigDecimal commitmentAmount) {
        this.commitmentAmount = commitmentAmount;
    }

    public BigDecimal getCompensationAmount() {
        return compensationAmount;
    }

    public void setCompensationAmount(BigDecimal compensationAmount) {
        this.compensationAmount = compensationAmount;
    }

    public BigDecimal getBlockingLimit() {
        return blockingLimit;
    }

    public void setBlockingLimit(BigDecimal blockingLimit) {
        this.blockingLimit = blockingLimit;
    }

    public BigDecimal getDeviceFee() {
        return deviceFee;
    }

    public void setDeviceFee(BigDecimal deviceFee) {
        this.deviceFee = deviceFee;
    }

    public AttributeValue getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(AttributeValue paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public AttributeValue getCollectionType() {
        return collectionType;
    }

    public void setCollectionType(AttributeValue collectionType) {
        this.collectionType = collectionType;
    }

    public Integer getInvoiceMaturity() {
        return invoiceMaturity;
    }

    public void setInvoiceMaturity(Integer invoiceMaturity) {
        this.invoiceMaturity = invoiceMaturity;
    }

    public Double getDieselDrDefined() {
        return dieselDrDefined;
    }

    public void setDieselDrDefined(Double dieselDrDefined) {
        this.dieselDrDefined = dieselDrDefined;
    }

    public Double getGasolineDrDefined() {
        return gasolineDrDefined;
    }

    public void setGasolineDrDefined(Double gasolineDrDefined) {
        this.gasolineDrDefined = gasolineDrDefined;
    }

    public Double getSpecialRate() {
        return specialRate;
    }

    public void setSpecialRate(Double specialRate) {
        this.specialRate = specialRate;
    }

    public AttributeValue getCurrencyStatus() {
        return currencyStatus;
    }

    public void setCurrencyStatus(AttributeValue currencyStatus) {
        this.currencyStatus = currencyStatus;
    }

    public BigDecimal getFuelCardLimRate() {
        return fuelCardLimRate;
    }

    public void setFuelCardLimRate(BigDecimal fuelCardLimRate) {
        this.fuelCardLimRate = fuelCardLimRate;
    }

    public BigDecimal getGiftCardLimRate() {
        return giftCardLimRate;
    }

    public void setGiftCardLimRate(BigDecimal giftCardLimRate) {
        this.giftCardLimRate = giftCardLimRate;
    }

    public Boolean getAutoLimit() {
        return autoLimit;
    }

    public void setAutoLimit(Boolean autoLimit) {
        this.autoLimit = autoLimit;
    }

    public Boolean getEntranceFee() {
        return entranceFee;
    }

    public void setEntranceFee(Boolean entranceFee) {
        this.entranceFee = entranceFee;
    }

    public Boolean getAccountingDif() {
        return accountingDif;
    }

    public void setAccountingDif(Boolean accountingDif) {
        this.accountingDif = accountingDif;
    }

    public Boolean getWarningOn() {
        return warningOn;
    }

    public void setWarningOn(Boolean warningOn) {
        this.warningOn = warningOn;
    }

    public Boolean getSpeRateDone() {
        return speRateDone;
    }

    public void setSpeRateDone(Boolean speRateDone) {
        this.speRateDone = speRateDone;
    }

    public Boolean getFuelcard() {
        return fuelcard;
    }

    public void setFuelcard(Boolean fuelcard) {
        this.fuelcard = fuelcard;
    }

    public Boolean getGiftcard() {
        return giftcard;
    }

    public void setGiftcard(Boolean giftcard) {
        this.giftcard = giftcard;
    }

    public Boolean getDubDbs() {
        return dubDbs;
    }

    public void setDubDbs(Boolean dubDbs) {
        this.dubDbs = dubDbs;
    }

    public Boolean geteCollection() {
        return eCollection;
    }

    public void seteCollection(Boolean eCollection) {
        this.eCollection = eCollection;
    }

    public Boolean getBlockAmount() {
        return blockAmount;
    }

    public void setBlockAmount(Boolean blockAmount) {
        this.blockAmount = blockAmount;
    }

    public Boolean getCurrentExtract() {
        return currentExtract;
    }

    public void setCurrentExtract(Boolean currentExtract) {
        this.currentExtract = currentExtract;
    }

    public Boolean getUpperLimit() {
        return upperLimit;
    }

    public void setUpperLimit(Boolean upperLimit) {
        this.upperLimit = upperLimit;
    }

    public Boolean getMounthlyLimit() {
        return mounthlyLimit;
    }

    public void setMounthlyLimit(Boolean mounthlyLimit) {
        this.mounthlyLimit = mounthlyLimit;
    }

    public Boolean getPreAuthModel() {
        return preAuthModel;
    }

    public void setPreAuthModel(Boolean preAuthModel) {
        this.preAuthModel = preAuthModel;
    }

    @Override
    public void updateSearchPre() {
        super.updateSearchPre();

        if (StringUtils.isNotBlank(currencyCode)) {
            search += " " + currencyCode;
        }

        if (StringUtils.isNotBlank(currencyName)) {
            search += " " + currencyName;
        }

        if (StringUtils.isNotBlank(name)) {
            search += " " + name;
        }

        search = search.toLowerCase(new Locale("tr", "TR"));
    }
}
