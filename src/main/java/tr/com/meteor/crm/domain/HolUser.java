package tr.com.meteor.crm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Formula;
import tr.com.meteor.crm.utils.idgenerator.IdType;
import tr.com.meteor.crm.utils.metadata.EntityMetadataAnn;
import tr.com.meteor.crm.utils.metadata.FieldMetadataAnn;
import tr.com.meteor.crm.utils.validate.AttributeValueValidate;

import javax.persistence.*;
import java.time.Instant;
import java.util.Locale;
import java.util.UUID;

@Entity
@IdType(idType = IdType.IdTypeEnum.UUID)
@EntityMetadataAnn(
    apiName = "holusers", displayField = "instanceName", title = "İzin Detayı", pluralTitle = "İzin Detayları",
    masterPath="user.id"
)
@Table(indexes = {@Index(columnList = "search")})
public class HolUser extends IdNameAuditingEntity<UUID> {
    @FieldMetadataAnn(title = "Detay Başlığı", search = true, readOnly = false, priority = 0, active = false)
    private String name;
    @ManyToOne
    @FieldMetadataAnn(title = "Talebi Oluşturan", display = false, priority = 0, filterable = true)
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private User user;

    @FieldMetadataAnn(title = "Doğum Tarihi", display = true, priority = 10, filterable = true)
    private Instant dogTar;
    @FieldMetadataAnn(title = "İşe Başlangıç Tarihi", display = true, priority = 20, filterable = true)
    private Instant isBas;
    @FieldMetadataAnn(title = "Yıllık İzin Eklenme Tarihi", display = true, priority = 30, filterable = true)
    private Instant yilHak;

    @FieldMetadataAnn(title = "Toplam Hakedilen İzin Gün Sayısı", display = true, priority = 70, filterable = true)
    private Double topHak = 0.00;

    @FieldMetadataAnn(title = "Toplam Kullanılan İzin Gün Sayısı", display = true, priority = 70, filterable = true)
    private Double topKul = 0.00;

    @FieldMetadataAnn(title = "Devreden İzin Gün Sayısı", display = true, priority = 30, filterable = true)
    private Double yilDevir = 0.00;

    @FieldMetadataAnn(title = "Yıllık Hakedilen İzin Gün Sayısı", display = true, priority = 30, filterable = true)
    private Double yilGun = 0.00;

    @FieldMetadataAnn(title = "Toplam Kullanılabilir İzin Gün Sayısı", display = true, priority = 70, filterable = true)
    private Double topYil = 0.00;
    @FieldMetadataAnn(title = "Dönem içi Kullanılan İzin Gün Sayısı", display = true, priority = 50, filterable = true)
    private Double kulYil = 0.00;
    @FieldMetadataAnn(title = "Kalan Yıllık İzin Gün Sayısı", display = true, priority = 60, filterable = true)
    private Double kalYil = 0.00;
    @FieldMetadataAnn(title = "Kullanılan Mazeret İzni", display = true, priority = 80, filterable = true)
    private Double kulMaz = 0.00;

    @FieldMetadataAnn(title = "Toplam Kullanılan Mazeret İzni", display = true, priority = 80, filterable = true)
    private Double topKulMaz = 0.00;
    @FieldMetadataAnn(title = "Kalan Mazeret İzni", display = true, priority = 90, filterable = true)
    private Double kalMaz = 0.00;

    @FieldMetadataAnn(title = "Kullanılan Babalık İzni", display = true, priority = 100, filterable = true)
    private Double kulBaba = 0.00;

   /* @FieldMetadataAnn(title = "Babalık İzin Hakkı", active = false)
    private Boolean hakBaba = true;*/
    @FieldMetadataAnn(title = "Kullanılan Ölüm İzni", display = true, priority = 110, filterable = true)
    private Double kulOlum = 0.00;

   /* @FieldMetadataAnn(title = "Ölüm İzin Hakkı", active = false)
    private Boolean hakOlum = true;*/
    @FieldMetadataAnn(title = "Kullanılan Evlilik İzni", display = true, priority = 120, filterable = true)
    private Double kulEvl = 0.00;

   /* @FieldMetadataAnn(title = "Evlilik İzin Hakkı", active = false)
    private Boolean hakEvl = true;*/
    @FieldMetadataAnn(title = "Kullanılan Doğum İzni", display = true, priority = 130, filterable = true)
    private Double kulDog = 0.00;

    /*@FieldMetadataAnn(title = "Doğum İzin Hakkı", active = false)
    private Boolean hakDog = true;*/

