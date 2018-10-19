package cau.study_202;

public class Member {

    private String userId="";
    private String name="";
    private String nickname="";
    private String tel="";
    private String email="";
    private String birthday="";
    private int groupID = -1;
    private double trust = 0.0;
    private int fine = 0; // 벌금
    private int leaderImageResoureceId = NO_IMAGE;  // leader 인지 아닌지에 대한 정보 (leader를 나타내는 이미지로 대신 표현)
    private static final int NO_IMAGE = -1;
    private int states = - 1;  // 출석상태 // -1은 출석전을 의미

    public Member(String userId, String name, String nickname, String tel,String email, String birthday){
        this.userId = userId;
        this.name = name;
        this.nickname = nickname;
        this.tel = tel;
        this.email = email;
        this.birthday = birthday;

    }

    public Member(String userId, double trust){
        this.userId = userId;
        this.trust = trust;
    }
    public  Member(){}

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname= nickname;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday= birthday;
    }

    public int getGroupID() { return groupID; }

    public void setGroupID(int id) { this.groupID = id; }

    public double getTrust() { return trust; }

    public void setTrust(double trust) { this.trust = trust; }

    public int getFine() { return fine; }

    public void setFine(int fine) { this.fine = fine; }

    public int getLeaderImageResoureceId() { return leaderImageResoureceId; }

    public void setLeader() { leaderImageResoureceId = R.drawable.crown;}

    public boolean isLeader() { return leaderImageResoureceId != NO_IMAGE; }

    public int getStates() { return states;}

    public void setStates(int states) { this.states = states; }
}