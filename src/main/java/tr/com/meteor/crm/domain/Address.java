package tr.com.meteor.crm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Formula;
import tr.com.meteor.crm.utils.idgenerator.IdType;
import tr.com.meteor.crm.utils.metadata.EntityMetadataAnn;
import tr.com.meteor.crm.utils.metadata.FieldMetadataAnn;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Entity
@IdType(idType = IdType.IdTypeEnum.UUID)
@EntityMetadataAnn(apiName = "addresses", displayField = "instanceName", title = "Adres", pluralTitle = "Adresler")
@Table(indexes = {@Index(columnList = "search")})
public class Address extends IdNameAuditingEntity<UUID> {

    @FieldMetadataAnn(required = true, title = "Adres Başlığı", search = true, display = true, priority = 10)
    private String name;

    @ManyToOne
    @FieldMetadataAnn(title = "Ülke", display = true, priority = 30, active = false)
    private Country country;

    @ManyToOne
    @FieldMetadataAnn(required = true, title = "İl", display = true, priority = 40)
    private City city;

    @ManyToOne
    @FieldMetadataAnn(required = true, title = "İlçe", priority = 50)
    private District district;

    @FieldMetadataAnn(required = true, title = "Adres Satırı", priority = 80)
    @Column(length = 512)
    private String detail;

    @FieldMetadataAnn(title = "Enlem", priority = 60, active = false)
    private Double latitude;

    @FieldMetadataAnn(title = "Boylam", priority = 70, active = false)
    private Double longitude;

    @ManyToOne
    @FieldMetadataAnn(title = "Tedarikçi", display = true, priority = 90)
    @JsonIgnoreProperties("addresses")
    private Customer customer;

    @Formula("name")
    @FieldMetadataAnn(readOnly = true, title = "Başlık")
    private String instanceName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public String getInstanceName() {
        return instanceName;
    }

    public String toAddressString() {
        List<String> parts = new ArrayList<>();
        if (StringUtils.isNotBlank(detail)) {
            parts.add(detail);
        }

        if (district != null && StringUtils.isNotBlank(district.getName())) {
            parts.add(district.getName());
        }

        if (city != null && StringUtils.isNotBlank(city.getName())) {
            parts.add(city.getName());
        }

        return String.join(" ", parts);
    }

    @Override
    public void updateSearchPre() {
        super.updateSearchPre();

        if (customer != null) {
            search += " " + customer.getCommercialTitle();
        }

        if (StringUtils.isNotBlank(name)) {
            search += " " + name;
        }

        search = search.toLowerCase(new Locale("tr", "TR"));
    }
}
