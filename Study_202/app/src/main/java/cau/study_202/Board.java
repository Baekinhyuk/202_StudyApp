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
    private String votedId;
    private int voteType;
    private int numofvoters;
    private int pros;
    private int cons;

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

    public Board(int id, int groupId, String memberId, String votedId, int voteType, String content, int numofvoters, int pros, int cons) {
        this(id, groupId, memberId, "", content);
        if (voteType == 0)
            title = votedId + "님에 대한 출석 투표";
        else
            title = votedId + "님에 대한 강퇴 투표";
        this.votedId = votedId;
        this.voteType = voteType;
        this.numofvoters = numofvoters;
        this.pros = pros;
        this.cons = cons;
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

    public int getNumofvoters() {return numofvoters;}
    public int getPros() {return pros;}
    public int getCons() {return cons;}
    public int getVoteType() {return voteType;}
    public String getVotedId() {return votedId;}


}
