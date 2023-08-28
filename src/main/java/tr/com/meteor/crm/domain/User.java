package tr.com.meteor.crm.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Formula;
import tr.com.meteor.crm.utils.ArrayListToStringConverter;
import tr.com.meteor.crm.utils.idgenerator.IdType;
import tr.com.meteor.crm.utils.metadata.EntityMetadataAnn;
import tr.com.meteor.crm.utils.metadata.FieldMetadataAnn;
import tr.com.meteor.crm.utils.validate.AttributeValueValidate;
import tr.com.meteor.crm.config.Constants;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * A user.
 */
@Entity
@Table(name = "jhi_user", indexes = {@Index(columnList = "search")})
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@IdType(idType = IdType.IdTypeEnum.Long, sequenceName = "sequence_generator")
@EntityMetadataAnn(apiName = "users", displayField = "instanceName", title = "Kullanıcı", pluralTitle = "Kullanıcılar", ownerPath = "id")
public class User extends IdNameAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    @Column(length = 50, unique = true, nullable = false)
    @FieldMetadataAnn(title = "Oturum", priority = 1, search = true, active = true, required = true)
    private String login;

    @NotNull
    @Size(min = 60, max = 60)
    @Column(name = "password_hash", length = 60, nullable = false)
    @FieldMetadataAnn(title = "Parola", priority = 2, active = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Size(max = 50)
    @Column(name = "first_name", length = 50)
    @FieldMetadataAnn(title = "Ad", display = true, priority = 3, search = true, filterable = true, required = true)
    private String firstName;

    @Size(max = 50)
    @Column(name = "last_name", length = 50)
    @FieldMetadataAnn(title = "Soyad", display = true, priority = 4, search = true, filterable = true, required = true)
    private String lastName;

    @Size(max = 11)
    @Column(name = "tck", length = 11)
    @FieldMetadataAnn(title = "TC Kimlik Nu", display = true, priority = 4, search = true, filterable = true, required = true)
    private String tck;
    @ManyToOne
    @JoinColumn(name = "sirket_id")
    @FieldMetadataAnn(title = "Bağlı Olduğu Şirket", defaultValue = "Sirket_Meteor", display = true, priority = 30, search = true, filterable = true, required = true)
    @AttributeValueValidate(attributeId = "Sirketler")
    private AttributeValue sirket;

    @ManyToOne
    @JoinColumn(name = "birim_id")
    @FieldMetadataAnn(title = "Bağlı Olduğu Birim", defaultValue = "Birim_IT", display = true, priority = 31, search = true, filterable = true, required = true)
    @AttributeValueValidate(attributeId = "Birimler")
    private AttributeValue birim;

    @ManyToOne
    @JoinColumn(name = "unvan_id")
    @FieldMetadataAnn(title = "Bağlı Olduğu Unvan", defaultValue = "Unvan_BT_Sef", display = true, priority = 32, search = true, filterable = true, required = true)
    @AttributeValueValidate(attributeId = "Unvanlar")
    private AttributeValue unvan;

    @ManyToOne
    @JoinColumn(name = "sgksirket_id")
    @FieldMetadataAnn(title = "Sigortalı Olduğu Şirket", defaultValue = "Sirket_Meteor", display = true, priority = 10, search = true, filterable = true)
    @AttributeValueValidate(attributeId = "Sirketler")
    private AttributeValue sgksirket;

    @ManyToOne
    @JoinColumn(name = "sgkbirim_id")
    @FieldMetadataAnn(title = "Sigortalı Olduğu Birim", defaultValue = "Birim_IT", active = false, priority = 11, search = true)
    @AttributeValueValidate(attributeId = "Birimler")
    private AttributeValue sgkbirim;

    @ManyToOne
    @JoinColumn(name = "sgkunvan_id")
    @FieldMetadataAnn(title = "Sigortalı Olduğu Unvan", defaultValue = "Unvan_BT_Sef", display = true, priority = 12, search = true, filterable = true)
    @AttributeValueValidate(attributeId = "Unvanlar")
    private AttributeValue sgkunvan;

    @ManyToOne
    @JoinColumn(name = "cinsiyet_id")
    @FieldMetadataAnn(title = "Cinsiyet", defaultValue = "Cin_Erk", search = true)
    @AttributeValueValidate(attributeId = "Cinsiyetler")
    private AttributeValue cinsiyet;

    @ManyToOne
    @JoinColumn(name = "askerlik_id")
    @FieldMetadataAnn(title = "Askerlik Durumu", defaultValue = "Ask_Tam", search = true)
    @AttributeValueValidate(attributeId = "Askerlik_Durumlari")
    private AttributeValue askerlik;

    @ManyToOne
    @JoinColumn(name = "egitim_id")
    @FieldMetadataAnn(title = "Eğitim Durumu", defaultValue = "Egt_Ilk", search = true)
    @AttributeValueValidate(attributeId = "Egitim_Durumlari")
    private AttributeValue egitim;

    @ManyToOne
    @JoinColumn(name = "ehliyet_id")
    @FieldMetadataAnn(title = "Ehliyet Sınıfı", defaultValue = "Ehl_Yok", search = true)
    @AttributeValueValidate(attributeId = "Ehliyet_Siniflari")
    private AttributeValue ehliyet;

    @Size(min = 26, max = 34)
    @Column(length = 34, unique = true)
    @FieldMetadataAnn(title = "IBAN", display = false, defaultValue = "TR", priority = 6, search = true)
    private String iban;

    @FieldMetadataAnn(title = "İşe Başlangıç Tarihi", display = true, priority = 40, filterable = true)
    private Instant startDate;

    @FieldMetadataAnn(title = "Sigorta Başlangıç Tarihi", display = true, priority = 40, filterable = true)
    private Instant sgkStartDate;

    @FieldMetadataAnn(title = "İşten Ayrıldığı Tarih", display = false)
    private Instant endDate;

    @FieldMetadataAnn(title = "Doğum Tarihi", display = true, priority = 40, filterable = true)
    private Instant birthDate;

    @Email
    @Size(min = 5, max = 254)
    @Column(length = 254, unique = true)
    @FieldMetadataAnn(title = "Mail", display = true, priority = 5, search = true, type = "Email", required = true, filterable = true)
    private String email;

    @Email
    @Size(min = 5, max = 254)
    @Column(length = 254, unique = true)
    @FieldMetadataAnn(title = "Gönderim Maili", display = false, priority = 5, search = true, type = "Email")
    private String eposta;

    @FieldMetadataAnn(title = "Acil İrtibat Telefonu", type="Phone")
    private String acilno;

    @FieldMetadataAnn(title = "Acil İrtibat Ad/Soyad")
    private String aciladsoyad;

    @FieldMetadataAnn(title = "Acil İrtibat Yakınlık")
    private String acilyakinlik;

    @FieldMetadataAnn(title = "Emekli")
    private Boolean emekli = false;

    @FieldMetadataAnn(title = "Engelli")
    private Boolean engelli = false;

    @FieldMetadataAnn(title = "Eş Çalışma Durumu")
    private Boolean escalisma = false;

    @FieldMetadataAnn(title = "Mesleki Yeterlilik Belgesi")
    private Boolean myb = false;

    @FieldMetadataAnn(title = "Açık Adres")
    private String adres;

    @FieldMetadataAnn(title = "T.C. Vatandaşlığı")
    private Boolean tcv = true;

    @FieldMetadataAnn(title = "Doğduğu İl")
    private String city;

    @FieldMetadataAnn(title = "Doğduğu İlçe")
    private String district;

    @FieldMetadataAnn(title = "Muafiyet Sebebi")
    private String muaf;

    @FieldMetadataAnn(title = "Kan Grubu")
    private String kan;

    @FieldMetadataAnn(title = "Mezun Olduğu Kurum")
    private String mezunkurum;

    @FieldMetadataAnn(title = "Mezun Olduğu Bölüm")
    private String mezunbolum;
    @FieldMetadataAnn(title = "Açıklama")
    private String aciklama;
    @NotNull
    @Column(nullable = false)
    @FieldMetadataAnn(title = "Aktiflik", display = false, priority = 6, required = true)
    private Boolean activated = true;

    @Size(min = 2, max = 10)
    @Column(name = "lang_key", length = 10)
    @FieldMetadataAnn(title = "Dil", priority = 7, active = false)
    private String langKey;

    @Size(max = 256)
    @Column(name = "image_url", length = 256)
    @FieldMetadataAnn(title = "Resim URL", priority = 8, active = false)
    private String imageUrl;

    @Size(max = 20)
    @Column(name = "activation_key", length = 20)
    @JsonIgnore
    @FieldMetadataAnn(title = "Aktivasyon Anahtarı", priority = 9, active = false)
    private String activationKey;

    @Size(max = 20)
    @Column(name = "reset_key", length = 20)
    @JsonIgnore
    @FieldMetadataAnn(title = "Resetleme Anahtarı", priority = 10, active = false)
    private String resetKey;

    @Column(name = "reset_date")
    @FieldMetadataAnn(title = "Resetleme Tarihi", priority = 11, active = false)
    private Instant resetDate = null;
    @FieldMetadataAnn(title = "Şirket Telefonu", type = "Phone", search = true, display = true, priority = 12, filterable = true)
    private String phone;

    @FieldMetadataAnn(title = "Kısa Kod", priority = 13, filterable = true)
    private String short_code;

    @FieldMetadataAnn(title = "Cep Telefonu", type = "Phone", search = true, display = false, priority = 14)
    private String phone2;
    @FieldMetadataAnn(title = "Sıralama", priority = 14, active = false)
    private String siralama;

    @FieldMetadataAnn(title = "İzin Görüntüleme", display = false, priority = 500)
    private Boolean izinGoruntuleme = false;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "jhi_user_role",
        joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @BatchSize(size = 20)
    private Set<Role> roles = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_group",
        joinColumns = {@JoinColumn(name = "member_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "group_id", referencedColumnName = "id")})
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private Set<User> groups = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_group",
        joinColumns = {@JoinColumn(name = "group_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "member_id", referencedColumnName = "id")})
    @JsonIgnoreProperties({"groups", "members", "createdBy", "lastModifiedBy", "roles"})
    private Set<User> members = new HashSet<>();

    /*@AttributeValueValidate(attributeId = "Müş_Seg")
    @ManyToMany
    @JoinTable(name = "user_segment",
        joinColumns = @JoinColumn(name = "user_id", nullable = true),
        inverseJoinColumns = @JoinColumn(name = "segment_id", nullable = true))
    private Set<AttributeValue> segments = new HashSet<>();*/


    @JsonIgnore
    @Convert(converter = ArrayListToStringConverter.class)
    @Column(columnDefinition = "TEXT")
    private HashSet<String> fcmTokens = new HashSet<>();

    @Formula("concat(first_name,' ',last_name)")
    @FieldMetadataAnn(readOnly = true, title = "Başlık", priority = 1)
    private String instanceName;

    public String getLogin() {
        return login;
    }

    // Lowercase the login before saving it in database
    public void setLogin(String login) {
        this.login = StringUtils.lowerCase(login, Locale.ENGLISH);
    }

    //@JsonIgnore
    public String getPassword() {
        return password;
    }

    //@JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public String getActivationKey() {
        return activationKey;
    }

    public void setActivationKey(String activationKey) {
        this.activationKey = activationKey;
    }

    public String getResetKey() {
        return resetKey;
    }

    public void setResetKey(String resetKey) {
        this.resetKey = resetKey;
    }

    public Instant getResetDate() {
        return resetDate;
    }

    public void setResetDate(Instant resetDate) {
        this.resetDate = resetDate;
    }
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getShort_code() {
        return short_code;
    }

    public void setShort_code(String short_code) {
        this.short_code = short_code;
    }

    public String getSiralama() {
        return siralama;
    }

    public void setSiralama(String siralama) {
        this.siralama = siralama;
    }

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<User> getGroups() {
        return groups;
    }

    public void setGroups(Set<User> groups) {
        this.groups = groups;
    }

    public Set<User> getMembers() {
        return members;
    }

    public void setMembers(Set<User> memebers) {
        this.members = memebers;
    }

    /*public Set<AttributeValue> getSegments() {
        return segments;
    }

    public void setSegments(Set<AttributeValue> segments) {
        this.segments = segments;
    }*/

    public HashSet<String> getFcmTokens() {
        return fcmTokens;
    }

    public void setFcmTokens(HashSet<String> fcmTokens) {
        this.fcmTokens = fcmTokens;
    }

    public void addFcmToken(String fcmToken) {
        if (fcmTokens == null) fcmTokens = new HashSet<>();
        fcmTokens.add(fcmToken);
    }
    @Override
    public String getInstanceName() {
        return instanceName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }
        return id != null && id.equals(((User) o).id);
    }

    @JsonIgnore
    @Transient
    public String getFullName() {
        return (StringUtils.isNotBlank(firstName) ? firstName : "") + " " + (StringUtils.isNotBlank(lastName) ? lastName : "");
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "User{" +
            "login='" + login + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", email='" + email + '\'' +
            ", imageUrl='" + imageUrl + '\'' +
            ", activated='" + activated + '\'' +
            ", langKey='" + langKey + '\'' +
            ", activationKey='" + activationKey + '\'' +
            "}";
    }

    @Override
    public void updateSearchPre() {
        super.updateSearchPre();

        if (StringUtils.isNotBlank(firstName)) {
            search += " " + firstName;
        }

        if (StringUtils.isNotBlank(lastName)) {
            search += " " + lastName;
        }

        if (StringUtils.isNotBlank(phone)) {
            search += " " + phone;
        }

        if (StringUtils.isNotBlank(login)) {
            search += " " + login;
        }

        if (StringUtils.isNotBlank(email)) {
            search += " " + email;
        }

        search = search.toLowerCase(new Locale("tr", "TR"));
    }

    @PreUpdate
    public void checkLAstModifiedBy() {
        User user = new User();
        user.setId(1L);
        setLastModifiedBy(user);
    }

    public AttributeValue getBirim() {
        return birim;
    }

    public void setBirim(AttributeValue birim) {
        this.birim = birim;
    }

    public AttributeValue getUnvan() {
        return unvan;
    }

    public void setUnvan(AttributeValue unvan) {
        this.unvan = unvan;
    }

    public AttributeValue getSirket() {
        return sirket;
    }

    public void setSirket(AttributeValue sirket) {
        this.sirket = sirket;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Instant birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getTck() {
        return tck;
    }

    public void setTck(String tck) {
        this.tck = tck;
    }

    public AttributeValue getSgksirket() {
        return sgksirket;
    }

    public void setSgksirket(AttributeValue sgksirket) {
        this.sgksirket = sgksirket;
    }

    public AttributeValue getSgkbirim() {
        return sgkbirim;
    }

    public void setSgkbirim(AttributeValue sgkbirim) {
        this.sgkbirim = sgkbirim;
    }

    public AttributeValue getSgkunvan() {
        return sgkunvan;
    }

    public void setSgkunvan(AttributeValue sgkunvan) {
        this.sgkunvan = sgkunvan;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getAcilno() {
        return acilno;
    }

    public void setAcilno(String acilno) {
        this.acilno = acilno;
    }

    public String getAciladsoyad() {
        return aciladsoyad;
    }

    public void setAciladsoyad(String aciladsoyad) {
        this.aciladsoyad = aciladsoyad;
    }

    public String getAcilyakinlik() {
        return acilyakinlik;
    }

    public void setAcilyakinlik(String acilyakinlik) {
        this.acilyakinlik = acilyakinlik;
    }

    public Boolean getEmekli() {
        return emekli;
    }

    public void setEmekli(Boolean emekli) {
        this.emekli = emekli;
    }

    public Boolean getEngelli() {
        return engelli;
    }

    public void setEngelli(Boolean engelli) {
        this.engelli = engelli;
    }

    public Boolean getEscalisma() {
        return escalisma;
    }

    public void setEscalisma(Boolean escalisma) {
        this.escalisma = escalisma;
    }

    public Boolean getMyb() {
        return myb;
    }

    public void setMyb(Boolean myb) {
        this.myb = myb;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public AttributeValue getCinsiyet() {
        return cinsiyet;
    }

    public void setCinsiyet(AttributeValue cinsiyet) {
        this.cinsiyet = cinsiyet;
    }

    public Boolean getTcv() {
        return tcv;
    }

    public void setTcv(Boolean tcv) {
        this.tcv = tcv;
    }

   public AttributeValue getAskerlik() {
        return askerlik;
    }

    public void setAskerlik(AttributeValue askerlik) {
        this.askerlik = askerlik;
    }

    public AttributeValue getEgitim() {
        return egitim;
    }

    public void setEgitim(AttributeValue egitim) {
        this.egitim = egitim;
    }

    public String getMuaf() {
        return muaf;
    }

    public void setMuaf(String muaf) {
        this.muaf = muaf;
    }

    public String getKan() {
        return kan;
    }

    public void setKan(String kan) {
        this.kan = kan;
    }

    public AttributeValue getEhliyet() {
        return ehliyet;
    }

    public void setEhliyet(AttributeValue ehliyet) {
        this.ehliyet = ehliyet;
    }

    public String getMezunkurum() {
        return mezunkurum;
    }

    public void setMezunkurum(String mezunkurum) {
        this.mezunkurum = mezunkurum;
    }

    public String getMezunbolum() {
        return mezunbolum;
    }

    public void setMezunbolum(String mezunbolum) {
        this.mezunbolum = mezunbolum;
    }

    public String getAciklama() {
        return aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getEposta() {
        return eposta;
    }

    public void setEposta(String eposta) {
        this.eposta = eposta;
    }

    public Instant getSgkStartDate() {
        return sgkStartDate;
    }

    public void setSgkStartDate(Instant sgkStartDate) {
        this.sgkStartDate = sgkStartDate;
    }

    public Boolean getIzinGoruntuleme() {
        return izinGoruntuleme;
    }

    public void setIzinGoruntuleme(Boolean izinGoruntuleme) {
        this.izinGoruntuleme = izinGoruntuleme;
    }
}
