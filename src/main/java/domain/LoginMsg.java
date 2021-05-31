package domain;

public class LoginMsg {
    private int resultFlag;//信息标识
    private String errormsg;//错误信息
    private String token;//token标识
    private LoginUser user;//用户信息

    public int getResultFlag() {
        return resultFlag;
    }

    public void setResultFlag(int resultFlag) {
        this.resultFlag = resultFlag;
    }

    public String getErrormsg() {
        return errormsg;
    }

    public void setErrormsg(String errormsg) {
        this.errormsg = errormsg;
    }

    public LoginUser getUser() {
        return user;
    }

    public void setUser(LoginUser user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "LoginMsg{" +
                "resultFlag=" + resultFlag +
                ", errormsg='" + errormsg + '\'' +
                ", token='" + token + '\'' +
                ", user=" + user +
                '}';
    }
}
