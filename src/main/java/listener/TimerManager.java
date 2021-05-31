package listener;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

public class TimerManager {
	// 时间间隔一天
	private static final long PERIOD_DAY = 24 * 60 * 60 * 1000;

	public TimerManager() {
		Calendar calendar = Calendar.getInstance();	//通过当前时间获取日历对象

		/*** 定制每日2:00执行方法 ***/

		calendar.set(Calendar.HOUR_OF_DAY, 2);	//获取当天2点的日历时间对象
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);

		Date date = calendar.getTime(); // 第一次执行定时任务的时间--当天2点
		System.out.println(date);
		System.out.println("before 方法比较：" + date.before(new Date()));
		// 如果第一次执行定时任务的时间 小于 当前的时间
		// 此时要在 第一次执行定时任务的时间 加一天，以便此任务在下个时间点执行。如果不加一天，任务会立即执行。循环执行的周期则以当前时间为准
		if (date.before(new Date())) {
			date = this.addDay(date, 1);
			System.out.println(date);
		}

		Timer timer = new Timer();

		NFDFlightDataTimerTask task = new NFDFlightDataTimerTask();
		// 安排指定的任务在指定的时间开始进行重复的固定延迟执行。
		timer.schedule(task, date, PERIOD_DAY);	//第一个参数是任务，第三个参数是循环周期
	}

	// 增加或减少天数
	public Date addDay(Date date, int num) {
//    	Calendar使用默认时区和语言环境获得一个日历,如果想设置为某个日期，可以一次设置年月日时分秒，由于月份下标从0开始赋值月份要-1
//    	https://blog.csdn.net/yx0628/article/details/79317440
//        System.out.println("年:" + cal.get(Calendar.YEAR));
//        System.out.println("月:" + (cal.get(Calendar.MONTH) + 1));       
//        System.out.println("日:" + cal.get(Calendar.DAY_OF_MONTH));
//        System.out.println("时:" + cal.get(Calendar.HOUR_OF_DAY));
//        System.out.println("分:" + cal.get(Calendar.MINUTE));
//        System.out.println("秒:" + cal.get(Calendar.SECOND));
		Calendar startDT = Calendar.getInstance();
		startDT.setTime(date);
		startDT.add(Calendar.DAY_OF_MONTH, num);
		return startDT.getTime();
	}
}
