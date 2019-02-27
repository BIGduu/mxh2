package net.mxh.common.exception;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;

/**
 * @description 统一异常处理
 * @author ZhangHan
 * @date 2017年7月20日
 */
public class GlobalExceptionHandler implements HandlerExceptionResolver {
	
	private static Logger logger = Logger.getLogger(GlobalExceptionHandler.class);
	
	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
		Exception ex) {
		logger.error(request.getRequestURL() + "接口抛出异常， 异常内容" + ex.getMessage(), ex);
		Map<String, Object> model = new HashMap<String, Object>();
		// 是否异步请求
		if (!(request.getHeader("accept").indexOf("application/json") > -1
			|| (request.getHeader("X-Requested-With") != null
				&& request.getHeader("X-Requested-With").indexOf("XMLHttpRequest") > -1))) {
			// 根据不同错误转向不同页面
			return new ModelAndView("/template/errors/500", model);
		}
		
		else {
			try {
				response.setContentType("application/json;charset=UTF-8");
				PrintWriter writer = response.getWriter();
				Map<String, Object> result = new HashMap<String, Object>();
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("status", 444);
				data.put("message", "系统异常");
				result.put("data", data);
				writer.write(JSONObject.toJSONString(result));
				writer.flush();
				writer.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
}
