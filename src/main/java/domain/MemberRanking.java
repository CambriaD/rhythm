package domain;

public class MemberRanking {
    private boolean tokenCode;//登录验证token标识
    private int uid;//用户id
    private String member;//成员名字
    private int ranking;//排名
    private int punch;//打卡

    public MemberRanking() {
    }

    @Override
    public String toString() {
        return "MemberRanking{" +
                "tokenCode=" + tokenCode +
                ", uid=" + uid +
                ", member='" + member + '\'' +
                ", ranking=" + ranking +
                ", punch=" + punch +
                '}';
    }

    public boolean isTokenCode() {
        return tokenCode;
    }

    public void setTokenCode(boolean tokenCode) {
        this.tokenCode = tokenCode;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public int getPunch() {
        return punch;
    }

    public void setPunch(int punch) {
        this.punch = punch;
    }
}
