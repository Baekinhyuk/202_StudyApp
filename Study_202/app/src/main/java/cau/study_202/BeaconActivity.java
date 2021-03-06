package cau.study_202;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;

import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.View;
import android.content.Intent;
import android.app.Activity;
import android.widget.Toast;
import android.os.Handler;

import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import cau.study_202.network.Phprequest;

public class BeaconActivity extends AppCompatActivity {

    BluetoothAdapter mBluetoothAdapter;

    //블루투스 요청 액티비티 코드
    //final static int BLUETOOTH_REQUEST_CODE = 100;

    BluetoothLeScanner mBluetoothLeScanner;

    BluetoothLeAdvertiser mBluetoothLeAdvertiser;

    private static final int PERMISSIONS = 100;

    Vector<Beacon> beacon;

    //ListView beaconListView;

    TextView beaconstate;

    TextView beaconabove;

    ImageView beaconImage;

    int state;

    //초기상태는 0 , 출석가능한비콘발견 1, 출석 후 비콘2
    private int attbeacon = 0;

    ScanSettings.Builder mScanSettings;

    List<ScanFilter> scanFilters;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.KOREAN);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS);
        //beaconListView = (ListView) findViewById(R.id.beaconListView);
        beaconstate = (TextView)findViewById(R.id.beacon);
        beaconabove = (TextView)findViewById(R.id.beacon_above);
        beaconImage = (ImageView) findViewById(R.id.beaconbutton);

        //Log.d("state받기 전",Integer.toString(state));
        state = getIntent().getIntExtra("state",2);
        //Log.d("state받은 후",Integer.toString(state));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //블루투스자동연결
        if(!mBluetoothAdapter.isEnabled()){
            mBluetoothAdapter.enable();
        }

        mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
        mBluetoothLeAdvertiser = mBluetoothAdapter.getBluetoothLeAdvertiser();
        beacon = new Vector<>();
        mScanSettings = new ScanSettings.Builder();
        mScanSettings.setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY);
        final ScanSettings scanSettings = mScanSettings.build();

        scanFilters = new Vector<>();
        ScanFilter.Builder scanFilter = new ScanFilter.Builder();
        scanFilter.setDeviceAddress("C2:01:01:00:00:61"); //ex) 00:00:00:00:00:00 내비콘에 Mac주소 입력
        ScanFilter scan = scanFilter.build();
        scanFilters.add(scan);

        Handler delayHandler = new Handler();
        delayHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mBluetoothLeScanner.startScan(scanFilters, scanSettings, mScanCallback);
            }
        }, 3000);

        beaconImage.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                //비콘다시 검색
                if(attbeacon == 0) {
                    //onResume();
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
                if(attbeacon == 1){
                    //여기서 서버에 출석정보전달
                    try {
                        Phprequest request = new Phprequest(Phprequest.BASE_URL +"beacon_att.php");
                        String result = request.beacon_attendence(LoginStatus.getMemberID(),Integer.toString(LoginStatus.getGroupID()),Integer.toString(state));
                        //비콘연결성공 및 출석하는 거 구현
                        if(state == 0){
                            beaconabove.setText("출석하였습니다.");
                        }
                        else if(state ==1){
                            beaconabove.setText("지각하였습니다.");
                        }
                        beaconImage.setImageResource(R.drawable.check_beacon);
                        attbeacon = 2;
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                        beaconabove.setText("출석에 실패하였습니다 다시시도바랍니다.");
                    }
                }
            }
        });
    }

    public ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            try {
                ScanRecord scanRecord = result.getScanRecord();
                Log.d("getTxPowerLevel()", scanRecord.getTxPowerLevel() + "");
                Log.d("onScanResult()", result.getDevice().getAddress() + "\n" + result.getRssi() + "\n" + result.getDevice().getName()
                        + "\n" + result.getDevice().getBondState() + "\n" + result.getDevice().getType());

                final ScanResult scanResult = result;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                beacon.add(0, new Beacon(scanResult.getDevice().getAddress(), scanResult.getRssi(), simpleDateFormat.format(new Date())));
                                //beaconAdapter = new BeaconAdapter(beacon, getLayoutInflater());
                                //beaconListView.setAdapter(beaconAdapter);
                                //beaconAdapter.notifyDataSetChanged();
                                beaconstate.setText("비콘스캔 결과 = "+scanResult.getDevice().getAddress()+" \nRSSi = "+scanResult.getRssi());
                                if(attbeacon != 2){
                                    beaconabove.setText("출석가능한 비콘을 발견했습니다.");
                                    beaconImage.setImageResource(R.drawable.connect_beacon);
                                    attbeacon = 1;
                                }
                            }
                        });
                    }
                }).start();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
            Log.d("onBatchScanResults", results.size() + "");
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            Log.d("onScanFailed()", errorCode + "");
        }
    };


    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mBluetoothLeScanner.stopScan(mScanCallback);
        mBluetoothAdapter.disable();
    }
}
