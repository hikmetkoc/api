package tr.com.meteor.crm.utils.jasper.rest;

import tr.com.meteor.crm.domain.User;
import tr.com.meteor.crm.service.BaseService;

public abstract class BaseController<TService extends BaseService> {

    final TService service;

    protected BaseController(TService baseService) {
        this.service = baseService;
    }

    User getCurrentUser() {
        return service.getCurrentUser();
    }

    Long getCurrentUserId() {
        return service.getCurrentUserId();
    }
}
