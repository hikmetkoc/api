package tr.com.meteor.crm.utils.attributevalues;

import tr.com.meteor.crm.domain.AttributeValue;

public enum MoneyTypeStatus {
    ATTRIBUTE("Par_Bir"),
    TL("Par_Bir_Tl"),
    DOLAR("Par_Bir_Dl");
    final String id;

    MoneyTypeStatus(String id) {
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
