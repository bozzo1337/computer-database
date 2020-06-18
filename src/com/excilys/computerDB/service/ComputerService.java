package com.excilys.computerDB.service;

import java.util.List;

import com.excilys.computerDB.model.Computer;
import com.excilys.computerDB.model.Page;
import com.excilys.computerDB.persistence.DAOComputer;

public class ComputerService {
	
	private static ComputerService singleInstance = null;
	private Page<Computer> pageComp;
	private DAOComputer dao;
	
	private ComputerService() {
		dao = DAOComputer.getInstance();
		pageComp = new Page<Computer>();
	}
	
	public static ComputerService getInstance() {
		if (singleInstance == null) {
			singleInstance = new ComputerService();
		}
		return singleInstance;
	}
	
	public List<Computer> selectAll() {
		return dao.findBatch(pageComp.getEntitiesPerPage(), pageComp.getIdxPage());
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
