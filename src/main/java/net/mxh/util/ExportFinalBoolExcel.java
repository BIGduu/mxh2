package net.mxh.util;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

import net.mxh.entity.OrderInfo;
import net.mxh.vo.FinalOrderExcel;

/**
 * 利用开源组件POI3.0.2动态导出EXCEL文档 转载时请保留以下信息，注明出处！
 * @author leno
 * @version v1.0
 * @param <T> 应用泛型，代表任意一个符合javabean风格的类 注意这里为了简单起见，boolean型的属性xxx的get器方式为getXxx(),而不是isXxx() byte[]表jpg格式的图片数据
 */
public class ExportFinalBoolExcel {
	public void exportExcel(String title, Collection<FinalOrderExcel> dataset, OutputStream out) {
		exportExcel(title, null, null, dataset, out, "yyyy-MM-dd");
	}
	
	public void exportExcel(String title, List<String> headers, List<String> numberPriceList,
		Collection<FinalOrderExcel> dataset, OutputStream out) {
		exportExcel(title, headers, numberPriceList, dataset, out, "yyyy-MM-dd");
	}

	/**
	 * 这是一个通用的方法，利用了JAVA的反射机制，可以将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上
	 * @param title 表格标题名
	 * @param headers 表格属性列名数组
	 * @param dataset 需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的 javabean属性的数据类型有基本数据类型及String,Date,byte[](图片数据)
	 * @param out 与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
	 * @param pattern 如果有时间数据，设定输出格式。默认为"yyy-MM-dd"
	 */
	@SuppressWarnings("unchecked")
	public void exportExcel(String title, List<String> headers, List<String> numberPriceList,
							Collection<FinalOrderExcel> dataset, OutputStream out, String pattern) {
		int mainHeaderLength = 6;
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个表格
		if (StringUtils.isEmpty(title)) {
			title = "导出excel";
		}
		HSSFCellStyle setBorder = workbook.createCellStyle();
		setBorder.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		setBorder.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		setBorder.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		setBorder.setBorderTop(HSSFCellStyle.BORDER_THIN);
		setBorder.setBorderRight(HSSFCellStyle.BORDER_THIN);
		setBorder.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		setBorder.setWrapText(true);

		HSSFSheet sheet = workbook.createSheet(title);
		// 设置表格
		sheet.setAutobreaks(true);
		// 设置这些样式
		// 表头标题样式
		HSSFFont headfont = workbook.createFont();
		headfont.setFontName("宋体");
		headfont.setFontHeightInPoints((short)22);// 字体大小
		HSSFCellStyle headstyle = workbook.createCellStyle();
		headstyle.setFont(headfont);
		headstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
		headstyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
		headstyle.setLocked(true);
		// 表头时间样式
		HSSFFont datefont = workbook.createFont();
		datefont.setFontName("宋体");
		datefont.setFontHeightInPoints((short)12);// 字体大小
		HSSFCellStyle datestyle = workbook.createCellStyle();
		datestyle.setFont(datefont);
		datestyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
		datestyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
		datestyle.setLocked(true);
		// 列名样式
		HSSFFont font = workbook.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short)12);// 字体大小
		HSSFCellStyle style = workbook.createCellStyle();
		style.setFont(font);
		style.setLocked(true);

		style.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框 style.setBorderLeft(HSSFCellStyle.BORDER_THIN);//
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框 style.setBorderRight(HSSFCellStyle.BORDER_THIN);//
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中

		// 普通单元格样式（中文）
		HSSFFont font2 = workbook.createFont();
		font2.setFontName("宋体");
		font2.setFontHeightInPoints((short)12);

		HSSFCellStyle style2 = workbook.createCellStyle();
		style2.setFont(font2);
		style2.setWrapText(true); // 换行
		style2.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
		style2.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		style2.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
		style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
		style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
		style2.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));

		HSSFCellStyle style3 = workbook.createCellStyle();
		style3.setFont(font2);
		style3.setWrapText(true); // 换行
		style3.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		style3.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
		style3.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		style3.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
		style3.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
		style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
		style3.setDataFormat(HSSFDataFormat.getBuiltinFormat("0"));


		// 声明一个画图的顶级管理器
		HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
		// 定义注释的大小和位置,详见文档
		// HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short)4, 2, (short)6, 5));
		// 设置注释内容
		// comment.setString(new HSSFRichTextString("可以在POI中添加注释！"));
		// 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
		// comment.setAuthor("leno");
		HSSFRow row = sheet.createRow(0);
		HSSFCell cellHeader = row.createCell(0);
		cellHeader.setCellValue("明兄辉对账单");
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headers.size() - 1));
		cellHeader.setCellStyle(headstyle);
		// 产生表格标题行
		row = sheet.createRow(1);
		for (short i = 0; i < headers.size(); i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellStyle(style2);
			HSSFRichTextString text = new HSSFRichTextString(headers.get(i));
			cell.setCellValue(text);
			if (i >= 0 && i < mainHeaderLength || i == headers.size() - 1) {
				sheet.addMergedRegion(new CellRangeAddress(1, 2, i, i));
			}
		}
		//
		row = sheet.createRow(2);
		for (short i = 0; i < headers.size() - 1; i++) {
			int j = i - mainHeaderLength;
			if (i >= mainHeaderLength && j < numberPriceList.size()) {
				HSSFCell cell = row.createCell(i);
				cell.setCellValue(new HSSFRichTextString(numberPriceList.get(j)));
				cell.setCellStyle(style2);
			}
		}
		// 遍历集合数据，产生数据行
		Iterator<FinalOrderExcel> it = dataset.iterator();
		int index = 2;
		while (it.hasNext()) {
			index++;
			row = sheet.createRow(index);
			FinalOrderExcel t = it.next();
			// 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
			Field[] fields = t.getClass().getDeclaredFields();
			for (short i = 0; i < headers.size(); i++) {
				HSSFCell cell = row.createCell(i);
				style2.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
				cell.setCellStyle(style2);
				if (i >= 0 && i < mainHeaderLength) {
					Field field = fields[i];
					String fieldName = field.getName();
					String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
					try {
						Class tCls = t.getClass();
						Method getMethod = tCls.getMethod(getMethodName, new Class[] {});
						Object value = getMethod.invoke(t, new Object[] {});
						// 判断值的类型后进行强制类型转换
						String textValue = null;
						if (value instanceof Integer) {
							int intValue = (Integer)value;
							cell.setCellValue(intValue);
						}
						else if (value instanceof Float) {
							float fValue = (Float)value;
							cell.setCellValue(fValue);
						}
						else if (value instanceof Double) {
							double dValue = (Double)value;
							cell.setCellValue(dValue);
						}
						else if (value instanceof Long) {
							long longValue = (Long)value;
							cell.setCellValue(longValue);
						}
						else if (value instanceof BigDecimal) {
							double longValue = ((BigDecimal)value).doubleValue();
							cell.setCellValue(longValue);
						}
//						else if (value instanceof Boolean) {
//							boolean bValue = (Boolean)value;
//							textValue = "男";
//							if (!bValue) {
//								textValue = "女";
//							}
//						}
						else if (value instanceof Date) {
							Date date = (Date)value;
							SimpleDateFormat sdf = new SimpleDateFormat(pattern);
							textValue = sdf.format(date);
							cell.setCellValue(textValue);
						}
//						else if (value instanceof byte[]) {
//							// 有图片时，设置行高为60px;
//							row.setHeightInPoints(60);
//							// 设置图片所在列宽度为80px,注意这里单位的一个换算
//							sheet.setColumnWidth(i, (short)(35.7 * 80));
//							// sheet.autoSizeColumn(i);
//							byte[] bsValue = (byte[])value;
//							HSSFClientAnchor anchor =
//									new HSSFClientAnchor(0, 0, 1023, 255, (short)6, index, (short)6, index);
//							anchor.setAnchorType(2);
//							patriarch.createPicture(anchor,
//									workbook.addPicture(bsValue, HSSFWorkbook.PICTURE_TYPE_JPEG));
//						}
						//空值则什么都不设置
						else if(null == value) {
						// 其它数据类型都当作字符串简单处理
						}else{
							textValue = value.toString();
							cell.setCellValue(textValue);
						}
//						// 如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
//						if (textValue != null) {
//							Pattern p = Pattern.compile("^//d+(//.//d+)?$");
//							Matcher matcher = p.matcher(textValue);
//							if (matcher.matches()) {
//								// 是数字当作double处理
//								cell.setCellValue(Double.parseDouble(textValue));
//							}
//							else {
//								HSSFRichTextString richString = new HSSFRichTextString(textValue);
//								HSSFFont font3 = workbook.createFont();
//								// font3.setColor(HSSFColor.BLUE.index);
//								richString.applyFont(font3);
//								cell.setCellValue(richString);
//							}
//						}
					}
					catch (SecurityException e) {
						e.printStackTrace();
					}
					catch (NoSuchMethodException e) {
						e.printStackTrace();
					}
					catch (IllegalArgumentException e) {
						e.printStackTrace();
					}
					catch (IllegalAccessException e) {
						e.printStackTrace();
					}
					catch (InvocationTargetException e) {
						e.printStackTrace();
					}
					finally {
						// 清理资源
					}
				}
				else if (i >= mainHeaderLength && i < headers.size() - 1) {
					List<OrderInfo> orderInfos = t.getOrderInfos();
					if (CollectionUtils.isNotEmpty(orderInfos)) {
						OrderInfo orderInfo = orderInfos.get(i - mainHeaderLength);

						//如果订单信息存在 则显示订单商品数量
						if (orderInfo != null) {
							//如果是退货单(总价为负数) 则数量显示负数
							if(orderInfo.getAllPrice().doubleValue()<0){
								cell.setCellStyle(style3);
								cell.setCellValue(orderInfo.getNumber().doubleValue()*-1);
							}else {
								cell.setCellStyle(style3);
								cell.setCellValue(orderInfo.getNumber().doubleValue());
							}

						}
					}

				}
				else {
					cell.setCellValue(t.getRemarks());
				}
			}
		}
		try {
			workbook.write(out);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
