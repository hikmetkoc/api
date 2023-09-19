package tr.com.meteor.crm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Formula;
import tr.com.meteor.crm.utils.idgenerator.IdType;
import tr.com.meteor.crm.utils.metadata.EntityMetadataAnn;
import tr.com.meteor.crm.utils.metadata.FieldMetadataAnn;
import tr.com.meteor.crm.utils.request.SortOrder;
import tr.com.meteor.crm.utils.validate.AttributeValueValidate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Entity
@IdType(idType = IdType.IdTypeEnum.UUID)
@EntityMetadataAnn(
    apiName = "resigns", displayField = "instanceName", sortBy = "id", sortOrder = SortOrder.DESC,
    title = "İşten Çıkış Anketi", pluralTitle = "İşten Çıkış Anketleri",
    ownerPath = "owner.id"
)
@Table(indexes = {@Index(columnList = "search")})
public class Resign extends IdNameAuditingEntity<UUID> {
    @FieldMetadataAnn(title = "İşlem Yapan", display = true, priority = 0, filterable = true)
    @ManyToOne
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User owner;
    @ManyToOne
    @FieldMetadataAnn(title = "Amir", priority = 1)
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User assigner;

    @FieldMetadataAnn(title = "İşin Detaylı İletilmesi", display = true, priority = 2)
    private Boolean isIletildiMi = false;

    @FieldMetadataAnn(title = "Özlük Hakları İletilmesi", display = true, priority = 3)
    private Boolean ozlukHaklari = false;

    @FieldMetadataAnn(title = "İleride Çalışmayı Düşünme", display = true, priority = 4)
    private Boolean ilerideCalismak = false;

    @FieldMetadataAnn(title = "Eşit ve Adil Davranma", display = true, priority = 5)
    private Boolean esitAdil = false;

    @FieldMetadataAnn(title = "Şirket Önerme", display = true, priority = 5)
    private Boolean sirketOneri = false;

    @FieldMetadataAnn(title = "Ayrılma Nedeni", display = true, priority = 6)
    private String ayrilmaNedeni;

    @FieldMetadataAnn(title = "Öneri/Tavsiye", display = true, priority = 7)
    private String oneriTavsiye;

    @ManyToOne
    @JoinColumn(name = "sorumluluk_id")
    @FieldMetadataAnn(title = "Sorumluluklar", priority = 8)
    @AttributeValueValidate(attributeId = "Resign_List")
    private AttributeValue sorumluluk;

    @FieldMetadataAnn(title = "Sorumluluklar Yorumu", display = true, priority = 9)
    private String sorumlulukYorumu;

    @ManyToOne
    @JoinColumn(name = "calisma_saat_id")
    @FieldMetadataAnn(title = "Çalışma Saatleri", priority = 10)
    @AttributeValueValidate(attributeId = "Resign_List")
    private AttributeValue calismaSaat;

    @FieldMetadataAnn(title = "Çalışma Saatleri Yorumu", display = true, priority = 11)
    private String calismaSaatYorumu;

    @ManyToOne
    @JoinColumn(name = "calisma_ortam_id")
    @FieldMetadataAnn(title = "Çalışma Ortamı", priority = 12)
    @AttributeValueValidate(attributeId = "Resign_List")
    private AttributeValue calismaOrtam;

    @FieldMetadataAnn(title = "Çalışma Ortamı Yorumu", display = true, priority = 13)
    private String calismaOrtamYorumu;

    @ManyToOne
    @JoinColumn(name = "odeme_id")
    @FieldMetadataAnn(title = "Ödeme Şartları", priority = 14)
    @AttributeValueValidate(attributeId = "Resign_List")
    private AttributeValue odeme;

    @FieldMetadataAnn(title = "Ödeme Şartları Yorumu", display = true, priority = 15)
    private String odemeYorumu;

