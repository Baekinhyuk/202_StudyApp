package cau.study_202;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

import cau.study_202.network.Phprequest;

// 게시판 생성
public class Studyrequest extends AppCompatActivity {

    private String pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studyrequest);

        //화면회전 금지
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);

        //Toolbar 적용하기 위해서 .HomeActivity Theme 제거(AndroidManifest에서)
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_board);
        setSupportActionBar(toolbar);
        // 돌아가기 눌렀을때
        Button returnButton = (Button) findViewById(R.id.return_button);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // 생성하기 눌렀을때
        Button createButton = (Button) findViewById(R.id.create_button);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(LoginStatus.getGroupID() != -1) {
                    Toast.makeText(Studyrequest.this, "이미 가입된 그룹이 있습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }

                EditText editTitle = (EditText) findViewById(R.id.input_title);
                // 제목 입력 확인
                if (editTitle.getText().toString().length() == 0) {
                    Toast.makeText(Studyrequest.this, "제목을 입력하세요", Toast.LENGTH_SHORT).show();
                    editTitle.requestFocus();
                    return;
                }
                EditText editContent = (EditText) findViewById(R.id.input_contents);
                // 내용 입력 확인
                if (editContent.getText().toString().length() == 0) {
                    Toast.makeText(Studyrequest.this, "내용을 입력하세요", Toast.LENGTH_SHORT).show();
                    editContent.requestFocus();
                    return;
                }

                // 출석시간 입력 확인
                EditText editPresentTime = (EditText) findViewById(R.id.input_present_time);
                if (editPresentTime.getText().toString().length() == 0) {
                    Toast.makeText(Studyrequest.this, "출석 시간을 입력하세요", Toast.LENGTH_SHORT).show();
                    editPresentTime.requestFocus();
                    return;
                }
                // 출석시간 형식 확인
                if (!checkTimeForm(editPresentTime.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "출석 시간을 올바르게 입력하세요.", Toast.LENGTH_SHORT).show();
                    editPresentTime.requestFocus();
                    return;
                }
                // 지각시간 입력 확인
                EditText editLateTime = (EditText) findViewById(R.id.input_late_time);
                if (editLateTime.getText().toString().length() == 0) {
                    Toast.makeText(Studyrequest.this, "지각 시간을 입력하세요", Toast.LENGTH_SHORT).show();
                    editLateTime.requestFocus();
                    return;
                }
                // 지각시간 형식 확인
                if (!checkTimeForm(editLateTime.getText().toString()) || !checkLateTimeForm(editPresentTime.getText().toString(), editLateTime.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "지각 시간을 올바르게 입력하세요", Toast.LENGTH_SHORT).show();
                    editLateTime.requestFocus();
                    return;
                }
                // 지각벌금 입력 확인
                EditText editLateFine = (EditText) findViewById(R.id.input_late_fine);
                if (editLateFine.getText().toString().length() == 0) {
                    Toast.makeText(Studyrequest.this, "지각 벌금을 입력하세요", Toast.LENGTH_SHORT).show();
                    editLateFine.requestFocus();
                    return;
                }
                // 지각벌금 형식 확인
                if (!checkFineForm(editLateFine.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "지각 벌금을 올바르게 입력하세요.", Toast.LENGTH_SHORT).show();
                    editLateFine.requestFocus();
                    return;
                }
                // 결석벌금 입력 확인
                EditText editAbsentFine = (EditText) findViewById(R.id.input_absent_fine);
                if (editAbsentFine.getText().toString().length() == 0) {
                    Toast.makeText(Studyrequest.this, "결석 벌금을 입력하세요", Toast.LENGTH_SHORT).show();
                    editAbsentFine.requestFocus();
                    return;
                }
                // 결석벌금 형식 확인
                if (!checkFineForm(editAbsentFine.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "결석 벌금을 올바르게 입력하세요.", Toast.LENGTH_SHORT).show();
                    editAbsentFine.requestFocus();
                    return;
                }



                    String result = Phprequest.createStudy(editTitle.getText().toString(),
                            editContent.getText().toString(), editPresentTime.getText().toString()
                            , editLateTime.getText().toString(), editLateFine.getText().toString(),
                            editAbsentFine.getText().toString());
                    pd = result;




                GetData task = new GetData();
                task.execute(Phprequest.BASE_URL + "create_study.php", "");

            }
        });
    }

    private class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(Studyrequest.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            if(!result.equals("-1")){
                Toast.makeText(getApplication(),"정상적으로 생성되었습니다.",Toast.LENGTH_SHORT).show();
                LoginStatus.setGroupID(Integer.parseInt(result));
                Log.i("loginstatus", LoginStatus.getGroupID()+"");
                finish();
            }
            else{
                Toast.makeText(getApplication(),"생성과정에 문제가 발생했습니다.",Toast.LENGTH_SHORT).show();
                Log.i("PHPRequest", result);
            }


        }

        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];
            String postParameters = "country=" + params[1];


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


    public boolean checkTimeForm(String src) {
        String timeRegex = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$";
        return Pattern.matches(timeRegex, src);
    }

    public boolean checkLateTimeForm(String present, String late) {

        int phour = Integer.parseInt(present.substring(0,2));
        int lhour = Integer.parseInt(late.substring(0,2));
        if( phour > lhour){
            return false;
        } else if( phour == lhour){
            if (Integer.parseInt(present.substring(3,5)) > Integer.parseInt(late.substring(3,5))) {
                return false;
            }
        }
            return true;
    }

    public boolean checkFineForm(String src) {
        int fine;
        try {
            fine = Integer.parseInt(src);
        } catch (Exception e) {
            return false;
        }
        if (fine >= 0) // 0이상만 입력가능
            return true;
        else
            return false;
    }

}
