package tr.com.meteor.crm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Formula;
import tr.com.meteor.crm.utils.idgenerator.IdType;
import tr.com.meteor.crm.utils.metadata.EntityMetadataAnn;
import tr.com.meteor.crm.utils.metadata.FieldMetadataAnn;
import tr.com.meteor.crm.utils.request.SortOrder;
import tr.com.meteor.crm.utils.validate.AttributeValueValidate;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Entity
@IdType(idType = IdType.IdTypeEnum.UUID)
@EntityMetadataAnn(
    apiName = "sapsoaps", displayField = "instanceName", sortBy = "id", sortOrder = SortOrder.DESC,
    title = "SOAP", pluralTitle = "SOAP"
)
@Table(indexes = {@Index(columnList = "search")})
public class SapSoap extends IdNameAuditingEntity<UUID> {

    @FieldMetadataAnn(title = "Sıra")
    private String dbname = "";

    @FieldMetadataAnn(title = "Sıra")
    private String sira = "";

    @FieldMetadataAnn(title = "CardCode")
    private String cardcode = "";

    @FieldMetadataAnn(title = "CardName")
    private String cardname = "";

    @FieldMetadataAnn(title = "VKN")
    private String vkn = "";

    @FieldMetadataAnn(title = "FaturaNo")
    private String faturano = "";

    @FieldMetadataAnn(title = "FaturaTarihi")
    private String faturatarihi = "";

    @FieldMetadataAnn(title = "GonderimTarihi")
    private String gonderimtarihi = "";

    @FieldMetadataAnn(title = "ToplamTutar")
    private String toplamtutar = "";

    @FieldMetadataAnn(title = "ParaBirimi")
    private String parabirimi = "";

    @FieldMetadataAnn(title = "Aciklama")
    private String aciklama = "";

    @FieldMetadataAnn(title = "Web")
    private String web = "";

    @FieldMetadataAnn(title = "Telefon")
    private String telefon = "";

    @FieldMetadataAnn(title = "Mail")
    private String mail = "";

    @FieldMetadataAnn(title = "Adres")
    private String adres = "";

    @FieldMetadataAnn(title = "Daire")
    private String daire = "";

    @FieldMetadataAnn(title = "MIlce")
    private String milce = "";

    @FieldMetadataAnn(title = "MSehir")
    private String msehir = "";

    @FieldMetadataAnn(title = "MUlke")
    private String mulke = "";

    @FieldMetadataAnn(title = "FIL")
    private String fil = "";

    @FieldMetadataAnn(title = "FILCE")
    private String filce = "";

    @FieldMetadataAnn(title = "FDurum")
    private String fdurum = "";

    @FieldMetadataAnn(title = "FSenaryo")
    private String fsenaryo = "";

    @FieldMetadataAnn(title = "FTipi")
    private String ftipi = "";

    @FieldMetadataAnn(title = "FPDF")
    private String fpdf = "";

    @FieldMetadataAnn(title = "ETTN")
    private String ettn = "";

    @Formula("cardname")
    @FieldMetadataAnn(readOnly = true, title = "Başlık", active = false)
    private String instanceName;



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
        search = search.toLowerCase(new Locale("tr", "TR"));
    }

    public String getSira() {
        return sira;
    }

    public void setSira(String sira) {
        this.sira = sira;
    }

    public String getCardcode() {
        return cardcode;
    }

    public void setCardcode(String cardcode) {
        this.cardcode = cardcode;
    }

    public String getCardname() {
        return cardname;
    }

    public void setCardname(String cardname) {
        this.cardname = cardname;
    }

    public String getVkn() {
        return vkn;
    }

    public void setVkn(String vkn) {
        this.vkn = vkn;
    }

    public String getFaturano() {
        return faturano;
    }

    public void setFaturano(String faturano) {
        this.faturano = faturano;
    }

    public String getFaturatarihi() {
        return faturatarihi;
    }

    public void setFaturatarihi(String faturatarihi) {
        this.faturatarihi = faturatarihi;
    }

    public String getGonderimtarihi() {
        return gonderimtarihi;
    }

    public void setGonderimtarihi(String gonderimtarihi) {
        this.gonderimtarihi = gonderimtarihi;
    }

    public String getToplamtutar() {
        return toplamtutar;
    }

    public void setToplamtutar(String toplamtutar) {
        this.toplamtutar = toplamtutar;
    }

    public String getParabirimi() {
        return parabirimi;
    }

    public void setParabirimi(String parabirimi) {
        this.parabirimi = parabirimi;
    }

    public String getAciklama() {
        return aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public String getDaire() {
        return daire;
    }

    public void setDaire(String daire) {
        this.daire = daire;
    }

    public String getMilce() {
        return milce;
    }

    public void setMilce(String milce) {
        this.milce = milce;
    }

    public String getMsehir() {
        return msehir;
    }

    public void setMsehir(String msehir) {
        this.msehir = msehir;
    }

    public String getMulke() {
        return mulke;
    }

    public void setMulke(String mulke) {
        this.mulke = mulke;
    }

    public String getFil() {
        return fil;
    }

    public void setFil(String fil) {
        this.fil = fil;
    }

    public String getFilce() {
        return filce;
    }

    public void setFilce(String filce) {
        this.filce = filce;
    }

    public String getFdurum() {
        return fdurum;
    }

    public void setFdurum(String fdurum) {
        this.fdurum = fdurum;
    }

    public String getFsenaryo() {
        return fsenaryo;
    }

    public void setFsenaryo(String fsenaryo) {
        this.fsenaryo = fsenaryo;
    }

    public String getFtipi() {
        return ftipi;
    }

    public void setFtipi(String ftipi) {
        this.ftipi = ftipi;
    }

    public String getFpdf() {
        return fpdf;
    }

    public void setFpdf(String fpdf) {
        this.fpdf = fpdf;
    }

    public String getDbname() {
        return dbname;
    }

    public void setDbname(String dbname) {
        this.dbname = dbname;
    }

    public String getEttn() {
        return ettn;
    }

    public void setEttn(String ettn) {
        this.ettn = ettn;
    }
}
