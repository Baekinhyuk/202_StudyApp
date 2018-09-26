package cau.study_202.member;

public class Member {

    private String userId="";
    private String name="";
    private String nickname="";
    private String tel="";
    private String email="";
    private String birthday="";

    public Member(String userId, String name, String nickname, String tel,String email, String birthday){
        this.userId = userId;
        this.name = name;
        this.nickname = nickname;
        this.tel = tel;
        this.email = email;
        this.birthday = birthday;

    }

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


}