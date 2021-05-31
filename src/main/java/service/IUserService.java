package service;

import domain.LoginUser;
import domain.MemberQuery;
import domain.Message;
import domain.User;

public interface IUserService {
    /**
     * 登录
     *
     * @param email
     * @param password
     * @return
     */
    public LoginUser login(String email, String password);

    /**
     * 发送验证码
     *
     * @param email
     */
    public int getCode(String email);


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
    public Message modifyPassword(String email, String password);

    /**
     * 修改个人资料
     * @param user
     * @return
     */
    public Message personalData(LoginUser user);

    /**
     * 获取用户信息
     * @param uid
     * @return
     */
    public User getUser(String uid);
}
