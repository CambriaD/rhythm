package domain;

public class Member {
    private String nickName;//昵称
    private int tid;//队伍号
    private int uid;//用户id
    private String teamName;//小队名称
    private String type;//打卡类型
    private String frequency;//打卡频率
    private String beginDate;//开始日期
    private String endDate;//截止日期
    private String startTime;//开始时间
    private String endTime;//结束时间
    private String synopsis;//简介
    private int punch;//是否打卡

    public Member() {
    }

    public Member(String nickName, int tid, int uid, String teamName, String type, String frequency, String beginDate, String endDate, String startTime, String endTime, String synopsis, int punch) {
        this.nickName = nickName;
        this.tid = tid;
        this.uid = uid;
        this.teamName = teamName;
        this.type = type;
        this.frequency = frequency;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.synopsis = synopsis;
        this.punch = punch;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public int getPunch() {
        return punch;
    }

    public void setPunch(int punch) {
        this.punch = punch;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Member{" +
                "nickName='" + nickName + '\'' +
                ", tid=" + tid +
                ", uid=" + uid +
                ", teamName='" + teamName + '\'' +
                ", type='" + type + '\'' +
                ", frequency='" + frequency + '\'' +
                ", beginDate='" + beginDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", synopsis='" + synopsis + '\'' +
                ", punch=" + punch +
                '}';
    }
}
