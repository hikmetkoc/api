package tr.com.meteor.crm.utils.attributevalues;

import tr.com.meteor.crm.domain.AttributeValue;

public enum CustomTaskStatus {
    ATTRIBUTE("T_Gör_Dur"),
    YENI("T_Gör_Dur_Yeni"),
    DEVAM_EDIYOR("T_Gör_Dur_Devam_Ediyor"),
    YAPILAMADI("T_Gör_Dur_Reddedildi"),
    TAMAMLANDI("T_Gör_Dur_Tamamlandı");

    final String id;

    CustomTaskStatus(String id) {
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