    @FieldMetadataAnn(title = "Kullanılan Rapor", display = true, priority = 130, filterable = true)
    private Double kulRap = 0.00;

    @FieldMetadataAnn(title = "Kullanılan İdari İzin", display = true, priority = 130, filterable = true)
    private Double kulIdr = 0.00;

    @FieldMetadataAnn(title = "Kullanılan İdari İzin", display = true, priority = 130, filterable = true)
    private Double kulUcr = 0.00;
    @Formula("name")
    @FieldMetadataAnn(title = "Başlık", active = false)
    private String instanceName;
    @Override
    public String getInstanceName() {
        return instanceName;
    }
    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void updateSearchPre() {
        super.updateSearchPre();

        if (StringUtils.isNotBlank(name)) {
            search += " " + name;
        }
        search = search.toLowerCase(new Locale("tr", "TR"));
    }

    public Instant getDogTar() {
        return dogTar;
    }

    public void setDogTar(Instant dogTar) {
        this.dogTar = dogTar;
    }

    public Instant getIsBas() {
        return isBas;
    }

    public void setIsBas(Instant isBas) {
        this.isBas = isBas;
    }

    public Instant getYilHak() {
        return yilHak;
    }

    public void setYilHak(Instant yilHak) {
        this.yilHak = yilHak;
    }

    public Double getKulYil() {
        return kulYil;
    }

    public void setKulYil(Double kulYil) {
        this.kulYil = kulYil;
    }

    public Double getKalYil() {
        return kalYil;
    }

    public void setKalYil(Double kalYil) {
        this.kalYil = kalYil;
    }

    public Double getTopYil() {
        return topYil;
    }

    public void setTopYil(Double topYil) {
        this.topYil = topYil;
    }

    public Double getKulMaz() {
        return kulMaz;
    }

    public void setKulMaz(Double kulMaz) {
        this.kulMaz = kulMaz;
    }

    public Double getKalMaz() {
        return kalMaz;
    }

    public void setKalMaz(Double kalMaz) {
        this.kalMaz = kalMaz;
    }

    public Double getKulBaba() {
        return kulBaba;
    }

    public void setKulBaba(Double kulBaba) {
        this.kulBaba = kulBaba;
    }

    public Double getKulOlum() {
        return kulOlum;
    }

    public void setKulOlum(Double kulOlum) {
        this.kulOlum = kulOlum;
    }

    public Double getKulEvl() {
        return kulEvl;
    }

    public void setKulEvl(Double kulEvl) {
        this.kulEvl = kulEvl;
    }

    public Double getKulDog() {
        return kulDog;
    }

    public void setKulDog(Double kulDog) {
        this.kulDog = kulDog;
    }

    public Double getKulRap() {
        return kulRap;
    }

    public void setKulRap(Double kulRap) {
        this.kulRap = kulRap;
    }

    public Double getKulIdr() {
        return kulIdr;
    }

    public void setKulIdr(Double kulIdr) {
        this.kulIdr = kulIdr;
    }

    /*public Boolean getHakBaba() {
        return hakBaba;
    }

    public void setHakBaba(Boolean hakBaba) {
        this.hakBaba = hakBaba;
    }

    public Boolean getHakOlum() {
        return hakOlum;
    }

    public void setHakOlum(Boolean hakOlum) {
        this.hakOlum = hakOlum;
    }

    public Boolean getHakEvl() {
        return hakEvl;
    }

    public void setHakEvl(Boolean hakEvl) {
        this.hakEvl = hakEvl;
    }

    public Boolean getHakDog() {
        return hakDog;
    }

    public void setHakDog(Boolean hakDog) {
        this.hakDog = hakDog;
    }*/

    public Double getKulUcr() {
        return kulUcr;
    }

    public void setKulUcr(Double kulUcr) {
        this.kulUcr = kulUcr;
    }

    public Double getYilGun() {
        return yilGun;
    }

    public void setYilGun(Double yilGun) {
        this.yilGun = yilGun;
    }

    public Double getYilDevir() {
        return yilDevir;
    }

    public void setYilDevir(Double yilDevir) {
        this.yilDevir = yilDevir;
    }

    public Double getTopKulMaz() {
        return topKulMaz;
    }

    public void setTopKulMaz(Double topKulMaz) {
        this.topKulMaz = topKulMaz;
    }

    public Double getTopHak() {
        return topHak;
    }

    public void setTopHak(Double topHak) {
        this.topHak = topHak;
    }

    public Double getTopKul() {
        return topKul;
    }

    public void setTopKul(Double topKul) {
        this.topKul = topKul;
    }
}
