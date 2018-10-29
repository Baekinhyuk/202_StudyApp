package cau.study_202;

public class LoginStatus {

    private static String memberID;

    private static int groupID = -1; // 임의로

    private static boolean isLeader = false;

    public static String getMemberID() {return memberID; }

    public static void setMemberID(String id) {memberID = id; }

    public static int getGroupID() {return groupID; }

    public static void setGroupID(int id) {groupID = id; }

    public static boolean isLeader() {return isLeader;}

    public static void setLeader() {isLeader = true;}

    public static void logout() {isLeader = false; groupID = -1; memberID = "";}

}
