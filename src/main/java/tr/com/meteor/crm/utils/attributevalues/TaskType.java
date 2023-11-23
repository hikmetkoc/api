package tr.com.meteor.crm.utils.attributevalues;

import tr.com.meteor.crm.domain.AttributeValue;
public enum TaskType {

    KONULAR("Konular"),

    BT_ISSURECLERI("Konular_Bil_Is"),
    KOLAY_AJANDA("Gör_Tip_Kolay_Ajanda");

    final String id;

    TaskType(String id) {
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

    public enum TaskBirim {
        BIRIMLER("Birimler"),
        BIRIM_IT("Birimler_IT"),
        BIRIM_Imim("Birimler_Imim"),
        BIRIM_IK("Birimler_IK"),
        BIRIM_Muh("Birimler_Muh"),
        BIRIM_Risk("Birimler_Risk"),
        BIRIM_Ins("Birimler_Ins"),
        BIRIM_Fin("Birimler_Fin"),
        BIRIM_Kasa("Birimler_Kasa"),
        BIRIM_IcDenetim("Birimler_IcDenetim"),
        BIRIM_Satis("Birimler_Satis"),
        BIRIM_Web("Birimler_Web"),
        BIRIM_Paz("Birimler_Paz"),
        BIRIM_Loher("Birimler_Loher"),
        BIRIM_Avelice("Birimler_Avelice"),

        BIRIM_SanayiSatis("Birimler_SanSat"),
        BIRIM_Satin("Birimler_Satin"),

        BIRIM_Ter("Birimler_Ter"),
        BIRIM_Kafe("Birimler_Kafe"),

        BIRIM_Gnl("Birimler_Gnl_Mud"),

        BIRIM_Yonetim("Birimler_Yonetim");

        final String id;

        TaskBirim(String id) {
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
}
