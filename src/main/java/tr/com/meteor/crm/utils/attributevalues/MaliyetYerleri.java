package tr.com.meteor.crm.utils.attributevalues;

import tr.com.meteor.crm.domain.AttributeValue;

public enum MaliyetYerleri {
    ATTRIBUTE("Maliyet_Yerleri"),
    METEORMERKEZ("Mal_Met_Mer"),
    TERMINAL("Mal_Met_Ter"),
    IZMIR("Mal_Met_Izm"),
    IGDIR("Mal_Met_Igd");
    final String id;

    MaliyetYerleri(String id) {
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
