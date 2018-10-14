package cau.study_202;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*로그인 눌렀을시 넘어가도록 설정해놓은거 */
        Button loginButton = (Button)findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(MainActivity.this,
                        HomeActivity.class);
                startActivity(i);
                finish();
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