    @ManyToOne
    @JoinColumn(name = "takdir_id")
    @FieldMetadataAnn(title = "Takdir", priority = 16)
    @AttributeValueValidate(attributeId = "Resign_List")
    private AttributeValue takdir;

    @FieldMetadataAnn(title = "Takdir Yorumu", display = true, priority = 17)
    private String takdirYorumu;

    @ManyToOne
    @JoinColumn(name = "gelistirme_id")
    @FieldMetadataAnn(title = "Kendini Geliştirme Olanağı", priority = 18)
    @AttributeValueValidate(attributeId = "Resign_List")
    private AttributeValue gelistirme;

    @FieldMetadataAnn(title = "Kendini Geliştirme Yorumu", display = true, priority = 19)
    private String gelistirmeYorumu;

    @ManyToOne
    @JoinColumn(name = "iliski_id")
    @FieldMetadataAnn(title = "Yönetici ile İlişki", priority = 20)
    @AttributeValueValidate(attributeId = "Resign_List")
    private AttributeValue iliski;

    @FieldMetadataAnn(title = "Yonetici ile İlişki Yorumu", display = true, priority = 21)
    private String iliskiYorumu;

    @ManyToOne
    @JoinColumn(name = "kariyer_id")
    @FieldMetadataAnn(title = "Kariyer İmkanı", priority = 22)
    @AttributeValueValidate(attributeId = "Resign_List")
    private AttributeValue kariyer;

    @FieldMetadataAnn(title = "Kariyer İmkanı Yorumu", display = true, priority = 23)
    private String kariyerYorumu;

    @ManyToOne
    @JoinColumn(name = "iletisim_id")
    @FieldMetadataAnn(title = "Şirket içi İletişim", priority = 24)
    @AttributeValueValidate(attributeId = "Resign_List")
    private AttributeValue iletisim;

    @FieldMetadataAnn(title = "Şirket içi İletişim Yorumu", display = true, priority = 25)
    private String iletisimYorumu;

    @FieldMetadataAnn(title = "Konu", search = true, readOnly = true, active = false, display = false)
    private String subject;
    @Formula("subject")
    @FieldMetadataAnn(readOnly = true, title = "Başlık", active = false)
    private String instanceName;
    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    @Override
    public String getInstanceName() {
        return instanceName;
    }

    @PreUpdate
    public void preUpdate() {
        generateSubject();
    }

    @PrePersist
    public void postUpdate() {
        generateSubject();
    }

    private void generateSubject() {
        List<String> parts = new ArrayList<>();
    }

