package cau.study_202;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import cau.study_202.network.Phprequest;

public class ChanShowBoardActivity extends AppCompatActivity {

    Intent intent;
    String title ;
    String content;
    String author;
    int boardId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chan_show_board);

        intent = getIntent();
        title = intent.getStringExtra("title");
        content = intent.getStringExtra("content");
        author = intent.getStringExtra("author");
        boardId = intent.getIntExtra("id", -1);
        Log.i("intent", "\ntitle:"+title+"\ncontent:"+content+"\nauthor:"+author+"\nboardid:"+boardId);


        //Toolbar 적용하기 위해서 .HomeActivity Theme 제거(AndroidManifest에서)
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_board);
        setSupportActionBar(toolbar);
        if (!(LoginStatus.getMemberID().equals(author))) {
            toolbar.setVisibility(View.GONE);
        }

        TextView titleTextView = (TextView) findViewById(R.id.board_title);
        titleTextView.setText(title);

        TextView contentTextView = (TextView) findViewById(R.id.board_contents);
        contentTextView.setText(content);

        // 돌아가기 눌렀을때
        Button returnButton = (Button) findViewById(R.id.board_return_button);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }
    // 삭제 및 수정 구현해야함..
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
        AlertDialog.Builder alertdialog = new AlertDialog.Builder(this);




        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete) {

            alertdialog.setMessage("삭제하시겠습니까?");
            // 확인버튼
            alertdialog.setPositiveButton("확인", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DeleteBoardData task = new DeleteBoardData();
                    task.execute( Phprequest.BASE_URL+"delete_board.php"+"?ID="+boardId,"");
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
        else if(id == R.id.action_modify){

            Intent i = new Intent(ChanShowBoardActivity.this,
                    ChanModifyBoardActivity.class);
            i.putExtra("title", title);
            i.putExtra("content", content);
            i.putExtra("author", author);
            i.putExtra("id", boardId);
            startActivity(i);
            finish();
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
}
