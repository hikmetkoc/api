package tr.com.meteor.crm.utils.request;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import tr.com.meteor.crm.domain.IdEntity;
import tr.com.meteor.crm.utils.metadata.MetadataReader;
import tr.com.meteor.crm.utils.metadata.EntityMetadataFull;

import java.text.MessageFormat;
import java.util.List;

public class RequestUtils {
    public static Pageable getPageable(Class<? extends IdEntity> entityClass, Request request) throws Exception {
        Integer page;
        Integer size;
        org.springframework.data.domain.Sort sort;EntityMetadataFull entityMetadataFull = MetadataReader.getClassMetadataList().get(entityClass.getName());

        if (entityMetadataFull == null) {
            throw new Exception(MessageFormat.format("Object {0} not found in metadata!", entityClass.getName()));
        }

        page = 0;
        size = entityMetadataFull.getSize();
        sort = createSort(entityMetadataFull.getSorts());

        if (request != null) {
            if (request.getPage() != null) {
                if(request.getPage() < 0) {
                    throw new Exception(MessageFormat.format("Request Page not valid! Size: ''{0}''", request.getPage()));
                }

                page = request.getPage();
            }

            if (request.getSize() != null) {
                if (request.getSize() < 1) {
                    throw new Exception(MessageFormat.format("Request Size not valid! Size: ''{0}''", request.getSize()));
                }

                size = request.getSize();
            }

            if(request.getSorts() != null && !request.getSorts().isEmpty()) {
                sort = createSort(request.getSorts());
            }
        }

        if (sort == null) {
            return PageRequest.of(page, size);
        } else {
            return PageRequest.of(page, size, sort);
        }
    }

    private static org.springframework.data.domain.Sort createSort(List<Sort> sortList) throws Exception {
        org.springframework.data.domain.Sort sort = null;

        if (sortList != null && !sortList.isEmpty()) {
            for (Sort s : sortList) {
                if (s.getSortBy() == null || s.getSortBy().isEmpty()) {
                    throw new Exception(MessageFormat.format("Sort By not valid! Size: ''{0}''", s.getSortBy()));
                }

                if (s.getSortOrder() == null) {
                    throw new Exception(MessageFormat.format("Sort Order not valid! Size: ''{0}''", s.getSortOrder()));
                }

                if (sort == null) {
                    sort = s.getSortOrder() == SortOrder.ASC
                        ? org.springframework.data.domain.Sort.by(s.getSortBy()).ascending()
                        : org.springframework.data.domain.Sort.by(s.getSortBy()).descending();
                } else {
                    sort = sort.and(s.getSortOrder() == SortOrder.ASC
                        ? org.springframework.data.domain.Sort.by(s.getSortBy()).ascending()
                        : org.springframework.data.domain.Sort.by(s.getSortBy()).descending());
                }
            }
        }

        return sort;
    }
}
