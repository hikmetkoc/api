package tr.com.meteor.crm.utils.attributevalues;

import tr.com.meteor.crm.domain.AttributeValue;

public enum CustomActivityType {
    ATTRIBUTE("Akt_Tip"),
    TELEFON("Akt_Tip_Telefon"),
    RANDEVU("Akt_Tip_Randevu"),
    CATKAPI("Akt_Tip_Çatkapı"),
    FIRMA_ZIYARETI("Akt_Tip_Firma_Ziyareti"),
    CIHAZ_TESLIMATI("Akt_Tip_Cihaz_Teslimatı"),
    TAHSILAT("Akt_Tip_Tahsilat"),
    SOZLESME("Akt_Tip_Sözleşme"),
    SOZLESME_YENILEME("Akt_Tip_Sözleşme_Yenileme"),
    METEORCARD_SOZLESME("Akt_Tip_MeteorCard_Sözleşme"),
    OTOFILO_SOZLESME("Akt_Tip_Otofilo_Sözleşme");

    final String id;

    CustomActivityType(String id) {
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
