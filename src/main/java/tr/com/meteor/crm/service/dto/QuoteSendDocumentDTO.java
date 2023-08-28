package tr.com.meteor.crm.service.dto;

import tr.com.meteor.crm.utils.Documents;

import java.util.List;
import java.util.UUID;

public class QuoteSendDocumentDTO {
    private List<Documents> docs;
    private UUID quoteId;

    private UUID ikfileId;

    public List<Documents> getDocs() {
        return docs;
    }

    public void setDocs(List<Documents> docs) {
        this.docs = docs;
    }

    public UUID getQuoteId() {
        return quoteId;
    }

    public UUID getIkfileId() {
        return ikfileId;
    }
    public void setQuoteId(UUID quoteId) {
        this.quoteId = quoteId;
    }

    public void setIkfileId(UUID ikfileId) {
        this.ikfileId = ikfileId;
    }
}
