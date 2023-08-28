package tr.com.meteor.crm.domain;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@MappedSuperclass
public abstract class IdNameEntity<TIdType extends Serializable> extends IdEntity<TIdType> implements Serializable, HaveInstanceName {

}
