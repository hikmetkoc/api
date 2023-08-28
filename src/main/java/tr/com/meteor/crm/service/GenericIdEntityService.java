package tr.com.meteor.crm.service;

import org.apache.xmlbeans.impl.xb.ltgfmt.FileDesc;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tr.com.meteor.crm.domain.*;
import tr.com.meteor.crm.utils.configuration.Configurations;
import tr.com.meteor.crm.utils.export.ExportUtils;
import tr.com.meteor.crm.utils.filter.*;
import tr.com.meteor.crm.utils.metadata.EntityMetadataAnn;
import tr.com.meteor.crm.utils.metadata.EntityMetadataFull;
import tr.com.meteor.crm.utils.metadata.MetadataReader;
import tr.com.meteor.crm.utils.operations.Operations;
import tr.com.meteor.crm.utils.request.*;
import tr.com.meteor.crm.utils.jasper.rest.errors.ObjectAccessDeniedException;
import tr.com.meteor.crm.utils.jasper.rest.errors.OperationAccessDeniedException;
import tr.com.meteor.crm.utils.jasper.rest.errors.RecordAccessDeniedException;
import tr.com.meteor.crm.utils.jasper.rest.errors.RecordNotFoundException;
import tr.com.meteor.crm.repository.GenericIdEntityRepository;
import tr.com.meteor.crm.security.RolesConstants;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.criteria.*;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Transactional(rollbackFor = Exception.class)
public abstract class GenericIdEntityService<TEntity extends IdEntity<TIdType>, TIdType extends Serializable, TRepository extends GenericIdEntityRepository<TEntity, TIdType>>
    extends BaseService {

    final TRepository repository;
    final Class<TEntity> entityClass;

    @PersistenceContext
    protected EntityManager entityManager;

    public GenericIdEntityService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                                  BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                                  BaseConfigurationService baseConfigurationService, Class<TEntity> entityClass, TRepository repository) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService);
        this.repository = repository;
        this.entityClass = entityClass;
    }

    public ResponseEntity<List<TEntity>> getData(User user, Request request) throws Exception {
        return getData(user, request, true);
    }

    public ResponseEntity<List<TEntity>> getData(User user, Request request, boolean applyPermissionRules) throws Exception {
        if (applyPermissionRules && !havePermission(user, Permission::isRead)) {
            throw new ObjectAccessDeniedException(ObjectAccessDeniedException.AccessType.READ, entityClass.getSimpleName());
        }

        return ResponseEntity.ok()
            .header("x-total-count", String.valueOf(countRequest(user, request, applyPermissionRules)))
            .body(processRequest(user, request, applyPermissionRules));
    }

    public ResponseEntity<List<Map<String, Object>>> getDataWithColumnSelection(User user, Request request, boolean applyPermission) throws Exception {
        if (request == null) request = new Request();
    //todo: otherSpecification AYARLANACAK.
        if (request.getFilter() != null) request.getFilter().validateAndFix(entityClass);
        if (request.getColumns() != null) request.setColumns(Column.validateAndFix(entityClass, request.getColumns()));

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = cb.createTupleQuery();

        Root root = query.from(entityClass);

        List<String> paths = CbUtils.getPaths(request);

        Map<String, Join> joinMap = CbUtils.getJoins(root, paths);

        javax.persistence.criteria.Predicate filterPredicate = Specificator.filterToPredicate(request.getFilter(), root, cb);
        javax.persistence.criteria.Predicate ownerPredicate = cb.equal(cb.literal(true), cb.literal(true));
        javax.persistence.criteria.Predicate assignerPredicate = cb.equal(cb.literal(true), cb.literal(true));
        javax.persistence.criteria.Predicate secondAssignerPredicate = cb.equal(cb.literal(true), cb.literal(true));
        javax.persistence.criteria.Predicate otherPredicate = cb.equal(cb.literal(true), cb.literal(true));

        switch (request.getOwner()) {
            case ME: {
                if (user == null) throw new Exception("Sahip filterisi kullanÄ±larken kullanÄ±cÄ± bilgisi gereklidir.");
                ownerPredicate = Specificator.filterToPredicate(Filter.FilterItem(getEntityMetaData().getOwnerPath(), FilterItem.Operator.EQUALS, user.getId()), root, cb);
            }
            break;
            case HIERARCHY: {
                if (user == null) throw new Exception("Sahip filterisi kullanÄ±larken kullanÄ±cÄ± bilgisi gereklidir.");
                List<Long> hierarchicalUserIds = baseUserService.getHierarchicalUsers(user).stream().map(User::getId).collect(Collectors.toList());
                ownerPredicate = Specificator.filterToPredicate(Filter.FilterItem(getEntityMetaData().getOwnerPath(), FilterItem.Operator.IN, hierarchicalUserIds), root, cb);
            }
            break;
            case HIERARCHY_D: {
                if (user == null) throw new Exception("Sahip filterisi kullanÄ±larken kullanÄ±cÄ± bilgisi gereklidir.");
                List<Long> hierarchicalUserIds = baseUserService.getHierarchicalUsersOnlyDownwards(user).stream().map(User::getId).collect(Collectors.toList());
                ownerPredicate = Specificator.filterToPredicate(Filter.FilterItem(getEntityMetaData().getOwnerPath(), FilterItem.Operator.IN, hierarchicalUserIds), root, cb);
            }
            break;
        }

        switch (request.getAssigner()) {
            case ME: {
                if (user == null) throw new Exception("Sahip filterisi kullanÄ±larken kullanÄ±cÄ± bilgisi gereklidir.");
                assignerPredicate = Specificator.filterToPredicate(Filter.FilterItem(getEntityMetaData().getAssignerPath(), FilterItem.Operator.EQUALS, user.getId()), root, cb);
            }
            break;
            case HIERARCHY: {
                if (user == null) throw new Exception("Sahip filterisi kullanÄ±larken kullanÄ±cÄ± bilgisi gereklidir.");
                List<Long> hierarchicalUserIds = baseUserService.getHierarchicalUsers(user).stream().map(User::getId).collect(Collectors.toList());
                assignerPredicate = Specificator.filterToPredicate(Filter.FilterItem(getEntityMetaData().getAssignerPath(), FilterItem.Operator.IN, hierarchicalUserIds), root, cb);
            }
            break;
            case HIERARCHY_D: {
                if (user == null) throw new Exception("Sahip filterisi kullanÄ±larken kullanÄ±cÄ± bilgisi gereklidir.");
                List<Long> hierarchicalUserIds = baseUserService.getHierarchicalUsersOnlyDownwards(user).stream().map(User::getId).collect(Collectors.toList());
                assignerPredicate = Specificator.filterToPredicate(Filter.FilterItem(getEntityMetaData().getAssignerPath(), FilterItem.Operator.IN, hierarchicalUserIds), root, cb);
            }
            break;
        }
        switch (request.getSecondAssigner()) {
            case ME: {
                if (user == null) throw new Exception("Sahip filterisi kullanÄ±larken kullanÄ±cÄ± bilgisi gereklidir.");
                secondAssignerPredicate = Specificator.filterToPredicate(Filter.FilterItem(getEntityMetaData().getSecondAssignerPath(), FilterItem.Operator.EQUALS, user.getId()), root, cb);
            }
            break;
            case HIERARCHY: {
                if (user == null) throw new Exception("Sahip filterisi kullanÄ±larken kullanÄ±cÄ± bilgisi gereklidir.");
                List<Long> hierarchicalUserIds = baseUserService.getHierarchicalUsers(user).stream().map(User::getId).collect(Collectors.toList());
                secondAssignerPredicate = Specificator.filterToPredicate(Filter.FilterItem(getEntityMetaData().getSecondAssignerPath(), FilterItem.Operator.IN, hierarchicalUserIds), root, cb);
            }
            break;
            case HIERARCHY_D: {
                if (user == null) throw new Exception("Sahip filterisi kullanÄ±larken kullanÄ±cÄ± bilgisi gereklidir.");
                List<Long> hierarchicalUserIds = baseUserService.getHierarchicalUsersOnlyDownwards(user).stream().map(User::getId).collect(Collectors.toList());
                secondAssignerPredicate = Specificator.filterToPredicate(Filter.FilterItem(getEntityMetaData().getSecondAssignerPath(), FilterItem.Operator.IN, hierarchicalUserIds), root, cb);
            }
            break;
        }

        switch (request.getOther()) {
            case ME: {
                if (user == null) throw new Exception("Sahip filterisi kullanÄ±larken kullanÄ±cÄ± bilgisi gereklidir.");
                otherPredicate = Specificator.filterToPredicate(Filter.FilterItem(getEntityMetaData().getOtherPath(), FilterItem.Operator.EQUALS, user.getId()), root, cb);
            }
            break;
            case HIERARCHY: {
                if (user == null) throw new Exception("Sahip filterisi kullanÄ±larken kullanÄ±cÄ± bilgisi gereklidir.");
                List<Long> hierarchicalUserIds = baseUserService.getHierarchicalUsers(user).stream().map(User::getId).collect(Collectors.toList());
                otherPredicate = Specificator.filterToPredicate(Filter.FilterItem(getEntityMetaData().getOtherPath(), FilterItem.Operator.IN, hierarchicalUserIds), root, cb);
            }
            break;
            case HIERARCHY_D: {
                if (user == null) throw new Exception("Sahip filterisi kullanÄ±larken kullanÄ±cÄ± bilgisi gereklidir.");
                List<Long> hierarchicalUserIds = baseUserService.getHierarchicalUsersOnlyDownwards(user).stream().map(User::getId).collect(Collectors.toList());
                otherPredicate = Specificator.filterToPredicate(Filter.FilterItem(getEntityMetaData().getOtherPath(), FilterItem.Operator.IN, hierarchicalUserIds), root, cb);
            }
            break;
        }

        if (applyPermission) {
            javax.persistence.criteria.Predicate permissionPredicate = getPermissionPredicate(user, root, cb);
            query.where(cb.and(permissionPredicate, cb.and(ownerPredicate, filterPredicate)));
        } else {
            query.where(cb.and(ownerPredicate, filterPredicate));
        }


        query.orderBy(CbUtils.createOrders(request, cb, root, joinMap));

        List<Selection<?>> selections = CbUtils.columnsToSelections(request, cb, root, joinMap);

        query.multiselect(selections);

        if (request.getColumns() != null && request.getColumns().stream().anyMatch(x -> x.getColumnType() != ColumnType.DEFAULT)) {
            query.groupBy(CbUtils.columnsToGroupBy(request, query, root, joinMap));
        }

        List<Tuple> list = entityManager.createQuery(query).getResultList();

        return ResponseEntity.ok()
            //.header("x-total-count", String.valueOf(countQuery(cb.and(permissionPredicate, filterPredicate))))
            .body(CbUtils.tupleListToMap(list));
    }

    private Long countQuery(javax.persistence.criteria.Predicate predicate) {
        CriteriaBuilder qb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = qb.createQuery(Long.class);
        cq.select(qb.count(cq.from(entityClass)));
        cq.where(predicate);
        return entityManager.createQuery(cq).getSingleResult();
    }

    private List<TEntity> processRequest(User user, Request request) throws Exception {
        return processRequest(user, request, true);
    }

    private List<TEntity> processRequest(User user, Request request, boolean applyPermissionRules) throws Exception {
        return repository.findAll(
            requestToSpecification(user, request, applyPermissionRules),
            RequestUtils.getPageable(entityClass, request)
        ).getContent();
    }

    private long countRequest(User user, Request request) throws Exception {
        return repository.count(requestToSpecification(user, request, true));
    }

    private long countRequest(User user, Request request, boolean applyPermissionRules) throws Exception {
        return repository.count(requestToSpecification(user, request, applyPermissionRules));
    }

    private Specification<TEntity> requestToSpecification(User user, Request request) throws Exception {
        return requestToSpecification(user, request, true);
    }

    private Specification<TEntity> requestToSpecification(User user, Request request, boolean applyPermissionRules) throws Exception {
        if (request == null) request = new Request();

        if (request.getFilter() != null) request.getFilter().validateAndFix(entityClass);
        if (request.getFilter() != null)
            request.setFilter(FilterUtils.addSearchColumns(entityClass, request.getFilter()));

        Specification<TEntity> filterSpecification = Specificator.filterToSpec(request.getFilter());
        Specification<TEntity> searchSpecification = Specificator.filterToSpec(FilterUtils.createSearchFilter(entityClass, request.getSearch()));
        Specification<TEntity> ownerSpecification = Specification.where(null);
        Specification<TEntity> assignerSpecification = Specification.where(null);
        Specification<TEntity> secondAssignerSpecification = Specification.where(null);
        Specification<TEntity> otherSpecification = Specification.where(null);

        switch (request.getOwner()) {
            case ME: {
                if (user == null) throw new Exception("Sahip filterisi kullanÄ±larken kullanÄ±cÄ± bilgisi gereklidir.");
                ownerSpecification = Specificator.filterToSpec(Filter.FilterItem(getEntityMetaData().getOwnerPath(), FilterItem.Operator.EQUALS, user.getId()));
            }
            break;
            case HIERARCHY: {
                if (user == null) throw new Exception("Sahip filterisi kullanÄ±larken kullanÄ±cÄ± bilgisi gereklidir.");
                List<Long> hierarchicalUserIds = baseUserService.getHierarchicalUsers(user).stream().map(User::getId).collect(Collectors.toList());
                ownerSpecification = Specificator.filterToSpec(Filter.FilterItem(getEntityMetaData().getOwnerPath(), FilterItem.Operator.IN, hierarchicalUserIds));
            }
            break;
            case HIERARCHY_D: {
                if (user == null) throw new Exception("Sahip filterisi kullanÄ±larken kullanÄ±cÄ± bilgisi gereklidir.");
                List<Long> hierarchicalUserIds = baseUserService.getHierarchicalUsersOnlyDownwards(user).stream().map(User::getId).collect(Collectors.toList());
                hierarchicalUserIds.add(getCurrentUser().getId());
                assignerSpecification = Specificator.filterToSpec(Filter.FilterItem(getEntityMetaData().getAssignerPath(), FilterItem.Operator.IN, hierarchicalUserIds));
                secondAssignerSpecification = Specificator.filterToSpec(Filter.FilterItem(getEntityMetaData().getSecondAssignerPath(), FilterItem.Operator.IN, hierarchicalUserIds));
                ownerSpecification = Specificator.filterToSpec(Filter.FilterItem(getEntityMetaData().getOwnerPath(), FilterItem.Operator.IN, hierarchicalUserIds));
                otherSpecification = Specificator.filterToSpec(Filter.FilterItem(getEntityMetaData().getOtherPath(), FilterItem.Operator.IN, hierarchicalUserIds));
            }
            break;
        }

        if (applyPermissionRules) {
            Specification<TEntity> permissionSpecification = getPermissionSpecification(user);

            /*Specification<TEntity> creatororassigner = Specification.where(Specificator.filterToSpec(Filter.equalsTo(getEntityMetaData().getAssignerPath(),getCurrentUser().getId())));

                creatororassigner = creatororassigner.or(Specificator.filterToSpec(Filter.equalsTo(getEntityMetaData().getOwnerPath(),getCurrentUser().getId())));*/

            return permissionSpecification.and(filterSpecification).and(searchSpecification).and(ownerSpecification.or(assignerSpecification).or(secondAssignerSpecification).or(otherSpecification));
        } else {
            return filterSpecification.and(searchSpecification).and(ownerSpecification);
        }
    }

    private boolean havePermission(User user, Predicate<Permission> predicate) {
        return user.getRoles().stream().anyMatch(x -> x.getId().equals(RolesConstants.ADMIN))
            || basePermissionService.getPermissionsByRoleAndObject(user.getRoles(), entityClass.getSimpleName())
            .stream().anyMatch(predicate);
    }

    public void delete(User user, TIdType id) throws Exception {
        if (!havePermission(user, Permission::isDelete)) {
            throw new ObjectAccessDeniedException(ObjectAccessDeniedException.AccessType.DELETE, entityClass.getSimpleName());
        }

        if (!(repository.findById(id).isPresent())) {
            throw new RecordNotFoundException(entityClass.getSimpleName(), id);
        }

        List<TEntity> dbEntity = processRequest(user, Request.idFilter(id));

        if (dbEntity.isEmpty()) {
            throw new RecordAccessDeniedException(RecordAccessDeniedException.AccessType.DELETE, entityClass.getSimpleName(), id);
        }

        repository.deleteSoft(dbEntity.get(0));
    }

    public TEntity update(User user, TEntity tEntity) throws Exception {
        if (!havePermission(user, Permission::isUpdate)) {
            throw new ObjectAccessDeniedException(ObjectAccessDeniedException.AccessType.UPDATE, entityClass.getSimpleName());
        }

        if (!(repository.findById(tEntity.getId())).isPresent()) {
            throw new RecordNotFoundException(entityClass.getSimpleName(), tEntity);
        }

        repository.update(tEntity);

        return tEntity;
    }

    public TEntity add(User user, TEntity tEntity) throws Exception {
        if (!havePermission(user, Permission::isUpdate)) {
            throw new ObjectAccessDeniedException(ObjectAccessDeniedException.AccessType.UPDATE, entityClass.getSimpleName());
        }

        repository.insert(tEntity);

        return tEntity;
    }

    public <ENTITY extends IdEntity> Specification<ENTITY> getPermissionSpecification(User user) {
        if (havePermission(user, x -> !x.isHierarchical())) {
            return Specification.where(null);
        }

        return Specification.where(Specificator.filterToSpec(getPermissionFilter(user)));
    }

    public javax.persistence.criteria.Predicate getPermissionPredicate(User user, Root root, CriteriaBuilder criteriaBuilder) {
        if (havePermission(user, x -> !x.isHierarchical())) {
            return criteriaBuilder.and();
        }

        return Specificator.filterToPredicate(getPermissionFilter(user), root, criteriaBuilder);
    }

    private Filter getPermissionFilter(User user) {
        Filter masterFilter = null;
        Filter segmentFilter = null;
        Filter dummyFilter = null;

        EntityMetadataAnn entityMetadataAnn = entityClass.getAnnotation(EntityMetadataAnn.class);

        if (entityMetadataAnn != null) {
            if (!entityMetadataAnn.masterPath().isEmpty()) {
                List<User> users = baseUserService.getHierarchicalUsers(user);

                masterFilter = Filter.FilterItem(
                    entityMetadataAnn.masterPath(),
                    FilterItem.Operator.IN,
                    users.stream().map(User::getId).collect(Collectors.toList())
                );

                if (entityMetadataAnn.masterPath().equals("customer.owner.id")) {
                    dummyFilter = Filter.And(
                        Filter.FilterItem("customer.owner", FilterItem.Operator.EQUALS, null),
                        Filter.FilterItem("customer.id", FilterItem.Operator.EQUALS, UUID.fromString("5cc36a61-9b21-4dd7-8685-2799c25bffbd"))
                    );
                }
            }

            // todo:AÇIKLAMA SATIRINA ALINDI, DÜZELTİLEBİLİR...
            /*if (!entityMetadataAnn.segmentPath().isEmpty() && !user.getSegments().isEmpty()) {
                segmentFilter = Filter.FilterItem(
                    entityMetadataAnn.segmentPath(),
                    FilterItem.Operator.IN,
                    user.getSegments().stream().map(AttributeValue::getId).collect(Collectors.toList())
                );
            }*/
        }

        return Filter.And(Filter.Or(masterFilter, dummyFilter), segmentFilter);
    }

    public FileDescriptor uploadFile(User user, MultipartFile file, String entityId, String description) throws Exception {
        if (!havePermission(user, Permission::isUpdate)) {
            throw new ObjectAccessDeniedException(ObjectAccessDeniedException.AccessType.UPDATE, FileDescriptor.class.getSimpleName());
        }

        long count = countRequest(user, Request.idFilter(entityId));

        if (count == 0) {
            throw new Exception("BÃ¶yle bir kayÄ±t yok ve ya bu kayda dosya ekleme izniniz yok.");
        }

        Integer maxFileSize = baseConfigurationService.getConfigurationById(Configurations.DOSYA_YUKLEME_SINIRI.getId()).getIntegerValue();

        if (file.getSize() > maxFileSize * 1024 * 1024) {
            throw new Exception("Dosya yÃ¼kleme sÄ±nÄ±rÄ± aÅŸÄ±ldÄ±. YÃ¼klenebilecek maksimum dosya boyutu: " + maxFileSize);
        }

        return baseFileDescriptorService.uploadFile(file, entityId, description, entityClass);
    }

    public Optional<FileDescriptor> getFileDetails(UUID fileId) throws Exception {
        // Dosya bilgilerini almak için gerekli işlemleri yapın
        Optional<FileDescriptor> fileDescriptor = baseFileDescriptorService.getFileDescriptorViewById(fileId);
        return fileDescriptor;
    }


    public ResponseEntity<List<FileDescriptor>> listFiles(User user, TIdType entityId) throws Exception {
        if (!havePermission(user, Permission::isRead)) {
            throw new ObjectAccessDeniedException(ObjectAccessDeniedException.AccessType.READ, FileDescriptor.class.getSimpleName());
        }

        long count = countRequest(
            user,
            Request.idFilter(entityId)
        );

        if (count == 0) {
            throw new Exception("BÃ¶yle bir kayÄ±t yok ve ya bu kaydÄ±n dosyalarÄ±nÄ± gÃ¶rÃ¼ntÃ¼leme izninz yok.");
        }

        return ResponseEntity.ok(baseFileDescriptorService.getFileDescriptorsByEntityId(entityId));
    }

    public ResponseEntity<Resource> downloadFile(User user, String fileId) throws Exception {
        if (!havePermission(user, Permission::isRead)) {
            throw new ObjectAccessDeniedException(ObjectAccessDeniedException.AccessType.READ, FileDescriptor.class.getSimpleName());
        }

        Optional<FileDescriptor> fileDescriptor = baseFileDescriptorService.getFileDescriptorById(UUID.fromString(fileId));

        if (!fileDescriptor.isPresent()) {
            throw new RecordNotFoundException(FileDescriptor.class.getName(), fileId);
        }

        long count = countRequest(user, Request.idFilter(fileDescriptor.get().getEntityId()));

        if (count == 0) {
            throw new Exception("Bu kaydÄ±n dosyalarÄ±nÄ± indirme izniniz yok..");
        }

        return baseFileDescriptorService.download(fileDescriptor.get());
    }

    public ResponseEntity<Resource> showFile(User user, String fileId) throws Exception {
        if (!havePermission(user, Permission::isRead)) {
            throw new ObjectAccessDeniedException(ObjectAccessDeniedException.AccessType.READ, FileDescriptor.class.getSimpleName());
        }

        Optional<FileDescriptor> fileDescriptor = baseFileDescriptorService.getFileDescriptorById(UUID.fromString(fileId));

        if (!fileDescriptor.isPresent()) {
            throw new RecordNotFoundException(FileDescriptor.class.getName(), fileId);
        }

        long count = countRequest(user, Request.idFilter(fileDescriptor.get().getEntityId()));

        if (count == 0) {
            throw new Exception("Bu kaydÄ±n dosyalarÄ±nÄ± indirme izniniz yok..");
        }

        return baseFileDescriptorService.download(fileDescriptor.get());
    }

    public void deleteFile(User user, String fileId) throws Exception {
        if (!havePermission(user, Permission::isDelete)) {
            throw new ObjectAccessDeniedException(ObjectAccessDeniedException.AccessType.DELETE, FileDescriptor.class.getSimpleName());
        }

        Optional<FileDescriptor> fileDescriptor = baseFileDescriptorService.getFileDescriptorById(UUID.fromString(fileId));

        if (!fileDescriptor.isPresent()) {
            throw new RecordNotFoundException(FileDescriptor.class.getName(), fileId);
        }

        long count = countRequest(user, Request.idFilter(fileDescriptor.get().getEntityId()));

        if (count == 0) {
            throw new Exception("Bu kaydÄ±n dosyalarÄ±nÄ± silme izniniz yok.");
        }

        baseFileDescriptorService.delete(fileDescriptor.get());
    }

    public ResponseEntity exportFile(User user, Request request) throws Exception {
        if (!isUserHaveOperation(getCurrentUserId(), Operations.DOSYA_EXPORT.getId())) {
            throw new OperationAccessDeniedException(Operations.DOSYA_EXPORT.getId());
        }

        if (request.getFileType() == ExportFileType.Excel) {
            return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=" + entityClass.getSimpleName() + ".xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel;charset=UTF-8"))
                .body(ExportUtils.toExcel(getData(user, request).getBody(), entityClass));
        } else {
            return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=" + entityClass.getSimpleName() + ".csv")
                .contentType(MediaType.parseMediaType("text/csv;charset=UTF-8"))
                .body(ExportUtils.toCsv(getData(user, request).getBody(), entityClass));
        }
    }

    public ResponseEntity exportFileWithColumnSelection(User user, Request request) throws Exception {
        if (!isUserHaveOperation(getCurrentUserId(), Operations.DOSYA_EXPORT.getId())) {
            throw new OperationAccessDeniedException(Operations.DOSYA_EXPORT.getId());
        }

        if (request.getFileType() == ExportFileType.Excel) {
            return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=" + entityClass.getSimpleName() + ".xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel;charset=UTF-8"))
                .body(ExportUtils.toExcel(getDataWithColumnSelection(user, request, false).getBody()));
        } else {
            throw new Exception("Csv export not supported on column selection!");
        }
    }

    public EntityMetadataFull getEntityMetaData() throws Exception {
        if (!MetadataReader.getClassMetadataList().containsKey(entityClass.getName())) {
            throw new Exception("Entity MetaData not found!");
        }

        return MetadataReader.getClassMetadataList().get(entityClass.getName());
    }

    public boolean exist(TIdType id) {
        return repository.existsById(id);
    }
}
