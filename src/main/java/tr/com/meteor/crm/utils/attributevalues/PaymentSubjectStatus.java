package tr.com.meteor.crm.utils.attributevalues;

import tr.com.meteor.crm.domain.AttributeValue;

public enum PaymentSubjectStatus {
    ATTRIBUTE("Payment_Sub"),
    TRAFIK("Payment_Sub_Tra"),
    PRIM("Payment_Sub_Prim");
    final String id;

    PaymentSubjectStatus(String id) {
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
