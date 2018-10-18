package cau.study_202;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class GroupPageAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public GroupPageAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {            // 멤버 게시판
            return new MemberBoardFragment();
        } else if (position == 1) {
            return new MemberFragment(); // 첫화면 멤버들 보이는것
        } else if (position == 2) {
            return new CheckInFragment(); // 출석 화면
        } else if (position == 3) {
            return new MemberStatsFragment(); // 멤버들 통계 화면
        } else if (position == 4){
            return new MemberVoteFragment(); // 투표 화면
        } else
            return new MemberManageFragment(); // 그룹 관리 하면

    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return mContext.getString(R.string.tab_board);
        } else if (position == 1) {
            return mContext.getString(R.string.tab_member);
        } else if (position == 2) {
            return mContext.getString(R.string.tab_check_in);
        } else if (position == 3){
            return mContext.getString(R.string.tab_member_stats);
        } else if (position == 4){
            return mContext.getString(R.string.tab_member_votes);
        } else
            return mContext.getString(R.string.tab_manage);
    }

    @Override
    public int getCount() {
        return 6; // 탭 6개
    }
}