    @Override
    public void updateSearchPre() {
        super.updateSearchPre();

        if (owner != null) {
            search += " " + owner.getFullName();
        }

        if (StringUtils.isNotBlank(instanceName)) {
            search += " " + owner;
        }

        search = search.toLowerCase(new Locale("tr", "TR"));
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public User getAssigner() {
        return assigner;
    }

    public void setAssigner(User assigner) {
        this.assigner = assigner;
    }
    public Boolean getIletildiMi() {
        return isIletildiMi;
    }

    public void setIletildiMi(Boolean iletildiMi) {
        isIletildiMi = iletildiMi;
    }

    public Boolean getOzlukHaklari() {
        return ozlukHaklari;
    }

    public void setOzlukHaklari(Boolean ozlukHaklari) {
        this.ozlukHaklari = ozlukHaklari;
    }

    public Boolean getIlerideCalismak() {
        return ilerideCalismak;
    }

    public void setIlerideCalismak(Boolean ilerideCalismak) {
        this.ilerideCalismak = ilerideCalismak;
    }

    public Boolean getEsitAdil() {
        return esitAdil;
    }

    public void setEsitAdil(Boolean esitAdil) {
        this.esitAdil = esitAdil;
    }

    public String getAyrilmaNedeni() {
        return ayrilmaNedeni;
    }

    public void setAyrilmaNedeni(String ayrilmaNedeni) {
        this.ayrilmaNedeni = ayrilmaNedeni;
    }

    public String getOneriTavsiye() {
        return oneriTavsiye;
    }

    public void setOneriTavsiye(String oneriTavsiye) {
        this.oneriTavsiye = oneriTavsiye;
    }

    public AttributeValue getSorumluluk() {
        return sorumluluk;
    }

    public void setSorumluluk(AttributeValue sorumluluk) {
        this.sorumluluk = sorumluluk;
    }

    public String getSorumlulukYorumu() {
        return sorumlulukYorumu;
    }

    public void setSorumlulukYorumu(String sorumlulukYorumu) {
        this.sorumlulukYorumu = sorumlulukYorumu;
    }

    public AttributeValue getCalismaSaat() {
        return calismaSaat;
    }

    public void setCalismaSaat(AttributeValue calismaSaat) {
        this.calismaSaat = calismaSaat;
    }

    public String getCalismaSaatYorumu() {
        return calismaSaatYorumu;
    }

    public void setCalismaSaatYorumu(String calismaSaatYorumu) {
        this.calismaSaatYorumu = calismaSaatYorumu;
    }

    public AttributeValue getCalismaOrtam() {
        return calismaOrtam;
    }

    public void setCalismaOrtam(AttributeValue calismaOrtam) {
        this.calismaOrtam = calismaOrtam;
    }

    public String getCalismaOrtamYorumu() {
        return calismaOrtamYorumu;
    }

    public void setCalismaOrtamYorumu(String calismaOrtamYorumu) {
        this.calismaOrtamYorumu = calismaOrtamYorumu;
    }

    public AttributeValue getOdeme() {
        return odeme;
    }

    public void setOdeme(AttributeValue odeme) {
        this.odeme = odeme;
    }

    public String getOdemeYorumu() {
        return odemeYorumu;
    }

    public void setOdemeYorumu(String odemeYorumu) {
        this.odemeYorumu = odemeYorumu;
    }

    public AttributeValue getTakdir() {
        return takdir;
    }

    public void setTakdir(AttributeValue takdir) {
        this.takdir = takdir;
    }

    public String getTakdirYorumu() {
        return takdirYorumu;
    }

    public void setTakdirYorumu(String takdirYorumu) {
        this.takdirYorumu = takdirYorumu;
    }

    public AttributeValue getGelistirme() {
        return gelistirme;
    }

    public void setGelistirme(AttributeValue gelistirme) {
        this.gelistirme = gelistirme;
    }

    public String getGelistirmeYorumu() {
        return gelistirmeYorumu;
    }

    public void setGelistirmeYorumu(String gelistirmeYorumu) {
        this.gelistirmeYorumu = gelistirmeYorumu;
    }

    public AttributeValue getIliski() {
        return iliski;
    }

    public void setIliski(AttributeValue iliski) {
        this.iliski = iliski;
    }

    public String getIliskiYorumu() {
        return iliskiYorumu;
    }

    public void setIliskiYorumu(String iliskiYorumu) {
        this.iliskiYorumu = iliskiYorumu;
    }

    public AttributeValue getKariyer() {
        return kariyer;
    }

    public void setKariyer(AttributeValue kariyer) {
        this.kariyer = kariyer;
    }

    public String getKariyerYorumu() {
        return kariyerYorumu;
    }

    public void setKariyerYorumu(String kariyerYorumu) {
        this.kariyerYorumu = kariyerYorumu;
    }

    public AttributeValue getIletisim() {
        return iletisim;
    }

    public void setIletisim(AttributeValue iletisim) {
        this.iletisim = iletisim;
    }

    public String getIletisimYorumu() {
        return iletisimYorumu;
    }

    public void setIletisimYorumu(String iletisimYorumu) {
        this.iletisimYorumu = iletisimYorumu;
    }

    public Boolean getSirketOneri() {
        return sirketOneri;
    }
}
