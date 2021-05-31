package dao.impl;

import java.text.SimpleDateFormat;
import java.util.*;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import dao.IMemberDao;
import domain.MemberRanking;
import util.JDBCUtils;

public class MemberDaoImpl implements IMemberDao {
    JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
    /**
     * 日历
     */
    @Override
    public int[] calendar(String uid,String tid,String month) {
    	int[] a=new int[31];
        //List<Date> listDate=new ArrayList();
    	List<Map<String , Object>> listDate=new ArrayList();
    	String sql="select punchTime from tab_members where uid=? and tid=? and punch=1";
    	try {
            listDate=template.queryForList(sql , uid , tid);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
		/*Calendar calendar = Calendar.getInstance();
    	calendar.set(2021, Integer.parseInt(month)-1, 1, 0, 0, 0);
    	Date begin=calendar.getTime();
    	calendar.set(2021, Integer.parseInt(month), 1, 0, 0, 0);
    	Date end=calendar.getTime();
    	int index=0;
    	for(int i=0;i<listDate.size();i++) {
    		if(begin.compareTo(listDate.get(i))==-1&&listDate.get(i).compareTo(end)==-1) {
    			a[index++]=listDate.get(i).getDate();
    		}
    	}
    	int[] value=new int[index];
    	System.arraycopy(a, 0, value, 0, index);
    	return value;*/

        int[] result = new int[listDate.size()];
        int imonth = Integer.parseInt(month);

        for (int i = 0 ; i < listDate.size() ; i++) {
            String temp = listDate.get(i).get("punchTime").toString();
            int m = Integer.parseInt(temp.substring(5,7));
            if (m == imonth) {
                result[i] = Integer.parseInt(temp.substring(8,10));
            }
        }

        return result;
    }
    
    /**
     * 图表功能
     */
    @Override
    public int[] chart(String tid){
    	int[] value=new int[7];
    	SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
    	Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR , 11);
        calendar.set(Calendar.MINUTE , 0);
        calendar.set(Calendar.SECOND , 0);
        calendar.add(Calendar.HOUR , -12);//不知道为啥set hour 11 拿到的时间是23
    	String ago=null;
    	String later=null;
    	
    	for(int i=6;i>=0;i--) {
    		later=df.format(calendar.getTime());	//先获取后面结点的时间字符串
    		calendar.add(Calendar.DAY_OF_MONTH, -1);	//每次循环该对象上调一天
    		ago=df.format(calendar.getTime());			//获取上调一天的字符串
    		String sql="select count(*) from tab_members where tid= ? and punchTime >= ? and punchTime < ?";
    		value[i]=template.queryForInt(sql, tid , ago , later);
    	}
    	return value;
    }
    
