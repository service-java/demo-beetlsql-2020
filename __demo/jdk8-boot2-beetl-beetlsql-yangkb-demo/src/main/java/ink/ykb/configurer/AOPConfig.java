package ink.ykb.configurer;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

//@Configuration
//@Aspect
public class AOPConfig {

//	@Pointcut("@within(org.springframework.stereotype.Controller)")  
    private void anyMethod(){}//定义一个切入点  
	
	
//	@Around("anyMethod()")
	public Object simpleAop(ProceedingJoinPoint pjp) throws Throwable{
		try {
	        Signature sig = pjp.getSignature();
	        MethodSignature msig = (MethodSignature) sig;//代理方法
	        Object target = pjp.getTarget();//代理类
	        Method currentMethod = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
	        System.out.println("method:"+target.getClass().getName()+"$"+msig.getName());
	        
	        String classUrl = null; 
	        Object classAnnotation = target.getClass().getAnnotation(RequestMapping.class);
	        if(classAnnotation != null){
	        	classUrl ="GET/POST:" +((RequestMapping)classAnnotation).value()[0];
	        }else{
	        	 classAnnotation = target.getClass().getAnnotation(GetMapping.class);
	        	 if(classAnnotation != null){
	        		 classUrl ="GET:" + ((GetMapping)classAnnotation).value()[0];
	        	 }else{
	        		 classAnnotation = target.getClass().getAnnotation(PostMapping.class);
	        		 if(classAnnotation != null){
	        			 classUrl = "POST:" +((PostMapping)classAnnotation).value()[0];
	        		 }
	        	 }
	        }
	        System.out.println("classUrl:"+classUrl);
	        String methodUrl = null;
	        Object methodAnnotation = currentMethod.getAnnotation(RequestMapping.class);
	        if(methodAnnotation != null){
	        	methodUrl ="GET/POST:" + ((RequestMapping)methodAnnotation).value()[0];
	        }else{
	        	methodAnnotation = currentMethod.getAnnotation(GetMapping.class);
	        	 if(methodAnnotation != null){
	        		 methodUrl ="GET:" + ((GetMapping)methodAnnotation).value()[0];
	        	 }else{
	        		 methodAnnotation = currentMethod.getAnnotation(PostMapping.class);
	        		 if(methodAnnotation != null){
	        			 methodUrl ="POST:" + ((PostMapping)methodAnnotation).value()[0];
	        		 }
	        	 }
	        }
	        System.out.println("methodUrl:"+methodUrl);
	        
			Object[] args = pjp.getArgs();
			System.out.println("args:"+Arrays.asList(args));
			//调用原方法
			Object o = pjp.proceed();
			System.out.println("return:"+o);
			return o;
		} catch (Exception e) {
			throw e;
		}
	}
}
