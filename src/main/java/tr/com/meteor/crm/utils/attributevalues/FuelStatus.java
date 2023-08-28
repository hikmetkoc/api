package tr.com.meteor.crm.utils.attributevalues;

import tr.com.meteor.crm.domain.AttributeValue;

public enum FuelStatus {
    ATTRIBUTE("Fuel_Dur"),
    ONAY("Fuel_Dur_Onay"),
    RED("Fuel_Dur_Red"),
    BEKLIYOR("Fuel_Dur_Bekle");
    final String id;

    FuelStatus(String id) {
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
