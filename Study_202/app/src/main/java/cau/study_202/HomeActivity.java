package cau.study_202;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeActivity  extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

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

            ImageView mystudy=(ImageView) findViewById(R.id.mystudy);
            mystudy.setOnClickListener(new Button.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(HomeActivity.this,
                            GroupActivity.class);
                    startActivity(i);
                }
            });
            /*Button button4=(Button) findViewById(R.id.button4);
            button4.setOnClickListener(new Button.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(HomeActivity.this,BoardList.class);
                    startActivity(i);
                }
            });*/
            ImageView joinGroup=(ImageView) findViewById(R.id.join_group);
            joinGroup.setOnClickListener(new Button.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(HomeActivity.this,BoardList.class);
                    startActivity(i);
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


}