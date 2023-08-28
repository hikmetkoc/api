package tr.com.meteor.crm.utils.attributevalues;

import tr.com.meteor.crm.domain.AttributeValue;

public enum BuyStatus {
    ATTRIBUTE("Sat_Dur"),
    BEKLE("Sat_Dur_Bekle"),
    ONAY("Sat_Dur_Onay"),
    IPTAL("Sat_Dur_Ipt"),

    RED("Sat_Dur_Red");
    final String id;

    BuyStatus(String id) {
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
