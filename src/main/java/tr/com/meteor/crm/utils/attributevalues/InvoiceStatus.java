package tr.com.meteor.crm.utils.attributevalues;

import tr.com.meteor.crm.domain.AttributeValue;

public enum InvoiceStatus {
    ATTRIBUTE("Fatura_Durumlari"),
    SAHIPSIZ("Fatura_Durumlari_Sahipsiz"),
    ATANDI("Fatura_Durumlari_Atandi"),
    SORUMLUBULUNAMADI("Fatura_Durumlari_Sor"),
    MUHASEBE("Fatura_Durumlari_Muhasebe"),

    MUKERRER("Fatura_Durumlari_Mukerrer"),
    IPTAL("Fatura_Durumlari_Iptal"),
    DON("Fatura_Durumlari_Donus");
    final String id;

    InvoiceStatus(String id) {
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
