package com.app.controller;

import com.app.model.Address;
import com.app.model.Employee;
import com.app.pojo.Response;
import com.app.repository.EmployeeRepository;
import com.app.utils.ExcelExporter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class TestController {



	@GetMapping("/foo")
	public String foo() {
		return "hello";
	}

//	@PostMapping("/save")
//	public Response<Employee> save(@RequestBody Employee employee) {
//	 Employee emp=	repo.save(employee);
//		Set<Address> addr = emp.getAddress();
//		emp.setAddress(addr);
//		return new Response<Employee>(HttpStatus.OK, emp, "success");
//	}
//
//	@PostMapping("/update")
//	public Response<Employee> update(@RequestBody Employee employee) {
//		Employee emp=	repo.save(employee);
//		Set<Address> addr = emp.getAddress();
//		emp.setAddress(addr);
//		return new Response<Employee>(HttpStatus.OK, emp, "success");
//	}

	//reading excelfile
	@RequestMapping(value=("/upload"),headers=("content-type=multipart/*"),method=RequestMethod.POST)
	public ResponseEntity<ByteArrayResource> upload(@RequestParam MultipartFile file)throws Exception {
		try {
			ExcelExporter excelExporter = new ExcelExporter(file);
			ByteArrayOutputStream stream = excelExporter.export();
			DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
			String currentDateTime = dateFormatter.format(new Date());
			HttpHeaders header = new HttpHeaders();
			header.setContentType(new MediaType("application", "force-download"));
			header.set(HttpHeaders.CONTENT_DISPOSITION, currentDateTime+".xlsx");
			byte outputStream[]=stream.toByteArray();
			return new ResponseEntity<>(new ByteArrayResource(stream.toByteArray()), header, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@GetMapping(value="/downloadTemplate")
	public ResponseEntity<ByteArrayResource> downloadTemplate() throws Exception {
		try {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			XSSFWorkbook workbook = createWorkBook(); // creates the workbook
			HttpHeaders header = new HttpHeaders();
			header.setContentType(new MediaType("application", "force-download"));
			header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ProductTemplate.xlsx");
			workbook.write(stream);
			workbook.close();
			return new ResponseEntity<>(new ByteArrayResource(stream.toByteArray()),header, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private XSSFWorkbook createWorkBook() {
		XSSFWorkbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Employee");
		Row row = sheet.createRow(0);
		row.createCell(0).setCellValue("Name");
		row.createCell(1).setCellValue("Email");
		row.createCell(2).setCellValue("Phone");
		row.createCell(3).setCellValue("Address");
		row.createCell(4).setCellValue("City");
		row.createCell(5).setCellValue("State");
		row.createCell(6).setCellValue("Zip");
		row.createCell(7).setCellValue("Country");
		return workbook;
	}


}
