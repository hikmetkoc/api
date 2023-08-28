package tr.com.meteor.crm.utils.attributevalues;

import tr.com.meteor.crm.domain.AttributeValue;

public enum PaymentStatus {
    ATTRIBUTE("Payment_Status"),
    ONAY1("Payment_Status_Bek1"),
    MUHASEBE("Payment_Status_Muh"),
    ONAY2("Payment_Status_Bek2"),
    ONAYLANDI("Payment_Status_Onay"),
    ODENDI("Payment_Status_Ode"),
    OTO("Payment_Status_OtoOde"),

    KISMI("Payment_Status_Kısmi"),

    RED("Payment_Status_Red");
    final String id;

    PaymentStatus(String id) {
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
