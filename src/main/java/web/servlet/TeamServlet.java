package web.servlet;


import java.io.IOException;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import domain.Member;
import domain.MemberQuery;
import domain.Message;
import redis.clients.jedis.Jedis;
import service.ITeamService;
import service.impl.TeamServiceImpl;
import util.JedisUtil;

@WebServlet("/team/*")
public class TeamServlet extends BaseServlet {
    ITeamService teamService = new TeamServiceImpl();
    /**
     * 队伍大厅
     *
     * @param request
     * @param response
     */
    public void findAll(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String teamNamestr = request.getParameter("teamName");
        String typestr = request.getParameter("type");
        String frequencystr = request.getParameter("frequency");
        //处理参数
        //队伍名
        String teamName = null;
        if (teamNamestr != null && teamNamestr.length() > 0 && !("null".equals(teamNamestr))) {
            teamName = teamNamestr;
        }
        //打卡类型
        String type = "全部";
        if (typestr != null && typestr.length() > 0 && !("全部".equals(typestr))) {
            type = typestr;
            if ("其他".equals(typestr)) {
                type = "其它";
            }
        }
        //打卡频率
        String frequency = "全部";
        if (frequencystr != null && frequencystr.length() > 0 && !("全部".equals(frequencystr))) {
            frequency = frequencystr;
        }
        //调用teamService的findAll方法
        MemberQuery<Member> query = teamService.findAll(teamName, type, frequency);
        query.setTokenCode(true);
        //写回页面
        writeValue(query, response);
    }

