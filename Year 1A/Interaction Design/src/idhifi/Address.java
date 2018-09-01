package idhifi;

/**
 * Utility class for storing address details and putting them into a json compatible string
 */
public class Address {
    @Override
    public String toString() {
        return "Address{" +
                "mName='" + mName + '\'' +
                ", mZipCode='" + mZipCode + '\'' +
                ", latitude=" + latitude +
                ", longtitude=" + longtitude +
                ", altitude=" + altitude +
                '}';
    }

    public Address(String mName, String mZipCode, double latitude, double longtitude, double altitude) {
        this.mName = mName;
        this.mZipCode = mZipCode;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.altitude = altitude;
    }

    private String mName;
    private String mZipCode;
    private double latitude;
    private double longtitude;
    private double altitude;

    public String getmName() {
        return mName;
    }

    public String getmZipCode() {
        return mZipCode;
    }


    public double getLongtitude() {
        return longtitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getAltitude() {
        return altitude;
    }

}
