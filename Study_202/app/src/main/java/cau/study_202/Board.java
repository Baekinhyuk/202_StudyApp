package cau.study_202;

public class Board {

    private int id;
    private int groupId;
    private String memberId;
    private String title;
    private String content;

    public Board(int id, int groupId, String memberId, String title, String content) {
        this.id = id;
        this.groupId = groupId;
        this.memberId = memberId;
        this.title = title;
        this.content = content;
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

}
