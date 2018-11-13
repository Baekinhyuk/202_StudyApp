package cau.study_202;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ChanDetailStatsAdapter extends ArrayAdapter<Stat> {

    public ChanDetailStatsAdapter(Context context, List<Stat> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.chan_detail_stat_list_item, parent, false);
        }

        // Get the {@link Word} object located at this position in the list
        Stat currentStat = getItem(position);

        TextView dateTextView = (TextView) listItemView.findViewById(R.id.date);

        dateTextView.setText(currentStat.getdate());

        TextView dayTextView = (TextView) listItemView.findViewById(R.id.day);

        dayTextView.setText(currentStat.getday());

        TextView fineTextView = (TextView) listItemView.findViewById(R.id.fine);

        fineTextView.setText(currentStat.getFine()+"");

        TextView stateTextView = (TextView) listItemView.findViewById(R.id.states);

        GradientDrawable stateCircle = (GradientDrawable) stateTextView.getBackground();

        int states = currentStat.getState();
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
