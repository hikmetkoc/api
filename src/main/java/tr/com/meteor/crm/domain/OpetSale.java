package tr.com.meteor.crm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.StringUtils;
import tr.com.meteor.crm.utils.idgenerator.IdType;
import tr.com.meteor.crm.utils.metadata.EntityMetadataAnn;
import tr.com.meteor.crm.utils.metadata.FieldMetadataAnn;
import tr.com.meteor.crm.utils.request.SortOrder;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

@Entity
@IdType(idType = IdType.IdTypeEnum.UUID)
@EntityMetadataAnn(
    apiName = "opetSales", displayField = "rId", sortBy = "id", sortOrder = SortOrder.DESC,
    title = "Opet Satış", pluralTitle = "Opet Satışlar"
)
@Table(indexes = {
    @Index(columnList = "search"), @Index(columnList = "fleetId"), @Index(columnList = "saleEnd"),
    @Index(columnList = "fleetCode")
})
public class OpetSale extends IdEntity<UUID> {

    @FieldMetadataAnn(title = "Satış Bitiş")
    private Instant saleEnd;

    @FieldMetadataAnn(title = "İşlem Zamanı")
    private Instant processTime;

    @FieldMetadataAnn(title = "İstasyon Id")
    private Integer stationId;

    @FieldMetadataAnn(title = "İstasyon Adı")
    private String stationName;

    @FieldMetadataAnn(title = "Filo Kodu")
    private Integer fleetCode;

    @FieldMetadataAnn(title = "Filo Id")
    private Integer fleetId;

    @FieldMetadataAnn(title = "Filo Adı")
    private String fleetName;

    @FieldMetadataAnn(title = "Grup Id")
    private Integer groupId;

    @FieldMetadataAnn(title = "Grup Adı")
    private String groupName;

    @FieldMetadataAnn(title = "Plaka")
    private String licensePlateNr;

    @FieldMetadataAnn(title = "Ürün Id")
    private Integer productId;

    @FieldMetadataAnn(title = "Ürün Adı")
    private String productName;

    @FieldMetadataAnn(title = "Toplam Fiyat")
    private BigDecimal total;

    @FieldMetadataAnn(title = "Toplam Litre")
    private BigDecimal volume;

    @FieldMetadataAnn(title = "Birim Fiyat")
    private BigDecimal unitPrice;

    @FieldMetadataAnn(title = "Odometer")
    private Integer Odometer;

    @FieldMetadataAnn(title = "ECR Receipt Nr")
    private Integer eCRReceiptNr;

    @FieldMetadataAnn(title = "Fatura Dönem Numarası")
    private String invoicePeriodNr;

    @FieldMetadataAnn(title = "İl")
    private String cityName;

    @FieldMetadataAnn(title = "İl Id")
    private Integer cityId;

    @FieldMetadataAnn(title = "Opet Id")
    private Integer rId;

    @FieldMetadataAnn(title = "Api Kullanıcısı")
    private String apiUser;

    @ManyToOne
    @FieldMetadataAnn(title = "Müşteri")
    private Customer customer;

    @ManyToOne
    @FieldMetadataAnn(title = "Kullanıcı")
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User user;

    public static boolean isEqual(OpetSale os1, OpetSale os2) {
        if (os1 == os2) return true;
        return Objects.equals(os1.saleEnd, os2.saleEnd) &&
            Objects.equals(os1.processTime, os2.processTime) &&
            os1.total != null &&
            os2.total != null &&
            Objects.equals(os1.total.setScale(3, RoundingMode.HALF_UP).doubleValue(), os2.total.setScale(3, RoundingMode.HALF_UP).doubleValue()) &&
            Objects.equals(os1.fleetId, os2.fleetId) &&
            Objects.equals(os1.productId, os2.productId);
    }

    public Instant getSaleEnd() {
        return saleEnd;
    }

    public void setSaleEnd(Instant saleEnd) {
        this.saleEnd = saleEnd;
    }

    public Instant getProcessTime() {
        return processTime;
    }

    public void setProcessTime(Instant processTime) {
        this.processTime = processTime;
    }

    public Integer getStationId() {
        return stationId;
    }

    public void setStationId(Integer stationId) {
        this.stationId = stationId;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public Integer getFleetCode() {
        return fleetCode;
    }

    public void setFleetCode(Integer fleetCode) {
        this.fleetCode = fleetCode;
    }

    public Integer getFleetId() {
        return fleetId;
    }

    public void setFleetId(Integer fleetId) {
        this.fleetId = fleetId;
    }

    public String getFleetName() {
        return fleetName;
    }

    public void setFleetName(String fleetName) {
        this.fleetName = fleetName;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getLicensePlateNr() {
        return licensePlateNr;
    }

    public void setLicensePlateNr(String licensePlateNr) {
        this.licensePlateNr = licensePlateNr;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getOdometer() {
        return Odometer;
    }

    public void setOdometer(Integer odometer) {
        Odometer = odometer;
    }

    public Integer geteCRReceiptNr() {
        return eCRReceiptNr;
    }

    public void seteCRReceiptNr(Integer eCRReceiptNr) {
        this.eCRReceiptNr = eCRReceiptNr;
    }

    public String getInvoicePeriodNr() {
        return invoicePeriodNr;
    }

    public void setInvoicePeriodNr(String invoicePeriodNr) {
        this.invoicePeriodNr = invoicePeriodNr;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Integer getrId() {
        return rId;
    }

    public void setrId(Integer rId) {
        this.rId = rId;
    }

    public String getApiUser() {
        return apiUser;
    }

    public void setApiUser(String apiUser) {
        this.apiUser = apiUser;
    }

    public OpetSale withApiUser(String apiUser) {
        this.apiUser = apiUser;
        return this;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "OpetSale{" +
            "saleEnd=" + saleEnd +
            ", processTime=" + processTime +
            ", stationId=" + stationId +
            ", stationName='" + stationName + '\'' +
            ", fleetId=" + fleetId +
            ", fleetName='" + fleetName + '\'' +
            ", groupId=" + groupId +
            ", groupName='" + groupName + '\'' +
            ", licensePlateNr='" + licensePlateNr + '\'' +
            ", productId=" + productId +
            ", productName='" + productName + '\'' +
            ", total=" + total +
            ", volume=" + volume +
            ", unitPrice=" + unitPrice +
            ", Odometer=" + Odometer +
            ", eCRReceiptNr=" + eCRReceiptNr +
            ", invoicePeriodNr='" + invoicePeriodNr + '\'' +
            ", cityName='" + cityName + '\'' +
            ", cityId=" + cityId +
            ", rId=" + rId +
            ", apiUser='" + apiUser + '\'' +
            '}';
    }

    @Override
    public void updateSearchPre() {
        super.updateSearchPre();

        if (fleetCode != null) {
            search += fleetCode;
        }

        if (StringUtils.isNotBlank(fleetName)) {
            search += " " + fleetName;
        }

        if (StringUtils.isNotBlank(licensePlateNr)) {
            search += " " + licensePlateNr;
        }

        search = search.toLowerCase(new Locale("tr", "TR"));
    }
}
