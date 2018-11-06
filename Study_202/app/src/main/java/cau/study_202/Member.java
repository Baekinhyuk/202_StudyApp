package cau.study_202;

public class Member {

    private String userId="";
    private String name="";
    private String tel="";
    private String email="";
    private String birthday="";
    private int groupID = -1;
    private double trust = 0.0;
    private int fine = 0; // 내야할 벌금
    private int leaderImageResoureceId = NO_IMAGE;  // leader 인지 아닌지에 대한 정보 (leader를 나타내는 이미지로 대신 표현)
    private static final int NO_IMAGE = -1;
    private int states = - 1;  // 출석상태 // -1은 출석전을 의미 // 0은 출석 // 1은 지각 // 2는 결석 // 3은 쉬는날
    private int total = 0; // 총 출석일수
    private int present = 0; // 총 출석일수
    private int absent = 0; // 총 결석일수
    private int late = 0; // 총 지각일수
    private int totalfine = 0; // 총 누적 벌금

    public Member(String userId, int total, int present, int absent, int late, int totalfine, int fine) {
        this.userId = userId;
        this.total = total;
        this.present = present;
        this.absent = absent;
        this.late = late;
        this.totalfine = totalfine;
        this.fine = fine;
    }

    public Member(String userId, String name, String tel,String email, String birthday){
        this.userId = userId;
        this.name = name;
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

    public int getTotal() {return total;}
    public int getPresent() {return present;}
    public int getAbsent() {return absent;}
    public int getLate() {return late;}
    public int getTotalfine() {return totalfine;}
}