package tr.com.meteor.crm.utils.attributevalues;

import tr.com.meteor.crm.domain.AttributeValue;

public enum OfferStage {
    ATTRIBUTE("Tek_Aşa"),
    YENI("Tek_Aşa_Yeni"),
    KAZANILDI("Tek_Aşa_Kazanıldı"),
    KAYBEDILDI("Tek_Aşa_Kaybedildi");

    final String id;

    OfferStage(String id) {
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
