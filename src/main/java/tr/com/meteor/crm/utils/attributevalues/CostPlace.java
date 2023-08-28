package tr.com.meteor.crm.utils.attributevalues;

import tr.com.meteor.crm.domain.AttributeValue;

public enum CostPlace {
    ATTRIBUTE("Cost_Place_Place"),
    METEORMERKEZ("Cost_Place_MeteorMerkez"),
    TERMINAL("Cost_Place_Terminal"),
    IZMIR("Cost_Place_MeteorIzmir"),
    IGDIR("Cost_Place_MeteorIgdir"),
    NCC("Cost_Place_Ncc"),
    CEMCAN("Cost_Place_Cemcan"),
    MUDANYA("Cost_Place_Mudanya"),
    BIRCE("Cost_Place_Birce"),
    SIMYA("Cost_Place_Simya"),
    AVELICE("Cost_Place_Avelice"),
    VITALYUM("Cost_Place_Vitalyum"),
    VITA("Cost_Place_Vita"),
    TEPE("Cost_Place_Tepe"),
    SAMANLI("Cost_Place_Samanli"),
    CIFTLIKKOY("Cost_Place_Ciftlikkoy"),
    SARHJ("Cost_Place_Sarj"),
    CHARGE("Cost_Place_Charge"),
    OTOBIL("Cost_Place_Otobil");
    final String id;

    CostPlace(String id) {
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
