package net.salah.locate;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

public class LocateFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, ResultCallback<LocationSettingsResult> {

    private static final int PERMISSION_CALLBACK_CONSTANT = 12332;
    private static final int REQUEST_LOCATION = 0x2;
    private static final int CONNECTION_RESULT = 0x8;
    private GoogleApiClient client;
    private LocationConfig locationConfig;
    private String ConnectionFailedMessage;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationConfig = (LocationConfig) getArguments().getSerializable("locationConfig");
        buildGoogleApiClient();
    }


    private void buildGoogleApiClient() {
        client = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private void createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();

        mLocationRequest.setInterval(locationConfig.interval);

        mLocationRequest.setFastestInterval(locationConfig.fastestInterval);

        mLocationRequest.setPriority(locationConfig.priority.getValue());

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(client, builder.build());

        result.setResultCallback(this);
    }



    private void getMyLocation() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(getString(R.string.needPermissions));
                    builder.setMessage(getString(R.string.whatPermissions));
                    builder.setPositiveButton(getString(R.string.grant), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_CALLBACK_CONSTANT);
                            }
                        }
                    });
                    builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();

                }else{
                   requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_CALLBACK_CONSTANT);
                }
            }else {
                Location mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(client);
                if (mCurrentLocation != null) {
                    double lat = mCurrentLocation.getLatitude();
                    double lng = mCurrentLocation.getLongitude();
                    Locate.listener.onGetLocationSuccess(lat,lng);
                }else{
                    Locate.listener.onGetLocationFailed(getString(R.string.failed));
                }
                finish();
            }
        }else {
            Location mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(client);
            if (mCurrentLocation != null) {
                double lat = mCurrentLocation.getLatitude();
                double lng = mCurrentLocation.getLongitude();
                Locate.listener.onGetLocationSuccess(lat,lng);
            }else {
                Locate.listener.onGetLocationFailed(getString(R.string.failed));
            }
        }

     }

    private void finish() {
        getFragmentManager().popBackStack();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        createLocationRequest();
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (i == 1){
            Locate.listener.onGetLocationFailed(getString(R.string.CAUSE_NETWORK_LOST));
        }else if (i == 2){
            Locate.listener.onGetLocationFailed(getString(R.string.CAUSE_SERVICE_DISCONNECTED));
        }
        finish();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        ConnectionFailedMessage = connectionResult.getErrorMessage();
        try {
            connectionResult.startResolutionForResult(getActivity(),CONNECTION_RESULT);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                getMyLocation();
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                try {
                    status.startResolutionForResult(getActivity(), REQUEST_LOCATION);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                Locate.listener.onGetLocationFailed(getString(R.string.SETTINGS_CHANGE_UNAVAILABLE));
                break;
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        Locate.listener.onGetLocationSuccess(lat,lng);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,@NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CALLBACK_CONSTANT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                   getMyLocation();
            }else {
                Toast.makeText(getActivity(), getString(R.string.unablePermission), Toast.LENGTH_LONG).show();
                Locate.listener.onGetLocationFailed(getString(R.string.unablePermission));
                finish();
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOCATION){
            if (resultCode == Activity.RESULT_OK){
                getMyLocation();
            }else {
                Locate.listener.onGetLocationFailed(getString(R.string.SETTINGS_CHANGE_UNAVAILABLE));
            }
         }
        if (requestCode == CONNECTION_RESULT){
            if (resultCode == Activity.RESULT_OK){
                client.connect();
            }else {
                Locate.listener.onGetLocationFailed(ConnectionFailedMessage);
            }
        }

    }


    @Override
    public void onStart() {
        super.onStart();
        client.connect();
    }


    @Override
    public void onStop() {
        super.onStop();
        client.disconnect();
    }


}



