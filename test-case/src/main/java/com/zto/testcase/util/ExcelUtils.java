package com.zto.testcase.util;

import com.zto.testcase.dto.ExcelDto;
import com.zto.testcase.enums.TestCasePriorityEnum;
import com.zto.testcase.exception.TemplateException;
import com.zto.testcase.model.ExcelColumn;
import java.awt.Color;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.extensions.XSSFCellBorder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: wxc
 * @Description:
 * @Date: Create in 上午10:55 2021/3/18
 */
public class ExcelUtils {

    private static final Logger logger = LoggerFactory.getLogger(ExcelUtils.class);

    /***
     * 判断文件格式
     * @param in
     * @param fileName
     * @return
     * @throws Exception
     */
    private static Workbook getWorkBook(InputStream in, String fileName) throws Exception {
        Workbook book = null;
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        if (".xls".equals(fileType)) {
            book = new HSSFWorkbook(in);
        } else if (".xlsx".equals(fileType)) {
            book = new XSSFWorkbook(in);
        } else {
            throw new Exception("请上传EXCEL文件！");
        }
        return book;
    }

    public static List getListByExcel(InputStream in, String fileName) throws Exception {
        List list = new ArrayList();
        //创建excel工作簿
        Workbook work = getWorkBook(in, fileName);
        if (null == work) {
            throw new Exception("创建excel工作簿为空！");
        }
        Sheet sheet = null;
        Row row = null;
        Cell cell = null;

        for (int i = 0; i < work.getNumberOfSheets(); i++) {
            sheet = work.getSheetAt(i);
            if (sheet == null) {
                continue;
            }
            List<Object> li = new ArrayList<>();
            //滤过第一行的话加条件： || row.getFirstCellNum()==j
            for (int j = sheet.getFirstRowNum(); j < sheet.getLastRowNum(); j++) {
                row = sheet.getRow(j);
                if (row == null || row.getFirstCellNum() == j) {
                    continue;
                }

                //遍历每一行的单元格
                for (int y = row.getFirstCellNum(); y < row.getLastCellNum(); y++) {
                    cell = row.getCell(y);
                    li.add(cell);
                }
                list.add(li);
            }
        }
        work.close();
        return list;
    }

    public static List<ExcelColumn> getList(InputStream in, String fileName) throws Exception {
        List<ExcelColumn> list = new ArrayList();
        //创建excel工作簿
        Workbook work = getWorkBook(in, fileName);
        if (null == work) {
            throw new Exception("创建excel工作簿为空！");
        }
        Sheet sheet = null;
        Row row = null;
        Cell cell = null;

        for (int i = 0; i < work.getNumberOfSheets(); i++) {
            sheet = work.getSheetAt(i);
            if (sheet == null) {
                continue;
            }
            //遍历第一行，检查表头
            //校验表头，如果不正确直接返回模板错误
            //如果第二个sheet格式不对，continue，日志记录一下
            if(i!=0 && !sheet.rowIterator().hasNext()){
                logger.warn("第一个sheet之后的sheet没有行，直接略过！");
                continue;
            }
            Row row1 = sheet.getRow(0);
            Map<String, Integer> colMap = new HashMap<>();
            for (int y = row1.getFirstCellNum(); y < row1.getLastCellNum(); y++) {
                colMap.put(row1.getCell(y).toString().trim(), y);
            }
            System.out.println("colMap==" + colMap);
            //表头检查,给提示，第几个sheet
            //如果第二个sheet表头格式不对，continue，日志记录一下
            if(i!=0){
                try {
                    checkRow1(colMap);
                }catch (Exception e){
                    logger.warn("第一个sheet之后的sheet表头校验失败，直接略过！");
                    continue;
                }
            }else{
                checkRow1(colMap);
            }
            //滤过第一行的话加条件： || row.getFirstCellNum()==j 这里不过滤第一行，后面通过第一行数据校验模板正确性
            for (int j = sheet.getFirstRowNum(); j <= sheet.getLastRowNum(); j++) {
                row = sheet.getRow(j);
                if (row == null) {
                    continue;
                }
                ExcelColumn excelColumn = new ExcelColumn();
                //遍历每一行的单元格
                //从map中找到对应列的index
                Integer containsIndex = colMap.get("用例所属");
                Integer caseNameIndex = colMap.get("用例名称");
                Integer priorityIndex = colMap.get("优先级");
                Integer caseTypeIndex = colMap.get("用例类型");
                Integer tagIndex = colMap.get("用例标签");
                Integer preconditionIndex = colMap.get("前置条件");
                Integer stepsIndex = colMap.get("步骤描述");
                Integer expectResultIndex = colMap.get("预期结果");
                Integer remarkIndex = colMap.get("备注");

                try {
                    for (int y = row.getFirstCellNum(); y < row.getLastCellNum(); y++) {
                        cell = row.getCell(y);
                        String cellValue = cell.toString().trim();
                        if (y == containsIndex) {
                            excelColumn.setContains(cellValue);
                        } else if (y == caseNameIndex) {
                            //用例名称不能为空
                            if (StringUtils.isNotBlank(cellValue)) {
                                excelColumn.setCaseName(cellValue);
                            } else {
                                throw new Exception("用例名称列不能为空，请检查");
                            }
                        } else if (y == priorityIndex) {
                            //优先级，高 中 低，默认值高
                            if (TestCasePriorityEnum.HIGH.getDesc().equals(cellValue) ||
                                    TestCasePriorityEnum.MIDDLE.getDesc().equals(cellValue) ||
                                    TestCasePriorityEnum.LOW.getDesc().equals(cellValue) || j == 0) {
                                excelColumn.setPriority(cellValue);
                            } else {
                                excelColumn.setPriority(TestCasePriorityEnum.HIGH.getDesc());
                            }
                        } else if (y == caseTypeIndex) {
                            //用例类型
                            excelColumn.setCaseType(cellValue);
                        } else if (y == tagIndex) {
                            //用例标签
                            excelColumn.setTag(cellValue);
                        } else if (y == preconditionIndex) {
                            //前置条件
                            excelColumn.setPrecondition(cellValue);
                        } else if (y == stepsIndex) {
                            //步骤描述
                            excelColumn.setSteps(cellValue);
                        } else if (y == expectResultIndex) {
                            //预期结果
                            excelColumn.setExpectResult(cellValue);
                        } else if (y == remarkIndex) {
                            //备注
                            excelColumn.setRemark(cellValue);
                        } else {
                            //多余列，暂时去除
                            System.out.println("多余列，暂时去除，值是：" + cellValue);
                        }

                    }
                } catch (Exception e) {
                    throw new Exception("数据转换异常，请检查数据，或者按照模板格式化文件！");
                }

                list.add(excelColumn);
            }
        }
        work.close();
        return list;
    }

