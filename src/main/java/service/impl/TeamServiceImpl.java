package service.impl;

import dao.ITeamDao;
import dao.impl.TeamDaoImpl;
import domain.Member;
import domain.MemberQuery;
import domain.Message;
import service.ITeamService;

import java.util.List;
import java.util.Map;

public class TeamServiceImpl implements ITeamService {
    ITeamDao teamDao = new TeamDaoImpl();
    /**
     * 队伍大厅
     * @param teamName
     * @param type
     * @param frequency
     * @return
     */
    @Override
    public MemberQuery<Member> findAll(String teamName, String type, String frequency) {
        MemberQuery<Member> memberQuery = new MemberQuery<Member>();
        List<Member> list = teamDao.findAll(teamName, type, frequency);
        if (list == null || list.size() ==0 ){
            //未查询到数据
            memberQuery.setCode(2);
        }else {
            //查询到数据
            memberQuery.setCode(1);
            memberQuery.setList(list);
        }
        return memberQuery;
    }

    /*
     * 队伍占比
     */
    public Map<String,Integer> ratio(){
    	return teamDao.ratio();
    }
    /**
     * 创建队伍
     * @param member
     * @return
     */
    @Override
    public Message teamEstablish(Member member) {
        Message message = new Message();
        boolean flag = teamDao.teamEstablish(member);
        if (flag){
            //创建成功
            message.setResultFlag(1);
        }else {
            //创建失败
            message.setResultFlag(2);
            message.setErrormsg("创建失败");
        }
        return message;
    }

    /**
     * 队伍详情
     * @param tid
     * @return
     */
    @Override
    public Member teamDetails(String tid) {
        return teamDao.teamDetails(tid);
    }

    /**
     * 显示功能
     * @param uid
     * @param tid
     * @return
     */
    @Override
    public Message isTeamMember(String uid, String tid) {
        Message message = new Message();
        boolean flag = teamDao.isTeamMember(uid, tid);
        if (flag){
            //查询到
            message.setResultFlag(1);
        }else {
            //没在队伍里
            message.setResultFlag(2);
        }
        return message;
    }

    /**
     * 加入功能
     * @param uid
     * @param tid
     * @return
     */
    @Override
    public Message joinTeam(String uid, String tid) {
        Message message = new Message();
        boolean flag = teamDao.joinTeam(uid, tid);
        if (flag){
            // 加入成功
            message.setResultFlag(1);
        }else {
            //加入失败
            message.setResultFlag(2);
            message.setErrormsg("已在该队伍中，请不要重复加入");
        }
        return message;
    }

    /**
     * 退出功能
     * @param uid
     * @param tid
     * @return
     */
    @Override
    public Message exitTeam(String uid, String tid) {
        Message message = new Message();
        boolean flag = teamDao.exitTeam(uid, tid);
        if (flag){
            // 退出成功
            message.setResultFlag(1);
        }else {
            //退出失败
            message.setResultFlag(2);
            message.setErrormsg("您是该队伍的创建者，请到我的->管理队伍进行解散");
        }
        return message;
    }

    /**
     * 我加入的队伍
     * @param uid
     * @return
     */
    @Override
    public MemberQuery<Member> teamQuery(String uid) {
        MemberQuery<Member> memberQuery = new MemberQuery<Member>();
        List<Member> list = teamDao.teamQuery(uid);
        if (list == null || list.size() == 0){
            //没有加入的队伍
            memberQuery.setCode(2);
        }else {
            memberQuery.setCode(1);
            memberQuery.setList(list);
        }
        return memberQuery;
    }

    /**
     * 我创建的队伍
     * @param uid
     * @return
     */
    @Override
    public MemberQuery<Member> myTeam(String uid) {
        MemberQuery<Member> memberQuery = new MemberQuery<Member>();
        List<Member> list = teamDao.myTeam(uid);
        if (list == null || list.size() == 0){
            //没有创建的队伍
            memberQuery.setCode(2);
        }else {
            memberQuery.setCode(1);
            memberQuery.setList(list);
        }
        return memberQuery;
    }

    /**
     * 解散小队
     * @param tid
     * @return
     */
    @Override
    public Message dissolutionTeam(String tid) {
        Message message = new Message();
        boolean flag = teamDao.dissolutionTeam(tid);
        if (flag){
            //解散成功
            message.setResultFlag(1);
        }else {
            message.setResultFlag(2);
            message.setErrormsg("解散失败");
        }
        return message;
    }

    /**
     * 队伍信息修改
     * @param member
     * @return
     */
    @Override
    public Message teamModify(Member member) {
        Message message = new Message();
        boolean flag = teamDao.teamModify(member);
        if (flag){
            //修改成功
            message.setResultFlag(1);
        }else {
            message.setResultFlag(2);
            message.setErrormsg("修改失败");
        }
        return message;
    }

}
