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

public class ChanStatAdapter extends ArrayAdapter<Member> {

    public ChanStatAdapter(Context context, List<Member> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.chan_stat_list_item, parent, false);
        }

        // Get the {@link Word} object located at this position in the list
        Member currentMember = getItem(position);

        TextView nicknameTextView = (TextView) listItemView.findViewById(R.id.nick_name);

        nicknameTextView.setText(currentMember.getUserId());

        // 총 출석일수
        TextView totalTextView = (TextView) listItemView.findViewById(R.id.total);

        totalTextView.setText(currentMember.getTotal()+"");

        // 총 출석 수
        TextView presentTextView = (TextView) listItemView.findViewById(R.id.present);

        presentTextView.setText(currentMember.getPresent()+"");

        // 총 결석 수
        TextView absentTextView = (TextView) listItemView.findViewById(R.id.absent);

        absentTextView.setText(currentMember.getAbsent()+"");

        // 총 지각 수
        TextView lateTextView = (TextView) listItemView.findViewById(R.id.late);

        lateTextView.setText(currentMember.getLate()+"");

        // 총 누적 벌금
        TextView totalfineTextView = (TextView) listItemView.findViewById(R.id.totalfine);

        totalfineTextView.setText(currentMember.getTotalfine()+"");

        // 총 출석일수
        TextView fineTextView = (TextView) listItemView.findViewById(R.id.fine);

        fineTextView.setText(currentMember.getFine()+"");


        return listItemView;

    }
}
