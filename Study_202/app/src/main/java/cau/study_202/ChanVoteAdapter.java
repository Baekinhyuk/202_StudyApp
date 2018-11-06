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

public class ChanVoteAdapter extends ArrayAdapter<Board> {

    public ChanVoteAdapter(Context context, List<Board> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.chan_vote_list_item, parent, false);
        }

        // Get the {@link Word} object located at this position in the list
        Board currentBoard = getItem(position);

        TextView titleTextView = (TextView) listItemView.findViewById(R.id.vote_title);

        titleTextView.setText(currentBoard.getTitle());

        // author
        TextView authorTextView = (TextView) listItemView.findViewById(R.id.vote_author);

        authorTextView.setText("신고자: " + currentBoard.getMemberId());

        // 투표율
        TextView voterateTextView = (TextView) listItemView.findViewById(R.id.vote_rate);

        voterateTextView.setText("투표율: "+ (currentBoard.getCons()+currentBoard.getPros()) + "/" + currentBoard.getNumofvoters());


        return listItemView;

    }
}
