package dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import dao.ITeamDao;
import domain.Member;
import domain.MemberRanking;
import util.JDBCUtils;

public class TeamDaoImpl implements ITeamDao {
    JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    /**
     * 队伍大厅
     *
     * @param teamName
     * @param type
     * @param frequency
     * @return
     */
    @Override
    public List<Member> findAll(String teamName, String type, String frequency) {
        List<Member> list = null;
        //select a.*,tab_user.nickName from tab_user inner join  (select * from tab_team where teamName = ? and type = ? and frequency = ? )as a on tab_user.uid = a.uid order by a.tid
        String sql = " select a.*,tab_user.nickName from tab_user inner join  (select * from tab_team where 1 = 1 ";
        List params = new ArrayList();
        StringBuilder sb = new StringBuilder(sql);
        if (type != null && type.length() > 0 && !"全部".equals(type)) {
            sb.append(" and type = ? ");
            params.add(type);
        }
        if (frequency != null && frequency.length() > 0 && !"全部".equals(frequency)) {
            sb.append(" and frequency = ? ");
            params.add(frequency);
        }
        if (teamName != null && teamName.length() > 0 && !"null".equals(teamName)) {
            sb.append(" and teamName like ? ");
            params.add("%" + teamName + "%");
        }
        sb.append(" )as a on tab_user.uid = a.uid order by a.tid DESC ");
        sql = sb.toString();

        try {
            list = template.query(sql, new BeanPropertyRowMapper<Member>(Member.class), params.toArray());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * 队伍占比
     */
    public Map<String,Integer> ratio(){
    	HashMap<String,Integer> map=new HashMap<String,Integer>();
    	String[] arr=new String[]{"运动","学习","出游","工作","节日/纪念日","其他"};
    	for(int i=0;i<arr.length;i++) {
    		String sql="select count(*) from tab_team where type='"+arr[i]+"'";
    		map.put(arr[i],	template.queryForInt(sql));
    	}
    	return map;
    }
    /**
     * 创建队伍
     *
     * @param member
     * @return
     */
    @Override
    public boolean teamEstablish(Member member) {
        boolean flag = false;
        //定义sql
        //insert into tab_team ( uid, teamName, type, frequency, beginDate, endDate, datailedTime, synopsis ) values ( ?, ?, ?, ?, ?, ?, ?, ? )
        String sql = " insert into tab_team ( uid, teamName, type, frequency, beginDate, endDate, startTime, endTime,synopsis ) "
                + " values ( ?, ?, ?, ?, ?, ?, ?, ?, ? ) ";
        try {
            template.update(sql,
                    member.getUid(),
                    member.getTeamName(),
                    member.getType(),
                    member.getFrequency(),
                    member.getBeginDate(),
                    member.getEndDate(),
                    member.getStartTime(),
                    member.getEndTime(),
                    member.getSynopsis());
            flag = true;
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        //定义成员sql
        String sqlMembers = " insert into tab_members(uid, tid, member) values ( ?, (select tid from tab_team where uid = ? and teamName = ? and type = ? and frequency = ? and beginDate = ? and endDate = ? and startTime = ? and endTime = ? and synopsis = ? ), ( select nickName from tab_user where uid = ? ) ) ";
        try {
            template.update(sqlMembers,
                    member.getUid(),
                    member.getUid(),
                    member.getTeamName(),
                    member.getType(),
                    member.getFrequency(),
                    member.getBeginDate(),
                    member.getEndDate(),
                    member.getStartTime(),
                    member.getEndTime(),
                    member.getSynopsis(),
                    member.getUid());
            flag = true;
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 队伍详情
     *
     * @param tid
     * @return
     */
    @Override
    public Member teamDetails(String tid) {
        //定义sql
        String sql = "select a.*,tab_user.nickName from tab_user inner join  (select * from tab_team where tid = ? )as a on tab_user.uid = a.uid order by a.tid";
        return template.queryForObject(sql, new BeanPropertyRowMapper<Member>(Member.class), tid);
    }

    /**
     * 显示功能
     *
     * @param uid
     * @param tid
     * @return
     */
    @Override
    public boolean isTeamMember(String uid, String tid) {
        boolean flag = false;
        List<Map<String , Object>> mem = new ArrayList<>();
        //定义sql
        String sql = "select * from tab_members where uid = ? and tid = ?";
        try {
            mem = template.queryForList(sql, uid, tid);
            flag = !mem.isEmpty();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 加入功能
     *
     * @param uid
     * @param tid
     * @return
     */
    @Override
    public boolean joinTeam(String uid, String tid) {
        boolean flag = false;
        List<MemberRanking> list=new ArrayList<MemberRanking>();
        String sqlSelect = "select * from tab_members where uid = ? and tid = ? ";
        String sql = "insert into tab_members ( uid, tid, member ) values ( ?, ?, ( select nickName from tab_user where uid = ? ) ) ";
        try {
            list=template.query(sqlSelect, new BeanPropertyRowMapper<MemberRanking>(MemberRanking.class), uid,tid);
            if(list.isEmpty()) {
                try {
                    template.update(sql, uid, tid, uid);
                    flag = true;
                } catch (DataAccessException e) {
                    e.printStackTrace();
                }
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 退出功能
     *
     * @param uid
     * @param tid
     * @return
     */
    @Override
    public boolean exitTeam(String uid, String tid) {
        boolean flag = false;
        Member member = null;
        //定义sql
        //查询退出队伍的人是否为该队伍的创建者
        String sqlIsTeamCreateer = " select * from tab_team where uid = ? and tid = ? ";
        try {
            member = template.queryForObject(sqlIsTeamCreateer,new BeanPropertyRowMapper<Member>(Member.class),uid,tid);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        if (member != null){
            //退出的是创建者
            flag = false;
            return  flag;
        }else {
            //退出的不是创建者
            List<Map<String,Object>> ranking = new ArrayList<>();
            String sqlSelect = "select * from tab_members where uid = ? and tid = ? ";
            String sql = "delete from tab_members where uid = ? and tid = ?";
            try {
                ranking = template.queryForList(sqlSelect, uid, tid);
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
            if (!ranking.isEmpty()) {
                //查到，可退出
                try {
                    template.update(sql, uid, tid);
                    flag = true;
                } catch (DataAccessException e) {
                    e.printStackTrace();
                }
            }
            return flag;
        }
    }

    /**
     * 我加入的队伍
     *
     * @param uid
     * @return
     */
    @Override
    public List<Member> teamQuery(String uid) {
        List<Member> list = null;
        //定义sql
        //SELECT c.punch,b.* FROM (SELECT punch,tid FROM tab_members WHERE uid = 1 ) AS c INNER JOIN (SELECT a.*,tab_user.nickName  FROM tab_user INNER JOIN  (SELECT * FROM tab_team WHERE tid IN (  SELECT tid FROM tab_members WHERE uid = 1) )AS a ON tab_user.uid = a.uid)AS b ON c.tid = b.tid ORDER BY b.tid
        String sql = " select c.punch,b.* from (select punch,tid from tab_members where uid = ? ) as c " +
                " inner join (select a.*,tab_user.nickName  from tab_user inner join  (select * from tab_team where tid in ( select tid from tab_members where uid = ? ) )as a on tab_user.uid = a.uid) as b " +
                " on c.tid = b.tid order by b.tid ";
        try {
            list = template.query(sql, new BeanPropertyRowMapper<Member>(Member.class), uid, uid);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 我创建的队伍
     *
     * @param uid
     * @return
     */
    @Override
    public List<Member> myTeam(String uid) {
        List<Member> list = null;
        //定义sql
        // SELECT a.*,tab_user.nickName FROM tab_user INNER JOIN  (SELECT * FROM tab_team WHERE uid = 1 )AS a ON tab_user.`uid` = a.uid ORDER BY a.tid
        String sql = " select a.*,tab_user.nickName from tab_user inner join (select * from tab_team where uid = ? ) as a on tab_user.uid = a.uid order by a.tid ";
        try {
            list = template.query(sql, new BeanPropertyRowMapper<Member>(Member.class), uid);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 解散小队
     *
     * @param tid
     * @return
     */
    @Override
    public boolean dissolutionTeam(String tid) {
        boolean flag = false;
        //定义sql
        //删除成员sql
        String sqlMembers = " delete from tab_members where tid = ? ";
        //删除队伍sql
        String sqlTeam = " delete from tab_team where tid = ? ";
        try {
            template.update(sqlMembers, tid);
            template.update(sqlTeam, tid);
            flag = true;
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 队伍信息修改
     *
     * @param member
     * @return
     */
    @Override
    public boolean teamModify(Member member) {
        boolean flag = false;
        //定义sql
        // update tab_team set tid = ?, teamName = ?, type = ?, frequency = ?, beginDate = ?, endDate = ?, datailedTime = ?, synopsis = ? where tid = ?
        String sql = " update tab_team set tid = ? ";
        List params = new ArrayList();
        StringBuilder sb = new StringBuilder(sql);
        params.add(member.getTid());
        if (member.getTeamName() != null && member.getTeamName().length() > 0 && !"null".equals(member.getTeamName())) {
            sb.append(" , teamName = ? ");
            params.add(member.getTeamName());
        }
        if (member.getType() != null && member.getType().length() > 0 && !"null".equals(member.getType())) {
            sb.append(" , type = ? ");
            params.add(member.getType());
        }
        if (member.getFrequency() != null && member.getFrequency().length() > 0 && !"null".equals(member.getFrequency())) {
            sb.append(" , frequency = ? ");
            params.add(member.getFrequency());
        }
        if (member.getBeginDate() != null && member.getBeginDate().length() > 0 && !"null".equals(member.getBeginDate())) {
            sb.append(" , beginDate = ? ");
            params.add(member.getBeginDate());
        }
        if (member.getEndDate() != null && member.getEndDate().length() > 0 && !"null".equals(member.getEndDate())) {
            sb.append(" , endDate = ? ");
            params.add(member.getEndDate());
        }
        if (member.getStartTime() != null && member.getStartTime().length() > 0 && !"null".equals(member.getStartTime())) {
            sb.append(" , startTime = ? ");
            params.add(member.getStartTime());
        }
        if (member.getEndTime() != null && member.getEndTime().length() > 0 && !"null".equals(member.getEndTime())) {
            sb.append(" , endTime = ? ");
            params.add(member.getEndTime());
        }
        if (member.getSynopsis() != null && member.getSynopsis().length() > 0 && !"null".equals(member.getSynopsis())) {
            sb.append(" , synopsis = ? ");
            params.add(member.getSynopsis());
        }
        sb.append(" where tid = ? ");
        params.add(member.getTid());
        sql = sb.toString();
        try {
            template.update(sql, params.toArray());
            flag = true;
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return flag;
    }
}
