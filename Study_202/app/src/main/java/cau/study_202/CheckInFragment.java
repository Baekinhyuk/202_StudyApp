package cau.study_202;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import cau.study_202.network.Phprequest;
/**
 * A simple {@link Fragment} subclass.
 */
public class CheckInFragment extends Fragment {
    private Activity activity;
    String pd;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            activity = (Activity) context;
        }
    }
    public CheckInFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_check_in, container, false);
        Button photoButton = rootView.findViewById(R.id.check_to_photo);
        Button beaconButton = rootView.findViewById(R.id.check_to_bluetooth);
        Button gpsButton = rootView.findViewById(R.id.check_to_gps);
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makepd(0);
                CheckIn task = new CheckIn();
                task.execute( Phprequest.BASE_URL+"check_in.php","");
            }
        });
        beaconButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makepd(1);
                CheckIn task = new CheckIn();
                task.execute( Phprequest.BASE_URL+"check_in.php","");
            }
        });
        gpsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLocationServiece();
            }
        });
        return rootView;
    }
    private void makepd(int method) {
        pd = "ID="+LoginStatus.getMemberID()+"&"
                +"GROUPID="+LoginStatus.getGroupID()+"&"
                +"METHOD="+method;
    }
    public class CheckIn extends AsyncTask<String, Void, String> {
        String errorString = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(result.equals("0")){
                Toast.makeText(activity, "출석", Toast.LENGTH_SHORT).show();
            }
            else if(result.equals("1")){
                Toast.makeText(activity,"지각",Toast.LENGTH_SHORT).show();
            } else if(result.equals("2")){
                Toast.makeText(activity,"결석",Toast.LENGTH_SHORT).show();
            } else if(result.equals("3")) {
                Toast.makeText(activity, "출석은 한시간 전부터 가능합니다.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(activity, "이미 출석하셨습니다.", Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        protected String doInBackground(String... params) {
            String serverURL = params[0];
            try {
                URL url = new URL(serverURL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setConnectTimeout(15000);
                conn.setDoOutput(true);
                conn.setDoInput(true);
                OutputStream outputStream = conn.getOutputStream();
                outputStream.write(pd.getBytes("utf-8"));
                outputStream.flush();
                outputStream.close();
                String result = readStream(conn.getInputStream());
                conn.disconnect();
                Log.i("PHPRequest", result);
                return result;
            } catch (Exception e) {
                Log.d("thread", "GetData : Error ", e);
                errorString = e.toString();
                return null;
            }
        }
        private String readStream(InputStream in) throws IOException {
            StringBuilder jsonHtml = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
            String line = null;
            while((line = reader.readLine()) != null)
                jsonHtml.append(line);
            reader.close();
            return jsonHtml.toString();
        }
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