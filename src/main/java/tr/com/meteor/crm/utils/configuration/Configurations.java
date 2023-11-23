package tr.com.meteor.crm.utils.configuration;

public enum Configurations {
    DOSYA_YUKLEME_SINIRI("Dosya Yükleme Sınırı"),
    BENZIN_MAKSIMUM_INDIRIM("Benzin Maksimum İndirim"),
    MOTORIN_MAKSIMUM_INDIRIM("Motorin Maksimum İndirim"),
    CHECKIN_CHECKOUT_MAKSIMUM_MESAFE("Check-in Check-Out Maksimum Mesafe"),
    OPET_SIRADAKI_GUN("Opet Sıradaki Gün"),
    SOZLESME_CC("Sözleşme CC"),
    BACKEND_VERSION("Backend Version"),
    KULLANMA_KILAVUZU_YOLU("Kullanma Kılavuzu Yolu"),
    FIREBASE_AUTHORIZATION_KEY("Firebase Authorization Key"),

    INVOICELIST_START_DATE("InvoiceList Start Date"),

    GERIDENFATURACEKME("InvoiceList Last Days");

    String id;

    Configurations(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
