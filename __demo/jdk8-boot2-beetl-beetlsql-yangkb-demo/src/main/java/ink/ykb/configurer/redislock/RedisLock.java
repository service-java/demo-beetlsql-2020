package ink.ykb.configurer.redislock;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * redis分布式锁
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface RedisLock {
	
	/**Redis的key */
	String value() default  "";	
	
	/**方法参数的名字，通过此参数值动态获取key，优先查看value的值，如果vlue值存在，则此设置失效*/
	String key();			
	
	/**Redis key的前缀*/
	String prefix() default  "";	
	
	/**未获取到Redis锁的异常是否抛出异常：默认抛出异常：true，不抛异常：false*/
	boolean isThrowExc() default  true;	
	
	/**未获取到Redis锁的异常处理：编号*/
	String code() default  "";
	
	/**
	 * 未获取到Redis锁的异常处理：消息
	 */
	String msg() default  "";
	
}