    /**
     * 队伍占比
     */
    public void ratio(HttpServletRequest request,HttpServletResponse response) throws Exception{
    	Map<String,Integer> map=teamService.ratio();
    	writeValue(map, response);
    }
    /**
     * 创建队伍
     *
     * @param request
     * @param response
     * @throws IOException
     */
    public void teamEstablish(HttpServletRequest request, HttpServletResponse response) throws IOException {
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

            String teamName = request.getParameter("teamName");

            String type = request.getParameter("type");
            if ("其他".equals(type)){
                type = "其它";
            }

            String frequency = request.getParameter("frequency");

            String beginDate = request.getParameter("beginDate");

            String endDate = request.getParameter("endDate");

            String startTime = request.getParameter("startTime");

            String endTime = request.getParameter("endTime");

            String synopsis = request.getParameter("synopsis");

            Member member = new Member();
            member.setUid(Integer.parseInt(uid));
            member.setTeamName(teamName);
            member.setType(type);
            member.setFrequency(frequency);
            member.setBeginDate(beginDate);
            member.setEndDate(endDate);
            member.setStartTime(startTime);
            member.setEndTime(endTime);
            member.setSynopsis(synopsis);
            //调用teamService的teamEstablish方法
            Message message = teamService.teamEstablish(member);
            message.setTokenCode(true);
            //写回界面
            writeValue(message, response);
        }
    }

    /**
     * 队伍详情
     *
     * @param request
     * @param response
     * @throws IOException
     */
    public void teamDetails(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //获取数据
        String tid = request.getParameter("tid");
        //调用teamService的teamDetails方法
        Member member = teamService.teamDetails(tid);
        //写回界面
        writeValue(member, response);
    }

    /**
     * 显示功能
     *
     * @param request
     * @param response
     * @throws IOException
     */
    public void isTeamMember(HttpServletRequest request, HttpServletResponse response) throws IOException {
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
            //调用teamService的isTeamMember方法
            Message message = teamService.isTeamMember(uid, tid);
            message.setTokenCode(true);
            //写回界面
            writeValue(message, response);

        }
    }

    /**
     * 加入功能
     *
     * @param request
     * @param response
     * @throws IOException
     */
    public void joinTeam(HttpServletRequest request, HttpServletResponse response) throws IOException {
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
            //调用teamService的joinTeam方法
            Message message = teamService.joinTeam(uid, tid);
            message.setTokenCode(true);
            //写回界面
            writeValue(message, response);
        }
    }

    /**
     * 退出队伍功能
     *
     * @param request
     * @param response
     * @throws IOException
     */
    public void exitTeam(HttpServletRequest request, HttpServletResponse response) throws IOException {
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
            //调用teamService的exitTeam方法
            Message message = teamService.exitTeam(uid, tid);
            message.setTokenCode(true);
            //写回界面
            writeValue(message, response);
        }
    }

    /**
     * 我加入的队伍
     *
     * @param request
     * @param response
     * @throws IOException
     */
    public void teamQuery(HttpServletRequest request, HttpServletResponse response) throws IOException {
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
            MemberQuery<Member> query = new MemberQuery<Member>();
            query.setTokenCode(false);
            //直接写回数据
            writeValue(query, response);
            return;
        } else {
            //认证成功
            jedis.expire(token, 60 * 60 * 24);//重置时间
            //关闭连接
            jedis.close();
            //获取数据
            String uid = request.getParameter("uid");
            //调用teamService的teamQuery方法
            MemberQuery<Member> query = teamService.teamQuery(uid);
            query.setTokenCode(true);
            //写回数据
            writeValue(query, response);
        }
    }

    /**
     * 管理我创建的队伍
     *
     * @param request
     * @param response
     * @throws IOException
     */
    public void myTeam(HttpServletRequest request, HttpServletResponse response) throws IOException {
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
            MemberQuery<Member> query = new MemberQuery<Member>();
            query.setTokenCode(false);
            //直接写回数据
            writeValue(query, response);
            return;
        } else {
            //认证成功
            jedis.expire(token, 60 * 60 * 24);//重置时间
            //关闭连接
            jedis.close();
            //获取数据
            String uid = request.getParameter("uid");
            //调用teamService的myTeam方法
            MemberQuery<Member> query = teamService.myTeam(uid);
            query.setTokenCode(true);
            //写回数据
            writeValue(query, response);
        }
    }

    /**
     * 解散小队
     *
     * @param request
     * @param response
     * @throws IOException
     */
    public void dissolutionTeam(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //获取数据
        String tid = request.getParameter("tid");
        //调用teamService的dissolutionTeam方法
        Message message = teamService.dissolutionTeam(tid);
        message.setTokenCode(true);
        //写回数据
        writeValue(message, response);
    }

    /**
     * 队伍信息修改
     *
     * @param request
     * @param response
     * @throws IOException
     */
    public void teamModify(HttpServletRequest request, HttpServletResponse response) throws IOException {
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
            String tid = request.getParameter("tid");

            String teamName = request.getParameter("teamName");
            /*if (teamName != null) {
                teamName = new String(teamName.getBytes("iso-8859-1"), "UTF-8");
            }*/

            String type = request.getParameter("type");
            /*if (type != null) {
                type = new String(type.getBytes("iso-8859-1"), "UTF-8");
            }*/

            String frequency = request.getParameter("frequency");
            /*if (frequency != null) {
                frequency = new String(frequency.getBytes("iso-8859-1"), "UTF-8");
            }*/

            String beginDate = request.getParameter("beginDate");

            String endDate = request.getParameter("endDate");

            String startTime = request.getParameter("startTime");

            String endTime = request.getParameter("endTime");

            String synopsis = request.getParameter("synopsis");
            /*if (synopsis != null) {
                synopsis = new String(synopsis.getBytes("iso-8859-1"), "UTF-8");
            }*/
            Member member = new Member();
            member.setTid(Integer.parseInt(tid));
            member.setTeamName(teamName);
            member.setType(type);
            member.setFrequency(frequency);
            member.setBeginDate(beginDate);
            member.setEndDate(endDate);
            member.setStartTime(startTime);
            member.setEndTime(endTime);
            member.setSynopsis(synopsis);
            //调用teamService的teamModify方法
            Message message = teamService.teamModify(member);
            message.setTokenCode(true);
            //写回数据
            writeValue(message, response);
        }
    }
}
