package cau.study_202;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
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

import java.security.acl.Group;

import cau.study_202.network.Phprequest;

public class BoardList extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private ArrayList<Board> items;
    private ChanBoardAdapter adapter;


    @Override
    public void onResume() {
        super.onResume();
        Log.i("fragment", "hi!");
        items.clear();
        GetBoardData task = new GetBoardData();
        task.execute(Phprequest.BASE_URL+"fetch_gboard.php","");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_boardlist);

        //화면회전 금지
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);

        //Toolbar 적용하기 위해서 .HomeActivity Theme 제거(AndroidManifest에서)
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_board);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_boardlist);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ImageButton button = (ImageButton) findViewById(R.id.btn_write);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Studyrequest.class);
                startActivity(intent);
            }
        });
        items = new ArrayList<Board>();
        adapter = new ChanBoardAdapter(this, items);




        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getApplicationContext(), Search_Studyboard.class); // intent 되는 activty에 알맞은 data 출력
                        Board currentBoard = items.get(position);
                        intent.putExtra("title", currentBoard.getTitle());
                        intent.putExtra("content", currentBoard.getContent());
                        intent.putExtra("author", currentBoard.getMemberId());
                        intent.putExtra("id", currentBoard.getId());
                        intent.putExtra("attendencetime", currentBoard.getAttendenctime());
                        intent.putExtra("attendencelatetime",currentBoard.getAttendencelatetime());
                        intent.putExtra("latefine",currentBoard.getLatefine());
                        intent.putExtra("absencefine",currentBoard.getAbsencefine());
                        startActivity(intent);
                    }
                });
    }

    // 툴바 제어
    void initialize_toolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_board); // Attaching the layout to the toolbar object
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_boardlist);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

/*    @Override
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
    }
*/
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent i = new Intent(BoardList.this,
                    HomeActivity.class);
            startActivity(i);
            finish();

        } else if (id == R.id.nav_searchstudy) {
            Intent i = new Intent(BoardList.this,
                    BoardList.class);
            startActivity(i);
            finish();

        } else if (id == R.id.nav_viewstudy) {
            Intent i = new Intent(BoardList.this,
                    GroupActivity.class);
            startActivity(i);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_boardlist);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class GetBoardData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

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
            String postParameters = "GROUPID=" + params[1];
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
                errorString = e.toString();

                return null;
            }

        }
    }

    public void showResult(String mJsonString){

        String TAG_JSON="board";
        String TAG_ID = "id";
        String TAG_TITLE = "title";
        String TAG_CONTENT = "content";
        String TAG_GROUPID = "groupid";
        String TAG_MEMBERID = "memberid";
        String TAG_ATTENDENCETIME = "attendencetime";
        String TAG_ATTENDENCELATETIME = "attendencelatetime";
        String TAG_LATEFINE = "latefine";
        String TAG_ABSENCEFINE = "absencefine";



        try {

            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                String id = item.getString(TAG_ID);
                String title = item.getString(TAG_TITLE);
                String content = item.getString(TAG_CONTENT);
                String groupid = item.getString(TAG_GROUPID);
                String memberid = item.getString(TAG_MEMBERID);
                String attendenctime = item.getString(TAG_ATTENDENCETIME);
                String attendencelatetime = item.getString(TAG_ATTENDENCELATETIME);
                String latefine = item.getString(TAG_LATEFINE);
                String absencefine = item.getString(TAG_ABSENCEFINE);


                Board boardData = new Board(Integer.parseInt(id),Integer.parseInt(groupid),memberid,title,content,attendenctime,attendencelatetime,latefine,absencefine);

                items.add(boardData);
                adapter.notifyDataSetChanged();
            }

        } catch (JSONException e) {

            Log.d("listview에 추가", "showResult : ", e);
        }

    }

}

