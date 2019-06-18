package ink.ykb.configurer.redislock;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
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

import lombok.extern.slf4j.Slf4j;

/**
 * redis无阻塞锁	<br>
 * 注意：无法嵌套，非可重入锁
 */
@Configuration
@Aspect
@Order(100)
@Slf4j
public class RedisLockAopConf {
		
	private final static String MSG_KEY_PATTERN = "\\$\\{(\\w+\\.?\\w*)\\}";

		@Autowired
		private RedisManagerService redisManagerService;
		
		
		@Around("@annotation(redisLock)")
		public Object redisLockAop(ProceedingJoinPoint pjp,RedisLock redisLock) throws Throwable{
		
			Object result = null;		//方法调用的返回值
			String key = null;				//Redis 的key
			Object paramValue = null;		//参数的值
			
			Map<String, Object> parameterNames = this.getParameterNames(pjp);//获取方法所有参数
			//是否获取到锁了
			boolean hasLock = false;
			try {
		        
		        if(redisLock == null){throw new ServiceException(ResponseMsg.CODE_FAIL,"未获取到注解："+RedisLock.class.getName());}
		        
		    	String paramName =  redisLock.key();
		    	String prefix = redisLock.prefix();
		    	String value = redisLock.value();
		    	boolean isThrowExc =  redisLock.isThrowExc();
		    	String code  =  redisLock.code();
		    	String msg  =  redisLock.msg();
		    	
		    	if(StringUtils.isBlank(paramName) && StringUtils.isBlank(value)){throw new ServiceException(ResponseMsg.CODE_FAIL,"Redis锁key不能为空");}
		    	
		    	if(StringUtils.isNotBlank(value)) {
		    		paramValue = value;
		    	}else if(StringUtils.isNotBlank(paramName)) {
			    	paramValue = this.getParamValue(parameterNames, paramName);
			    	if(paramValue == null || StringUtils.isBlank(paramValue.toString())){throw new ServiceException(ResponseMsg.CODE_FAIL,"未获取到Redis锁key");}
		    	}
		    	key = prefix + paramValue.toString();
		    	hasLock  =  redisManagerService.lock(key,60L,TimeUnit.SECONDS);
				if(hasLock){//1分钟
						log.info("获取到Redis锁：{}",key);
						result = pjp.proceed();//调用原方法
				}else{
					log.info("未获取到Redis锁：{}",key);
					if(isThrowExc){
						if(StringUtils.isBlank(code)){
							code = "101";
						}
						if(StringUtils.isBlank(msg)){
							msg = "业务正在处理中，请稍后...";
						}else{
							
						      Pattern r = Pattern.compile(MSG_KEY_PATTERN);
						      Matcher m = r.matcher(msg);
						      
						      String newMsg = msg;
						      while(m.find()){
						    		  String fieldName = m.group(1);
						    		 Object o =  this.getParamValue(parameterNames, fieldName);
						    		 if(o != null){
						    			 newMsg = newMsg.replace(m.group(0),o.toString());
						    		 }
						      }
							msg = newMsg;
							
						}
						throw new ServiceException(code,msg);
					}
					
				}
				
			} catch (Exception e) {
				throw e;
			}finally {
				if(hasLock) {
					redisManagerService.unlock(key);
					log.info("释放Redis锁：{}",key);
				}
				
			}
			return result;
		}
		
		/**
		 * 获取属性，如果获取不到，再遍历所有map中value对象
		 */
		private Object getParamValue(Map<String, Object> parameterNames,String paramName) throws Exception{

			Object paramValue = parameterNames.get(paramName);
			
			//根据方法参数名.参数对象的属性名获取指定属性
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
		 */
		private Map<String, Object> getParameterNames(ProceedingJoinPoint pjp) throws NoSuchMethodException, SecurityException{
			
			String[] names = null;//参数名
			Object[] args = pjp.getArgs();//参数值
			
			Signature signature = pjp.getSignature();  
	        MethodSignature methodSignature = (MethodSignature) signature;  
			
			String jv = System.getProperty("java.version");
			String[] jvs = jv.split("\\.");
			if(Integer.parseInt(jvs[0]+jvs[1]) >= 18) {//jdk8直接获取参数名
		        names = methodSignature.getParameterNames(); 
			}else {
				LocalVariableTableParameterNameDiscoverer localVariableTableParameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
		        Object target = pjp.getTarget();//代理类
		        Method currentMethod = target.getClass().getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
		        names = localVariableTableParameterNameDiscoverer.getParameterNames(currentMethod);
			}
			if(names == null) {
				throw new ServiceException(ResponseMsg.CODE_FAIL,pjp.getTarget().getClass().getName()+"$"+signature.getName()+"：未获取到参数名称列表");
			}
			
			Map<String, Object> map = new HashMap<>();
			if(names != null && names.length == args.length) {
				for (int i = 0; i < names.length; i++) {
					map.put(names[i], args[i]);
				}
			}
			return map;
		}
		
}
