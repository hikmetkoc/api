package tr.com.meteor.crm.service.dto;

import java.util.HashMap;
import java.util.Map;

public class CrmNotification {
    private Object to = "/topics/all";
    private Notification notification = new Notification();
    private Map<String, String> data = new HashMap<>();

    public CrmNotification() {
        setType(Type.DEFAULT);
    }

    public String getTitle() {
        return data.getOrDefault("title", null);
    }

    public void setTitle(String title) {
        data.put("title", title);
        notification.setTitle(title);
    }

    public CrmNotification title(String title) {
        data.put("title", title);
        notification.setTitle(title);
        return this;
    }

    public String getBody() {
        return data.getOrDefault("body", null);
    }

    public void setBody(String body) {
        data.put("body", body);
        notification.setBody(body);
    }

    public CrmNotification body(String body) {
        data.put("body", body);
        notification.setBody(body);
        return this;
    }

    public String getEntityName() {
        return data.getOrDefault("entityName", null);
    }

    public void setEntityName(String entityName) {
        data.put("entityName", entityName);
    }

    public CrmNotification entityName(String entityName) {
        data.put("entityName", entityName);
        return this;
    }

    public String getEntityId() {
        return data.getOrDefault("entityId", null);
    }

    public void setEntityId(String entityId) {
        data.put("entityId", entityId);
    }

    public CrmNotification entityId(String entityId) {
        data.put("entityId", entityId);
        return this;
    }

    public String getNotificationOwnerId() {
        return data.getOrDefault("notificationOwnerId", null);
    }

    public void setNotificationOwnerId(Long notificationOwnerId) {
        data.put("notificationOwnerId", notificationOwnerId == null ? null : String.valueOf(notificationOwnerId));
    }

    public CrmNotification notificationOwnerId(Long notificationOwnerId) {
        data.put("notificationOwnerId", notificationOwnerId == null ? null : String.valueOf(notificationOwnerId));
        return this;
    }

    public String getType() {
        return data.getOrDefault("type", null);
    }

    public void setType(Type type) {
        data.put("type", type == null ? null : String.valueOf(type));
    }

    public CrmNotification type(Type type) {
        data.put("type", type == null ? null : String.valueOf(type));
        return this;
    }

    public Object getTo() {
        return to;
    }

    public void setTo(Object to) {
        this.to = to;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    public void addData(String key, String value) {
        data.put(key, value);
    }

    public enum Type {
        DEFAULT, ANNOUNCEMENT, TASK_ASSIGNED
    }

    static class Notification {
        private String title;
        private String body;
        private String click_action = "FCM_PLUGIN_ACTIVITY";

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public String getClick_action() {
            return click_action;
        }

        public void setClick_action(String click_action) {
            this.click_action = click_action;
        }
    }
}
