package tr.com.meteor.crm.utils.attributevalues;

import tr.com.meteor.crm.domain.AttributeValue;

public enum SurveyQuestionType {
    ATTRIBUTE("AnS_Tip"),
    VARSAYILAN("AnS_Tip_Varsayılan");

    final String id;

    SurveyQuestionType(String id) {
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
