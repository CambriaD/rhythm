package domain;

public class LoginUser {
    private int uid;//用户id
    private String email;//邮箱
    private String password;//密码
    private String nickName;//昵称
    private String sex;//性别
    private String birthday;//生日
    private String address;//所在地
    private String school;//学校
    private String workUnit;//工作单位

    public LoginUser(int uid, String email, String password, String nickName, String sex, String birthday, String address, String school, String workUnit) {
        this.uid = uid;
        this.email = email;
        this.password = password;
        this.nickName = nickName;
        this.sex = sex;
        this.birthday = birthday;
        this.address = address;
        this.school = school;
        this.workUnit = workUnit;
    }

    public LoginUser() {
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getWorkUnit() {
        return workUnit;
    }

    public void setWorkUnit(String workUnit) {
        this.workUnit = workUnit;
    }

    @Override
    public String toString() {
        return "LoginUser{" +
                "uid=" + uid +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", nickName='" + nickName + '\'' +
                ", sex='" + sex + '\'' +
                ", birthday='" + birthday + '\'' +
                ", address='" + address + '\'' +
                ", school='" + school + '\'' +
                ", workUnit='" + workUnit + '\'' +
                '}';
    }
}
