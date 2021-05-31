package listener;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import util.JDBCUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimerTask;

public class NFDFlightDataTimerTask extends TimerTask {
    JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
    private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void run() {
        try {
            //在这里写你要执行的内容
            //定义sql
            String sql = "update tab_members set punch = 0 ";
            template.update(sql);
            System.out.println("执行当前时间" + formatter.format(Calendar.getInstance().getTime()));
        } catch (Exception e) {
            System.out.println("-------------解析信息发生异常--------------");
        }
    }
}
