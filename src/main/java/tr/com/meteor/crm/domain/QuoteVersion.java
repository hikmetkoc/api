package tr.com.meteor.crm.domain;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Formula;
import tr.com.meteor.crm.utils.idgenerator.IdType;
import tr.com.meteor.crm.utils.metadata.EntityMetadataAnn;
import tr.com.meteor.crm.utils.metadata.FieldMetadataAnn;
import tr.com.meteor.crm.utils.validate.AttributeValueValidate;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Locale;
import java.util.UUID;

@Entity
@IdType(idType = IdType.IdTypeEnum.UUID)
@EntityMetadataAnn(
    apiName = "quote-versions", displayField = "instanceName", title = "Teklif Versiyonu",
    pluralTitle = "Teklif Versiyonları", masterPath = "quote.customer.owner.id"
    //pluralTitle = "Teklif Versiyonları", masterPath = "quote.customer.owner.id", segmentPath = "quote.customer.segment.id"
)
@Table(indexes = {@Index(columnList = "search")})
public class QuoteVersion extends IdNameAuditingEntity<UUID> {

    @FieldMetadataAnn(title = "Teklif Versiyon İsmi", search = true)
    private String name;

    @ManyToOne
    @FieldMetadataAnn(title = "Teklif")
    private Quote quote;

    @ManyToOne
    @FieldMetadataAnn(title = "Durum")
    @AttributeValueValidate(attributeId = "Tek_Dur")
    private AttributeValue status;

    @ManyToOne
    @FieldMetadataAnn(title = "Müşteri")
    private Customer customer;

    @Formula("name")
    @FieldMetadataAnn(readOnly = true, title = "Başlık")
    private String instanceName;

    @Override
    public String getInstanceName() {
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Quote getQuote() {
        return quote;
    }

    public void setQuote(Quote quote) {
        this.quote = quote;
    }

    public AttributeValue getStatus() {
        return status;
    }

    public void setStatus(AttributeValue status) {
        this.status = status;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public void updateSearchPre() {
        super.updateSearchPre();

        if (StringUtils.isNotBlank(name)) {
            search += " " + name;
        }

        search = search.toLowerCase(new Locale("tr", "TR"));
    }
}
