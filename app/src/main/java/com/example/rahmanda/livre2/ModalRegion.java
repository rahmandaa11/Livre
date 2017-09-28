package com.example.rahmanda.livre2;

/**
 * Created by rahmanda on 9/27/17.
 */

public class ModalRegion {
    String locationId;
    String locationName;

    public ModalRegion(){

    }

    public ModalRegion(String locationId, String locationName) {
        this.locationId = locationId;
        this.locationName = locationName;
    }

    public String getLocationId() {
        return locationId;
    }

    public String getLocationName() {
        return locationName;
    }

}
