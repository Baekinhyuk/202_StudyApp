package cau.study_202;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MemberBoardFragment extends Fragment {

    ArrayList<Board> boards = new ArrayList<Board>();

    public MemberBoardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.chan_board_list, container, false);

        /* db에서 해당 그룹 멤버 가져와야 함 현재는 테스트 */
        Board b1 = new Board(0, 0, "chb2561", "공부하자","공부하는거 어떤가요 제발 합시다 너네들 모하니" +
                "제발 공부합시다 머할까 오늘 아니 머해 너네 제발 공부해라 머하니 너네 으으어으어우어우어우어우어");
        Board b2 = new Board(1, 1, "bih2561", "공부할래요?","아니아니 우어웡울어우러울얼ㄴ이랑ㄴ러ㅣㅏ 공부하는거 어떤가요 제발 합시다 너네들 모하니" +
                "제발 공부합시다 머할까 오늘 아니 머해 너네 제발 공부해라 머하니 너네 으으어으어우어우어우어우어");

        boards.add(b1);
        boards.add(b2);
        /*db 구축전 테스트*/
        ChanBoardAdapter adapter = new ChanBoardAdapter(getActivity(), boards);

        ListView listview = (ListView) rootView.findViewById(R.id.board_list);
        listview.setAdapter(adapter);


        return rootView;
    }

}
