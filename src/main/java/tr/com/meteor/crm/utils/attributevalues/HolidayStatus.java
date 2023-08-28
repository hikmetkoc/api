package tr.com.meteor.crm.utils.attributevalues;

import tr.com.meteor.crm.domain.AttributeValue;

public enum HolidayStatus {
    ATTRIBUTE("Izın_Turu"),
    YILLIK("Iz_Tur_Yil"),

    MAZERET("Iz_Tur_Maz"),
    BABALIK("Iz_Tur_Bab"),
    DOGUM("Iz_Tur_Dog"),
    OLUM("Iz_Tur_Olm"),
    RAPOR("Iz_Tur_Rap"),

    EVLILIK("Iz_Tur_Evl"),

    IDARI("Iz_Tur_Idr"),

    UCRETSIZ("Iz_Tur_Ucr");
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
