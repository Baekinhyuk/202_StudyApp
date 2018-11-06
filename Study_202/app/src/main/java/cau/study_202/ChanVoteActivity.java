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
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class ChanVoteActivity extends AppCompatActivity {

    Intent intent;
    String ID;
    String author;
    String pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chan_vote);

        intent = getIntent();
        ID = intent.getStringExtra("ID");
        Log.i("intent", "\nid:"+ID+"\n");

        TextView title = findViewById(R.id.vote_title);
        title.setText(ID + "님에 대한 투표");


        // 돌아가기 눌렀을때
        Button returnButton = (Button) findViewById(R.id.board_return_button);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // 생성하기 눌렀을때
        Button createButton = (Button) findViewById(R.id.board_create_button);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText editContent = (EditText) findViewById(R.id.board_input_contents);
                // 내용 입력 확인
                if (editContent.getText().toString().length() == 0) {
                    Toast.makeText(ChanVoteActivity.this, "내용을 입력하세요", Toast.LENGTH_SHORT).show();
                    editContent.requestFocus();
                    return;
                }

                RadioGroup group=(RadioGroup)findViewById(R.id.radioGroup);
                RadioButton checkin=(RadioButton)findViewById(R.id.check_in_vote);
                RadioButton leave=(RadioButton)findViewById(R.id.leave_vote);
                //버튼에 따라 보내는거 달라짐

                pd = "AUTHOR="+LoginStatus.getMemberID()+"&"
                        +"GROUPID="+LoginStatus.getGroupID()+"&"
                        +"VOTEDID="+ID;

                CreateVote task = new CreateVote();
                task.execute(Phprequest.BASE_URL + "create_vote.php", "");
            }
        });
    }

    private class CreateVote extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if(result.equals("1")){
                Toast.makeText(getApplication(),"정상적으로 생성되었습니다.",Toast.LENGTH_SHORT).show();
                finish();
            }
            else{
                Toast.makeText(getApplication(),"생성과정에 문제가 발생했습니다.",Toast.LENGTH_SHORT).show();
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
