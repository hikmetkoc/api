package tr.com.meteor.crm.domain;

import org.apache.commons.lang3.StringUtils;
import tr.com.meteor.crm.utils.idgenerator.IdType;
import tr.com.meteor.crm.utils.metadata.EntityMetadataAnn;
import tr.com.meteor.crm.utils.metadata.FieldMetadataAnn;
import tr.com.meteor.crm.utils.request.SortOrder;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.util.Locale;
import java.util.UUID;

@Entity
@IdType(idType = IdType.IdTypeEnum.UUID)
@EntityMetadataAnn(
    apiName = "opetFleets", displayField = "fleetName", sortBy = "id", sortOrder = SortOrder.DESC,
    title = "Opet Filo", pluralTitle = "Opet Filolar"
)
@Table(indexes = {@Index(columnList = "search")})
public class OpetFleet extends IdEntity<UUID> {

    @FieldMetadataAnn(title = "Filo Adı", search = true, display = true, priority = 10)
    private String fleetName;

    @FieldMetadataAnn(title = "Filo Kodu", search = true, display = true, priority = 20)
    private Integer fleetCode;

    public String getFleetName() {
        return fleetName;
    }

    public void setFleetName(String fleetName) {
        this.fleetName = fleetName;
    }

    public OpetFleet fleetName(String fleetName) {
        this.fleetName = fleetName;
        return this;
    }

    public Integer getFleetCode() {
        return fleetCode;
    }

    public void setFleetCode(Integer fleetCode) {
        this.fleetCode = fleetCode;
    }

    public OpetFleet fleetCode(Integer fleetCode) {
        this.fleetCode = fleetCode;
        return this;
    }

    @Override
    public void updateSearchPre() {
        super.updateSearchPre();

        if (StringUtils.isNotBlank(fleetName)) {
            search += " " + fleetName;
        }

        if (fleetCode != null) {
            search += " " + fleetCode;
        }

        search = search.toLowerCase(new Locale("tr", "TR"));
    }
}
