package cau.study_202;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import cau.study_202.R;

public class GroupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(LoginStatus.getGroupID() == -1) {
            Toast.makeText(GroupActivity.this, "가입된 스터디가 없습니다.", Toast.LENGTH_SHORT).show();
            finish();
        }

        setContentView(R.layout.activity_group);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        GroupPageAdapter adapter = new GroupPageAdapter(this, getSupportFragmentManager());

        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    protected void onResume() {
        super.onResume();


        출처: http://kwangsics.tistory.com/entry/Android-Activity에서-Fragment-함수-호출 [린기린기린의 개인 기록 공간]
        Log.i("GroupActivity", "hi?");
    }
}
