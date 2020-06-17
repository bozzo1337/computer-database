package com.excilys.computerDB.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.excilys.computerDB.mapper.ComputerMapper;
import com.excilys.computerDB.persistence.QueryExecutor;

public class Page {

	private static Page singleInstance = null;
	private int idxPage;
	private ArrayList<Computer> computers;
	private int compPerPage;
	private int maxPage;
	
	private Page() {
		idxPage = 0;
		compPerPage = 20;
		computers = new ArrayList<Computer>();
	}
	
	public static Page getInstance() {
		if (singleInstance == null)
			singleInstance = new Page();
		return singleInstance;
	}
	
	public void fill() {
		computers = ComputerMapper.getInstance().retrieveComputers(compPerPage, idxPage);
	}
	
	public void previousPage() {
		if (idxPage != 0) {
			idxPage--;
		}
	}
	
	public void nextPage() {
		if (idxPage != maxPage) {
			idxPage++;
		}
	}
	
	public void maxPageInit() {
		ResultSet rs = QueryExecutor.getInstance().computerCount();
		int nbComps = 0;
		try {
			rs.next();
			nbComps = rs.getInt("compcount");
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		maxPage = (int) Math.ceil(nbComps / compPerPage);
	}
	
	@Override
	public String toString() {
		String output = "";
		for (Computer comp : computers) {
			output += comp.toString();
		}
		return output;
	}
}
