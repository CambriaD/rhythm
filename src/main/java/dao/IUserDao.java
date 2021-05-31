package dao;

import domain.LoginUser;
import domain.Message;

public interface IUserDao {
    /**
     * 登录
     * @param email
     * @return
     */
    public LoginUser login(String email);

    /**
     * 注册
     * @param nickName
     * @param sex
     * @param email
     * @param password
     * @return
     */
    public boolean regist(String nickName, String sex, String email, String password);

    /**
     * 重置密码
     * @param email
     * @param password
     * @return
     */
    public boolean modifyPassword(String email, String password);

    /**
     * 修改个人资料
     * @param user
     * @return
     */
    public boolean personalData(LoginUser user);

    /**
     * 获取用户信息
     * @param uid
     * @return
     */
    public LoginUser getUser(String uid);
}
