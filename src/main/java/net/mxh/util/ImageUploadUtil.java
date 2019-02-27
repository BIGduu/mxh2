package net.mxh.util;

import java.io.File;
import java.util.Date;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

public class ImageUploadUtil {
	
	private static Logger logger = Logger.getLogger(ImageUploadUtil.class);
	
	public final static String imgPath = PropertiesUtil.getPropertiesKey("imgPath");
	
	/**
	 * 单图片上传
	 * @param file
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static String imgUpload(MultipartFile file)
		throws Exception {
		String dateStr = DateUtil.getDateFormat(new Date(), "yyyy/MM/");
		String filePath = imgPath + dateStr;
		String imageUrl = "";
		try {
			String extUpp =
				file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();
			String filename = UUID.randomUUID().toString().replaceAll("-", "") + "." + extUpp;
			String path = filePath + filename;
			File localFile = new File(path);
			if (!localFile.exists()) {
				localFile.mkdirs();
			}
			
			file.transferTo(localFile);
			imageUrl = dateStr + filename;
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error("imgUploadHomework图片上传失败， 异常内容" + e.getMessage(), e);
			throw e;
		}
		
		return imageUrl;
	}
	
	public static boolean checkImageFile(MultipartFile uploadImg) {
		
		String fileName = uploadImg.getOriginalFilename();
		String extUpp = fileName.substring(fileName.lastIndexOf(".") + 1).toUpperCase();
		
		if (!extUpp.matches("^[(JPG)|(PNG)|(GIF)|(JPEG)]+$")) {
			return false;
		}
		
		if (uploadImg.getSize() > 2 * 1024 * 1024 * 1024) {
			return false;
		}
		
		return true;
	}
	
	public static String getRealUrl(String remoteFilePath) {
		if (StringUtil.isEmpty(remoteFilePath)) {
			return "";
		}
		else {
			return CategoryUtil.fileUrl + remoteFilePath;
		}
	}
	
}
