package cau.study_202;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import cau.study_202.network.NetworkUtil;
import cau.study_202.network.Phprequest;


public class MainActivity extends AppCompatActivity {
    private EditText editID, editPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NetworkUtil.setNetworkPolicy();

        editID = (EditText) findViewById(R.id.userEmail);
        editPassword = (EditText) findViewById(R.id.userPassword);

        /*로그인 눌렀을시 넘어가도록 설정해놓은거 */
        Button loginButton = (Button)findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // 아이디 입력 확인
                if (editID.getText().toString().length() == 0) {
                    Toast.makeText(getApplication(), "아이디를 입력하세요", Toast.LENGTH_SHORT).show();
                    editID.requestFocus();
                    return;
                }

                // 비밀번호 입력 확인
                if (editPassword.getText().toString().length() == 0) {
                    Toast.makeText(getApplication(), "비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
                    editPassword.requestFocus();
                    return;
                }
                login_network();
            }
        });

        /*회원가입 눌렀을시 넘어가도록 설정해놓은거 */
        Button registButton = (Button)findViewById(R.id.registButton);
        registButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(MainActivity.this,
                        RegistActivity.class);
                startActivity(i);
            }
        });
    }

    private class GetData extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);

        String errorString = null;

        @Override
        protected void onPreExecute() {
            Log.i("로그인오류","onPostExecute진입전");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMessage("인터넷 연결을 재시도 중입니다.");
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            login_network();
        }


        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                for (int i = 0; i < 100; i++) {
                    progressDialog.setProgress(i);
                    Thread.sleep(10);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private void login_network(){
        try {
            Phprequest request = new Phprequest(Phprequest.BASE_URL +"login.php");
            String result = request.login(String.valueOf(editID.getText()),String.valueOf(editPassword.getText()));
            if(result.equals("1")){
                Toast.makeText(getApplication(),"비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show();
            }
            else if(result.equals("0")){
                Toast.makeText(getApplication(),"존재하지 않는 아이디 입니다.",Toast.LENGTH_SHORT).show();
            }
            else {
                /* 여기도 php 통신해서 leaderid, groupid 가져와서 Loginstatus에 저장 해야함 로그인 정보를 통해서 판단*/
                LoginStatus.setMemberID(String.valueOf(editID.getText()));
                try{
                    inputloginstatus(result,String.valueOf(editID.getText()));
                }catch (IllegalArgumentException e) {
                    LoginStatus.setGroupID(-1);
                }
                /*groupID 제대로 찍히는지 test*/
                /*Toast.makeText(getApplication(),LoginStatus.getGroupID()+String.valueOf(LoginStatus.isLeader()),Toast.LENGTH_SHORT).show();*/
                Intent i = new Intent(MainActivity.this,
                        HomeActivity.class);
                startActivity(i);
                finish();
            }
        }catch (MalformedURLException e){
            Log.i("로그인오류","try-catch문 에러");
            e.printStackTrace();
        }catch (NullPointerException e){
            Log.i("로그인오류","try-catch문 NULLPOINTER에러");
            GetData task = new GetData();
            task.execute();
            e.printStackTrace();
        }
    }

    private void inputloginstatus(String result,String ID) {

        String TAG_JSON = "loginstatus";
        String TAG_LEADERID = "leaderid";
        String TAG_GROUPID = "groupid";

        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            JSONObject item = jsonArray.getJSONObject(0);
            String leaderid = item.getString(TAG_LEADERID);
            String groupid = item.getString(TAG_GROUPID);
            LoginStatus.setGroupID(Integer.parseInt(groupid));
            if (leaderid.equals(ID)) {
                LoginStatus.setLeader();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

