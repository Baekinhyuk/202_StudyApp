package cau.study_202;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
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

public class ChanShowVoteActivity extends AppCompatActivity {

    Intent intent;
    String title ;
    String content;
    String author;
    int id;
    String votedid;
    int votetype;
    int numofvoters;
    int pros;
    int cons;
    String pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chan_show_vote);

        intent = getIntent();
        title = intent.getStringExtra("title");
        content = intent.getStringExtra("content");
        author = intent.getStringExtra("author");
        id = intent.getIntExtra("id", -1);
        votedid = intent.getStringExtra("votedid");
        votetype = intent.getIntExtra("votetype", -1);
        numofvoters = intent.getIntExtra("numofvoters", -1);
        pros = intent.getIntExtra("pros", -1);
        cons = intent.getIntExtra("cons", -1);

        Log.i("intent", "\ntitle:"+title+"\ncontent:"+content+"\nauthor:"+author+"\nboardid:"+ pros);


        TextView titleTextView = (TextView) findViewById(R.id.board_title);
        titleTextView.setText(title);

        TextView contentTextView = (TextView) findViewById(R.id.board_contents);
        contentTextView.setText(content);


        // 투표 눌렀을때
        Button voteButton = (Button) findViewById(R.id.vote);
        voteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton pros=(RadioButton)findViewById(R.id.pros);

                pd = "ID="+id+"&"+
                        "MEMBERID=" + LoginStatus.getMemberID() + "&";
                if (pros.isChecked()){
                    pd = pd + "VOTE="+0;
                } else
                    pd = pd + "VOTE="+1;

                Vote task = new Vote();
                task.execute(Phprequest.BASE_URL+"vote.php","");

                finish();
            }
        });


    }

    public class Vote extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.i("뭔데", result);
            if(result.equals("1")){
                Toast.makeText(getApplication(),"투표하셨습니다.",Toast.LENGTH_SHORT).show();

            }
            else{
                Toast.makeText(getApplication(),"이미 투표 하셨습니다.",Toast.LENGTH_SHORT).show();
                Log.i("이미 투표", result);
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
