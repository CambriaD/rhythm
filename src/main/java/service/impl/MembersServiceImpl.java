package service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.IMemberDao;
import dao.impl.MemberDaoImpl;
import domain.MemberQuery;
import domain.MemberRanking;
import domain.Message;
import service.IMembersService;

public class MembersServiceImpl implements IMembersService {
    IMemberDao memberDao = new MemberDaoImpl();

    /**
     * 日历
     */
    @Override
    public int[] calendar(String uid,String tid,String month) {
    	return memberDao.calendar(uid, tid, month);
    }
    
    /**
     * 图表功能
     */
    public int[] chart(String tid) {
    	return memberDao.chart(tid);
    }

    @Override
    public MemberQuery<MemberRanking> memberRankingForSpDay(String tid , String day) {
        List<MemberRanking> list = memberDao.memberRankingForSpDay(tid , day);
        MemberQuery<MemberRanking> query = new MemberQuery<MemberRanking>();
        if (list == null || list.size() ==0 ){
            //均未打卡
            query.setCode(2);
        }else {
            query.setCode(1);
            query.setList(list);
        }
        return query;
    }

    /**
     * 任意时间段内某组打卡次数
     * @param tid 队伍id
     * @param stime 起始时间
     * @param etime 终止时间
     * @return
     */
    @Override
    public Map<String , Integer> chatPlus(String tid , String stime , String etime) {
        Map<String , Integer> result = new HashMap<>();
        int temp;
        long st = Long.parseLong(stime) * 1000000;
        long et = Long.parseLong(etime) * 1000000;
        long today = st;
        long tommorrow;

        Calendar calendar = new GregorianCalendar();
        calendar.set((int)(st/10000) , (int)((st/100)%100-1) , (int)(st%100) , 0 , 0 , 0);
        calendar.add(Calendar.HOUR , -13);
        calendar.add(Calendar.DATE , 1);

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");

        while (today < et) {
            tommorrow = Long.parseLong(df.format(calendar.getTime()));
            temp = memberDao.timesOfPunch(tid , today , tommorrow);
            if (temp >= 0) {
                result.put(String.valueOf(today) , temp);
            } else {
                result.clear();
                result.put("status" , 500);
                return result;
            }
            calendar.add(Calendar.DATE , 1);
            today = tommorrow;
        }

        result.put("status" , 200);
        return result;
    }
    
    /**
     * 打卡榜（前五）
     * @param tid
     * @return
     */
    @Override
    public MemberQuery<MemberRanking> memberRanking(String tid) {
        List<MemberRanking> list = memberDao.memberRanking(tid);
        MemberQuery<MemberRanking> query = new MemberQuery<MemberRanking>();
        if (list == null || list.size() ==0 ){
            //均未打卡
            query.setCode(2);
        }else {
            query.setCode(1);
            query.setList(list);
        }
        return query;
    }

    /**
     * 打卡排名
     * @param uid
     * @param tid
     * @return
     */
    @Override
    public MemberRanking punchRanking(String uid, String tid) {
        return memberDao.punchRanking(uid, tid);
    }

    /**
     * 立即打卡
     * @param uid
     * @param tid
     * @param punchTime
     * @return
     */
    @Override
    public Message memberPunch(String uid, String tid, String punchTime) {
        Message message = new Message();
        boolean flag = memberDao.memberPunch(uid, tid, punchTime);
        if (flag){
            //成功打卡
            message.setResultFlag(1);
        }else {
            message.setResultFlag(2);
        }
        return message;
    }

    /**
     * 打卡榜
     * @param tid
     * @return
     */
    @Override
    public MemberQuery<MemberRanking> punchList(String tid) {
        List<MemberRanking> list = memberDao.memberRanking(tid);
        MemberQuery<MemberRanking> query = new MemberQuery<MemberRanking>();
        if (list == null || list.size() ==0 ){
            //均未打卡
            query.setCode(2);
        }else {
            query.setCode(1);
            query.setList(list);
        }
        return query;
    }

    /**
     * 我的队员
     * @param tid
     * @return
     */
    @Override
    public MemberQuery<MemberRanking> myMembers(String uid, String tid) {
        List<MemberRanking> list = memberDao.myMembers(uid, tid);
        MemberQuery<MemberRanking> query = new MemberQuery<MemberRanking>();
        if (list == null || list.size() ==0 ){
            //没有队员
            query.setCode(2);
        }else {
            query.setCode(1);
            query.setList(list);
        }
        return query;
    }

    /**
     * 删除成员
     * @param uid
     * @param tid
     * @return
     */
    @Override
    public Message membersDelete(String uid, String tid) {
        Message message = new Message();
        boolean flag = memberDao.membersDelete(uid, tid);
        if (flag){
            //删除成功
            message.setResultFlag(1);
        }else {
            message.setResultFlag(2);
            message.setErrormsg("删除失败");
        }
        return message;
    }
}
