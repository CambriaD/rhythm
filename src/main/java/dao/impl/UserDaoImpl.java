package dao.impl;

import dao.IUserDao;
import domain.LoginUser;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import util.JDBCUtils;

public class UserDaoImpl implements IUserDao {
	JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
	private static Logger logger = Logger.getLogger(UserDaoImpl.class);

	/**
	 * 登录
	 *
	 * @param email
	 * @return
	 */
	@Override
	public LoginUser login(String email) {
		LoginUser user = null;
		try {
			String sql = " select * from tab_user where email = ? ";
			user = template.queryForObject(sql, new BeanPropertyRowMapper<LoginUser>(LoginUser.class), email);
		} catch (DataAccessException e) {
			user=null;
		}
		return user;
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
		boolean flag = false;
		LoginUser user = null;

		try {
			String sqlSelect = " select * from tab_user where email = ? ";
			user = template.queryForObject(sqlSelect, new BeanPropertyRowMapper<LoginUser>(LoginUser.class), email);
		} catch (Exception e) {
			user = null;
		}
		if (user == null) {// 数据库没有该信息，插入
			try {
				String sql = " insert into tab_user (nickName,sex,email,password) values (?,?,?,?) ";
				template.update(sql, nickName, sex, email, password);
				flag = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return flag;
	}
	
	/**
	 * 重置密码
	 *
	 * @param email
	 * @param password
	 * @return
	 */
	@Override
	public boolean modifyPassword(String email, String password) {
		boolean flag = false;
		String sql = " update tab_user set password = ? where email = ? ";
		try {
			template.update(sql, password, email);
			flag = true;
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 修改个人资料
	 *
	 * @param user
	 * @return
	 */
	@Override
	public boolean personalData(LoginUser user) {
		boolean flag = false;
		String sql = "update tab_user set nickName = ? , sex = ? , birthday = ? , address = ? , school = ? , workUnit = ? where uid = ?";
		try {
			template.update(sql, user.getNickName(), user.getSex(), user.getBirthday(), user.getAddress(),
					user.getSchool(), user.getWorkUnit(), user.getUid());
			flag = true;
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 获取用户信息
	 *
	 * @param uid
	 * @return
	 */
	@Override
	public LoginUser getUser(String uid) {
		LoginUser user = null;
		// 定义sql
		String sql = " select * from tab_user where uid = ? ";
		try {
			user = template.queryForObject(sql, new BeanPropertyRowMapper<LoginUser>(LoginUser.class), uid);
		} catch (DataAccessException e) {
			user=null;
		}
		return user;
	}
}
