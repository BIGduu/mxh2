package net.mxh.task;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import net.mxh.controller.WeixinController;
import net.mxh.util.ImHttpClientUtil;
import net.mxh.util.PropertiesUtil;
import net.mxh.util.StringUtil;

@Component
public class WeixinTask {
	
	private static Logger logger = Logger.getLogger(WeixinTask.class);
	
	public static String appid = PropertiesUtil.getPropertiesKey("wxAppid"); // 微信开放平台审核通过的应用APPID
	
	public static String appSecret = PropertiesUtil.getPropertiesKey("wxAppSecret");// 微信开放平台
	
	private static String accessToken;
	
	public static String jsapiTicket;
	
	public static void main(String[] args) {
		String url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=" + accessToken;
		String str =
			"{\"button\":[{\"type\":\"view\",\"name\":\"进入平台\",\"url\":\"http://mxh.mingxionghuijc.com/mxh/web/authorizeUrl\"}}";
		System.out.println(ImHttpClientUtil.httpPost(url, str));
	}
	
	@Scheduled(fixedRate = 60 * 60 * 1000)
	public static void timing() {
		if (accessTokenRefresh(0))
			jsapiTicketRefresh(0);
	}
	
	public static boolean accessTokenRefresh(int num) {
		if (num < 10) {
			String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appid
				+ "&secret=" + appSecret;
			JSONObject param = ImHttpClientUtil.httpGet(url);
			Integer errcode = StringUtil.parseInt(param.getString("errcode"));
			if (null == errcode || errcode == 0) {
				accessToken = param.getString("access_token");
				return true;
			}
			else if (errcode == -1) {
				try {
					Thread.sleep(10);
					accessTokenRefresh(++num);
				}
				catch (InterruptedException e) {
					logger.error("公众号的全局唯一接口调用异常", e);
				}
			}
			else {
				logger.error("公众号的全局唯一接口调用凭据获取失败，errcode:" + errcode + ",errmsg:" + param.getString("errmsg"));
			}
		}
		else {
			logger.error("公众号的全局唯一接口调用凭据获取失败，微信系统繁忙");
		}
		return false;
	}
	
	public static boolean jsapiTicketRefresh(int num) {
		if (num < 10) {
			String url =
				"https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + accessToken + "&type=jsapi";
			JSONObject param = ImHttpClientUtil.httpGet(url);
			Integer errcode = StringUtil.parseInt(param.getString("errcode"));
			if (null == errcode || errcode == 0) {
				jsapiTicket = param.getString("ticket");
				return true;
			}
			else if (errcode == -1) {
				try {
					Thread.sleep(10);
					jsapiTicketRefresh(++num);
				}
				catch (InterruptedException e) {
					logger.error("公众号调用微信JS接口的临时票据调用异常", e);
				}
			}
			else {
				logger.error("公众号调用微信JS接口的临时票据获取失败，errcode:" + param.getString("errcode") + ",");
			}
		}
		else {
			logger.error("公众号调用微信JS接口的临时票据获取失败，微信系统繁忙");
		}
		return false;
	}
	
	public static String signature(String url, String nonceStr, Long timestamp) {
		Map<String, Object> map = new HashMap<>();
		map.put("noncestr", nonceStr);
		map.put("jsapi_ticket", jsapiTicket);
		map.put("timestamp", timestamp);
		map.put("url", url);
		Set<String> keySet = map.keySet();
		String[] keyArray = keySet.toArray(new String[] {});
		Arrays.sort(keyArray);
		StringBuilder sb = new StringBuilder();
		for (String key : keyArray) {
			if (null != map.get(key)) {
				sb.append(key + "=" + map.get(key) + "&");
			}
		}
		return WeixinController.sha1(sb.substring(0, sb.length() - 1));
	}
	
	/**
	 * @description 通过code获取openid
	 * @author ZhongHan
	 * @date 2017年7月10日
	 * @param code
	 * @return
	 */
	public static String openid(String code) {
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appid + "&secret=" + appSecret
			+ "&code=" + code + "&grant_type=authorization_code";
		JSONObject param = ImHttpClientUtil.httpGet(url);
		String openid = param.getString("openid");
		return openid;
	}
	
	public static String image(String mediaId) {
		if (accessToken == null)
			timing();
		String url = "https://api.weixin.qq.com/cgi-bin/media/get?access_token=" + accessToken + "&media_id=" + mediaId;
		return ImHttpClientUtil.wxHttpGet(url);
	}
	
	/**
	 * @description 发送模板消息
	 * @author ZhongHan
	 * @date 2017年8月22日
	 * @param openId
	 * @param templateId
	 * @param redirectUrl
	 * @param param
	 */
	public static void sendMessage(String openId, String templateId, String redirectUrl, Map<String, String> param) {
		String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + accessToken;
		Map<String, Object> map = new HashMap<>();
		map.put("touser", openId);
		map.put("template_id", templateId);
		map.put("url", redirectUrl);
		Map<String, Object> data = new HashMap<>();
		for (String key : param.keySet()) {
			Map<String, Object> keynote = new HashMap<>();
			keynote.put("value", param.get(key));
			keynote.put("color", "#173177");
			data.put(key, keynote);
		}
		map.put("data", data);
		JSONObject result = ImHttpClientUtil.httpPost(url, JSON.toJSONString(map));
		if (0 != result.getInteger("errcode")) {
			System.out.println(JSON.toJSONString(result));
		}
	}
	
}
