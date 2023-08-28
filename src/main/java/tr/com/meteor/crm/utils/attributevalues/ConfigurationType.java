package tr.com.meteor.crm.utils.attributevalues;

import tr.com.meteor.crm.domain.AttributeValue;

public enum ConfigurationType {
    ATTRIBUTE("Kon_Tip"),
    STRING("Kon_Tip_String"),
    INTEGER("Kon_Tip_Integer"),
    DOUBLE("Kon_Tip_Double"),
    LONG("Kon_Tip_Long"),
    INSTANT("Kon_Tip_Instant"),
    LOCAL_DATE("Kon_Tip_Local_Date"),
    BOOLEAN("Kon_Tip_Boolean");

    final String id;

    ConfigurationType(String id) {
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
