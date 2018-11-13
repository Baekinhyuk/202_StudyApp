package cau.study_202;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import cau.study_202.network.Phprequest;


/**
 * A simple {@link Fragment} subclass.
 */
public class MemberStatsFragment extends Fragment {

    private Activity activity;
    AlertDialog.Builder alertdialog;
    ArrayList<Member> mMemberList;
    ChanStatAdapter adapter;

    @Override
    public void onResume() {
        super.onResume();
        Log.i("managefragment", "managefragment");
        mMemberList.clear();
        GetMemberStat task = new GetMemberStat();
        task.execute( Phprequest.BASE_URL+"fetch_stat.php"+"?GROUPID="+LoginStatus.getGroupID(),"");
    }

    public MemberStatsFragment() {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        alertdialog = new AlertDialog.Builder(activity);

        View rootView = inflater.inflate(R.layout.fragment_member_stats, container, false);

        mMemberList = new ArrayList<Member>();
        // 여기에 DB로 가져오는 거..
        adapter = new ChanStatAdapter(getActivity(), mMemberList);

        ListView listview = rootView.findViewById(R.id.statlist);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(activity, ChanShowStatActivity.class); // intent 되는 activty에 알맞은 data 출력
                        Member currentMember = mMemberList.get(position);
                        intent.putExtra("id", currentMember.getUserId());

                        startActivity(intent);


                    }
                });

        return rootView;
    }

    public class GetMemberStat extends AsyncTask<String, Void, String> {

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
                return null;
            }

        }
    }

    public void showResult(String mJsonString){

        String TAG_JSON="Stats";
        String TAG_ID = "id";
        String TAG_TOTAL = "total";
        String TAG_PRESENT = "present";
        String TAG_LATE = "late";
        String TAG_ABSENT = "absent";
        String TAG_TOTALFINE = "totalfine";
        String TAG_FINE = "fine";

        try {

            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                String id = item.getString(TAG_ID);
                String total = item.getString(TAG_TOTAL);
                String present = item.getString(TAG_PRESENT);
                String late = item.getString(TAG_LATE);
                String absent = item.getString(TAG_ABSENT);
                String totalfine = item.getString(TAG_TOTALFINE);
                String fine = item.getString(TAG_FINE);

                Member memberData = new Member(id,Integer.parseInt(total),Integer.parseInt(present),Integer.parseInt(absent),Integer.parseInt(late),Integer.parseInt(totalfine),Integer.parseInt(fine));

                mMemberList.add(memberData);
                adapter.notifyDataSetChanged();
            }

        } catch (JSONException e) {

            Log.d("listview에 추가", "showResult : ", e);
        }

    }


}
