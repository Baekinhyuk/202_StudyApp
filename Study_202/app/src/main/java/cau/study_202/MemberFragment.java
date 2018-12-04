package cau.study_202;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import cau.study_202.network.Phprequest;


/**
 * A simple {@link Fragment} subclass.
 */
public class MemberFragment extends Fragment {

    private Activity activity;
    AlertDialog.Builder alertdialog;
    ArrayList<Member> mMemberList;
    SwipeRefreshLayout pullToRefresh;
    View rootView;

    public MemberFragment() {
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
        mMemberList.clear();
        accept_member_list(rootView,mMemberList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        alertdialog = new AlertDialog.Builder(activity);

        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.chan_member_list, container, false);

        mMemberList = new ArrayList<Member>();

        pullToRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh_memberlist);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mMemberList.clear();
                accept_member_list(rootView,mMemberList);
                pullToRefresh.setRefreshing(false);
            }

        });

        return rootView;

    }

    private void accept_member_list(View rootView,final ArrayList<Member> mMemberList){
        //서버와의 연동하는 부분을 함수로 처리
        member_output();
        ChanMemberAdapter adapter = new ChanMemberAdapter(getActivity(), mMemberList);

        ListView listview = (ListView) rootView.findViewById(R.id.list);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        final Member member = mMemberList.get(position);

                        alertdialog.setMessage(member.getUserId()+"님을 신고 하시겠습니까?");
                        // 확인버튼
                        alertdialog.setPositiveButton("확인", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(getActivity(), ChanVoteActivity.class); // intent 되는 activty에 알맞은 data 출력

                                intent.putExtra("ID", member.getUserId());

                                startActivity(intent);

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
    }
    private void member_output(){
        try {
            Phprequest request = new Phprequest(Phprequest.BASE_URL +"member_output.php");
            String result = request.memberoutput(Integer.toString(LoginStatus.getGroupID()));
            show_member(result,mMemberList);
            //Toast.makeText(getActivity(),result,Toast.LENGTH_SHORT).show();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (NullPointerException e){
            Log.i("회원목록출력오류","try-catch문 NULLPOINTER에러");
            GetData task = new GetData();
            task.execute();
            e.printStackTrace();
        }
    }

    private class GetData extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDialog = new ProgressDialog(activity);

        String errorString = null;

        @Override
        protected void onPreExecute() {
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMessage("인터넷 연결을 재시도 중입니다.");
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            mMemberList.clear();
            accept_member_list(rootView,mMemberList);
        }


        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                for (int i = 0; i < 100; i++) {
                    progressDialog.setProgress(i);
                    Thread.sleep(10);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private void show_member(String result, ArrayList<Member> mMemberList) {

        String TAG_JSON="member";
        String TAG_ID = "id";
        String TAG_NAME = "name";
        String TAG_EMAIL = "email";
        String TAG_TEL = "phone";
        String TAG_LEADER = "leaderID";
        String TAG_TRUST = "trust";
        String TAG_ATT = "attendence";

        try {

            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                String id = item.getString(TAG_ID);
                String name = item.getString(TAG_NAME);
                String email = item.getString(TAG_EMAIL);
                String phone = item.getString(TAG_TEL);
                String leader = item.getString(TAG_LEADER);
                String trust = item.getString(TAG_TRUST);
                String att = item.getString(TAG_ATT);

                Member personalData = new Member();

                personalData.setUserId(id);
                personalData.setName(name);
                personalData.setEmail(email);
                personalData.setTel(phone);
                personalData.setTrust(Double.parseDouble(trust) * 100);
                //임의로 state일단 해놓음
                //personalData.setStates(0);
                personalData.setStates(Integer.parseInt(att));

                if(id.equals(leader)){
                    personalData.setLeader();
                }
                mMemberList.add(personalData);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
