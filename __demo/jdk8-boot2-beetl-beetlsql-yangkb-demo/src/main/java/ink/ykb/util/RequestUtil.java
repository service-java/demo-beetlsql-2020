package ink.ykb.util;

import javax.servlet.http.HttpServletRequest;

public class RequestUtil {

	/**
	 * 获取当前项目的webroot的方法
	 * @param request 当前请求
	 * @return
	 */
	public static String getAppURL(HttpServletRequest request) {
		if (request == null){
			return "";
		}
		StringBuffer url = new StringBuffer();
		int port = request.getServerPort();
		if (port < 0) {
			port = 80; // Work around java.net.URL bug
		}
		String scheme = request.getScheme();
		url.append(scheme);
		url.append("://");
		url.append(request.getServerName());
		if ((scheme.equals("http") && (port != 80))|| (scheme.equals("https") && (port != 443))) {
			url.append(':');
			url.append(port);
		}
		url.append(request.getContextPath());
		return url.toString();
	}
}
