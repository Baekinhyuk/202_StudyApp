package cau.study_202;

import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.drawable.GradientDrawable;

import java.util.List;

public class ChanMemberAdapter extends ArrayAdapter<Member> {

    public ChanMemberAdapter(Context context, List<Member> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.chan_member_list_item, parent, false);
        }

        // Get the {@link Word} object located at this position in the list
        Member currentMember = getItem(position);

        // nickname
        /*
        TextView nicknameTextView = (TextView) listItemView.findViewById(R.id.nick_name);

        nicknameTextView.setText(currentMember.getNickname());*/

        // nickname부분에 이름을 추가
        TextView nicknameTextView = (TextView) listItemView.findViewById(R.id.nick_name);

        nicknameTextView.setText(currentMember.getUserId());

        // 신뢰도
        TextView trustTextView = (TextView) listItemView.findViewById(R.id.trust);

        trustTextView.setText("신뢰도: " +String.format("%.2f",currentMember.getTrust()));

        // leader 이미지
        ImageView leaderImageView = (ImageView) listItemView.findViewById(R.id.leader_image);
        // 동그랗게
        leaderImageView.setBackground(new ShapeDrawable(new OvalShape()));
        leaderImageView.setClipToOutline(true);

        if (currentMember.isLeader()) {
            leaderImageView.setImageResource(currentMember.getLeaderImageResoureceId());
            leaderImageView.setVisibility(View.VISIBLE);
        }
        else
            leaderImageView.setVisibility(View.INVISIBLE);

        /* 추가 해야 할 것 */
        // 투표 버튼 이미지에다가 이벤트 걸기
        ImageView voteImageView = (ImageView) listItemView.findViewById(R.id.vote_image);
        voteImageView.setBackground(new ShapeDrawable(new OvalShape()));
        voteImageView.setClipToOutline(true);



        TextView stateTextView = (TextView) listItemView.findViewById(R.id.states);

        GradientDrawable stateCircle = (GradientDrawable) stateTextView.getBackground();

        int states = currentMember.getStates();
        switch (states) {
            case 0 :
                stateTextView.setText("출석");
                stateCircle.setColor(getContext().getResources().getColor(R.color.present));
                break;
            case 1 :
                stateTextView.setText("지각");
                stateCircle.setColor(getContext().getResources().getColor(R.color.late));
                break;
            case 2 :
                stateTextView.setText("결석");
                stateCircle.setColor(getContext().getResources().getColor(R.color.absent));
                break;
            default :
                stateCircle.setColor(getContext().getResources().getColor(R.color.before_present));

        }

        return listItemView;

    }
}
