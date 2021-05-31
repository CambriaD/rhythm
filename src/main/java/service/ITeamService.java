package service;

import java.util.Map;

import domain.Member;
import domain.MemberQuery;
import domain.Message;

public interface ITeamService {
    /**
     * 队伍大厅
     * @param teamName
     * @param type
     * @param frequency
     * @return
     */
    public MemberQuery<Member> findAll(String teamName,String type, String frequency);

    /**
     * 队伍占比
     */
    public Map<String,Integer> ratio();
    /**
     * 创建队伍
     * @param member
     * @return
     */
    public Message teamEstablish (Member member);

    /**
     * 队伍详情
     * @param tid
     * @return
     */
    public Member teamDetails(String tid);

    /**
     * 显示功能
     * @param uid
     * @param tid
     * @return
     */
    public Message isTeamMember(String uid, String tid);

    /**
     * 加入功能
     * @param uid
     * @param tid
     * @return
     */
    public Message joinTeam(String uid, String tid);
    /**
     * 退出功能
     * @param uid
     * @param tid
     * @return
     */
    public Message exitTeam(String uid, String tid);
    /**
     * 我加入的队伍
     * @param uid
     * @return
     */
    public MemberQuery<Member> teamQuery(String uid);

    /**
     * 我创建的队伍
     * @param uid
     * @return
     */
    public MemberQuery<Member> myTeam(String uid);

    /**
     * 解散小队
     * @param tid
     * @return
     */
    public Message dissolutionTeam(String tid);

    /**
     * 队伍信息修改
     * @param member
     * @return
     */
    public Message teamModify(Member member);
}
