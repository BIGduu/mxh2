package net.mxh.common.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AdminFilter implements Filter {
	/**
	 * 排除不用登录也可以用的路径
	 */
	private static Map<String, String> map = new HashMap<String, String>();
	
	static {
		map.put("login", "登录");
		map.put("imageValidate", "验证码图片");
		map.put("logout", "退出");
		map.put("forget", "忘记密码");
		map.put("upload", "上传富文本编辑器图片");
		map.put("detail", "文章查看");
	}
	
	@Override
	public void destroy() {
		
	}
	
	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)
		throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)arg0;
		HttpServletResponse response = (HttpServletResponse)arg1;
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String url = request.getRequestURL().toString();
		int contextBegin = url.indexOf(request.getContextPath(), 10);
		boolean flag = false;
		String path = "";
		if (contextBegin != -1) {
			int end = url.indexOf("?", contextBegin + (request.getContextPath() + "/admin").length() + 1);
			if (end == -1) {
				end = url.indexOf("/", contextBegin + (request.getContextPath() + "/admin").length() + 1);
			}
			if (end == -1) {
				end = url.length();
			}
			if (end > contextBegin) {
				if (!request.getContextPath().equals("/")) {
					path = url.substring(contextBegin + (request.getContextPath() + "/admin").length() + 1, end);
				}
				else {
					path = url.substring(contextBegin + (request.getContextPath() + "/admin").length(), end);
				}
				flag = true;
			}
		}
		
		if (flag) {
			if (map.get(path) != null) {
				arg2.doFilter(request, response);
			}
			else {
				Object object = request.getSession().getAttribute("admin");
				if (object == null) {
					response.setCharacterEncoding("UTF-8");
					PrintWriter writer = response.getWriter();
					if (request.getHeader("MyAjax") != null && request.getHeader("MyAjax").equals("1")) {
						writer.print("{\"status\":\"999999\"}");
					}
					else {
						writer.print("<script type='text/javascript'>window.location.href='" + request.getContextPath()
							+ "/admin/login';</script>");
					}
					writer.flush();
					writer.close();
				}
				else {
					arg2.doFilter(request, response);
				}
			}
		}
		else {
			response.sendError(404);
		}
	}
	
	@Override
	public void init(FilterConfig arg0)
		throws ServletException {
		
	}
	
}
