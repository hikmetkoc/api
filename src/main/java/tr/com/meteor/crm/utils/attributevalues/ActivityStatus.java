package tr.com.meteor.crm.utils.attributevalues;

import tr.com.meteor.crm.domain.AttributeValue;

public enum ActivityStatus {
    ATTRIBUTE("Akt_Dur"),
    YENI("Akt_Dur_Yeni"),
    DEVAM_EDIYOR("Akt_Dur_DevamEdiyor"),
    IPTAL("Akt_Dur_İptal"),
    TAMAMLANDI("Akt_Dur_Tamamlandı");

    final String id;

    ActivityStatus(String id) {
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
