package cau.study_202;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class Search_Studyboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchstudy);

        //화면회전 금지
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);

        //Toolbar 적용하기 위해서 .HomeActivity Theme 제거(AndroidManifest에서)
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_board);
        setSupportActionBar(toolbar);

        Button button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    // 툴바 제어
    void initialize_toolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_board); // Attaching the layout to the toolbar object
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        /*
        //뒤로가기 버튼 구현한거
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.icon_back);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        tv_title.setText("스터디 구인 게시판");*/
    }
}
