package cau.study_202;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.Random;
import cau.study_202.network.Phprequest;

public class HomeActivity  extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private String quotecontent = "";
    private String quoteauthor = "";

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_home);

            //화면회전 금지
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);

            //Toolbar 적용하기 위해서 .HomeActivity Theme 제거(AndroidManifest에서)
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            TextView content = (TextView)findViewById(R.id.saying);
            TextView author = (TextView)findViewById(R.id.author);


        try {
            Phprequest request = new Phprequest(Phprequest.BASE_URL +"quote.php");
            Random random = new Random();
            String result = request.quote(String.valueOf((random.nextInt(24)+1)));
            inputquote(result);
            content.setText(quotecontent);
            author.setText(quoteauthor);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

            ImageView mystudy=(ImageView) findViewById(R.id.mystudy);
            mystudy.setOnClickListener(new Button.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(HomeActivity.this,
                            GroupActivity.class);
                    startActivity(i);
                }
            });

            ImageView searchstudy=(ImageView) findViewById(R.id.searchstudy);
            searchstudy.setOnClickListener(new Button.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(HomeActivity.this,Board.class);
                    startActivity(i);
                }
            });

        ImageView gps=(ImageView) findViewById(R.id.gps);
        gps.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ad = new AlertDialog.Builder(HomeActivity.this);
                ad.setTitle("현재위치 설정");
                ad.setMessage("출석 위치를 지정하시겠습니까?");

                ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(HomeActivity.this,Gps_attendant.class);
                        startActivity(i);

                        dialog.dismiss();     //닫기
                        // Event
                    }
                });

                ad.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();     //닫기
                        // Event
                    }
                });
                ad.show();
            }
        });

            ImageView logout=(ImageView) findViewById(R.id.logout);
            logout.setOnClickListener(new Button.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(HomeActivity.this, Intro.class);
                    startActivity(i);
                    LoginStatus.logout();
                    finish();
                }
            });

        }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

  /*  @Override
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
        if (id == R.id.action_delete) {
            return true;
        }
        else if(id == R.id.action_modify){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent i = new Intent(HomeActivity.this,
                    HomeActivity.class);
            startActivity(i);
            finish();

        } else if (id == R.id.nav_searchstudy) {
            Intent i = new Intent(HomeActivity.this,
                    BoardList.class);
            startActivity(i);

        } else if (id == R.id.nav_viewstudy) {
            Intent i = new Intent(HomeActivity.this,
                    GroupActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void inputquote(String result) {

        String TAG_JSON = "quote";
        String TAG_CONTENT = "content";
        String TAG_AUTHOR = "author";

        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            JSONObject item = jsonArray.getJSONObject(0);
            quotecontent = item.getString(TAG_CONTENT);
            quoteauthor = item.getString(TAG_AUTHOR);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}