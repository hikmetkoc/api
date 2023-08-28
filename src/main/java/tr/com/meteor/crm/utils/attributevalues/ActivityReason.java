package tr.com.meteor.crm.utils.attributevalues;

import tr.com.meteor.crm.domain.AttributeValue;

public enum ActivityReason {
    ATTRIBUTE("Akt_Ned"),
    MEMNUYET("Akt_Ned_Memnuniyet"),
    SATIS("Akt_Ned_Satış"),
    SIKAYET("Akt_Ned_Şikayet"),
    SOZLESME_YENILEME("Akt_Ned_SözleşmeYenileme");

    final String id;

    ActivityReason(String id) {
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
