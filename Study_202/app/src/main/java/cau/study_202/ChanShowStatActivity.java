package cau.study_202;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import cau.study_202.network.Phprequest;

public class ChanShowStatActivity extends AppCompatActivity {

    Intent intent;
    String id;
    String pd;
    int fine;
    TextView fineText;
    EditText fineEditText;

    ArrayList<Stat> stats;
    ChanDetailStatsAdapter adapter;
    AlertDialog.Builder alertdialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chan_show_stat);

        alertdialog = new AlertDialog.Builder(this);

        stats = new ArrayList<Stat>();
        adapter = new ChanDetailStatsAdapter(this, stats);

        ListView listView = findViewById(R.id.detail_stat_list);
        listView.setAdapter(adapter);

        intent = getIntent();
        id = intent.getStringExtra("id");

        GetStatData task = new GetStatData();
        task.execute(Phprequest.BASE_URL + "fetch_detail_stat.php?GROUPID=" + LoginStatus.getGroupID() + "&ID=" + id, "");

        TextView title = findViewById(R.id.title);
        title.setText(id + "의 출결 내역");

        if (LoginStatus.isLeader()) {
            fineText = findViewById(R.id.fine);
            fineEditText = findViewById(R.id.input_fine);

            GetFine task2 = new GetFine();
            task2.execute(Phprequest.BASE_URL + "fetch_fine.php?ID=" + id, "");

            Button button = findViewById(R.id.caculate);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int input = Integer.parseInt(fineEditText.getText().toString());
                    if ( input > fine || input == 0) {
                        Toast.makeText(getApplicationContext(), "벌금을 정확히 입력해주세요.", Toast.LENGTH_SHORT).show();
                    } else {
                        alertdialog.setMessage("정산하시겠습니까?");
                        // 확인버튼
                        alertdialog.setPositiveButton("확인", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Calculate task = new Calculate();
                                pd = "ID="+id+"&VALUE="+input;
                                task.execute( Phprequest.BASE_URL+"calculate_fine.php","");


                            }
                        });
                        // 취소버튼
                        alertdialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        // 메인 다이얼로그 생성
                        AlertDialog alert = alertdialog.create();
                        alert.show();
                    }
                }
            });




        } else {
            LinearLayout linearLayout = findViewById(R.id.bottom);
            linearLayout.setVisibility(View.GONE);
        }





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
                if (httpURLConnection != null) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
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

    public void showResult(String mJsonString) {

        String TAG_JSON = "attendence";
        String TAG_METHOD = "method";
        String TAG_DATETIME = "datetime";
        String TAG_STATE = "state";
        String TAG_FINE = "fine";

        try {

            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject item = jsonArray.getJSONObject(i);
                String method = item.getString(TAG_METHOD);
                String datetime = item.getString(TAG_DATETIME);
                String state = item.getString(TAG_STATE);
                String fine = item.getString(TAG_FINE);

                Stat StatData = new Stat(id, Integer.parseInt(method), datetime, Integer.parseInt(state), Integer.parseInt(fine));

                stats.add(StatData);
                adapter.notifyDataSetChanged();
            }

        } catch (JSONException e) {

            Log.d("listview에 추가", "showResult : ", e);
        }

    }

    public class GetFine extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            fineText.setText(result);
            fineEditText.setText(result);
            fine = Integer.parseInt(result);
            Log.i("result", result);
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
                if (httpURLConnection != null) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
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

    public class Calculate extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result.equals("1")) {
                Toast.makeText(getApplicationContext(), "처리 되었습니다.", Toast.LENGTH_SHORT).show();
            }

                GetFine task = new GetFine();
                task.execute(Phprequest.BASE_URL + "fetch_fine.php?ID=" + id, "");
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

}
