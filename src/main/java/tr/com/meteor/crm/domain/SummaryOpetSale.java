package tr.com.meteor.crm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import tr.com.meteor.crm.utils.idgenerator.IdType;
import tr.com.meteor.crm.utils.metadata.EntityMetadataAnn;
import tr.com.meteor.crm.utils.metadata.FieldMetadataAnn;
import tr.com.meteor.crm.utils.request.SortOrder;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@IdType(idType = IdType.IdTypeEnum.UUID)
@EntityMetadataAnn(
    apiName = "summary-opet-sales", displayField = "id", sortBy = "id", sortOrder = SortOrder.DESC,
    title = "Özet Opet Satış", pluralTitle = "Özet Opet Satışlar"
)
@Table(indexes = {
    @Index(columnList = "search"), @Index(columnList = "fleetId"), @Index(columnList = "saleEnd"),
    @Index(columnList = "fleetCode")
})
public class SummaryOpetSale extends IdEntity<UUID> {

    @ManyToOne
    @FieldMetadataAnn(title = "Müşteri")
    private Customer customer;

    @ManyToOne
    @FieldMetadataAnn(title = "Kullanıcı")
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User user;

    @FieldMetadataAnn(title = "Satış Bitiş")
    private Instant saleEnd;

    @FieldMetadataAnn(title = "Filo Id")
    private Integer fleetId;

    @FieldMetadataAnn(title = "Filo Kodu")
    private Integer fleetCode;

    @FieldMetadataAnn(title = "Ürün Adı")
    private String productName;

    @FieldMetadataAnn(title = "Toplam Fiyat")
    private BigDecimal total;

    @FieldMetadataAnn(title = "Toplam Litre")
    private BigDecimal volume;

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

    public Instant getSaleEnd() {
        return saleEnd;
    }

    public void setSaleEnd(Instant saleEnd) {
        this.saleEnd = saleEnd;
    }

    public Integer getFleetId() {
        return fleetId;
    }

    public void setFleetId(Integer fleetId) {
        this.fleetId = fleetId;
    }

    public Integer getFleetCode() {
        return fleetCode;
    }

    public void setFleetCode(Integer fleetCode) {
        this.fleetCode = fleetCode;
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
}
