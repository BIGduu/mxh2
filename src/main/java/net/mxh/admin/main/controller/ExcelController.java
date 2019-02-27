package net.mxh.admin.main.controller;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.mxh.entity.Merchandise;
import net.mxh.entity.Storage;
import net.mxh.service.MerchandiseService;
import net.mxh.service.StorageService;
import net.mxh.util.CategoryUtil;
import net.mxh.util.DateUtil;
import net.mxh.util.ExcelUtil;
import net.mxh.util.StringUtil;

//@Controller
//@RequestMapping(value = "/excel/*")
public class ExcelController {
	
	@Autowired
	private StorageService appinfoService;
	
	@Resource
	private MerchandiseService merchandiseService;
	
	@RequestMapping(value = "exportfeedback")
	@ResponseBody
	public String exportFeedBack(HttpServletResponse response, String beginTime, String endTime) {
		
		String fileName = "反馈明细" + System.currentTimeMillis() + ".xls"; // 文件名
		String sheetName = "反馈明细";// sheet名
		
		String[] title = new String[] {"进货日期", "单号", "送货单位", "品名", "规格型号", "结算数量", "结算单位", "送货数量", "送货单位", "单价", "货款金额",
			"运费单价", "运费金额", "总金额", "货款付款日期", "货款收款人", "运费付款日期", "运费收款人", "备注"};// 标题
		
		List<Storage> list = appinfoService.findBy(CategoryUtil.STORAGETYPE.ONE,
			DateUtil.getDateTimeFormat(beginTime).getTime(),
			DateUtil.getDateTimeFormat(endTime).getTime());
		
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		String[][] values = new String[list.size()][];
		for (int i = 0; i < list.size(); i++) {
			values[i] = new String[title.length];
			// 将对象内容转换成string
			Storage obj = list.get(i);
			Merchandise merchandise = merchandiseService.findById(obj.getMerchandiseId());
			values[i][0] = DateUtil.getDateFormat(new Date(obj.getCreateTime()), "MM月dd日");
			values[i][1] = obj.getReceiptNumber();
			values[i][2] = obj.getDeliveryCompany();
			values[i][3] = merchandise.getSpecification();
			values[i][4] = StringUtil.toString(obj.getBillingQuantity());
			values[i][5] = obj.getBillingUnit();
			values[i][6] = StringUtil.toString(obj.getNumber());
			values[i][7] = merchandise.getUnit();
			if (null != obj.getPurchasePrice()) {
				values[i][8] = obj.getPurchasePrice().toPlainString();
			}
			values[i][9] = obj.getPurchasePrice().toPlainString();
			
		}
		
		HSSFWorkbook wb = ExcelUtil.getHSSFWorkbook(sheetName, title, values, null);
		
		// 将文件存到指定位置
		try {
			this.setResponseHeader(response, fileName);
			OutputStream os = response.getOutputStream();
			wb.write(os);
			os.flush();
			os.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return "ok";
	}
	
	public void setResponseHeader(HttpServletResponse response, String fileName) {
		try {
			try {
				fileName = new String(fileName.getBytes(), "ISO8859-1");
			}
			catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			response.setContentType("application/octet-stream;charset=ISO8859-1");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
			response.addHeader("Pargam", "no-cache");
			response.addHeader("Cache-Control", "no-cache");
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}