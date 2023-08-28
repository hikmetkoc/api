package tr.com.meteor.crm.utils.attributevalues;

import tr.com.meteor.crm.domain.AttributeValue;

public enum SpendStatus {
    ATTRIBUTE("Spend_Status"),
    ODENMEDI("Spend_Status_No"),

    REDDEDILDI("Spend_Status_Red"),
    ODENDI("Spend_Status_Yes");

    final String id;

    SpendStatus(String id) {
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
