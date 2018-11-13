package cau.study_202;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ChanBoardAdapter extends ArrayAdapter<Board> {

    public ChanBoardAdapter(Context context, List<Board> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.chan_board_list_item, parent, false);
        }

        // Get the {@link Word} object located at this position in the list
        Board currentBoard = getItem(position);

        // title
        TextView titleTextView = (TextView) listItemView.findViewById(R.id.board_title);

        titleTextView.setText(currentBoard.getTitle());

        // content
        TextView contentTextView = (TextView) listItemView.findViewById(R.id.board_content);
        if(!currentBoard.getContent().equals("")) {
            contentTextView.setText(currentBoard.getContent());
        } else {
            contentTextView.setText("출석 인증 사진입니다.");
        }


        // author
        TextView authorTextView = (TextView) listItemView.findViewById(R.id.board_author);

        authorTextView.setText(currentBoard.getMemberId());

        return listItemView;

    }
}
