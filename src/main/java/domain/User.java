package domain;

public class User {
    private boolean tokenCode;//登录验证token标识
    private LoginUser user;//用户信息

    public boolean isTokenCode() {
        return tokenCode;
    }

    public void setTokenCode(boolean tokenCode) {
        this.tokenCode = tokenCode;
    }

    public LoginUser getUser() {
        return user;
    }

    public void setUser(LoginUser user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "User{" +
                "tokenCode=" + tokenCode +
                ", user=" + user +
                '}';
    }
}
