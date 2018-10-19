package cau.study_202;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cau.study_202.network.Phprequest;

// 게시판 신청하기
public class Search_Studyboard extends AppCompatActivity {

    Intent intent;
    String title ;
    String content;
    String author;
    int boardId;
    String atime;
    String ltime;
    String lf;
    String af;
    int groupid;

    String pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchstudy);

        intent = getIntent();
        title = intent.getStringExtra("title");
        content = intent.getStringExtra("content");
        author = intent.getStringExtra("author");
        boardId = intent.getIntExtra("id", -1);
        atime = intent.getStringExtra("attendencetime");
        ltime = intent.getStringExtra("attendencelatetime");
        lf = intent.getStringExtra("latefine");
        af = intent.getStringExtra("absencefine");
        groupid = intent.getIntExtra("groupid", -1);
        Log.i("intent", "\ntitle:"+title+"\ncontent:"+content+"\nauthor:"+author+"\nboardid:"+boardId+"\ngroupid:"+groupid);

        TextView titleTextView = findViewById(R.id.title);
        titleTextView.setText(title);

        TextView contentTextView =  findViewById(R.id.content);
        contentTextView.setText(content);

        TextView presentTextView = findViewById(R.id.present_time);
        presentTextView.setText(atime);

        TextView latetimeTextView = findViewById(R.id.latetime);
        latetimeTextView.setText(ltime);

        TextView latefineTextView = findViewById(R.id.latefine);
        latefineTextView.setText(lf);

        TextView absentfineTextView = findViewById(R.id.absentfine);
        absentfineTextView.setText(af);


        //화면회전 금지
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);

        //Toolbar 적용하기 위해서 .HomeActivity Theme 제거(AndroidManifest에서)
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_board);
        setSupportActionBar(toolbar);

        //돌아가기 눌렀을 경우
        Button returnButton = (Button) findViewById(R.id.return_button);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //신청하기 눌렀을 경우 (가입)
        Button applyButton = (Button) findViewById(R.id.group_apply_button);
        applyButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertdialog = new AlertDialog.Builder(getApplicationContext());

                if (LoginStatus.getGroupID() != -1) {
                    Toast.makeText(Search_Studyboard.this, "이미 가입된 스터디가 있습니다.", Toast.LENGTH_SHORT).show();
                    return;
                } else {

                    pd = "ID="+LoginStatus.getMemberID()+"&" + "GROUPID=" + groupid;
                    GroupJoin task = new GroupJoin();
                    task.execute(Phprequest.BASE_URL+"join_group.php");

                }

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bottom, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        AlertDialog.Builder alertdialog = new AlertDialog.Builder(this);

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete) {
            if(!(LoginStatus.getMemberID().equals(author))) {
                Toast.makeText(getApplicationContext(), "작성자만 삭제할 수 있습니다.", Toast.LENGTH_SHORT).show();
                return true;
            }
            alertdialog.setMessage("삭제하시겠습니까?");
            // 확인버튼
            alertdialog.setPositiveButton("확인", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DeleteBoardData task = new DeleteBoardData();
                    task.execute( Phprequest.BASE_URL+"delete_gboard.php"+"?ID="+boardId,"");
                    Toast.makeText(getApplicationContext(), "삭제 되었습니다.", Toast.LENGTH_SHORT).show();


                    finish();
                }
            });
            // 취소버튼
            alertdialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getApplicationContext(), "'취소'버튼을 눌렀습니다.", Toast.LENGTH_SHORT).show();
                }
            });
            // 메인 다이얼로그 생성
            AlertDialog alert = alertdialog.create();
            alert.show();
            return true;

        }


        return super.onOptionsItemSelected(item);
    }

    public class DeleteBoardData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.i("showboard", result);


        }


        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];
            String postParameters = "BOARDID=" + params[1];
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
                errorString = e.toString();

                return null;
            }

        }
    }

    public class GroupJoin extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.i("뭔데", result);
            if(result.equals("1")){
                Toast.makeText(getApplication(),"정상적으로 가입되었습니다.",Toast.LENGTH_SHORT).show();
                LoginStatus.setGroupID(groupid);
            }
            else{
                Toast.makeText(getApplication(),"가입과정에 문제가 발생했습니다.",Toast.LENGTH_SHORT).show();
                Log.i("가입과정에 문제", result);
            }
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
