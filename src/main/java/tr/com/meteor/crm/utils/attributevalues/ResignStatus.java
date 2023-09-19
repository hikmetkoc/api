package tr.com.meteor.crm.utils.attributevalues;

import tr.com.meteor.crm.domain.AttributeValue;

public enum ResignStatus {
    ATTRIBUTE("Resign_List"),
    BEK_ALTINDA("Resign_List_Bek_Alt"),
    BEK_GIBI("Resign_List_Bek_Gibi"),
    BEK_USTUNDE("Resign_List_Bek_Ust"),
    MUKEMMEL("Resign_List_Bek_Muk");

    final String id;

    ResignStatus(String id) {
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
