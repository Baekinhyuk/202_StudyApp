package cau.study_202;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import cau.study_202.network.Phprequest;

public class ChanShowStatActivity extends AppCompatActivity {

    Intent intent;
    String id;
    String pd;


    ArrayList<Stat> stats;
    ChanDetailStatsAdapter adapter;

    @Override
    public void onResume() {
        super.onResume();
        Log.i("ChanShowStatActivity", "hi!");

        GetStatData task = new GetStatData();
        task.execute( Phprequest.BASE_URL+"fetch_detail_stat.php?GROUPID="+LoginStatus.getGroupID()+"&ID="+id,"");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chan_show_stat);

        intent = getIntent();
        id = intent.getStringExtra("id");

    }


    public class GetStatData extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            showResult(result);
        }

        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];
            HttpURLConnection httpURLConnection = null;

            try {

                URL url = new URL(serverURL);
                httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("GET");

                httpURLConnection.connect();

                InputStream inputStream;
                if(httpURLConnection != null) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString().trim();


            } catch (Exception e) {

                Log.d("http", "GetData : Error ", e);
                return null;
            }

        }
    }

    public void showResult(String mJsonString){

        String TAG_JSON="attendence";
        String TAG_METHOD = "method";
        String TAG_DATETIME = "datetime";
        String TAG_STATE = "state";
        String TAG_FINE = "fine";

        try {

            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);
                String method = item.getString(TAG_METHOD);
                String datetime = item.getString(TAG_DATETIME);
                String state = item.getString(TAG_STATE);
                String fine = item.getString(TAG_FINE);

                Stat StatData = new Stat(id,Integer.parseInt(method),datetime,Integer.parseInt(state),Integer.parseInt(fine));

                stats.add(StatData);
                adapter.notifyDataSetChanged();
            }

        } catch (JSONException e) {

            Log.d("listview에 추가", "showResult : ", e);
        }

    }
}
