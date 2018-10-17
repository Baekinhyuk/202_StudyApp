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

public class ChanModifyBoardActivity extends AppCompatActivity {

    Intent intent;
    String title ;
    String content;
    String author;
    int boardId;

    String pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chan_modify_board);

        intent = getIntent();
        title = intent.getStringExtra("title");
        content = intent.getStringExtra("content");
        author = intent.getStringExtra("author");
        boardId = intent.getIntExtra("id", -1);
        Log.i("modify-intent", "\ntitle:"+title+"\ncontent:"+content+"\nauthor:"+author+"\nboardid:"+boardId);

        final EditText titleTextView = (EditText) findViewById(R.id.board_edit_title);
        titleTextView.setText(title);

        final EditText contentTextView = (EditText) findViewById(R.id.board_edit_contents);
        contentTextView.setText(content);

        // 돌아가기 눌렀을때
        Button returnButton = (Button) findViewById(R.id.board_return_button);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button modifyButton = (Button) findViewById(R.id.board_edit_button);
        modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 제목 입력 확인
                if (titleTextView.getText().toString().length() == 0) {
                    Toast.makeText(ChanModifyBoardActivity.this, "제목을 입력하세요", Toast.LENGTH_SHORT).show();
                    titleTextView.requestFocus();
                    return;
                }
                // 내용 입력 확인
                if (contentTextView.getText().toString().length() == 0) {
                    Toast.makeText(ChanModifyBoardActivity.this, "내용을 입력하세요", Toast.LENGTH_SHORT).show();
                    contentTextView.requestFocus();
                    return;
                }

                String result = Phprequest.modifyBoard(titleTextView.getText().toString(), contentTextView.getText().toString(), boardId+"");
                pd = result;

                ModifyBoardData task = new ModifyBoardData();
                task.execute(Phprequest.BASE_URL + "modify_board.php","");
            }
        });
    }

    public class ModifyBoardData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            if(result.equals("1")){
                Toast.makeText(getApplication(),"정상적으로 수정되었습니다.",Toast.LENGTH_SHORT).show();
                finish();
            }
            else{
                Toast.makeText(getApplication(),"수정과정에 문제가 발생했습니다.",Toast.LENGTH_SHORT).show();
                Log.i("PHPRequest", result);
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
                errorString = e.toString();

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
