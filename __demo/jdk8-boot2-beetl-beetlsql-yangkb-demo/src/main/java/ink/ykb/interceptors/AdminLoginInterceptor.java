package ink.ykb.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import ink.ykb.util.RequestUtil;

public class AdminLoginInterceptor implements HandlerInterceptor{

	/**
	 * 页面渲染完以后调用
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}
	
	/**
	 * Controller调用后，渲染页面前调用
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}

	/**
	 * Controller前调用的方法
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		Integer userId = (Integer)request.getAttribute("userId");
		if(userId == null){
			response.sendRedirect(RequestUtil.getAppURL(request)+"/login.html");
			return false;
		}
		return true;
	}

	
}
