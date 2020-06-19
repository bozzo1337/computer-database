package com.excilys.computerDB.service;

import com.excilys.computerDB.model.Computer;
import com.excilys.computerDB.model.Page;
import com.excilys.computerDB.persistence.DAOComputer;

public class ComputerService {
	
	private static ComputerService singleInstance = null;
	private Page<Computer> pageComp;
	private DAOComputer dao;
	private String pageHeader = "ID | Name | Intro | Disc | CompID%n";
	
	private ComputerService() {
		dao = DAOComputer.getInstance();
		pageComp = new Page<Computer>(pageHeader);
	}
	
	public static ComputerService getInstance() {
		if (singleInstance == null) {
			singleInstance = new ComputerService();
		}
		return singleInstance;
	}
	
	public void resetPages() {
		pageComp.init(dao.getCount());
	}
	
	public Page<Computer> selectAll() {
		return pageComp.filled(dao.findBatch(pageComp.getEntitiesPerPage(), pageComp.getIdxPage()));
	}
	
	public Page<Computer> getNextPage() {
		pageComp.nextPage();
		return selectAll();
	}
	
	public Page<Computer> getPreviousPage() {
		pageComp.previousPage();
		return selectAll();
	}
	
	public Computer selectById(Long id) {
		return dao.findById(id);
	}
	
	public void create(Computer comp) {
		dao.create(comp);
	}
	
	public void update(Computer comp) {
		dao.update(comp);
	}
	
	public void delete(Computer comp) {
		dao.delete(comp);
	}
}
