package listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;

public class NFDFlightDataTaskListener implements ServletContextListener {
	public void contextInitialized(ServletContextEvent sce) {
		/**
		 * 当Servlet 容器启动Web 应用时调用该方法。在调用完该方法之后，容器再对Filter 初始化， 并且对那些在Web
		 * 应用启动时就需要被初始化的Servlet 进行初始化。
		 */
		new TimerManager();
	}

	public void contextDestroyed(ServletContextEvent sce) {
		// 断开连接，清理线程，不然Tomcat关闭或者重启会报异常
		AbandonedConnectionCleanupThread.uncheckedShutdown();
	}
}
