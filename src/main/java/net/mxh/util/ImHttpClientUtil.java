package net.mxh.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;

/**
 * 网易云信http请求
 * @author zhh
 */
public class ImHttpClientUtil {
	
	private static Logger logger = Logger.getLogger(ImHttpClientUtil.class);
	
	/**
	 * post请求传输String参数 例如：name=Jack&sex=1&type=2 Content-type:application/x-www-form-urlencoded
	 * @param url
	 * @param strParam
	 * @return
	 */
	public static JSONObject httpPost(String url, String strParam) {
		// post请求返回结果
		CloseableHttpClient httpClient = HttpClients.createDefault();
		JSONObject jsonResult = null;
		HttpPost httpPost = new HttpPost(url);
		// 设置请求和传输超时时间
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(2000).setConnectTimeout(2000).build();
		httpPost.setConfig(requestConfig);
		try {
			if (null != strParam) {
				// 解决中文乱码问题
				StringEntity entity = new StringEntity(strParam, "utf-8");
				entity.setContentEncoding("UTF-8");
				entity.setContentType("application/x-www-form-urlencoded");
				httpPost.setEntity(entity);
			}
			CloseableHttpResponse result = httpClient.execute(httpPost);
			// 请求发送成功，并得到响应
			if (result.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String str = "";
				try {
					// 读取服务器返回过来的json字符串数据
					str = EntityUtils.toString(result.getEntity(), "utf-8");
					// 把json字符串转换成json对象
					jsonResult = JSONObject.parseObject(str);
				}
				catch (Exception e) {
					logger.error("post请求提交失败:" + url, e);
				}
			}
		}
		catch (IOException e) {
			logger.error("post请求提交失败:" + url, e);
		}
		finally {
			httpPost.releaseConnection();
		}
		return jsonResult;
	}
	
	/**
	 * post请求传输String参数 例如：name=Jack&sex=1&type=2 Content-type:application/x-www-form-urlencoded
	 * @param url
	 * @param strParam
	 * @return
	 */
	public static String wxHttpPost(String url, String strParam) {
		// post请求返回结果
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String str = "";
		HttpPost httpPost = new HttpPost(url);
		// 设置请求和传输超时时间
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(2000).setConnectTimeout(2000).build();
		httpPost.setConfig(requestConfig);
		try {
			if (null != strParam) {
				// 解决中文乱码问题
				StringEntity entity = new StringEntity(strParam, "utf-8");
				entity.setContentEncoding("UTF-8");
				entity.setContentType("application/x-www-form-urlencoded");
				httpPost.setEntity(entity);
			}
			CloseableHttpResponse result = httpClient.execute(httpPost);
			// 请求发送成功，并得到响应
			if (result.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				try {
					// 读取服务器返回过来的json字符串数据
					str = EntityUtils.toString(result.getEntity(), "utf-8");
				}
				catch (Exception e) {
					logger.error("post请求提交失败:" + url, e);
				}
			}
		}
		catch (IOException e) {
			logger.error("post请求提交失败:" + url, e);
		}
		finally {
			httpPost.releaseConnection();
		}
		return str;
	}
	
	/**
	 * 从微信服务器下载临时素材
	 * @param url 路径
	 * @return
	 */
	public static String wxHttpGet(String url) {
		String imageUrl = null;
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet request = new HttpGet(url);
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(2000).setConnectTimeout(2000).build();
		request.setConfig(requestConfig);
		FileOutputStream outputStream = null;
		InputStream inputStream = null;
		try {
			CloseableHttpResponse response = client.execute(request);
			
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				inputStream = response.getEntity().getContent();
				String dateStr = DateUtil.getDateFormat(new Date(), "yyyy/MM/");
				String filePath = ImageUploadUtil.imgPath + dateStr;
				File file = new File(filePath);
				if (!file.exists()) {
					file.mkdirs();
				}
				String filename = UUID.randomUUID().toString().replaceAll("-", "") + ".png";
				outputStream = new FileOutputStream(filePath + filename);
				byte[] data = new byte[1024];
				int len = 0;
				while ((len = inputStream.read(data)) != -1) {
					outputStream.write(data, 0, len);
				}
				imageUrl = dateStr + filename;
			}
			
			else {
				logger.error("get请求提交失败:" + url);
			}
		}
		catch (IOException e) {
			logger.error("get请求提交失败:" + url, e);
		}
		finally {
			request.releaseConnection();
			if (inputStream != null)
				try {
					inputStream.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			
			if (outputStream != null)
				try {
					outputStream.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
		}
		return imageUrl;
	}
	
	public static void main(String[] args) {
		System.out.println(wxHttpGet(
			"https://api.weixin.qq.com/cgi-bin/media/get?access_token=f8vXZqhqFyuigK5ywQQKyjiPoEi9FfCv4R7c32qQ09QVUs0LA-NeIUYtMZLtCnyC705e7AbZT5dsm5CvaWXYBVwrEs5bzWz8GU9Pyl4sKtnToeBGDsJWaqxTrbZV5wKlPFKiAGAJZE&media_id=enSu-3t6OiQkY3eAQi_gyKnBa6ZgRo8AALaMdNKETWTl72IDvJZaANsV91JOyEO8"));
	}
	
	/**
	 * 发送get请求
	 * @param url 路径
	 * @return
	 */
	public static JSONObject httpGet(String url) {
		// get请求返回结果
		JSONObject jsonResult = null;
		CloseableHttpClient client = HttpClients.createDefault();
		// 发送get请求
		HttpGet request = new HttpGet(url);
		// 设置请求和传输超时时间
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(2000).setConnectTimeout(2000).build();
		request.setConfig(requestConfig);
		try {
			CloseableHttpResponse response = client.execute(request);
			
			// 请求发送成功，并得到响应
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// 读取服务器返回过来的json字符串数据
				HttpEntity entity = response.getEntity();
				String strResult = EntityUtils.toString(entity, "utf-8");
				// 把json字符串转换成json对象
				jsonResult = JSONObject.parseObject(strResult);
			}
			else {
				logger.error("get请求提交失败:" + url);
			}
		}
		catch (IOException e) {
			logger.error("get请求提交失败:" + url, e);
		}
		finally {
			request.releaseConnection();
		}
		return jsonResult;
	}
}
