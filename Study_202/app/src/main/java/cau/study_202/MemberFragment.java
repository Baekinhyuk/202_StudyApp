package cau.study_202;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MemberFragment extends Fragment {


    public MemberFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.chan_member_list, container, false);

        ArrayList<Member> members = new ArrayList<Member>();
        /* db에서 해당 그룹 멤버 가져와야 함 현재는 테스트 */
        Member m1 = new Member("qorcksgml", "백찬희", "공부공부", "010-222-2222","chb2561@naver.com", "1994-10-06");
        m1.setLeader();
        m1.setTrust(21.2);
        m1.setStates(0);
        Member m2 = new Member("qorcksgml", "백찬희", "합격하자", "010-222-2222","chb2561@naver.com", "1994-10-11");
        m2.setTrust(-0.2);
        m2.setStates(1);
        Member m3 = new Member("qorcksgml", "백찬희", "캡스톤", "010-222-2222","chb2561@naver.com", "1994-10-22");
        m3.setTrust(11);
        m3.setStates(2);
        members.add(m1);
        members.add(m2);
        members.add(m3);
        members.add(new Member("qorcksgml", "백찬희", "노노", "010-222-2222","chb2561@naver.com", "1994-10-06"));
        members.add(new Member("qorcksgml", "백찬희", "고싱", "010-222-2222","chb2561@naver.com", "1994-10-06"));
        members.add(new Member("qorcksgml", "백찬희", "헤", "010-222-2222","chb2561@naver.com", "1994-10-06"));
        /*db 구축전 테스트*/

        ChanMemberAdapter adapter = new ChanMemberAdapter(getActivity(), members);

        ListView listview = (ListView) rootView.findViewById(R.id.list);
        listview.setAdapter(adapter);

        return rootView;


    }

}
