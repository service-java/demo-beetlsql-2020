package ink.ykb.configurer;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import ink.ykb.interceptors.AdminLoginInterceptor;

@Configuration
public class MvcConfig implements WebMvcConfigurer{

	/**
	 * 跨域访问
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
//		registry.addMapping("/**");//允许所有的请求访问
//		registry.addMapping("/api/**")
//		.allowCredentials(true)
//		.allowedOrigins("http://domain2.com")
//		.allowedMethods("POST","GET");
		
	}

	/**
	 * 格式化
	 */
	@Override
	public void addFormatters(FormatterRegistry registry) {
		
		registry.addFormatter(new DateFormatter("yyyy-MM-dd HH:mm:ss"));
		
	}

	/**
	 *	拦截器
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		
		registry.addInterceptor(new AdminLoginInterceptor())
			.addPathPatterns("/admin/**");
		
	}

	/**
	 * URI到视图的映射
	 */
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		
//		registry.addViewController("/test1").setViewName("/login.html");
		
	}

	
}
