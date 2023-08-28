package tr.com.meteor.crm.utils.attributevalues;

import tr.com.meteor.crm.domain.AttributeValue;

public enum LeadStatus {
    ATTRIBUTE("MüA_Dur"),
    YENI("MüA_Dur_Yeni"),
    BASARISIZ("MüA_Dur_Başarısız"),
    ISLENDI("MüA_Dur_İşlendi");

    final String id;

    LeadStatus(String id) {
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
