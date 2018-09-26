package cau.study_202.member;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;

public class MemberList {

    private static MemberList memberList;
    private ArrayList<Member> members;

    public static MemberList get(Context context){
        if(memberList == null){
            memberList = new MemberList(context);
        }
        return memberList;
    }


    public MemberList(Context context){
        members = new ArrayList<>();
//        for(int i=0;i<4;i++){
//            Member member = new Member();
//            member.setName("멤버"+i);
//            members.add(member);
//        }
    }
    public MemberList(){
        members = new ArrayList<>();
//        for(int i=0;i<4;i++){
//            Member member = new Member();
//            member.setName("멤버"+i);
//            members.add(member);
//        }
    }

    public MemberList(ArrayList<Member> members){
        this.members = members;
    }

    public ArrayList<Member> getMembers(){
        return members;
    }

    public void addMember(Member m){
        members.add(m);
    }
}
