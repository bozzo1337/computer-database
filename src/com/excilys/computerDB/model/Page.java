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
	private int idxMaxPage;
	
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
		if (idxPage != idxMaxPage) {
			idxPage++;
		}
	}
	
	public void init() {
		ResultSet rs = QueryExecutor.getInstance().computerCount();
		double nbComps = 0;
		try {
			rs.next();
			nbComps = rs.getDouble("compcount");
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		idxMaxPage = (int) (Math.ceil(nbComps / compPerPage) - 1);
		idxPage = 0;
	}
	
	public int getIdxPage() {
		return idxPage;
	}
	
	public int getIdxMaxPage() {
		return idxMaxPage;
	}
	
	@Override
	public String toString() {
		String output = "ID | Name | LocalDate intro | LocalDate disc | Comp ID%n";
		for (Computer comp : computers) {
			output += comp.toString() + "%n";
		}
		return output;
	}
}
