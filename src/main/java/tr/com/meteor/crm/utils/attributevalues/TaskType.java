package tr.com.meteor.crm.utils.attributevalues;

import tr.com.meteor.crm.domain.AttributeValue;
public enum TaskType {

    KONULAR("Konular"),

    BT_ISSURECLERI("Konu_Bil_Is"),
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
        BIRIM_IT("Birim_IT"),
        BIRIM_Imim("Birim_Imim"),
        BIRIM_IK("Birim_IK"),
        BIRIM_Muh("Birim_Muh"),
        BIRIM_Risk("Birim_Risk"),
        BIRIM_Ins("Birim_Ins"),
        BIRIM_Fin("Birim_Fin"),
        BIRIM_Kasa("Birim_Kasa"),
        BIRIM_IcDenetim("Birim_IcDenetim"),
        BIRIM_Satis("Birim_Satis"),
        BIRIM_Web("Birim_Web"),
        BIRIM_Paz("Birim_Paz"),
        BIRIM_Loher("Birim_Loher"),
        BIRIM_Avelice("Birim_Avelice"),
        BIRIM_Satin("Birim_Satin"),
        BIRIM_Kafe("Birim_Kafe"),

        BIRIM_Gnl("Birim_Gnl_Mud"),

        BIRIM_Yonetim("Birim_Yonetim");

        //BIRIM_SOT("Birim_SOT");
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
    public enum TaskImportance {
        ONEM_DERECELERI("Önem Derecesi"),
        DUSUK("Düşük"),
        ORTA("Orta"),
        YUKSEK("Yüksek"),
        COKYUKSEK("Çok Yüksek");

        final String id;

        TaskImportance(String id) {
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

    public enum Sirket {
        METEOR("Sirket_Meteor");

        final String id;

        Sirket(String id) {
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
