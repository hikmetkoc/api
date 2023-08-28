package tr.com.meteor.crm.utils.attributevalues;

import tr.com.meteor.crm.domain.AttributeValue;

public enum QuoteApprovalStatus {
    ATTRIBUTE("Tek_Ona"),
    ONAY_GEREKTIRMEZ("Tek_Ona_Onay_Gerektirmez"),
    ONAY_BEKLIYOR("Tek_Ona_Onay_Bekliyor"),
    ONAYLANDI("Tek_Ona_Onaylandı"),
    REDDEDILDI("Tek_Ona_Reddedildi");

    final String id;

    QuoteApprovalStatus(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public final AttributeValue getAttributeValue() {
        AttributeValue attributeValue = new AttributeValue();
        attributeValue.setId(id);

        return attributeValue;
    }
}
