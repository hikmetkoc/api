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
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Entity
@IdType(idType = IdType.IdTypeEnum.UUID)
@EntityMetadataAnn(
    apiName = "ikfiles", displayField = "instanceName", sortBy = "id", sortOrder = SortOrder.DESC,
    title = "Dosya", pluralTitle = "Dosyalar",
    ownerPath = "owner.id"
)
@Table(indexes = {@Index(columnList = "search")})
public class Ikfile extends IdNameAuditingEntity<UUID> {

    @FieldMetadataAnn(title = "İşlem Yapan", display = true, priority = 1, filterable = true)
    @ManyToOne
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User owner;

    @ManyToOne
    @FieldMetadataAnn(title = "Kullanıcı", priority = 0)
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User user;

    @FieldMetadataAnn(title = "Nüfus Cüzdanı Fotokopisi", display = true, priority = 2)
    private Boolean nufus = false;

    @FieldMetadataAnn(title = "Nüfus Kayıt Örneği", display = true, priority = 3)
    private Boolean nufuskayit = false;

    @FieldMetadataAnn(title = "İkametgah Belgesi", display = true, priority = 4)
    private Boolean ikametgah = false;

    @FieldMetadataAnn(title = "Sağlık Raporu", display = true, priority = 5)
    private Boolean saglik = false;

    @FieldMetadataAnn(title = "Diploma Fotokopisi", display = true, priority = 6)
    private Boolean diploma = false;

    @FieldMetadataAnn(title = "Adli Sicil Kaydı", display = true, priority = 7)
    private Boolean sicil = false;

    @FieldMetadataAnn(title = "2 Adet Vesikalık Fotoğraf", display = true, priority = 8)
    private Boolean vesikalik = false;

    @FieldMetadataAnn(title = "Aile Durumunu Bildirir Belge", active = false, priority = 9)
    private Boolean ailebelge = false;

    @FieldMetadataAnn(title = "Askerlik Durum Belgesi", display = true, priority = 10)
    private Boolean askerlik = false;

    @FieldMetadataAnn(title = "İş Sözleşmesi / Hizmet Sözleşmesi", display = true, priority = 11)
    private Boolean issozlesme = false;

    @FieldMetadataAnn(title = "SGK İşe Giriş Bildirgesi", display = true, priority = 12)
    private Boolean sgk = false;

    @FieldMetadataAnn(title = "Fazla Mesai ve Fazla Çalışma İçin İşçi Onay Belgesi", display = true, priority = 13)
    private Boolean fazlamesai = false;

    @FieldMetadataAnn(title = "Aydınlatma ve Açık Rıza Onay Formu", display = true, priority = 14)
    private Boolean kvk = false;

    @FieldMetadataAnn(title = "İşçi Ehliyet Fotokopisi", display = true, priority = 15)
    private Boolean ehliyet = false;

    @FieldMetadataAnn(title = "Araç Zimmet Formu", display = true, priority = 16)
    private Boolean zimmetliarac = false;

    @FieldMetadataAnn(title = "Gizlilik Sözleşmesi", display = true, priority = 17)
    private Boolean gizlilik = false;

    @FieldMetadataAnn(title = "İş Tanımı", display = true, priority = 18)
    private Boolean istanimi = false;

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


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getNufus() {
        return nufus;
    }

    public void setNufus(Boolean nufus) {
        this.nufus = nufus;
    }

    public Boolean getNufuskayit() {
        return nufuskayit;
    }

    public void setNufuskayit(Boolean nufuskayit) {
        this.nufuskayit = nufuskayit;
    }

    public Boolean getIkametgah() {
        return ikametgah;
    }

    public void setIkametgah(Boolean ikametgah) {
        this.ikametgah = ikametgah;
    }

    public Boolean getSaglik() {
        return saglik;
    }

    public void setSaglik(Boolean saglik) {
        this.saglik = saglik;
    }

    public Boolean getDiploma() {
        return diploma;
    }

    public void setDiploma(Boolean diploma) {
        this.diploma = diploma;
    }

    public Boolean getSicil() {
        return sicil;
    }

    public void setSicil(Boolean sicil) {
        this.sicil = sicil;
    }

    public Boolean getVesikalik() {
        return vesikalik;
    }

    public void setVesikalik(Boolean vesikalik) {
        this.vesikalik = vesikalik;
    }

    public Boolean getAilebelge() {
        return ailebelge;
    }

    public void setAilebelge(Boolean ailebelge) {
        this.ailebelge = ailebelge;
    }

    public Boolean getAskerlik() {
        return askerlik;
    }

    public void setAskerlik(Boolean askerlik) {
        this.askerlik = askerlik;
    }

    public Boolean getIssozlesme() {
        return issozlesme;
    }

    public void setIssozlesme(Boolean issozlesme) {
        this.issozlesme = issozlesme;
    }

    public Boolean getSgk() {
        return sgk;
    }

    public void setSgk(Boolean sgk) {
        this.sgk = sgk;
    }

    public Boolean getFazlamesai() {
        return fazlamesai;
    }

    public void setFazlamesai(Boolean fazlamesai) {
        this.fazlamesai = fazlamesai;
    }

    public Boolean getKvk() {
        return kvk;
    }

    public void setKvk(Boolean kvk) {
        this.kvk = kvk;
    }

    public Boolean getEhliyet() {
        return ehliyet;
    }

    public void setEhliyet(Boolean ehliyet) {
        this.ehliyet = ehliyet;
    }

    public Boolean getZimmetliarac() {
        return zimmetliarac;
    }

    public void setZimmetliarac(Boolean zimmetliarac) {
        this.zimmetliarac = zimmetliarac;
    }

    public Boolean getGizlilik() {
        return gizlilik;
    }

    public void setGizlilik(Boolean gizlilik) {
        this.gizlilik = gizlilik;
    }

    public Boolean getIstanimi() {
        return istanimi;
    }

    public void setIstanimi(Boolean istanimi) {
        this.istanimi = istanimi;
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
/*
        if (type != null) {
            parts.add(type.getLabel());
        }
*/
        /*if (checkInTime != null) {
            parts.add(DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone(ZoneId.systemDefault()).format(checkInTime));
        }

        if (customer != null) {
            parts.add(customer.getName());
        }*/

        //this.subject = String.join(" - ", parts);
    }

    @Override
    public void updateSearchPre() {
        super.updateSearchPre();

        if (user != null) {
            search += " " + user.getFullName();
        }

        if (StringUtils.isNotBlank(instanceName)) {
            search += " " + user;
        }

        search = search.toLowerCase(new Locale("tr", "TR"));
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
