package cau.study_202;

public class LoginStatus {

    private static String memberID;

    private static String nickName;

    private static int groupID = 12; // 임의로


    public static String getMemberID() {return memberID; }

    public static void setMemberID(String id) {memberID = id; }

    public static int getGroupID() {return groupID; }

    public static void setGroupID(int id) {groupID = id; }

    public static String getNickName() {return nickName;}

    public static void setNickName(String nickname) {nickName = nickname;}

}
