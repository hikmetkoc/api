package tr.com.meteor.crm.utils.attributevalues;

import tr.com.meteor.crm.domain.AttributeValue;

public enum HolDayStatus {
    ATTRIBUTE("Izin_Gun"),
    PAZARTESI("Izin_Gun_Pzt"),
    SALI("Izin_Gun_Salı"),
    CARSAMBA("Izin_Gun_Çrş"),
    PERSEMBE("Izin_Gun_Prş"),
    CUMA("Izin_Gun_Cuma"),
    CUMARTESI("Izin_Gun_Cts"),
    PAZAR("Izin_Gun_Pzr");
    final String id;

    HolDayStatus(String id) {
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
