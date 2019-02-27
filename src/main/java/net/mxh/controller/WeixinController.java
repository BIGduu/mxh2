package net.mxh.controller;

import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.mxh.task.WeixinTask;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import net.mxh.entity.Admin;
import net.mxh.entity.User;
import net.mxh.service.AdminService;
import net.mxh.service.SigninService;
import net.mxh.service.UserService;
import net.mxh.util.CategoryUtil;

@Controller
@RequestMapping("/weixin")
public class WeixinController {
	
	private static String token = "zhh";
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AdminService adminService;
	
	@Autowired
	private SigninService signinService;
	
	@RequestMapping("/init")
	@ResponseBody
	public String verification(HttpServletRequest request) {
		/**
		 * 接收微信服务器发送请求时传递过来的4个参数
		 */
		String signature = request.getParameter("signature");// 微信加密签名signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
		String timestamp = request.getParameter("timestamp");// 时间戳
		String nonce = request.getParameter("nonce");// 随机数
		String echostr = request.getParameter("echostr");// 随机字符串
		// 排序
		String sortString = sort(token, timestamp, nonce);
		// 加密
		String mySignature = sha1(sortString);
		// 校验签名
		if (mySignature != null && mySignature != "" && mySignature.equals(signature)) {
			return echostr;
		}
		else {
			return null;
		}
	}
	
	@RequestMapping("/openId")
	@ResponseBody
	public ModelAndView openId(String code, String state, HttpServletRequest request, HttpServletResponse response) {
		if ("yubay".equals(state)) {
			return new ModelAndView("redirect:/weixin/getOpenId?code=" + code);
		}
		return null;
	}
	
	@RequestMapping("/getOpenId")
	@ResponseBody
	public ModelAndView getOpenId(String code, HttpSession session, HttpServletRequest request) {

		String openid = WeixinTask.openid(code);
		//用于本地测试的openid
		//String openid = "oAG0V0kE4Cj7XBzWnJNxMeOKX4XA";


		if (null != openid) {
			session.invalidate();
			session = request.getSession();
			User user = userService.findByOpenid(openid);
			if (null != user) {
				if (CategoryUtil.USERSTATUS.THREE != user.getStatus()) {
					return new ModelAndView("redirect:/web/error");
				}
				session.setAttribute("user", user);
				session.setAttribute("type", user.getDepartmentId());
				if (user.getDepartmentId() == 2) {
					if (signinService.countByTime(user.getId()) > 0)
						session.setAttribute("sign", 1);
					else
						session.setAttribute("sign", 0);
				}
				if (CategoryUtil.DEPARTMENTID.ONE == user.getDepartmentId()) {
					return new ModelAndView("redirect:/web/product");
				}
				else {
					return new ModelAndView("redirect:/web/my");
				}
			}
			else {
				Admin admin = adminService.findByOpenid(openid);
				if (null != admin) {
					if (CategoryUtil.WHETHER.NO == admin.getIsUse()) {
						session.setAttribute("message", "用户已禁用");
						return new ModelAndView("redirect:/web/error");
					}
					session.setAttribute("admin", admin);
					session.setAttribute("type", CategoryUtil.TYPE.THREE);
					return new ModelAndView("redirect:/web/my");
				}
				
			}
			session.setAttribute("openid", openid);
			return new ModelAndView("redirect:/web/register");
		}
		return new ModelAndView("redirect:/web/authorizeUrl");
	}
	
	/**
	 * 解析微信发来的请求（XML）
	 * @param request 封装了请求信息的HttpServletRequest对象
	 * @return map 解析结果
	 * @throws Exception
	 */
	public static Map<String, String> parseXml(HttpServletRequest request)
		throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		InputStream inputStream = request.getInputStream();
		SAXReader reader = new SAXReader();
		Document document = reader.read(inputStream);
		Element root = document.getRootElement();
		@SuppressWarnings("unchecked")
		List<Element> elementList = root.elements();
		
		for (Element e : elementList) {
			map.put(e.getName(), e.getText());
		}
		
		inputStream.close();
		inputStream = null;
		return map;
	}
	
	/**
	 * 排序方法
	 * @param token
	 * @param timestamp
	 * @param nonce
	 * @return
	 */
	public String sort(String token, String timestamp, String nonce) {
		String[] strArray = {token, timestamp, nonce};
		Arrays.sort(strArray);
		StringBuilder sb = new StringBuilder();
		for (String str : strArray) {
			sb.append(str);
		}
		
		return sb.toString();
	}
	
	/**
	 * 将字符串进行sha1加密
	 * @param str 需要加密的字符串
	 * @return 加密后的内容
	 */
	public static String sha1(String str) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			digest.update(str.getBytes());
			byte messageDigest[] = digest.digest();
			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			// 字节数组转换为 十六进制 数
			for (int i = 0; i < messageDigest.length; i++) {
				String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
				if (shaHex.length() < 2) {
					hexString.append(0);
				}
				hexString.append(shaHex);
			}
			return hexString.toString();
			
		}
		catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}
}
