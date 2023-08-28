package tr.com.meteor.crm.domain;

import java.time.Instant;
import java.util.HashSet;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(User.class)
public abstract class User_ extends tr.com.meteor.crm.domain.IdNameAuditingEntity_ {

	public static volatile SingularAttribute<User, Instant> resetDate;
	public static volatile SingularAttribute<User, Instant> endDate;
	public static volatile SingularAttribute<User, AttributeValue> egitim;
	public static volatile SingularAttribute<User, String> phone2;
	public static volatile SingularAttribute<User, String> resetKey;
	public static volatile SingularAttribute<User, HashSet> fcmTokens;
	public static volatile SingularAttribute<User, String> password;
	public static volatile SingularAttribute<User, String> aciklama;
	public static volatile SetAttribute<User, User> members;
	public static volatile SingularAttribute<User, AttributeValue> sgksirket;
	public static volatile SingularAttribute<User, String> adres;
	public static volatile SingularAttribute<User, String> short_code;
	public static volatile SingularAttribute<User, Boolean> engelli;
	public static volatile SingularAttribute<User, String> activationKey;
	public static volatile SingularAttribute<User, String> firstName;
	public static volatile SingularAttribute<User, String> phone;
	public static volatile SingularAttribute<User, AttributeValue> sgkunvan;
	public static volatile SingularAttribute<User, AttributeValue> cinsiyet;
	public static volatile SingularAttribute<User, String> iban;
	public static volatile SingularAttribute<User, Boolean> myb;
	public static volatile SingularAttribute<User, String> district;
	public static volatile SingularAttribute<User, Instant> startDate;
	public static volatile SingularAttribute<User, String> lastName;
	public static volatile SingularAttribute<User, Boolean> emekli;
	public static volatile SingularAttribute<User, AttributeValue> sirket;
	public static volatile SingularAttribute<User, String> city;
	public static volatile SingularAttribute<User, String> instanceName;
	public static volatile SingularAttribute<User, Boolean> escalisma;
	public static volatile SetAttribute<User, Role> roles;
	public static volatile SingularAttribute<User, String> eposta;
	public static volatile SingularAttribute<User, String> login;
	public static volatile SingularAttribute<User, AttributeValue> sgkbirim;
	public static volatile SingularAttribute<User, AttributeValue> unvan;
	public static volatile SingularAttribute<User, String> aciladsoyad;
	public static volatile SingularAttribute<User, Boolean> izinGoruntuleme;
	public static volatile SingularAttribute<User, String> imageUrl;
	public static volatile SingularAttribute<User, String> mezunkurum;
	public static volatile SingularAttribute<User, String> kan;
	public static volatile SingularAttribute<User, Instant> sgkStartDate;
	public static volatile SingularAttribute<User, String> tck;
	public static volatile SingularAttribute<User, String> email;
	public static volatile SingularAttribute<User, Boolean> tcv;
	public static volatile SingularAttribute<User, String> mezunbolum;
	public static volatile SingularAttribute<User, String> siralama;
	public static volatile SetAttribute<User, User> groups;
	public static volatile SingularAttribute<User, String> acilno;
	public static volatile SingularAttribute<User, Instant> birthDate;
	public static volatile SingularAttribute<User, String> acilyakinlik;
	public static volatile SingularAttribute<User, AttributeValue> askerlik;
	public static volatile SingularAttribute<User, String> langKey;
	public static volatile SingularAttribute<User, AttributeValue> ehliyet;
	public static volatile SingularAttribute<User, AttributeValue> birim;
	public static volatile SingularAttribute<User, String> muaf;
	public static volatile SingularAttribute<User, Boolean> activated;

	public static final String RESET_DATE = "resetDate";
	public static final String END_DATE = "endDate";
	public static final String EGITIM = "egitim";
	public static final String PHONE2 = "phone2";
	public static final String RESET_KEY = "resetKey";
	public static final String FCM_TOKENS = "fcmTokens";
	public static final String PASSWORD = "password";
	public static final String ACIKLAMA = "aciklama";
	public static final String MEMBERS = "members";
	public static final String SGKSIRKET = "sgksirket";
	public static final String ADRES = "adres";
	public static final String SHORT_CODE = "short_code";
	public static final String ENGELLI = "engelli";
	public static final String ACTIVATION_KEY = "activationKey";
	public static final String FIRST_NAME = "firstName";
	public static final String PHONE = "phone";
	public static final String SGKUNVAN = "sgkunvan";
	public static final String CINSIYET = "cinsiyet";
	public static final String IBAN = "iban";
	public static final String MYB = "myb";
	public static final String DISTRICT = "district";
	public static final String START_DATE = "startDate";
	public static final String LAST_NAME = "lastName";
	public static final String EMEKLI = "emekli";
	public static final String SIRKET = "sirket";
	public static final String CITY = "city";
	public static final String INSTANCE_NAME = "instanceName";
	public static final String ESCALISMA = "escalisma";
	public static final String ROLES = "roles";
	public static final String EPOSTA = "eposta";
	public static final String LOGIN = "login";
	public static final String SGKBIRIM = "sgkbirim";
	public static final String UNVAN = "unvan";
	public static final String ACILADSOYAD = "aciladsoyad";
	public static final String IZIN_GORUNTULEME = "izinGoruntuleme";
	public static final String IMAGE_URL = "imageUrl";
	public static final String MEZUNKURUM = "mezunkurum";
	public static final String KAN = "kan";
	public static final String SGK_START_DATE = "sgkStartDate";
	public static final String TCK = "tck";
	public static final String EMAIL = "email";
	public static final String TCV = "tcv";
	public static final String MEZUNBOLUM = "mezunbolum";
	public static final String SIRALAMA = "siralama";
	public static final String GROUPS = "groups";
	public static final String ACILNO = "acilno";
	public static final String BIRTH_DATE = "birthDate";
	public static final String ACILYAKINLIK = "acilyakinlik";
	public static final String ASKERLIK = "askerlik";
	public static final String LANG_KEY = "langKey";
	public static final String EHLIYET = "ehliyet";
	public static final String BIRIM = "birim";
	public static final String MUAF = "muaf";
	public static final String ACTIVATED = "activated";

}

