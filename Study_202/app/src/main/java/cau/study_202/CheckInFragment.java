package cau.study_202;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class CheckInFragment extends Fragment {


    public CheckInFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_check_in, container, false);

        Button gps = (Button) rootView.findViewById(R.id.check_to_gps);
        gps.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                startLocationServiece();
            }
        });
        return rootView;
    }

    public void startLocationServiece() {
        LocationManager manager = (LocationManager) getLayoutInflater().getContext().getSystemService(Context.LOCATION_SERVICE);
        Activity root = getActivity();

        if (ActivityCompat.checkSelfPermission(root, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(root, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }
            return;
        }

        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
                new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        Activity root = getActivity();

                        Toast.makeText(root, "GPS\n"+"위도 : " + latitude + "\n 경도 : " + longitude, Toast.LENGTH_SHORT).show();
                        LocationManager lm = (LocationManager)getLayoutInflater().getContext().getSystemService(Context. LOCATION_SERVICE);
                        // Stop the update as soon as get the location.
                        lm.removeUpdates(this);
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                });

        manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,
                new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        Activity root = getActivity();
                        Toast.makeText(root, "NETWORK\n"+"위도 : " + latitude + "\n 경도 : " + longitude, Toast.LENGTH_SHORT).show();

                        LocationManager lm = (LocationManager)getLayoutInflater().getContext().getSystemService(Context. LOCATION_SERVICE);
                        // Stop the update as soon as get the location.
                        lm.removeUpdates(this);
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                });
    }
}
