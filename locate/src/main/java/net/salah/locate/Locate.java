package net.salah.locate;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;

/**
 * Created by Salah Nahed on 4/12/17.
 */

public class Locate {

    static LocationResult listener;



    private Locate(LocationBuilder locationBuilder){
        WeakReference<Activity> context = locationBuilder.context;
        LocationConfig locationConfig = locationBuilder.locationConfig;


        if (Helper.isGpsEnabled(context.get())){
            LocateFragment locateFragment = new LocateFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("locationConfig",locationConfig);
            locateFragment.setArguments(bundle);
            context.get()
                    .getFragmentManager()
                    .beginTransaction()
                    .add(locateFragment,"locationConfig")
                    .commit();
        }else {
            Intent intent = new Intent(context.get(),LocateActivity.class);
            intent.putExtra("locationConfig",locationConfig);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            context.get().startActivity(intent);
            context.get().overridePendingTransition(0,0);
        }
    }


    public static LocationBuilder with(Activity context){
        return new LocationBuilder(context);
    }


    enum Priority {
        PRIORITY_HIGH_ACCURACY(100), PRIORITY_BALANCED_POWER_ACCURACY(102), PRIORITY_LOW_POWER(104), PRIORITY_NO_POWER(105);
        private final int value;

        Priority(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }


    public interface LocationResult {

        void onGetLocationSuccess(double lat, double lng);

        void onGetLocationFailed(@Nullable String message);

    }

    public static final class LocationBuilder implements LocationBuilderBase {

        private final WeakReference<Activity> context;
        private LocationConfig locationConfig;

        private LocationBuilder(Activity context) {
            this.context = new WeakReference<>(context);
            locationConfig = new LocationConfig();
        }


        @Override
        public LocationBuilder debuggingMode(boolean debug) {
            locationConfig.debug = debug;
            return this;
        }

        @Override
        public LocationBuilder setPriority(Priority priority) {
            locationConfig.priority = priority;
            return this;
        }

        @Override
        public LocationBuilder setInterval(long interval) {
            locationConfig.interval = interval;
            return this;
        }

        @Override
        public LocationBuilder setFastInterval(long fastInterval) {
            locationConfig.fastestInterval = fastInterval;
            return this;
        }

        @Override
        public LocationBuilder addLocationResultListener(@NonNull  LocationResult listener) {
            Locate.listener = listener;
            return this;
        }

        @Override
        public Locate build() {
           return new Locate(this);
        }
    }

}
