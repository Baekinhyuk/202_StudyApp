package cau.study_202;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapView;

import java.net.MalformedURLException;

import cau.study_202.network.Phprequest;

public class Gps_attendant extends AppCompatActivity {

    TMapView tMapView = null;
    TMapGpsManager tMapGps = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps_attendant);


        tMapView = new TMapView(Gps_attendant.this);
        tMapGps = new TMapGpsManager(Gps_attendant.this);

        RelativeLayout relativeLayout = findViewById(R.id.relative);
        tMapView.setSKTMapApiKey("195b1e8a-7c4b-48cb-b1f6-81dc09eb1d1c");
        relativeLayout.addView(tMapView);
        tMapView.setMapType(TMapView.MAPTYPE_STANDARD);
        tMapView.setLanguage(TMapView.LANGUAGE_KOREAN);

        tMapView.setIconVisibility(true);
        tMapView.setZoomLevel(15);
        tMapView.setTrackingMode(true);
        setContentView(relativeLayout);

        Button button = findViewById(R.id.button01);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                setGps();
            }
        });
    }

    public void setGps(){
        final LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        LocationListener mLocationListener;
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치제공자(실내에선 NETWORK_PROVIDER 권장)
                1000, // 통지사이의 최소 시간간격 (miliSecond)
                1, // 통지사이의 최소 변경거리 (m)
                mLocationListener = new LocationListener(){
                    @Override
                    public void onLocationChanged(final Location location) {
                        if (location != null) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            tMapView.setLocationPoint(longitude, latitude);
                            tMapView.setCenterPoint(longitude, latitude);
                            tMapView.setZoomLevel(18);

                            LocationManager ln = (LocationManager)getLayoutInflater().getContext().getSystemService(Context. LOCATION_SERVICE);
                            // Stop the update as soon as get the location.
                            ln.removeUpdates(this);

                            Button button2 = findViewById(R.id.button02);
                            button2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    designate(location);
                                }
                            });
                        }
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

    public void designate(final Location location){
        AlertDialog.Builder ad = new AlertDialog.Builder(Gps_attendant.this);
        ad.setTitle("현재위치 설정");
        ad.setMessage("현재 위치로 지정하시겠습니까?\n"+"위도 : "+location.getLatitude()+"\n경도 : "+location.getLongitude());

        ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    Phprequest request = new Phprequest(Phprequest.BASE_URL +"saveGPS.php");
                    String result = request.save_GPS(LoginStatus.getMemberID(),Double.toString(location.getLatitude()),Double.toString(location.getLongitude()));
                }catch (MalformedURLException e){
                    e.printStackTrace();
                }
                finish();
                dialog.dismiss();     //닫기
                // Event
            }
        });

        ad.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();     //닫기
                // Event
            }
        });
        ad.show();
    }
}
