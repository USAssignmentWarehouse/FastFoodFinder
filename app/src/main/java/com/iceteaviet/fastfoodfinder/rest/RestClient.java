package com.iceteaviet.fastfoodfinder.rest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.iceteaviet.fastfoodfinder.activity.MapRoutingActivity;
import com.iceteaviet.fastfoodfinder.model.Routing.MapsDirection;
import com.iceteaviet.fastfoodfinder.model.Store.Store;
import com.iceteaviet.fastfoodfinder.utils.Keys;
import com.iceteaviet.fastfoodfinder.utils.RetrofitUtils;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by tamdoan on 13/07/2017.
 */

public class RestClient {
    public static String MAP_BASE_URL = "https://maps.googleapis.com/maps/api/directions/";
    private static RestClient mInstance = null;
    public String googlemapBrowserKey = "";
    private MapsDirectionApi mMapDirectionApi;

    private RestClient() {
        mMapDirectionApi = RetrofitUtils.get(googlemapBrowserKey, MAP_BASE_URL).create(MapsDirectionApi.class);
    }

    public static RestClient getInstance() {
        if (mInstance == null)
            mInstance = new RestClient();

        return mInstance;
    }

    public void showDirection(final Activity activity, Map<String, String> queries, final Store store) {
        mMapDirectionApi.getDirection(queries).enqueue(new Callback<MapsDirection>() {
            @Override
            public void onResponse(Call<MapsDirection> call, Response<MapsDirection> response) {
                Intent intent = new Intent(activity, MapRoutingActivity.class);
                Bundle extras = new Bundle();
                extras.putParcelable(Keys.KEY_ROUTE_LIST, response.body());
                extras.putParcelable(Keys.KEY_DES_STORE, store);
                intent.putExtras(extras);
                activity.startActivity(intent);

            }

            @Override
            public void onFailure(Call<MapsDirection> call, Throwable t) {
                Log.e("MAPP", "Get direction failed");
            }
        });
    }

    public void setGooglemapBrowserKey(String key) {
        if (key != null)
            this.googlemapBrowserKey = key;
    }
}
