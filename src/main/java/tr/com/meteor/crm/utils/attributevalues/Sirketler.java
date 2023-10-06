package tr.com.meteor.crm.utils.attributevalues;

import tr.com.meteor.crm.domain.AttributeValue;

public enum Sirketler {
    ATTRIBUTE("Sirketler"),
    METEOR("Sirketler_Meteor"),
    TERMINAL("Sirketler_Terminal"),
    GORUKLE("Sirketler_Gorukle"),
    ISTANBUL("Sirketler_Ist"),
    BURSA("Sirketler_Bursa"),
    IGDIR("Sirketler_Igdir"),
    TEPE("Sirketler_Tepe"),
    IZMIR("Sirketler_Izmir"),
    NCC("Sirketler_Ncc"),
    CEMCAN("Sirketler_Cemcan"),
    SIMYA("Sirketler_Simya"),
    BIRCE("Sirketler_Birce"),
    MUDANYA("Sirketler_Mudanya"),
    STAR("Sirketler_Star"),
    CHARGE("Sirketler_Charge"),
    AVELICE("Sirketler_Avelice");
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
