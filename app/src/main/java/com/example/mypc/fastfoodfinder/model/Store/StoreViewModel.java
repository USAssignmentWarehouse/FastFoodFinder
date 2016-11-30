package com.example.mypc.fastfoodfinder.model.Store;

import android.location.Location;
import android.support.annotation.DrawableRes;

import com.example.mypc.fastfoodfinder.R;
import com.example.mypc.fastfoodfinder.utils.MapUtils;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by nhoxb on 11/9/2016.
 */
public class StoreViewModel {
    public String getStoreName() {
        return mStoreName;
    }

    public double getStoreDistance() {
        return mStoreDistance;
    }

    public String getStoreAddress() {
        return mStoreAddress;
    }

    public int getDrawableLogo() {
        return mDrawableLogo;
    }

    private String mStoreName;
    private double mStoreDistance;
    private String mStoreAddress;
    private int mDrawableLogo;
    private LatLng mPosition;

    public StoreViewModel(@DrawableRes int drawableLogo, String storeName, double storeDistance, String storeAddress, LatLng position) {
        mDrawableLogo = drawableLogo;
        mStoreName = storeName;
        mStoreDistance = storeDistance;
        mStoreAddress = storeAddress;
        mPosition = position;
    }

    public StoreViewModel(Store store, LatLng currCameraPosition)
    {
        //Must refactor
        mStoreName = store.getTitle();
        mStoreAddress = store.getTitle();
        mDrawableLogo = MapUtils.getLogoDrawableId(store.getType());
        mPosition = store.getPosition();

        //
        Location start = new Location("pointA");
        start.setLatitude(currCameraPosition.latitude);
        start.setLongitude(currCameraPosition.longitude);
        Location end = new Location("pointB");
        end.setLatitude(Double.parseDouble(store.getLat()));
        end.setLongitude(Double.parseDouble(store.getLng()));

        mStoreDistance = (start.distanceTo(end)/1000.0);

    }

    public LatLng getPosition() {
        return mPosition;
    }
}