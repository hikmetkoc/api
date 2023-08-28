package tr.com.meteor.crm.domain;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public interface HaveInstanceName {
    String getInstanceName();
}
