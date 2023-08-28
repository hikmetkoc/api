package tr.com.meteor.crm.service.dto;

public class NearestCustomersInputDTO {
    private LocationDTO northEast;
    private LocationDTO southWest;
    private LocationDTO position;

    public LocationDTO getNorthEast() {
        return northEast;
    }

    public void setNorthEast(LocationDTO northEast) {
        this.northEast = northEast;
    }

    public LocationDTO getSouthWest() {
        return southWest;
    }

    public void setSouthWest(LocationDTO southWest) {
        this.southWest = southWest;
    }

    public LocationDTO getPosition() {
        return position;
    }

    public void setPosition(LocationDTO position) {
        this.position = position;
    }

    public static class LocationDTO {
        private Double lat;
        private Double lng;

        public Double getLat() {
            return lat;
        }

        public void setLat(Double lat) {
            this.lat = lat;
        }

        public Double getLng() {
            return lng;
        }

        public void setLng(Double lng) {
            this.lng = lng;
        }
    }
}
