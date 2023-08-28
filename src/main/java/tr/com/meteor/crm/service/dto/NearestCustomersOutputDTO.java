package tr.com.meteor.crm.service.dto;

import tr.com.meteor.crm.domain.User;

import java.util.UUID;

public class NearestCustomersOutputDTO {
    private UUID id;
    private String name;
    private String email;
    private String phone;
    private Double latitude;
    private Double longitude;
    private User owner;

    public UUID getId() {
        return id;
    }

    public NearestCustomersOutputDTO setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public NearestCustomersOutputDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public NearestCustomersOutputDTO setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public NearestCustomersOutputDTO setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public Double getLatitude() {
        return latitude;
    }

    public NearestCustomersOutputDTO setLatitude(Double latitude) {
        this.latitude = latitude;
        return this;
    }

    public Double getLongitude() {
        return longitude;
    }

    public NearestCustomersOutputDTO setLongitude(Double longitude) {
        this.longitude = longitude;
        return this;
    }

    public User getOwner() {
        return owner;
    }

    public NearestCustomersOutputDTO setOwner(User owner) {
        this.owner = owner;
        return this;
    }
}
