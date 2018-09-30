package cau.study_202.member;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import java.util.ArrayList;


import cau.study_202.R;

public class MemberAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private MemberList mMemberList;
    private Activity context = null;


    MemberAdapter(Activity context,MemberList mMemberList){
        this.context = context;
        this.mMemberList = mMemberList;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        protected TextView userId;
        protected TextView name;
        protected TextView nickname;
        protected TextView tel;
        protected TextView email;

        MyViewHolder(View view){
            super(view);
            userId = view.findViewById(R.id.card_userid);
            name = view.findViewById(R.id.card_member_name);
            nickname = view.findViewById(R.id.card_nickname);
            tel = view.findViewById(R.id.card_tel);
            email = view.findViewById(R.id.card_email);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_member, parent, false);
        MyViewHolder holder = new MyViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        MyViewHolder myViewHolder = (MyViewHolder) holder;
        myViewHolder.userId.setText(mMemberList.getMembers().get(position).getUserId());
        myViewHolder.name.setText(mMemberList.getMembers().get(position).getName());
        myViewHolder.nickname.setText(mMemberList.getMembers().get(position).getNickname());
        myViewHolder.tel.setText(mMemberList.getMembers().get(position).getTel());
        myViewHolder.email.setText(mMemberList.getMembers().get(position).getEmail());
    }

    @Override
    public int getItemCount() {
        return mMemberList.getMembers().size();
    }
}

