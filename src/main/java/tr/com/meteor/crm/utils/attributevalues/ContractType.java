package tr.com.meteor.crm.utils.attributevalues;

import tr.com.meteor.crm.domain.AttributeValue;

public enum ContractType {
    ATTRIBUTE("Söz_Tip"),
    SOZLESME("Söz_Tip_Sözleşme"),
    OTOFILO_SOZLESME("Söz_Tip_Otofilo_Sözleşme"),
    METEORCARD_SOZLESME("Söz_Tip_Meteorcard_Sözleşme");

    final String id;

    ContractType(String id) {
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
