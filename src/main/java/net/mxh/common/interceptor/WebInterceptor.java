package net.mxh.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import net.mxh.entity.Admin;
import net.mxh.entity.User;
import net.mxh.util.CategoryUtil;

@Controller
public class WebInterceptor implements HandlerInterceptor {
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
		throws Exception {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		
		HttpSession session = request.getSession();
		Object obj = session.getAttribute("type");
		boolean flag = true;
		if (null == obj) {
			flag = false;
		}
		else if ("1".equals(obj.toString())) {
			User user = (User)session.getAttribute("user");
			if (null == user || user.getDepartmentId() != (long)CategoryUtil.TYPE.ONE) {
				flag = false;
			}
			else {
				if (user.getStatus() != CategoryUtil.USERSTATUS.THREE) {
					response.sendRedirect(request.getContextPath() + "/web/error");
					return false;
				}
			}
		}
		else if ("2".equals(obj.toString())) {
			User user = (User)session.getAttribute("user");
			if (null == user || user.getDepartmentId() != (long)CategoryUtil.TYPE.TOW) {
				flag = false;
			}
			else {
				if (user.getStatus() != CategoryUtil.USERSTATUS.THREE) {
					response.sendRedirect(request.getContextPath() + "/web/error");
					return false;
				}
			}
		}
		else if ("3".equals(obj.toString())) {
			Admin admin = (Admin)session.getAttribute("admin");
			if (null == admin || admin.getRoleId() != 3L) {
				flag = false;
			}
			
			if (CategoryUtil.WHETHER.NO == admin.getIsUse()) {
				session.setAttribute("message", "用户已禁用");
				response.sendRedirect(request.getContextPath() + "/web/error");
				return false;
			}
		}
		else {
			flag = false;
		}
		
		if (!flag) {
			response.sendRedirect(request.getContextPath() + "/web/authorizeUrl");
			return false;
		}
		return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
		ModelAndView modelAndView)
		throws Exception {
		
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
		throws Exception {
		
	}
	
}
