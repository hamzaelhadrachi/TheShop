package com.zerotohero.admin.utils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;
import org.thymeleaf.util.StringUtils;

import com.zerotohero.entities.User;

@Component
public class UserCsvExporter {

	public void export(List<User> users, HttpServletResponse resp) throws IOException {
		
		// Building FileName
		DateFormat date = new SimpleDateFormat("yyy-MM-dd_HH-mm-ss");
		String timeStamp = date.format(new Date());
		String fileName = StringUtils.concat(new String[] {"users_", timeStamp, ".csv"});
		
		// Defining File Properties
		resp.setContentType("text/csv");
		
		String headerKey = "Content-Disposition";
		String headerValue = "attachement; filename="+fileName;
		resp.setHeader(headerKey, headerValue);
		
		// writing header and DB content into output file
		ICsvBeanWriter writer = new CsvBeanWriter(resp.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		
		String[] csvHeader = {"User ID","E-Mail","First Name","Last Name","Roles","Enabled"};
		String[] fieldMapping = {"id","email","firstName","lastName","roles","enabled"};
		
		writer.writeHeader(csvHeader);
		for(User u : users) {
			writer.write(u, fieldMapping);
		}
		
		// writer Closing
		writer.close();
	}

}
