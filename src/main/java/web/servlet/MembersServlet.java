package web.servlet;


import java.io.IOException;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import domain.MemberQuery;
import domain.MemberRanking;
import domain.Message;
import redis.clients.jedis.Jedis;
import service.IMembersService;
import service.impl.MembersServiceImpl;
import util.JedisUtil;

@WebServlet("/members/*")
public class MembersServlet extends BaseServlet {
    IMembersService membersService = new MembersServiceImpl();

    /**
     * 打卡榜
     *
     * @param request
     * @param response
     * @throws IOException
     */
    public void memberRanking(HttpServletRequest request, HttpServletResponse response) throws IOException {
        /*//获取数据
        String tid = request.getParameter("tid");
        //调用membersService的memberRanking方法
        MemberQuery<MemberRanking> memberQuery = membersService.memberRanking(tid);
        memberQuery.setTokenCode(true);
        //写回界面
        writeValue(memberQuery, response);*/

        //获取数据
        String tid = request.getParameter("tid");
        String day = request.getParameter("day");
        //调用membersService的memberRanking方法
        MemberQuery<MemberRanking> memberQuery = membersService.memberRankingForSpDay(tid , day);
        memberQuery.setTokenCode(true);
        //写回界面
        writeValue(memberQuery, response);
    }
    /**
     * 日历功能
     *
     * @param request
     * @param response
     * @throws IOException
     */
    public void calendar(HttpServletRequest request,HttpServletResponse response) throws Exception{
        String token = request.getHeader("token");
        token = token == null ? "" : token;
        Jedis jedis = JedisUtil.getJedis();
        String s = jedis.get(token);
        if (s == null) {
            jedis.close();
            MemberRanking memberRanking = new MemberRanking();
            memberRanking.setTokenCode(false);
            writeValue(memberRanking, response);
        } else {
            jedis.expire(token, 60 * 60 * 24);
            jedis.close();
            String uid = request.getParameter("uid");
            String tid = request.getParameter("tid");
            String month =request.getParameter("month");
            int[] value=membersService.calendar(uid, tid,month);
            writeValue(value,response);
        }
    }

    /**
     * 图表功能
     */
    public void chart(HttpServletRequest request,HttpServletResponse response)throws Exception{
    	String tid=request.getParameter("tid");
    	int[] value=membersService.chart(tid);
        writeValue(value,response);
    }
    
    /**
     * 特定某天的打卡排行榜
     * member ranking for specific day
     * @param request
     * @param response
     * @throws IOException
     */
    public void memberRankingForSpDay(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //获取数据
        String tid = request.getParameter("tid");
        String day = request.getParameter("day");
        //调用membersService的memberRanking方法
        MemberQuery<MemberRanking> memberQuery = membersService.memberRankingForSpDay(tid , day);
        memberQuery.setTokenCode(true);
        //写回界面
        writeValue(memberQuery, response);
    }

    /**
     * 任意时间段的折线图信息
     * @param request
     * @param response
     * @throws IOException
     */
    public void chatPlus(HttpServletRequest request , HttpServletResponse response) throws IOException {
        String tid = request.getParameter("tid");
        String st = request.getParameter("startTime");
        String et = request.getParameter("endTime");

        Map result = membersService.chatPlus(tid , st , et);

        writeValue(result , response);
    }
    
    /*
     * 打卡排名
     *
     * @param request
     * @param response
     * @throws IOException
     */
    public void punchRanking(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //获取请求头中的token
        String token = request.getHeader("token");
        token = token == null ? "" : token;
        //查询redis中是否存在
        Jedis jedis = JedisUtil.getJedis();
        String s = jedis.get(token);
        if (s == null) {
            //关闭连接
            jedis.close();
            //未登录
            MemberRanking memberRanking = new MemberRanking();
            memberRanking.setTokenCode(false);
            //直接写回数据
            writeValue(memberRanking, response);
            return;
        } else {
            //认证成功
            jedis.expire(token, 60 * 60 * 24);//重置时间
            //关闭连接
            jedis.close();
            //获取数据
            String uid = request.getParameter("uid");
            String tid = request.getParameter("tid");
            //调用membersService的punchRanking方法
            MemberRanking ranking = membersService.punchRanking(uid, tid);
            if (ranking != null) {
                ranking.setTokenCode(true);
            }
            if (ranking == null) {
                //未打卡
                MemberRanking memberRanking = new MemberRanking();
                memberRanking.setTokenCode(true);
                memberRanking.setPunch(0);					//这里有bug，就是补卡的功能没有写到数据库里面
                memberRanking.setRanking(0);
                writeValue(memberRanking, response);
            } else {
                //写回数据
                writeValue(ranking, response);
            }
        }
    }

