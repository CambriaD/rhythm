package domain;

import java.util.List;

public class MemberQuery<T> {
    private boolean tokenCode;//token登录验证标识
    private int code;//标识
    private List<T> list;

    public MemberQuery() {
    }

    public boolean isTokenCode() {
        return tokenCode;
    }

    public void setTokenCode(boolean tokenCode) {
        this.tokenCode = tokenCode;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "MemberQuery{" +
                "tokenCode=" + tokenCode +
                ", code=" + code +
                ", list=" + list +
                '}';
    }
}
