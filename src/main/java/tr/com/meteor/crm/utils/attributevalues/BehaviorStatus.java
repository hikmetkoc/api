package tr.com.meteor.crm.utils.attributevalues;

import tr.com.meteor.crm.domain.AttributeValue;

public enum BehaviorStatus {
    ATTRIBUTE("Akt_Dur"),
    ALACAK("Har_Tip_Alacak"),
    BORC("Har_Tip_Borç");

    final String id;

    BehaviorStatus(String id) {
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
