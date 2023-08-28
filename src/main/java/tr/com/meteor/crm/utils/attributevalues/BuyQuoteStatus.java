package tr.com.meteor.crm.utils.attributevalues;

import tr.com.meteor.crm.domain.AttributeValue;

public enum BuyQuoteStatus {
    ATTRIBUTE("Tek_Dur"),
    BEKLE("Tek_Dur_Haz"),
    ONAY("Tek_Dur_Tam");
    final String id;

    BuyQuoteStatus(String id) {
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