    /**
     * 立即打卡
     *
     * @param request
     * @param response
     * @throws IOException
     */
    public void memberPunch(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //获取请求头中的token
        String token = request.getHeader("token");
        token = token == null ? "" : token;
        //查询redis中是否存在
        Jedis jedis = JedisUtil.getJedis();
        String s = jedis.get(token);
        if (s == null) {
            //关闭连接
            jedis.close();
            //未登录
            Message message = new Message();
            message.setTokenCode(false);
            //直接写回数据
            writeValue(message, response);
            return;
        } else {
            //认证成功
            jedis.expire(token, 60 * 60 * 24);//重置时间
            //关闭连接
            jedis.close();
            //获取数据
            String uid = request.getParameter("uid");
            String tid = request.getParameter("tid");
            String punchTime = request.getParameter("punchTime");
            //调用membersService的memberPunch方法
            Message message = membersService.memberPunch(uid, tid, punchTime);
            message.setTokenCode(true);
            //数据写回
            writeValue(message, response);
        }
    }

    /**
     * 打卡榜
     *
     * @param request
     * @param response
     * @throws IOException
     */
    public void punchList(HttpServletRequest request, HttpServletResponse response) throws IOException {

        //获取数据
        String tid = request.getParameter("tid");
        //调用membersService的punchList方法
        MemberQuery<MemberRanking> memberQuery = membersService.punchList(tid);
        memberQuery.setTokenCode(true);
        //数据写回
        writeValue(memberQuery, response);
    }

    /**
     * 我的队员
     *
     * @param request
     * @param response
     * @throws IOException
     */
    public void myMembers(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //获取请求头中的token
        String token = request.getHeader("token");
        token = token == null ? "" : token;
        //查询redis中是否存在
        Jedis jedis = JedisUtil.getJedis();
        String s = jedis.get(token);
        if (s == null) {
            //关闭连接
            jedis.close();
            //未登录
            MemberQuery<MemberRanking> memberQuery = new MemberQuery<MemberRanking>();
            memberQuery.setTokenCode(false);
            //直接写回数据
            writeValue(memberQuery, response);
            return;
        } else {
            //认证成功
            jedis.expire(token, 60 * 60 * 24);//重置时间
            //关闭连接
            jedis.close();
            //获取数据
            String uid = request.getParameter("uid");
            String tid = request.getParameter("tid");
            //调用membersService的punchList方法
            MemberQuery<MemberRanking> memberQuery = membersService.myMembers(uid, tid);
            memberQuery.setTokenCode(true);
            //数据写回
            writeValue(memberQuery, response);
        }
    }

    /**
     * 删除成员
     *
     * @param request
     * @param response
     * @throws IOException
     */
    public void membersDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //获取请求头中的token
        String token = request.getHeader("token");
        token = token == null ? "" : token;
        //查询redis中是否存在
        Jedis jedis = JedisUtil.getJedis();
        String s = jedis.get(token);
        if (s == null) {
            //关闭连接
            jedis.close();
            //未登录
            Message message = new Message();
            message.setTokenCode(false);
            //直接写回数据
            writeValue(message, response);
            return;
        } else {
            //认证成功
            jedis.expire(token, 60 * 60 * 24);//重置时间
            //关闭连接
            jedis.close();
            //获取数据
            String uid = request.getParameter("uid");
            String tid = request.getParameter("tid");
            //调用membersService的punchList方法
            Message message = membersService.membersDelete(uid, tid);
            message.setTokenCode(true);
            //数据写回
            writeValue(message, response);
        }
    }
}