    /**
     * 查询某天的打卡排行
     * @param tid 队伍id
     * @param day 查询日期，格式“yyyyMMdd”
     * @return
     */
    @Override
    public List<MemberRanking> memberRankingForSpDay(String tid , String day) {
        List<MemberRanking> list = null;
        String sql = "select uid,member,punch,(@rowNum:=@rowNum+1) as ranking from tab_members,(select (@rowNum :=0) ) b where punch = 1 and tid = ?\n" +
                "and punchTime >= ? and punchTime < ?" +
                " order by tab_members.punchTime asc limit 5 ";

        int st = Integer.parseInt(day);
        Calendar calendar = new GregorianCalendar();
        calendar.set(st/10000 , (st/100)%100-1 , st%100 , 0 , 0 , 0);
        calendar.add(Calendar.HOUR , -13);
        Date sdate = calendar.getTime();
        calendar.add(Calendar.DATE , 1);
        Date edate = calendar.getTime();

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");

        try {
            list = template.query(sql, new BeanPropertyRowMapper<MemberRanking>(MemberRanking.class), tid , df.format(sdate) , df.format(edate));
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 查询某天某队伍的全员打卡次数
     * @param tid 队伍id
     * @param st 起始时间
     * @param et 终止时间
     * @return 某天某队伍的全员打卡次数
     */
    @Override
    public int timesOfPunch(String tid , long st , long et) {
        int count;
        String sql = "select count(MID) from tab_members where tid = ? and punchTime >= ? and punchTime < ?";

        try {
            count = template.queryForInt(sql , tid , st , et);
        } catch (Exception e) {
            e.printStackTrace();
            count = -1;
        }

        return count;
    }

    /**
     * 打卡重置
     */
    public void resetPunch() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String date = df.format(new Date());// new Date()为获取当前系统时间
        String sql = "update tab_members set punch = case when punchTime >= ? then 1 else 0 end ";
        try {
            template.update(sql, date);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打卡榜（前五）
     *
     * @param tid
     * @return
     */
    @Override
    public List<MemberRanking> memberRanking(String tid) {
        List<MemberRanking> list = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String date = df.format(new Date());// new Date()为获取当前系统时间
        String sql = "select uid,member,punch,(@rowNum:=@rowNum+1) as ranking from tab_members,(select (@rowNum :=0) ) b where punch = 1 and tid = ? and punchTime>= ? \n" +
                "order by tab_members.punchTime asc limit 5 ";
        try {
            list = template.query(sql, new BeanPropertyRowMapper<MemberRanking>(MemberRanking.class), tid , date);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 打卡排名
     *
     * @param uid
     * @param tid
     * @return
     */
    @Override
    public MemberRanking punchRanking(String uid, String tid) {
        MemberRanking ranking = null;
        /*SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String date = df.format(new Date());// new Date()为获取当前系统时间
        //定义sql
        String sqlPunch = "update tab_members set punch = (case when punchTime>= ? then 1 else 0 end) where uid = ? and tid = ? ";
        try {
            //更新打卡
            template.update(sqlPunch, date, uid, tid);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }*/
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.add(Calendar.HOUR,-13);

        String date = df.format(calendar.getTime());

        String sql = " select u.uid,u.tid,u.member,u.punch,u.ranking from (\n" +
                "select uid,punchTime,tid,member,punch,(@rowNum:=@rowNum+1) as ranking\n" +
                "from tab_members,\n" +
                "(select (@rowNum :=0) ) b where punch = 1 and tid = ? \n and punchTime >= ?" +
                "order by tab_members.punchTime asc ) u where u.uid = ? ";
        try {
            ranking = template.queryForObject(sql, new BeanPropertyRowMapper<MemberRanking>(MemberRanking.class), tid, date , uid);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return ranking;
    }


    /**
     * 立即打卡
     *
     * @param uid
     * @param tid
     * @param punchTime
     * @return
     */
    @Override
    public boolean memberPunch(String uid, String tid, String punchTime) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String date = df.format(new Date(System.currentTimeMillis() - 46800000));//-13 hours
        boolean flag = false;
        String nickName=null;
        String sql1="SELECT nickname FROM tab_user where uid=?";
        try {
            nickName=template.queryForObject(sql1, String.class, uid);
        } catch (DataAccessException e) {
            e.printStackTrace();
            return flag;//打卡用户不存在
        }
        String sql2="insert into tab_members (uid,tid,member,punchTime,punch) values (?,?,?,?,?)";
        try {
            if(template.update(sql2, uid,tid,nickName,date,1)==1) {
                flag=true;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 打卡榜
     *
     * @param tid
     * @return
     */
    @Override
    public List<MemberRanking> punchList(String tid) {
        List<MemberRanking> list = null;
        //定义sql
        String sql = "select uid,member,punch,(@rowNum:=@rowNum+1) as ranking from tab_members,(select (@rowNum :=0) ) b where punch = 1 and tid = ? \n" +
                "order by tab_members.punchTime asc ";
        try {
            list = template.query(sql, new BeanPropertyRowMapper<MemberRanking>(MemberRanking.class), tid);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 我的队员
     *
     * @param tid
     * @return
     */
    @Override
    public List<MemberRanking> myMembers(String uid, String tid) {
        List<MemberRanking> list = null;
        //定义sql
        String sql = " select * from tab_members where tid = ? and uid != ? ";
        try {
            list = template.query(sql, new BeanPropertyRowMapper<MemberRanking>(MemberRanking.class), tid,uid);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 删除成员
     * @param uid
     * @param tid
     * @return
     */
    @Override
    public boolean membersDelete(String uid, String tid) {
        boolean flag = false;
        //定义sql
        String sql = " delete from tab_members where uid = ? and tid = ? ";
        try {
            template.update(sql,uid,tid);
            flag = true;
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return flag;
    }
}
