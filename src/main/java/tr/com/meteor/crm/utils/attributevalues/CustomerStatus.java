package tr.com.meteor.crm.utils.attributevalues;

import tr.com.meteor.crm.domain.AttributeValue;

public enum CustomerStatus {
    ATTRIBUTE("Müş_Dur"),
    YENI("Müş_Dur_Yeni"),
    MEVCUT("Müş_Dur_Mevcut"),
    MEVCUT_AKTIF("Müş_Dur_MevcutAktif"),
    MEVCUT_PASIF("Müş_Dur_MevcutPasif");

    final String id;

    CustomerStatus(String id) {
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
