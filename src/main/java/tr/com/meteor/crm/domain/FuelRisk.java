package tr.com.meteor.crm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Formula;
import tr.com.meteor.crm.utils.idgenerator.IdType;
import tr.com.meteor.crm.utils.metadata.EntityMetadataAnn;
import tr.com.meteor.crm.utils.metadata.FieldMetadataAnn;
import tr.com.meteor.crm.utils.validate.AttributeValueValidate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Locale;
import java.util.UUID;

@Entity
@IdType(idType = IdType.IdTypeEnum.UUID)
@EntityMetadataAnn(apiName = "fuel_risks", displayField = "instanceName", title = "Risk Analizi", pluralTitle = "Risk Analizleri")
@Table(indexes = {@Index(columnList = "search")})
public class FuelRisk extends IdNameAuditingEntity<UUID> {

    @FieldMetadataAnn(title = "Risk", search = true, readOnly = true, priority = 5, active = false)
    private String name;

    @ManyToOne
    @FieldMetadataAnn(title = "Limit Talebi", display = true, priority = 10, filterable = true)
    private FuelLimit fuellimit;

    @FieldMetadataAnn(title = "Banka Adı")
    private String bank;

    @FieldMetadataAnn(title = "Dbs Limit")
    private BigDecimal dbs;

    @FieldMetadataAnn(title = "Onaylı Fatura")
    private BigDecimal onayli;

    @FieldMetadataAnn(title = "Nakit Risk")
    private BigDecimal nakit;

    @Formula("name")
    @FieldMetadataAnn(readOnly = true, title = "Başlık", active = false)
    private String instanceName;

    @Override
    public String getInstanceName() {
        return instanceName;
    }
    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
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

        if (StringUtils.isNotBlank(name)) {
            search += " " + name;
        }

        search = search.toLowerCase(new Locale("tr", "TR"));
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public BigDecimal getDbs() {
        return dbs;
    }

    public void setDbs(BigDecimal dbs) {
        this.dbs = dbs;
    }

    public BigDecimal getOnayli() {
        return onayli;
    }

    public void setOnayli(BigDecimal onayli) {
        this.onayli = onayli;
    }

    public BigDecimal getNakit() {
        return nakit;
    }

    public void setNakit(BigDecimal nakit) {
        this.nakit = nakit;
    }

    public FuelLimit getFuellimit() {
        return fuellimit;
    }

    public void setFuellimit(FuelLimit fuellimit) {
        this.fuellimit = fuellimit;
    }
}
