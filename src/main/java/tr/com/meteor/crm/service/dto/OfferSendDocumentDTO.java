package tr.com.meteor.crm.service.dto;

import tr.com.meteor.crm.utils.Documents;

import java.util.List;
import java.util.UUID;

public class OfferSendDocumentDTO {
    private List<Documents> docs;
    private UUID offerId;

    public List<Documents> getDocs() {
        return docs;
    }

    public void setDocs(List<Documents> docs) {
        this.docs = docs;
    }

    public UUID getOfferId() {
        return offerId;
    }

    public void setOfferId(UUID offerId) {
        this.offerId = offerId;
    }
}
