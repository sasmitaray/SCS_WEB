package com.sales.crm.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.sales.crm.model.Address;
import com.sales.crm.model.Customer;

public class CustomerXLSProcessor {
	
	private static Logger logger = Logger.getLogger(CustomerXLSProcessor.class);


	public static List<Customer> processCustomerXLS(InputStream is, Map<Integer, List<String>> errors, int tenantID) throws Exception{
		List<Customer> customers = new ArrayList<Customer>();
		try {
			// Finds the workbook instance for XLSX file
			XSSFWorkbook myWorkBook = new XSSFWorkbook(is);
			// Return first sheet from the XLSX workbook
			XSSFSheet mySheet = myWorkBook.getSheetAt(0);
			// Get iterator to all the rows in current sheet
			Iterator<Row> rowIterator = mySheet.iterator();
			row:while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				if(row.getRowNum() < 2){
					continue row;
				}
				Customer customer = new Customer();
				Address mainAddress = new Address();
				mainAddress.setAddrressType(1);
				Address billingAddress = new Address();
				billingAddress.setAddrressType(2);
				List<Address> addresses = new ArrayList<Address>();
				List<String> errorMsgs = new ArrayList<String>();
				boolean hasErrors = false;
				for (int i = 1; i < 27; i++) {
					Cell cell = row.getCell(i);
					switch (i) {
					case 1:
						if (cell == null || cell.getStringCellValue().trim().isEmpty()) {
							errorMsgs.add("Customer name is required.");
							hasErrors = true;
						} else {
							customer.setName(cell.getStringCellValue());
						}
						break;
					case 2:
						if(cell != null){
							customer.setDescription(cell.getStringCellValue());
						}
						break;
					case 3:
						if (cell == null || cell.getStringCellValue().trim().isEmpty()) {
							errorMsgs.add("Main Address Contact Person is required.");
							hasErrors = true;
						} else {
							mainAddress.setContactPerson(cell.getStringCellValue());
						}
						break;
					case 4:
						if (cell == null || cell.getStringCellValue().trim().isEmpty()) {
							errorMsgs.add("Main Address, Address Line1 is required.");
							hasErrors = true;
						} else {
							mainAddress.setAddressLine1(cell.getStringCellValue());
						}
						break;
					case 5:
						if(cell != null){
							mainAddress.setAddressLine2(cell.getStringCellValue());
						}
						break;
					case 6:
						if(cell != null){
							mainAddress.setStreet(cell.getStringCellValue());
						}
						break;
					case 7:
						if (cell == null || cell.getStringCellValue().trim().isEmpty()) {
							errorMsgs.add("Main Address City is required.");
							hasErrors = true;
						} else {
							mainAddress.setCity(cell.getStringCellValue());
						}
						break;
					case 8:
						if (cell == null || cell.getStringCellValue().trim().isEmpty()) {
							errorMsgs.add("Main Address State is required.");
							hasErrors = true;
						} else {
							mainAddress.setState(cell.getStringCellValue());
						}
						break;
					case 9:
						if (cell == null || cell.getStringCellValue().trim().isEmpty()) {
							errorMsgs.add("Main Address Country is required.");
							hasErrors = true;
						} else {
							mainAddress.setCountry(cell.getStringCellValue());
						}
						break;
					case 10:
						if (cell == null) {
							errorMsgs.add("Main Address Postal Code is required.");
							hasErrors = true;
						} else {
							cell.setCellType(Cell.CELL_TYPE_STRING);
							mainAddress.setPostalCode(cell.getStringCellValue());
						}
						break;
					case 11:
						if(cell != null){
							cell.setCellType(Cell.CELL_TYPE_STRING);
							mainAddress.setPhoneNumber(cell.getStringCellValue());
						}
						break;
					case 12:
						if (cell == null) {
							errorMsgs.add("Main Address Primary Mobile Number is required.");
							hasErrors = true;
						} else {
							cell.setCellType(Cell.CELL_TYPE_STRING);
							mainAddress.setMobileNumberPrimary(cell.getStringCellValue());
						}
						break;
					case 13:
						if(cell != null){
							cell.setCellType(Cell.CELL_TYPE_STRING);
							mainAddress.setMobileNumberSecondary(cell.getStringCellValue());
						}
						break;
					case 14:
						if(cell != null){
							billingAddress.setContactPerson(cell.getStringCellValue());
						}
						break;
					case 15:
						if(cell != null){
							billingAddress.setAddressLine1(cell.getStringCellValue());
						}
						break;
					case 16:
						if(cell != null){
							billingAddress.setAddressLine2(cell.getStringCellValue());
						}
						break;
					case 17:
						if(cell != null){
							billingAddress.setStreet(cell.getStringCellValue());
						}
						break;
					case 28:
						if(cell != null){
							billingAddress.setCity(cell.getStringCellValue());
						}
						break;
					case 19:
						if(cell != null){
							billingAddress.setState(cell.getStringCellValue());
						}
						break;
					case 20:
						if(cell != null){
							billingAddress.setCountry(cell.getStringCellValue());
						}
						break;
					case 21:
						if(cell != null){
							cell.setCellType(Cell.CELL_TYPE_STRING);
							billingAddress.setPostalCode(cell.getStringCellValue());
						}
						break;
					case 22:
						if(cell != null){
							cell.setCellType(Cell.CELL_TYPE_STRING);
							billingAddress.setPhoneNumber(cell.getStringCellValue());
						}
						break;
					case 23:
						if(cell != null){
							cell.setCellType(Cell.CELL_TYPE_STRING);
							billingAddress.setMobileNumberPrimary(cell.getStringCellValue());
						}
						break;
					case 24:
						if(cell != null){
							cell.setCellType(Cell.CELL_TYPE_STRING);
							billingAddress.setMobileNumberSecondary(cell.getStringCellValue());
						}
						break;
					case 25:
						if(cell != null){
							customer.setBeatName(cell.getStringCellValue());
						}
						break;
					}

				}
				if (!hasErrors) {
					mainAddress.setDateCreated(new Date());
					addresses.add(mainAddress);
					billingAddress.setDateCreated(new Date());
					addresses.add(billingAddress);
					customer.setAddress(addresses);
					customer.setDateCreated(new Date());
					customer.setTenantID(tenantID);
					customers.add(customer);
				} else {
					errors.put(row.getRowNum()+1, errorMsgs);
				}

			}
		} catch (Exception exception) {
			logger.error("Processing customer xls is not successfuul.", exception);
			throw exception;
		}

		return customers;

	}

	/**
	public static void main(String a[]) {
		Map<Integer, List<String>> errors = new HashMap<Integer, List<String>>();
		try {
			processCustomerXLS(new File("/Users/npattana/Project/SalesCRM/customers.xlsx"), errors);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	**/

}
