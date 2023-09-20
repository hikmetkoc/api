package tr.com.meteor.crm.utils.attributevalues;

import tr.com.meteor.crm.domain.AttributeValue;

public enum CustomTaskType {

    KONULAR("Gorev_Konular"),

    SOZLESME_YENILEME("Gorev_Konular_SozY"),
    SOZLESME("Gorev_Konular_Soz"),
    RANDEVU("Gorev_Konular_Ran"),
    TAHSILAT("Gorev_Konular_Tah");

    final String id;
    CustomTaskType(String id) {
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
