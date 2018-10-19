package cau.study_202;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
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

import cau.study_202.network.Phprequest;


/**
 * A simple {@link Fragment} subclass.
 */
public class MemberManageFragment extends Fragment {

    private Activity activity;
    ArrayList<Member> members;
    ChanWaitingAdapter adapter;
    SwipeRefreshLayout pullToRefresh;
    AlertDialog.Builder alertdialog;
    String pd;

    public MemberManageFragment() {
        // Required empty public constructor
    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            activity = (Activity) context;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("managefragment", "managefragment");
        members.clear();
        GetMemberData task = new GetMemberData();
        task.execute( Phprequest.BASE_URL+"fetch_waiting.php"+"?GROUPID="+LoginStatus.getGroupID(),"");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        alertdialog = new AlertDialog.Builder(activity);
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_member_manage, container, false);

        members = new ArrayList<Member>();

        adapter = new ChanWaitingAdapter(getActivity(), members);

        ListView listview = (ListView) rootView.findViewById(R.id.watinglist);
        listview.setAdapter(adapter);

        // 개발 해야함 가입 수락
        listview.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        /* 가입 수락 하시겠습니까? dialog // leader인 경우에만 dialog 띄우기
                        Intent intent = new Intent(getActivity(), ChanShowBoardActivity.class); // intent 되는 activty에 알맞은 data 출력
                        Board currentBoard = boards.get(position);
                        intent.putExtra("title", currentBoard.getTitle());
                        intent.putExtra("content", currentBoard.getContent());
                        intent.putExtra("author", currentBoard.getMemberId());
                        intent.putExtra("id", currentBoard.getId());

                        startActivity(intent);
                        */
                    }
                });

        TextView exitTextView = (TextView) rootView.findViewById(R.id.leave);
        exitTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                alertdialog.setMessage("탈퇴하시겠습니까?");
                // 확인버튼
                alertdialog.setPositiveButton("확인", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //리더가 나가면 신뢰도 순으로 물려주는거 구현 해야함
                       LeaveGroup task = new LeaveGroup();
                        task.execute( Phprequest.BASE_URL+"leave_group.php","");
                        pd = "ID="+LoginStatus.getMemberID()+"&"+"ISLEADER="+LoginStatus.isLeader()+"&"
                                +"GROUPID="+LoginStatus.getGroupID();

                    }
                });
                // 취소버튼
                alertdialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            }
                });
                // 메인 다이얼로그 생성
                AlertDialog alert = alertdialog.create();
                alert.show();
            }
        });

        return rootView;
    }


    public class GetMemberData extends AsyncTask<String, Void, String> {

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

        String TAG_JSON="waiting";
        String TAG_ID = "id";
        String TAG_TRUST = "trust";

        try {

            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                String id = item.getString(TAG_ID);
                String trust = item.getString(TAG_TRUST);

                Member memberData = new Member(id,Double.parseDouble(trust));

                members.add(memberData);
                adapter.notifyDataSetChanged();
            }

        } catch (JSONException e) {

            Log.d("listview에 추가", "showResult : ", e);
        }

    }

    public class LeaveGroup extends AsyncTask<String, Void, String> {

        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            if(result.equals("1")){
                Toast.makeText(activity, "탈퇴 되었습니다.", Toast.LENGTH_SHORT).show();
                LoginStatus.setGroupID(-1);

                activity.finish();
            }
            else{
                Toast.makeText(activity,"탈퇴 과정에 문제가 발생했습니다.",Toast.LENGTH_SHORT).show();
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
