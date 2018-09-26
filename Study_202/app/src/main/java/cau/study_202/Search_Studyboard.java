package cau.study_202;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import cau.study_202.member.MemberActivity;

public class Search_Studyboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    private EditText input_id;
    private EditText input_pwd;
    private EditText input_title;
    private EditText input_content;
    TextView tv_title;
    ImageButton btn_write;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchstudy);

        //화면회전 금지
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);

        //Toolbar 적용하기 위해서 .HomeActivity Theme 제거(AndroidManifest에서)
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_board);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_board);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        /*
        input_id = findViewById(R.id.input_id);
        input_pwd = findViewById(R.id.input_pwd);
        input_title = findViewById(R.id.input_title);
        input_content = findViewById(R.id.input_content);*/

        /*
        tv_title = (TextView) findViewById(R.id.tv_title);
        btn_write = (ImageButton) findViewById(R.id.btn_write);
        View btn_delete = (ImageButton) findViewById(R.id.btn_delete); //글 삭제 버튼  없애기
        btn_delete.setVisibility(View.GONE);
        */

        //공유버튼 클릭시! alert창
        /*btn_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(WriteTalkActivity.this);
                alert_confirm.setMessage("글을 게시하겠습니까?").setCancelable(false).setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                post();
                            }
                        }).setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        });
                AlertDialog alert = alert_confirm.create();
                alert.show();

            }
        });
*/
        //initialize_toolbar();
        //index();
    }

/*
    public void post() {

        String t_user_id = input_id.getText().toString();
        String t_pwd = input_pwd.getText().toString();
        String t_title = input_title.getText().toString();
        String t_content = input_content.getText().toString();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        APIservice retrofitService = retrofit.create(APIservice.class);
        Call<Post_CallBackItem> call = retrofitService.writeTalk(t_user_id, t_pwd, t_title, t_content, 0);

        call.enqueue(new Callback<Post_CallBackItem>() {
            @Override
            public void onResponse(Call<Post_CallBackItem> call, Response<Post_CallBackItem> response) {
                Log.d("--------------성공!", response.body().toString());
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onFailure(Call<Post_CallBackItem> call, Throwable t) {
                Log.e("Not Response", t.getMessage());
            }
        });
    }
*/

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


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_board);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_right, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_searchstudy) {
            Intent i = new Intent(Search_Studyboard.this,
                    Search_Studyboard.class);
            startActivity(i);
            finish();
            // Handle the camera action
        } else if (id == R.id.nav_viewstudy) {

        } else if (id == R.id.nav_member) {
            Intent i = new Intent(Search_Studyboard.this,
                    MemberActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_attendence) {

        } else if (id == R.id.drawer_menu_profile) {

        } else if (id == R.id.drawer_menu_login) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_board);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
