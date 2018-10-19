package cau.study_202;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class MemberManageFragment extends Fragment {

    ArrayList<Member> members;
    ChanWaitingAdapter adapter;
    SwipeRefreshLayout pullToRefresh;

    public MemberManageFragment() {
        // Required empty public constructor
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
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_member_manage, container, false);

        members = new ArrayList<Member>();

        adapter = new ChanWaitingAdapter(getActivity(), members);

        ListView listview = (ListView) rootView.findViewById(R.id.watinglist);
        listview.setAdapter(adapter);
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
}
