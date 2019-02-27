package net.mxh.common.filter;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @description 缓存资源获取，防止刚上传的图片出现404错误，不然要5秒后才可以拿到
 * @author LuBo
 * @date 2017年8月2日
 */
public class CacheFilter implements Filter {
	
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
		int start = url.indexOf("/cache/");
		String filePath = url.substring(start);
		File file = new File(request.getServletContext().getRealPath(filePath));
		if (file.exists() && file.isFile()) {
			BufferedInputStream bis = null;
			BufferedOutputStream bos = null;
			byte[] buffer = new byte[4096];
			try {
				bis = new BufferedInputStream(new FileInputStream(file));
				bos = new BufferedOutputStream(response.getOutputStream());
				int read = 0;
				long now = System.currentTimeMillis();
				while ((read = bis.read(buffer)) != -1 && System.currentTimeMillis() - now < 30000L) {
					bos.write(buffer, 0, read);
					bos.flush();
				}
			}
			catch (Exception e) {
				
			}
			finally {
				if (bis != null) {
					try {
						bis.close();
					}
					catch (Exception e1) {
					}
				}
				if (bos != null) {
					try {
						bos.close();
					}
					catch (Exception e1) {
						
					}
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
