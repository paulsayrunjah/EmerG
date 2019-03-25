package com.groupbsse.ourapp.classes;

/**
 * Created by Sayrunjah on 10/02/2017.
 */
public class MyLocation {
    String locationname,district,country,featurename;

    public MyLocation(String locationname, String district, String country,String featurename) {
        this.locationname = locationname;
        this.district = district;
        this.country = country;
        this.featurename = featurename;
    }

    public String getLocationname() {
        return locationname;
    }

    public String getFeaturename() {
        return featurename;
    }

    public String getDistrict() {
        return district;
    }

    public String getCountry() {
        return country;
    }
}
