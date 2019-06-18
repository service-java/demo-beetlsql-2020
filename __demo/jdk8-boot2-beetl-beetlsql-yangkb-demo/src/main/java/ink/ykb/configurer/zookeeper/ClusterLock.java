package ink.ykb.configurer.zookeeper;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * zookeeper分布式锁
 * @author 马丁的早晨
 *
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ClusterLock {
	
	/**根路径 */
	String path();	
	
	/**锁的值 */
	String value() default "";	
	
	/**方法参数的名字，通过此参数值动态获取key，优先查看value的值，如果vlue值存在，则此设置失效*/
	String key();	
	
}
