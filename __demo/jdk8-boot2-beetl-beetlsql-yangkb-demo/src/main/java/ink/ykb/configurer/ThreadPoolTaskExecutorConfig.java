package ink.ykb.configurer;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


@Configuration
public class ThreadPoolTaskExecutorConfig {
	
	@Bean
	public ThreadPoolTaskExecutor createThreadPoolTaskExecutor() {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
//		 核心线程数
		taskExecutor.setCorePoolSize(4);
//		最大线程数
		taskExecutor.setMaxPoolSize(10);
//		队列最大长度
		taskExecutor.setQueueCapacity(5000);
//		空闲线程存活时间
		taskExecutor.setKeepAliveSeconds(2000);
		
		//建议：在实际使用中，将任务放入MQ，Kafka消息队列更好，消息能持久化，不会丢失
		
//		线程池对拒绝任务(无线程可用)的处理策略 
		
//		方法1：（注意：此策略如果用于异步通知回调的场景时，由于采用调用方线程，如果请求方的同步请求方法和异步回调方法用的是同一把阻塞锁，会导致死锁问题。）
//		ThreadPoolExecutor.CallerRunsPolicy策略 ,调用者的线程会执行该任务,如果执行器已关闭,则丢弃. 
//		RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.CallerRunsPolicy();
//		taskExecutor.setRejectedExecutionHandler(rejectedExecutionHandler);
		
//		方法2：队列满了后直接阻塞调用者，等待队列空闲
		taskExecutor.setRejectedExecutionHandler(new RejectedExecutionHandler() {
			
			@Override
			public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
				try {
					executor.getQueue().put(r);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		
		return taskExecutor;
	}

}