    /**
     * 使用浏览器选择路径下载
     *
     * @param response
     * @param fileName
     * @param data
     * @throws Exception
     */
    public static void exportExcel(HttpServletResponse response, String fileName, ExcelDto data) throws Exception {
        // 告诉浏览器用什么软件可以打开此文件
        // response.setHeader("content-Type", "application/vnd.ms-excel");
        // response.setHeader("content-Type", "application/msexcel;charset=GBK");
        // response.setContentType("application/msexcel;charset=GBK");
        // 下载文件的默认名称
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));
        //设置类型
        response.setContentType("application/msexcel;charset=GBK");
        //设置头
        response.setHeader("Pragma", "No-cache");
        //设置头
        response.setHeader("Cache-Control", "no-cache");
        //设置日期头
        response.setDateHeader("Expires", 0);
        exportExcel(data, response.getOutputStream());
    }

    private static void exportExcel(ExcelDto data, OutputStream out) throws Exception {
        logger.info(">>>>>>exportExcel start");
        XSSFWorkbook wb = new XSSFWorkbook();
        try {
            String sheetName = data.getName();
            if (null == sheetName) {
                sheetName = "Sheet1";
            }
            XSSFSheet sheet = wb.createSheet(sheetName);
            writeExcel(wb, sheet, data);
            wb.write(out);
            out.flush();
            logger.info(">>>>>>exportExcel succeed");
        } catch (Exception e) {
            logger.error(">>>>>>exportExcel error");
            e.printStackTrace();
        } finally {
            out.close();
            wb.close();
        }
    }

    /**
     * 表显示字段
     *
     * @param wb
     * @param sheet
     * @param data
     * @return
     */
    private static int writeExcel(XSSFWorkbook wb, Sheet sheet, ExcelDto data) {
        int rowIndex;
        rowIndex = writeTitlesToExcel(wb, sheet, data.getTitles());
        rowIndex = writeRowsToExcel(wb, sheet, data.getRows(), rowIndex);
        // autoSizeColumns(sheet, data.getTitles().size() + 1);
        return rowIndex;
    }

    /**
     * 设置表头
     *
     * @param wb
     * @param sheet
     * @param titles
     * @return
     */
    private static int writeTitlesToExcel(XSSFWorkbook wb, Sheet sheet, List<String> titles) {
        int rowIndex = 0;
        int colIndex = 0;
        Font titleFont = wb.createFont();
        //设置字体
        titleFont.setFontName("微软雅黑");
        //设置粗体
        //  titleFont.setBoldweight(Short.MAX_VALUE);
        //设置字号
        titleFont.setFontHeightInPoints((short) 10);
        //设置颜色
        titleFont.setColor(IndexedColors.BLACK.index);
        XSSFCellStyle titleStyle = wb.createCellStyle();
        //水平居中
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        //垂直居中
        //  titleStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        //设置图案颜色
//        titleStyle.setFillForegroundColor(new XSSFColor(new Color(182, 184, 192)));
        //设置图案样式
        //titleStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        titleStyle.setFont(titleFont);
        setBorder(titleStyle, BorderStyle.THIN, new XSSFColor(new Color(0, 0, 0)));
        Row titleRow = sheet.createRow(rowIndex);
        titleRow.setHeightInPoints(25);
        colIndex = 0;
        for (String field : titles) {
            Cell cell = titleRow.createCell(colIndex);
            cell.setCellValue(field);
            cell.setCellStyle(titleStyle);
            colIndex++;
        }
        rowIndex++;
        return rowIndex;
    }

    /**
     * 设置内容
     *
     * @param wb
     * @param sheet
     * @param rows
     * @param rowIndex
     * @return
     */
    private static int writeRowsToExcel(XSSFWorkbook wb, Sheet sheet, List<List<Object>> rows, int rowIndex) {
        int colIndex;
        Font dataFont = wb.createFont();
        dataFont.setFontName("微软雅黑");
        dataFont.setFontHeightInPoints((short) 10);
        dataFont.setColor(IndexedColors.BLACK.index);
//
        XSSFCellStyle dataStyle = wb.createCellStyle();
        dataStyle.setWrapText(true);
//      //  dataStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
//       // dataStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
//        dataStyle.setFont(dataFont);
        setBorder(dataStyle, BorderStyle.THIN, new XSSFColor(new Color(0, 0, 0)));
        for (List<Object> rowData : rows) {
            Row dataRow = sheet.createRow(rowIndex);
            dataRow.setHeightInPoints(25);
            colIndex = 0;
            for (Object cellData : rowData) {
                Cell cell = dataRow.createCell(colIndex);
                if (cellData != null) {
                    cell.setCellValue(cellData.toString());
                    cell.setCellStyle(dataStyle);
                } else {
                    cell.setCellValue("");
                }
                colIndex++;
            }
            rowIndex++;
        }
        return rowIndex;
    }

    /**
     * 设置边框
     *
     * @param style
     * @param border
     * @param color
     */
    private static void setBorder(XSSFCellStyle style, BorderStyle border, XSSFColor color) {
        style.setBorderTop(border);
        style.setBorderLeft(border);
        style.setBorderRight(border);
        style.setBorderBottom(border);
        style.setBorderColor(XSSFCellBorder.BorderSide.TOP, color);
        style.setBorderColor(XSSFCellBorder.BorderSide.LEFT, color);
        style.setBorderColor(XSSFCellBorder.BorderSide.RIGHT, color);
        style.setBorderColor(XSSFCellBorder.BorderSide.BOTTOM, color);
    }

    //校验表头的方法
    private static void checkRow1(Map map) {
        if (!map.containsKey("用例所属")) {
            throw new TemplateException("-1", "'用例所属'列不存在，请检查并更新导入文件！");
        }
        if (!map.containsKey("用例名称")) {
            throw new TemplateException("-1", "'用例名称'列不存在，请检查并更新导入文件！");
        }
        if (!map.containsKey("优先级")) {
            throw new TemplateException("-1", "'优先级'列不存在，请检查并更新导入文件！");
        }
        if (!map.containsKey("用例类型")) {
            throw new TemplateException("-1", "'用例类型'列不存在，请检查并更新导入文件！");
        }
        if (!map.containsKey("用例标签")) {
            throw new TemplateException("-1", "'用例标签'列不存在，请检查并更新导入文件！");
        }
        if (!map.containsKey("前置条件")) {
            throw new TemplateException("-1", "'前置条件'列不存在，请检查并更新导入文件！");
        }
        if (!map.containsKey("步骤描述")) {
            throw new TemplateException("-1", "'步骤描述'列不存在，请检查并更新导入文件！");
        }
        if (!map.containsKey("预期结果")) {
            throw new TemplateException("-1", "'预期结果'列不存在，请检查并更新导入文件！");
        }
        if (!map.containsKey("备注")) {
            throw new TemplateException("-1", "'备注'列不存在，请检查并更新导入文件！");
        }
    }

}
