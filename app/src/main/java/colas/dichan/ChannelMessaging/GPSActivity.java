package colas.dichan.ChannelMessaging;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
//import android.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;


public class GPSActivity extends ActionBarActivity implements LocationListener {
    private GoogleApiClient mGoogleApiClient;
    private Location mCurrentLocation;
    private boolean updateActive;
    private static int MY_PERMISSION_REQUEST_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        System.out.println("on create");

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        ActivityCompat.requestPermissions(GPSActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSION_REQUEST_LOCATION);
                        GPSActivity.this.updateCoord();
                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                })
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED){
                //
            }else{
                finish();
            }
        }
    }



    public Location getLastCoord() {
        if (mGoogleApiClient.isConnected()) {
            /*
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return null;
            }*/
            //noinspection MissingPermission
            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            return lastLocation;
        } else {
            return null;
        }
    }

    public void updateCoord() {
        System.out.println("update coor");
        updateActive = true;

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        //Correspond à l’intervalle moyen de temps entre chaque mise à jour des coordonnées
        mLocationRequest.setFastestInterval(5000);
        //Correspond à l’intervalle le plus rapide entre chaque mise à jour des coordonnées
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //Définit la demande de mise à jour avec un niveau de précision maximal
        //noinspection MissingPermission
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mGoogleApiClient.isConnected() && updateActive == false)
            this.updateCoord();
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (LocationListener) this);
        updateActive = false;

    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        System.out.println(location);
        System.out.println("sidjfhsdhfsdkfqsdfqsdfsdqf");
    }
}
