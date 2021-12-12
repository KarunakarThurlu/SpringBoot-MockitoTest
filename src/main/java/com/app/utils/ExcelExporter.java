package com.app.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookType;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class ExcelExporter {

    private final XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private final MultipartFile file;
    InputStream excelIs = null;

    private final XSSFWorkbook outputworkbook;
    private XSSFSheet outputsheet;

    public ExcelExporter(MultipartFile file)throws Exception{
        this.file = file;
        excelIs=file.getInputStream();
        workbook = (XSSFWorkbook) WorkbookFactory.create(excelIs);
        outputworkbook=new XSSFWorkbook();
    }

    public ByteArrayOutputStream export()throws Exception {
        try(ByteArrayOutputStream bos=new ByteArrayOutputStream()){
            //writeHeaderline();
            writeDataLines();
            outputworkbook.write(bos);
            outputworkbook.close();
            excelIs.close();
            workbook.close();
            byte[] bytes = bos.toByteArray();
            bos.close();
            return  bos;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private void writeDataLines() {
        outputsheet=outputworkbook.createSheet("Sheet1");
        Sheet sheet = workbook.getSheetAt(0);
        DataFormatter formatter = new DataFormatter();
        CellStyle cs= outputworkbook.createCellStyle();
        Font font=outputworkbook.createFont();
        font.setFontHeightInPoints((short)12);
        cs.setFont(font);
        cs.setAlignment(HorizontalAlignment.CENTER);

        sheet.forEach(row -> {
            Row r=outputsheet.createRow(row.getRowNum());
            r.setRowStyle(cs);
            row.forEach(cell -> {
                int cloumnIndex=cell.getColumnIndex();
                Cell c=r.createCell(cloumnIndex);
                if(cloumnIndex>=3 || cloumnIndex == 0) {
                    if (cell.getNumericCellValue() < 100)
                        c.setCellValue("");
                    else
                        c.setCellValue(cell.getNumericCellValue());
                }else{
                    if(cloumnIndex==1) {
                        DateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                        Date d=cell.getDateCellValue();
                        c.setCellValue(df.format(d));
                    }else {
                        //24 hour format
                        DateFormat tf = new SimpleDateFormat("HH:mm");
                        Date t=cell.getDateCellValue();
                        c.setCellValue(tf.format(t));
                    }
                }
            });
        });
    }
    private void writeHeaderline() {
        outputsheet=outputworkbook.createSheet("Sheet1");
        Row row=outputsheet.createRow(0);
        CellStyle cs= outputworkbook.createCellStyle();
        Font font=outputworkbook.createFont();
        font.setFontHeightInPoints((short)12);
        font.setBold(true);
        cs.setFont(font);
        cs.setAlignment(HorizontalAlignment.CENTER);
        cs.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        cs.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        row.setRowStyle(cs);
        for (int i = 0; i < 10; i++) {
            outputsheet.setColumnWidth(i,3000);
            row.createCell(i).setCellValue("Column" + (i + 1));
        }
    }
    public void createCell(int row, int col, Object value){
        Row r=outputsheet.getRow(row);
        Cell c=r.createCell(col);
        if(value instanceof String)
            c.setCellValue((String)value);
        if (value instanceof Integer)
            c.setCellValue((Integer)value);
        if (value instanceof Double)
            c.setCellValue((Double)value);
    }
}
