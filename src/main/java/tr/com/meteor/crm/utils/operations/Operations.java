package tr.com.meteor.crm.utils.operations;

public enum Operations {
    DEFAULT("Default"),
    RAPOR_AlICISI("Rapor_Alıcısı"),
    DOSYA_EXPORT("Dosya_Export"),
    TEKLIF_ONAYLAYABILME("Teklif_Onaylayabilme"),
    HEDEF_RAPORU_ALICISI("Hedef_Raporu_Alıcısı"),
    BACK_OFFICE("Back_Office");

    private String id;

    Operations(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
