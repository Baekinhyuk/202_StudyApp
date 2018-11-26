package cau.study_202;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;

import cau.study_202.network.Phprequest;

public class GpsActivity extends AppCompatActivity {
    private Activity activity;
    double latitude;
    double longitude;
    double save_latitude;
    double save_longitude;
    int state;

    TextView gpsabove;
    ImageView gpsImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);
        gpsabove = (TextView)findViewById(R.id.GPS_above);
        gpsImage = (ImageView) findViewById(R.id.GPS_btn);
        state = getIntent().getIntExtra("state",2);
        Log.i("GPS STATE 값 : ",Integer.toString(state));
    }

    @Override
    protected void onResume() {
        super.onResume();

        startLocationServiece();
        Handler delayHandler = new Handler();
        delayHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                check_GPS(state);
            }
        }, 3000);

    }

    public void startLocationServiece() {
        Log.i("startLocationServicet시작","시작");
        final LocationManager manager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }
            Log.i("startLocationServicet시작","이부분3");
            return;
        }
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
                new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        Log.i("startLocationServicet시작","이부분2");
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        //Toast.makeText(getApplication(), "GPS\n"+"위도 : " + latitude + "\n 경도 : " + longitude, Toast.LENGTH_SHORT).show();
                        LocationManager lm = (LocationManager)getSystemService(Context. LOCATION_SERVICE);
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
                        Log.i("startLocationServicet시작","이부분1");
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        //Toast.makeText(getApplication(), "NETWORK\n"+"위도 : " + latitude + "\n 경도 : " + longitude, Toast.LENGTH_SHORT).show();
                        LocationManager lm = (LocationManager)getSystemService(Context. LOCATION_SERVICE);
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

    private static double distance(double lat1, double lon1, double lat2, double lon2) {

        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        dist = dist * 1609.344;

        return (dist);
    }

    // This function converts decimal degrees to radians
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    // This function converts radians to decimal degrees
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    private void get_saveGPS(String result) {

        String TAG_JSON="GPS_state";
        String TAG_Latitude = "Latitude";
        String TAG_Longitude = "Longitude";

        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            JSONObject item = jsonArray.getJSONObject(0);

            String s_latitude = item.getString(TAG_Latitude);
            String s_longitude = item.getString(TAG_Longitude);

            save_latitude = Double.parseDouble(s_latitude);
            save_longitude = Double.parseDouble(s_longitude);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void check_GPS(int state){
        try {
            Phprequest request = new Phprequest(Phprequest.BASE_URL +"get_GPS.php");
            String get_GPS = request.get_GPS(LoginStatus.getMemberID());
            Log.i("테스트 GPS 리턴값get_GPS값",get_GPS);
            if (!get_GPS.equals("-1")) {
                get_saveGPS(get_GPS);
                double distantMeter = distance(latitude, longitude, save_latitude, save_longitude);
                //Toast.makeText(getActivity(),Double.toString(distantMeter), Toast.LENGTH_SHORT).show();
                Log.i("GPS 위도결과","latitude : "+Double.toString(latitude)+" longitude : "+Double.toString(longitude)+" save_latitude : "+Double.toString(save_latitude)+" save_longitude : "+Double.toString(save_longitude));
                Log.i("테스트 GPS거리계산",Double.toString(distantMeter));
                if (distantMeter >= 0) {
                    try {
                        Phprequest request2 = new Phprequest(Phprequest.BASE_URL + "GPS_att.php");
                        String get_GPS2 = request2.GPS_attendence(LoginStatus.getMemberID(), Integer.toString(LoginStatus.getGroupID()), Integer.toString(state));
                    } catch (MalformedURLException e1) {
                        e1.printStackTrace();
                    }
                    if(state == 1) {
                        gpsabove.setText("지각하였습니다");
                        gpsImage.setImageResource(R.drawable.check_beacon);
                    }
                    else{
                        gpsabove.setText("출석하였습니다");
                        gpsImage.setImageResource(R.drawable.check_beacon);
                    }
                } else {
                    gpsabove.setText("출석가능한 거리가 아닙니다");
                }
            }
            else{
                gpsabove.setText("저장되어있는 위치값이 없습니다");
            }
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
    }
}
