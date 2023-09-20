package tr.com.meteor.crm.utils.attributevalues;

import tr.com.meteor.crm.domain.AttributeValue;

public enum HolidayStatus {
    ATTRIBUTE("Izin_Turu"),
    YILLIK("Izin_Turu_Yil"),

    MAZERET("Izin_Turu_Maz"),
    BABALIK("Izin_Turu_Bab"),
    DOGUM("Izin_Turu_Dog"),
    OLUM("Izin_Turu_Olm"),
    RAPOR("Izin_Turu_Rap"),

    EVLILIK("Izin_Turu_Evl"),

    IDARI("Izin_Turu_Idr"),

    UCRETSIZ("Izin_Turu_Ucr");
    final String id;

    HolidayStatus(String id) {
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
