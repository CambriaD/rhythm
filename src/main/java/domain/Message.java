package domain;

public class Message {
    private boolean tokenCode;//token登录验证标识
    private int resultFlag;//信息标识
    private String errormsg;//错误信息
    private int identification;//前端信息标识

    public Message() {
    }

    public boolean isTokenCode() {
        return tokenCode;
    }

    public void setTokenCode(boolean tokenCode) {
        this.tokenCode = tokenCode;
    }

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

    public int getIdentification() {
        return identification;
    }

    public void setIdentification(int identification) {
        this.identification = identification;
    }

    @Override
    public String toString() {
        return "Message{" +
                "tokenCode=" + tokenCode +
                ", resultFlag=" + resultFlag +
                ", errormsg='" + errormsg + '\'' +
                ", identification=" + identification +
                '}';
    }
}
