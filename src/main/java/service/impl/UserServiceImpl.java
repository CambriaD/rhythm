package service.impl;

import dao.IUserDao;
import dao.impl.UserDaoImpl;
import domain.LoginUser;
import domain.MemberQuery;
import domain.Message;
import domain.User;
import service.IUserService;
import util.MailUtils;
import util.RandomUtils;

public class UserServiceImpl implements IUserService {
    IUserDao userDao = new UserDaoImpl();
    Message message = new Message();
    RandomUtils random = new RandomUtils();
    /**
     * 登录
     *
     * @param email
     * @param password
     * @return
     */
    @Override
    public LoginUser login(String email, String password) {
        return userDao.login(email);
    }

    /**
     * 获取验证码
     *
     * @param email
     */
    @Override
    public int getCode(String email) {
        //调用getRandom方法生成验证码
        int checkCode = random.getRandom();
        //调用MailUtils方法向指定用户发送邮件
        try {
            MailUtils.sendMail(email, "欢迎您使用律动打卡,本次验证码为：<span style=\"font-size:20px;color:#35B59C;\">"
                    + checkCode +
                    "</span>,切勿将验证码泄露于他人！", "【验证码】");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return checkCode;
    }

    /**
     * 注册
     *
     * @param nickName
     * @param sex
     * @param email
     * @param password
     * @return
     */
    @Override
    public boolean regist(String nickName, String sex, String email, String password) {
        return userDao.regist(nickName, sex, email, password);
    }

    /**
     * 重置密码
     */
    @Override
    public Message modifyPassword(String email, String password) {
        Message message = new Message();
        boolean flag = userDao.modifyPassword(email, password);
        if (flag) {
            //修改密码成功
            message.setResultFlag(1);
        } else {
            message.setResultFlag(2);
            message.setErrormsg("修改失败");
        }
        return message;
    }

    /**
     * 修改个人资料
     *
     * @param user
     * @return
     */
    @Override
    public Message personalData(LoginUser user) {
        Message message = new Message();
        boolean flag = userDao.personalData(user);
        if (flag) {
            //修改个人资料成功
            message.setResultFlag(1);
        } else {
            message.setResultFlag(2);
            message.setErrormsg("您还有信息未填写");
        }
        return message;
    }

    /**
     * 获取用户信息
     */
    @Override
    public User getUser(String uid) {
        User user = new User();
        LoginUser usermsg = userDao.getUser(uid);
        user.setUser(usermsg);
        return user;
    }
}
