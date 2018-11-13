package cau.study_202;

public class Stat {

    private int state;
    private String date;
    private String day;
    private int method;
    private String id;
    private int fine;

    public Stat(String id, int method, String datetime, int state, int fine) {
        this.id = id;
        this.method = method;
        String[] split = datetime.split("\\s+");
        this.date = split[0];
        this.day = split[1];
        this.state = state;
        this.fine =fine;
    }

    public int getState() {return state;}
    public int getMethod() {return method;}
    public int getFine() {return fine;}
    public String getdate() {return date;}
    public String getday() {return day;}
    public String getid() {return id;}
}
