package tr.com.meteor.crm.utils.attributevalues;

import tr.com.meteor.crm.domain.AttributeValue;

public enum FileDescriptorType {
    ATTRIBUTE("Dos_Tip"),
    FILE("Dos_Tip_Dosya"),
    FOLDER("Dos_Tip_Klasör");

    String id;

    FileDescriptorType(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AttributeValue getAttributeValue() {
        AttributeValue attributeValue = new AttributeValue();
        attributeValue.setId(id);

        return attributeValue;
    }
}
