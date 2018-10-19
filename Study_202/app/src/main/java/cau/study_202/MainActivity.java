package cau.study_202;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.MalformedURLException;
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
                        /* 여기도 php 통신해서 nickname, groupid 가져와서 Loginstatus에 저장해야함*/
                        LoginStatus.setMemberID(String.valueOf(editID.getText()));
                        try{
                            LoginStatus.setGroupID(Integer.parseInt(result));
                        }catch (IllegalArgumentException e) {
                            LoginStatus.setGroupID(-1);
                        }
                        Log.i("login status", "groupid"+LoginStatus.getGroupID()+" isLedaer" + LoginStatus.isLeader());
                        /*groupID 제대로 찍히는지 test*/
                        //Toast.makeText(getApplication(),String.valueOf(LoginStatus.getGroupID()),Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(MainActivity.this,
                                HomeActivity.class);
                        startActivity(i);
                        finish();
                    }
                }catch (MalformedURLException e){
                    e.printStackTrace();
                }

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
}
