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
        if (position == 0) {
            return new MemberFragment(); // 첫화면 멤버들 보이는것
        } else if (position == 1) {
            return new CheckInFragment(); // 출석 화면
        } else  {
            return new MemberStatsFragment(); // 멤버들 통계 화면
        }

    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return mContext.getString(R.string.tab_member);
        } else if (position == 1) {
            return mContext.getString(R.string.tab_check_in);
        } else  {
            return mContext.getString(R.string.tab_member_stats);
        }
    }

    @Override
    public int getCount() {
        return 3; // 화면 3개
    }
}
