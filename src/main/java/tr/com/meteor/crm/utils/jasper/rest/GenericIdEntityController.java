package tr.com.meteor.crm.utils.jasper.rest;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tr.com.meteor.crm.domain.IdEntity;
import tr.com.meteor.crm.service.FileStorageService;
import tr.com.meteor.crm.utils.filter.Filter;
import tr.com.meteor.crm.domain.FileDescriptor;
import tr.com.meteor.crm.repository.GenericIdEntityRepository;
import tr.com.meteor.crm.service.GenericIdEntityService;
import tr.com.meteor.crm.utils.request.Request;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public abstract class GenericIdEntityController<TEntity extends IdEntity<TIdType>, TIdType extends Serializable, TRepository extends GenericIdEntityRepository<TEntity, TIdType>, TService extends GenericIdEntityService<TEntity, TIdType, TRepository>>
    extends BaseController<TService> {

    public GenericIdEntityController(TService service) {
        super(service);
    }

    @PostMapping
    public ResponseEntity getAll(@RequestBody Request request) throws Exception {
        if (request == null) request = new Request();

        boolean applyPermission = true;
        if (request.getOwnerId() != null) {
            applyPermission = false;
            Filter userFilter = Filter.equalsTo(service.getEntityMetaData().getOwnerPath(), request.getOwnerId());

            if (request.getFilter() == null) {
                request.setFilter(userFilter);
            } else {
                request.setFilter(Filter.And(userFilter, request.getFilter()));
            }
        }

        if (request.getColumns() == null || request.getColumns().isEmpty()) {
            if (request.getFileType() == null) {
                return service.getData(getCurrentUser(), request, applyPermission);
            } else {
                return service.exportFile(getCurrentUser(), request);
            }
        } else {
            if (applyPermission) {
                if (request.getFileType() == null) {
                    return service.getDataWithColumnSelection(getCurrentUser(), request, false);
                } else {
                    return service.exportFileWithColumnSelection(getCurrentUser(), request);
                }
            } else {
                throw new Exception("OwnerId filter does not supported on column selection!");
            }
        }
    }

    @DeleteMapping
    public void delete(@RequestParam TIdType id) throws Exception {
        service.delete(getCurrentUser(), id);
    }

    @PutMapping
    public void update(@RequestBody TEntity tEntity) throws Exception {
        if (tEntity.getId() == null) {
            service.add(getCurrentUser(), tEntity);
        } else {
            service.update(getCurrentUser(), tEntity);
        }

    }

    @PostMapping("file-list")
    public ResponseEntity<List<FileDescriptor>> listFiles(@RequestParam TIdType entityId) throws Exception {
        return service.listFiles(getCurrentUser(), entityId);
    }

    @PutMapping("file-upload")
    public FileDescriptor uploadFile(@RequestParam("file") MultipartFile file,
                                     @RequestParam("entityId") String entityId,
                                     @RequestParam("description") String description) throws Exception {
        return service.uploadFile(getCurrentUser(), file, entityId, description);
    }

    @PostMapping("file-download")
    public ResponseEntity<Resource> downloadFile(@RequestParam String fileId) throws Exception {
        return service.downloadFile(getCurrentUser(), fileId);
    }

    @PostMapping("file-show")
    public ResponseEntity<Resource> showFile(@RequestParam String fileId) throws Exception {
        return service.showFile(getCurrentUser(), fileId);
    }

    @DeleteMapping("file-delete")
    public void deleteFile(@RequestParam(name = "id") String fileId) throws Exception {
        service.deleteFile(getCurrentUser(), fileId);
    }

}
