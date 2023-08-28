package tr.com.meteor.crm.utils.attributevalues;

import tr.com.meteor.crm.domain.AttributeValue;

public enum HolidayStateStatus {
    ATTRIBUTE("Izin_Dur"),
    AKTIF("Izin_Dur_Aktif"),
    PASIF("Izin_Dur_Pasif"),
    IPTAL("Izin_Dur_Ipt"),
    ISLENDI("Izin_Dur_Isl"),

    RED("Izin_Dur_Red");
    final String id;

    HolidayStateStatus(String id) {
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
