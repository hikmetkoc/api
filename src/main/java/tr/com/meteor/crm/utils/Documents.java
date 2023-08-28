package tr.com.meteor.crm.utils;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ByteArrayResource;

import java.io.IOException;

public enum Documents {
    OTOBIL_BILGILENDIRME("Otobil Bilgilendirme", "OtobilBilgilendirme.pdf"),

    IK_REPORT("İnsan Kaynakları Raporu", "IKReport.pdf"),

    OTOBIL_SOZLESME("Otobil Sözleşme", "OtobilSozlesme.pdf"),
    E_ARSIV_TALEP_DILEKCESI("E-Arşiv Talep Dilekçesi", "E-ArsivTalepDilekcesi.docx",
        "/templates/mail/attachments/e_arsiv_uygulamasi_talep_dilekcesi.docx"),
    FIRMA_BILGILERI_FORMU("Firma Bilgileri Formu", "FirmaBilgileriFormu.doc",
        "/templates/mail/attachments/firma_bilgileri_formu.doc"),
    OTOBIL_ARAC_KAYIT_FORMU("Otobil Araç Kayıt Formu", "OtobilAracKayitFormu.xlsx",
        "/templates/mail/attachments/otobil_arac_kayit_formu.xlsx"),

    OTOFILO_SOZLESME("Otofilo Sözleşme", "OtofiloSozlesme.pdf"),
    OTOFILO_KAYIT_FORMU("Otofilo Kayıt Formu", "OtofiloKayıtFormu.xls",
        "/templates/mail/attachments/otofilo_kayit_formu.xls"),

    METEORCARD_SOZLESME("Yakıt Kart Sözleşme", "YakitKartSozlesme.pdf"),
    METEORCARD_AKTIVASYON_TALEP_FORMU("Yakıt Kart Aktivasyon Talep Formu", "YakitKartAktivasyonTalepFormu.xlsx",
        "/templates/mail/attachments/meteorcard_aktivasyon_talep_formu.xlsx"),
    METEORCARD_TALEP_FORMU("Yakıt Kart Talep Formu", "YakitKartTalepFormu.xlsx",
        "/templates/mail/attachments/meteorcard_talep_formu.xlsx");

    String label;
    String fileName;
    String fileLocation;

    Documents(String label, String fileName, String fileLocation) {
        this.label = label;
        this.fileLocation = fileLocation;
        this.fileName = fileName;
    }

    Documents(String label, String fileName) {
        this.label = label;
        this.fileName = fileName;
    }

    public String getLabel() {
        return label;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public String getFileName() {
        return fileName;
    }

    public ByteArrayResource getFile() throws IOException {
        return new ByteArrayResource(IOUtils.toByteArray(this.getClass().getResourceAsStream(fileLocation)));
    }
}
