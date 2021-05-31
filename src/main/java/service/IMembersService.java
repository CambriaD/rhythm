package service;

import java.util.Map;

import domain.MemberQuery;
import domain.MemberRanking;
import domain.Message;

public interface IMembersService {
	/**
	 * 日历功能
	 * @param uid
	 * @param tid
	 */
	public int[] calendar(String uid,String tid,String month);
	
	/**
	 * 图表功能
	 */
	public int[] chart(String tid);

	  /**
     * 查询某天的打卡榜
     * @param tid 队伍id
     * @param day 查询日期，格式“yyyyMMdd”
     * @return 成员信息结果集合
     */
    public MemberQuery<MemberRanking> memberRankingForSpDay(String tid , String day);

    /**
     * 查询任意时间段某组每天打卡次数
     * @param tid 队伍id
     * @param stime 起始时间
     * @param etime 终止时间
     * @return
     */
    public Map<String , Integer> chatPlus(String tid , String stime , String etime);
	
    /**
     * 打卡榜（前五）
     *
     * @param tid
     * @return
     */
    public MemberQuery<MemberRanking> memberRanking(String tid);

    /**
     * 打卡排名
     *
     * @param uid
     * @param tid
     * @return
     */
    public MemberRanking punchRanking(String uid, String tid);

    /**
     * 立即打卡
     *
     * @param uid
     * @param tid
     * @param punchTime
     * @return
     */
    public Message memberPunch(String uid, String tid, String punchTime);

    /**
     * 打卡榜
     * @param tid
     * @return
     */
    public MemberQuery<MemberRanking> punchList(String tid);

    /**
     * 我的队员
     * @param tid
     * @return
     */
    public MemberQuery<MemberRanking> myMembers(String uid, String tid);

    /**
     * 删除成员
     * @param uid
     * @param tid
     * @return
     */
    public Message membersDelete(String uid, String tid);
}
