package cau.study_202;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
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

import java.util.List;

public class ChanWaitingAdapter extends ArrayAdapter<Member> {

    public ChanWaitingAdapter(Context context, List<Member> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.chan_wating_list, parent, false);
        }

        // Get the {@link Word} object located at this position in the list
        Member currentMember = getItem(position);

        // id
        TextView idTextView = (TextView) listItemView.findViewById(R.id.id);

        idTextView.setText(currentMember.getUserId());

        // 신뢰도
        TextView trustTextView = (TextView) listItemView.findViewById(R.id.trust);

        trustTextView.setText("신뢰도 : " +currentMember.getTrust());

        // 선택 이미지
        ImageView leaderImageView = (ImageView) listItemView.findViewById(R.id.accept);


        if (LoginStatus.isLeader()) {

            leaderImageView.setVisibility(View.VISIBLE);


        }
        else {
            leaderImageView.setVisibility(View.INVISIBLE);

        }

        return listItemView;

    }
}
