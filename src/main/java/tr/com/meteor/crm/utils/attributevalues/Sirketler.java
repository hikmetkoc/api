package tr.com.meteor.crm.utils.attributevalues;

import tr.com.meteor.crm.domain.AttributeValue;

public enum Sirketler {
    ATTRIBUTE("Sirketler"),
    METEOR("Sirketler_Meteor"),
    CEMCAN("Sirketler_Cemcan"),
    INSAAT("Sirketler_MeteorIns"),
    IGDIR("Sirketler_Igdır");
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
