package cau.study_202;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
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
public class MemberVoteFragment extends Fragment {

    ArrayList<Board> votes;
    ChanVoteAdapter adapter;
    SwipeRefreshLayout pullToRefresh;


    public MemberVoteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("fragment", "hi!");
        votes.clear();
        GetVoteData task = new GetVoteData();
        task.execute( Phprequest.BASE_URL+"fetch_vote.php"+"?GROUPID="+LoginStatus.getGroupID(),"");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.chan_board_list, container, false);

        ImageView write = rootView.findViewById(R.id.btn_board_write);
        write.setVisibility(View.GONE);

        votes = new ArrayList<Board>();

        pullToRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                votes.clear();
                GetVoteData task = new GetVoteData();
                task.execute( Phprequest.BASE_URL+"fetch_vote.php"+"?GROUPID="+LoginStatus.getGroupID(),"");
                pullToRefresh.setRefreshing(false);
            }

        });

        adapter = new ChanVoteAdapter(getActivity(), votes);

        ListView listview = (ListView) rootView.findViewById(R.id.board_list);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                        Intent intent = new Intent(getActivity(), ChanShowVoteActivity.class); // intent 되는 activty에 알맞은 data 출력
                        Board currentvote = votes.get(position);
                        intent.putExtra("title", currentvote.getTitle());
                        intent.putExtra("content", currentvote.getContent());
                        intent.putExtra("author", currentvote.getMemberId());
                        intent.putExtra("id", currentvote.getId());
                        intent.putExtra("votedid", currentvote.getVotedId());
                        intent.putExtra("votetype", currentvote.getVoteType());
                        intent.putExtra("numofvoters", currentvote.getNumofvoters());
                        intent.putExtra("pros", currentvote.getPros());
                        intent.putExtra("cons", currentvote.getCons());

                        startActivity(intent);

                    }
                });


        //ChanVoteAdapter 만들어야함.
        return rootView;
    }

    public class GetVoteData extends AsyncTask<String, Void, String> {

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

        String TAG_JSON="vote";
        String TAG_ID = "id";
        String TAG_AUTHOR = "author";
        String TAG_CONTENT = "content";
        String TAG_VOTEDID = "votedid";
        String TAG_GROUPID = "groupid";
        String TAG_VOTETYPE = "votetype";
        String TAG_NUMOFVOTERS = "numofvoters";
        String TAG_PROS = "pros";
        String TAG_CONS = "cons";

        try {

            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);
                String id = item.getString(TAG_ID);
                String author = item.getString(TAG_AUTHOR);
                String content = item.getString(TAG_CONTENT);
                String groupid = item.getString(TAG_GROUPID);
                String votedid = item.getString(TAG_VOTEDID);
                String votetype = item.getString(TAG_VOTETYPE);
                String numofvoters = item.getString(TAG_NUMOFVOTERS);
                String pros = item.getString(TAG_PROS);
                String cons = item.getString(TAG_CONS);

                Board boardData = new Board(Integer.parseInt(id),Integer.parseInt(groupid),author,votedid,Integer.parseInt(votetype),content,Integer.parseInt(numofvoters),Integer.parseInt(pros),Integer.parseInt(cons));

                votes.add(boardData);
                adapter.notifyDataSetChanged();
            }

        } catch (JSONException e) {

            Log.d("listview에 추가", "showResult : ", e);
        }

    }
}
