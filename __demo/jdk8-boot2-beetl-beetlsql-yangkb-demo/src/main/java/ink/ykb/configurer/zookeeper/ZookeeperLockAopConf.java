package ink.ykb.configurer.zookeeper;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.annotation.Order;

import ink.ykb.util.PlatformRuntimeException;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Aspect
@Order(101)
@Slf4j
public class ZookeeperLockAopConf {
	
		@Autowired
		private CuratorFramework zkClient;
		
		@Around("@annotation(clusterLock)")
		public Object zkLockAop(ProceedingJoinPoint pjp,ClusterLock clusterLock) throws Throwable{
		
			Object o = null;
			InterProcessMutex lock = null;
			//Zookeeper 的key
			String key = null;		
			
			//获取方法所有参数
			Map<String, Object> parameterNames = this.getParameterNames(pjp);
			try {
		        if(clusterLock == null){throw new PlatformRuntimeException("未获取到注解："+ClusterLock.class.getName());}
		        
		      //参数的值
				Object paramValue = null;
		    	String lockPath = clusterLock.path();
		    	String paramName  = clusterLock.key();
		    	String keyValue = clusterLock.value();
		    	
		    	if(StringUtils.isBlank(lockPath)){
		    		throw new PlatformRuntimeException("zk锁路径不能为空");
		    	}
		    	if(StringUtils.isNotBlank(keyValue)) {
		    		paramValue = keyValue;
		    	}else if(StringUtils.isNotBlank(paramName)) {
		    		paramValue = this.getParamValue(parameterNames,paramName);
			    	if(paramValue == null || StringUtils.isBlank(paramValue.toString())){
			    		throw new PlatformRuntimeException("zk锁类型key不能为空");
			    	}
		    	}
		    	
		    	key = lockPath+"/"+paramValue.toString();
				
				lock = new InterProcessMutex(zkClient, key);
				
				if(lock.acquire(60, TimeUnit.SECONDS)){
						log.info("获取到zk锁：{}",key);
						//调用原方法
				        o = pjp.proceed();
				}
			} catch (Exception e) {
				throw e;
			}finally {
				if(lock != null){
					lock.release();
				}
				log.info("释放zk锁：{}",key);
			}
			return o;
		}
		

		/**
		 * 获取属性，如果获取不到，再遍历所有map中value对象
		*@Author ykb
		*@Date 2018年7月26日 下午2:33:16
		* @param parameterNames
		* @param paramName
		* @return
		* @throws Exception
		 */
		private Object getParamValue(Map<String, Object> parameterNames,String paramName) throws Exception{
			
			Object paramValue = parameterNames.get(paramName);
			
			//根据“方法参数名.参数值对象的属性名”获取指定属性
			if(paramValue == null || StringUtils.isBlank(paramValue.toString())){
				if(paramName.contains(".")){
					String[] split = paramName.split("\\.");
					if(split.length == 2){
						Object object = parameterNames.get(split[0]);
						if(object != null){
							String firstLetter = split[1].substring(0, 1).toUpperCase();    
				            String getter = "get" + firstLetter + split[1].substring(1); 
				            
							Method method = BeanUtils.findMethodWithMinimalParameters(object.getClass(), getter);
			    			if(method != null){
			    				paramValue = method.invoke(object, new Object[] {});
				    			if(paramValue != null){
				    				return paramValue;
				    			}
			    			}
						}
					}
				}
			}
			//反射获取,目前只支持属性获取，不能获取属性的属性
	    	if(paramValue == null || StringUtils.isBlank(paramValue.toString())){
	    		
	    		String firstLetter = paramName.substring(0, 1).toUpperCase();    
	            String getter = "get" + firstLetter + paramName.substring(1); 
	    		for (Object o : parameterNames.values()) {
	    			Method method = BeanUtils.findMethodWithMinimalParameters(o.getClass(), getter);
	    			if(method != null){
	    				paramValue = method.invoke(o, new Object[] {});
		    			if(paramValue != null){
		    				break;
		    			}
	    			}
	    		}
	    	}
	    	return paramValue;
		}

		/**
		 * 获取方法的参数
		 * @param pjp
		 * @return
		 * @throws SecurityException 
		 * @throws NoSuchMethodException 
		 * @throws  
		 * @throws Exception 
		 */
		private Map<String, Object> getParameterNames(ProceedingJoinPoint pjp) throws NoSuchMethodException, SecurityException{
			//参数名
			String[] names = null;
			//参数值
			Object[] args = pjp.getArgs();
			
			Signature signature = pjp.getSignature();  
	        MethodSignature methodSignature = (MethodSignature) signature;  
			
			String jv = System.getProperty("java.version");
			String[] jvs = jv.split("\\.");
			//jdk8直接获取参数名
			if(Integer.parseInt(jvs[0]+jvs[1]) >= 18) {
		        names = methodSignature.getParameterNames(); 
			}else {
				LocalVariableTableParameterNameDiscoverer localVariableTableParameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
				//代理类
		        Object target = pjp.getTarget();
		        Method currentMethod = target.getClass().getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
		        names = localVariableTableParameterNameDiscoverer.getParameterNames(currentMethod);
			}
			Map<String, Object> map = new HashMap<>(16);
			if(names != null && names.length == args.length) {
				for (int i = 0; i < names.length; i++) {
					map.put(names[i], args[i]);
				}
			}
			return map;
		}
		
}
