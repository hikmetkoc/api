package tr.com.meteor.crm.utils.attributevalues;

import tr.com.meteor.crm.domain.AttributeValue;

public enum TaskStatus {
    ATTRIBUTE("Gör_Dur"),
    YENI("Gör_Dur_Yeni"),
    DEVAM_EDIYOR("Gör_Dur_Devam_Ediyor"),
    YAPILAMADI("Gör_Dur_Yapılamadı"),
    TAMAMLANDI("Gör_Dur_Tamamlandı"),
    HATALI_ISLEM("Gör_Dur_Hatali");

    final String id;

    TaskStatus(String id) {
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
