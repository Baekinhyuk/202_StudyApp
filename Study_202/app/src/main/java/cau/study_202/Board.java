package cau.study_202;

public class Board {

    private int id;
    private int groupId;
    private String memberId;
    private String title;
    private String content;
    private String attendenctime;
    private String attendencelatetime;
    private String latefine;
    private String absencefine;

    public Board(int id, int groupId, String memberId, String title, String content) {
        this.id = id;
        this.groupId = groupId;
        this.memberId = memberId;
        this.title = title;
        this.content = content;
    }

    public Board(int id, int groupId, String memberId, String title, String content, String attendenctime, String attendencelatetime, String latefine, String absencefine) {
        this(id,groupId,memberId,title,content);
        this.attendenctime = attendenctime;
        this.attendencelatetime = attendencelatetime;
        this.latefine = latefine;
        this.absencefine = absencefine;
    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public int getGroupId() {return groupId;}
    public void setGroupId(int id) {this.groupId = id;}

    public String getMemberId() {return memberId;}
    public void setMemberId(String s) {this.memberId = s;}

    public String getTitle() {return title;}
    public void setTitle(String s) {this.title = s;}

    public String getContent() {return content;}
    public void setContent(String s) {this.content = s;}

    public String getAttendenctime() {return attendenctime;}
    public String getAttendencelatetime() {return attendencelatetime;}
    public String getLatefine() {return latefine;}
    public String getAbsencefine() {return absencefine;}

}
