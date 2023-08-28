package tr.com.meteor.crm.utils.attributevalues;

import tr.com.meteor.crm.domain.AttributeValue;

public enum Sirketler {
    ATTRIBUTE("Sirketler"),
    METEOR("Sirket_Meteor"),
    CEMCAN("Sirket_Cemcan"),
    INSAAT("Sirket_MeteorIns"),
    IGDIR("Sirket_Igdır");
    final String id;

    Sirketler(String id) {
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
