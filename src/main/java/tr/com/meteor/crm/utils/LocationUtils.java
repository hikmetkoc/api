package tr.com.meteor.crm.utils;

public final class LocationUtils {

    public static double distanceInMeterBetweenEarthCoordinates(double lat1, double lon1, double lat2, double lon2) {
        double p = 0.017453292519943295;
        double aa = 0.5 - Math.cos((lat2 - lat1) * p) / 2 +
            Math.cos(lat1 * p) * Math.cos(lat2 * p) * (1 - Math.cos((lon2 - lon1) * p)) / 2;
        return 12742 * Math.asin(Math.sqrt(aa)) * 1000;
    }
}
