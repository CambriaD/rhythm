package dao;

import java.util.List;

import domain.MemberRanking;

public interface IMemberDao {
	/**
	 * 日历功能，
	 * @param uid
	 * @param tid
	 * @return
	 */
	public int[] calendar(String uid,String tid,String month);
	
	/**
	 * 图表功能
	 */
	public int[] chart(String tid);
	
	 /**
     * 查询某天的打开排行
     * @param tid 队伍id
     * @param day 查询日期，格式“yyyyMMdd”
     * @return 成员信息结果集合
     */
    public List<MemberRanking> memberRankingForSpDay(String tid , String day);

    /**
     * 查询某天某队伍的全员打卡次数
     * @param tid 队伍id
     * @param st 起始时间
     * @param et 终止时间
     * @return 某天某队伍的全员打卡次数
     */
    public int timesOfPunch(String tid , long st , long et);

    /**
     * 打卡榜（前五）
     *
     * @param tid
     * @return
     */
    public List<MemberRanking> memberRanking(String tid);

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
    public boolean memberPunch(String uid, String tid, String punchTime);

    /**
     * 打卡榜
     *
     * @param tid
     * @return
     */
    public List<MemberRanking> punchList(String tid);

    /**
     * 我的队员
     *
     * @param tid
     * @return
     */
    public List<MemberRanking> myMembers(String uid, String tid);

    /**
     * 删除成员
     * @param uid
     * @param tid
     * @return
     */
    public boolean membersDelete(String uid, String tid);
}
