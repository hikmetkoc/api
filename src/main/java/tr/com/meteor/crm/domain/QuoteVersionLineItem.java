package tr.com.meteor.crm.domain;

import tr.com.meteor.crm.utils.idgenerator.IdType;
import tr.com.meteor.crm.utils.metadata.EntityMetadataAnn;
import tr.com.meteor.crm.utils.metadata.FieldMetadataAnn;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@IdType(idType = IdType.IdTypeEnum.UUID)
@EntityMetadataAnn(
    apiName = "quote-version-line-items", displayField = "instanceName", title = "Teklif Versiyon Satırı",
    pluralTitle = "Teklif Versiyon Satırları", masterPath = "quoteVersion.quote.customer.owner.id"
    //segmentPath = "quoteVersion.quote.customer.segment.id"
)
@Table(indexes = {@Index(columnList = "search")})
public class QuoteVersionLineItem extends IdEntity<UUID> {

    @FieldMetadataAnn(title = "Teklif Versiyon")
    private QuoteVersion quoteVersion;

    public QuoteVersion getQuoteVersion() {
        return quoteVersion;
    }

    public void setQuoteVersion(QuoteVersion quoteVersion) {
        this.quoteVersion = quoteVersion;
    }
}
