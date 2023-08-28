package tr.com.meteor.crm.utils.attributevalues;

import tr.com.meteor.crm.domain.AttributeValue;

public enum ContractStatus {
    ATTRIBUTE("Söz_Dur"),
    AKTIF("Söz_Dur_Aktif"),
    PASIF("Söz_Dur_Pasif"),
    IPTAL("Söz_Dur_Ipt"),

    RED("Söz_Dur_Red"),
    MERKEZDEN("Söz_Dur_Kar");
    final String id;

    ContractStatus(String id) {
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
