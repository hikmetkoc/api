package tr.com.meteor.crm.utils.attributevalues;

import tr.com.meteor.crm.domain.AttributeValue;

public enum FaturaSirketleri {
    ATTRIBUTE("Fatura_Sirketleri"),
    METEOR("Fatura_Sirketleri_Meteor"),

    TERMINAL("Fatura_Sirketleri_Terminal"),
    CEMCAN("Fatura_Sirketleri_Cemcan"),
    NCC("Fatura_Sirketleri_Ncc"),
    INSAAT("Fatura_Sirketleri_MeteorIns"),
    IGDIR("Fatura_Sirketleri_Igdir"),
    IZMIR("Fatura_Sirketleri_Izmir"),
    SIMYA("Fatura_Sirketleri_Simya"),
    BIRCE("Fatura_Sirketleri_Birce"),
    MUDANYA("Fatura_Sirketleri_Mudanya"),
    STAR("Fatura_Sirketleri_Star"),
    CHARGE("Fatura_Sirketleri_Charge"),
    AVELICE("Fatura_Sirketleri_Avelice");

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
