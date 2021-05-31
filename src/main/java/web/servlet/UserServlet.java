package web.servlet;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import domain.LoginMsg;
import domain.LoginUser;
import domain.Message;
import domain.User;
import redis.clients.jedis.Jedis;
import service.IUserService;
import service.impl.UserServiceImpl;
import util.JedisUtil;
import util.UuidUtil;

@WebServlet("/user/*")
public class UserServlet extends BaseServlet {
	IUserService userService = new UserServiceImpl();
	private static Logger logger = Logger.getLogger(UserServlet.class);

	/**
	 * 登录
	 *
	 * @param request
	 * @param response
	 */
	public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		LoginUser user = userService.login(email, password);
		LoginMsg message = new LoginMsg();
		if (user == null) {
			// 数据库中没有该邮箱信息
			message.setResultFlag(2); // 设置信息标志为2
			message.setErrormsg("没有该邮箱账号的相关信息，请检查邮箱的正确性或进行注册");
		} else {
			// 数据库有该邮箱信息
			if ((user.getPassword()).equals(password)) {
				message.setResultFlag(1); // 设置信息标志为1
				String token = UuidUtil.getUuid();
				message.setToken(token);
				// 将用户写回
				message.setUser(user);
				// 将token存入redis缓存
				Jedis jedis = JedisUtil.getJedis();
				jedis.setex(token, 60 * 60 * 24 * 30, String.valueOf(user));// 存一天
				jedis.close();
			} else {
				// 密码不一致
				message.setResultFlag(3);
				message.setErrormsg("邮箱或密码错误");
			}
		}
		// 将message写回
		writeValue(message, response);
	}

	/**
	 * 获取验证码
	 *
	 * @param request
	 * @param response
	 */
	public void getCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 获取数据
		String email = request.getParameter("email");
		// 调用userService的getCode方法
		int checkCode = userService.getCode(email);
		// 将验证码存入session
		request.getSession().setAttribute("CheckCode", checkCode);
	}

	/**
	 * 邮箱验证
	 *
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void emailVerification(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String checkCode = request.getParameter("checkCode");
		if (checkCode == null) {
			checkCode = "";
		}
		// 获取session中的验证码
		int sessionCode=(int) request.getSession().getAttribute("CheckCode");
		// 删除验证码session
		request.getSession().removeAttribute("CheckCode");
		// 2.将页面中得到的验证码数据转换成整型
		int code = Integer.parseInt(checkCode);
		// 3.判断验证码的正确性
		Message message = new Message();
		if (code==sessionCode) {
			message.setResultFlag(1);
			message.setIdentification(1);
		} else {
			message.setResultFlag(2);
			message.setErrormsg("验证码错误");
			message.setIdentification(99);
		}
		message.setTokenCode(true);
		writeValue(message, response);
	}

	/**
	 * 注册
	 *
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void regist(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String nickName = request.getParameter("nickName");
		String sex = request.getParameter("sex");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		// 调用userService的regist方法
		boolean flag = userService.regist(nickName, sex, email, password);
		Message message = new Message();
		if (flag) {
			// 注册成功
			message.setResultFlag(1);
		} else {
			// 注册失败
			message.setResultFlag(2);
			message.setErrormsg("已有该邮箱信息，请检查邮箱的正确性");
		}
		message.setTokenCode(true);
		writeValue(message, response);
	}

	/**
	 * 退出登录
	 *
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void exitUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Message message = new Message();
		// 获取token
		String token = request.getHeader("token");
		if (token == null) {
			token = "";
		}
		// 删除redis中该token对应的键值对
		Jedis jedis = JedisUtil.getJedis();
		Long del = jedis.del(token);
		// 关闭
		jedis.close();
		if (del == 1) {
			// 注销成功
			message.setResultFlag(1);
			message.setErrormsg("注销成功");
		} else {
			// 注销失败
			message.setResultFlag(2);
			message.setErrormsg("注销失败");
		}
		writeValue(message, response);
	}

	/**
	 * 重置密码
	 *
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void modifyPassword(HttpServletRequest request, HttpServletResponse response) throws IOException {
		/*
		 * //获取请求头中的token String token = request.getHeader("token"); token = token ==
		 * null ? "" : token; //查询redis中是否存在 Jedis jedis = JedisUtil.getJedis(); String
		 * s = jedis.get(token); if (s == null) { //关闭连接 jedis.close(); //未登录 Message
		 * message = new Message(); message.setTokenCode(false); //直接写回数据
		 * writeValue(message, response); return; } else { //认证成功 jedis.expire(token, 60
		 * * 60 * 24);//重置时间 //关闭连接 jedis.close();
		 */

		String email = request.getParameter("email");
		String password = request.getParameter("password");
		// 调用userService的modifyPassword方法
		Message message = userService.modifyPassword(email, password);
		message.setTokenCode(true);
		writeValue(message, response);
	}

	/**
	 * 修改个人资料
	 *
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void personalData(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 获取请求头中的token
		String token = request.getHeader("token");
		token = token == null ? "" : token;
		// 查询redis中是否存在
		Jedis jedis = JedisUtil.getJedis();
		String s = jedis.get(token);
		if (s == null) {
			// 关闭连接
			jedis.close();
			// 未登录
			Message message = new Message();
			message.setTokenCode(false);
			// 直接写回数据
			writeValue(message, response);
			return;
		} else {
			// 认证成功
			jedis.expire(token, 60 * 60 * 24);// 重置时间
			// 关闭连接
			jedis.close();
			// 获取数据
			String uid = request.getParameter("uid");
			String nickName = request.getParameter("nickName");
			String sex = request.getParameter("sex");
			String birthday = request.getParameter("birthday");
			String address = request.getParameter("address");
			String school = request.getParameter("school");
			String workUnit = request.getParameter("workUnit");
			LoginUser user = new LoginUser();
			user.setUid(Integer.parseInt(uid));
			user.setNickName(nickName);
			user.setSex(sex);
			user.setBirthday(birthday);
			user.setAddress(address);
			user.setSchool(school);
			user.setWorkUnit(workUnit);
			// 调用userService的personalData方法
			Message message = userService.personalData(user);
			message.setTokenCode(true);
			// 数据写回
			writeValue(message, response);
		}
	}

	/**
	 * 获取用户信息
	 *
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void getUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 获取请求头中的token
		String token = request.getHeader("token");
		token = token == null ? "" : token;
		// 查询redis中是否存在
		Jedis jedis = JedisUtil.getJedis();
		String s = jedis.get(token);
		if (s == null) {
			// 关闭连接
			jedis.close();
			// 未登录
			User user = new User();
			user.setTokenCode(false);
			// 直接写回数据
			writeValue(user, response);
			return;
		} else {
			// 认证成功
			jedis.expire(token, 60 * 60 * 24);// 重置时间
			// 关闭连接
			jedis.close();
			// 获取数据
			String uid = request.getParameter("uid");
			// 调用userService的getUser方法
			User user = userService.getUser(uid);
			user.setTokenCode(true);
			writeValue(user, response);
		}
	}
}
