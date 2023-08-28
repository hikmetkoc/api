package tr.com.meteor.crm.utils.attributevalues;

import tr.com.meteor.crm.domain.AttributeValue;

public enum FaturaSirketleri {
    ATTRIBUTE("Fatura_Sirketleri"),
    METEOR("Fatura_Sirketleri_Meteor"),
    CEMCAN("Fatura_Sirketleri_Cemcan"),
    INSAAT("Fatura_SirketleriMeteorIns"),
    IGDIR("Fatura_Sirketleri_Igdır");
    final String id;

    FaturaSirketleri(String id) {
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
