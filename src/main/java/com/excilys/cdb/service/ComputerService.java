package com.excilys.cdb.service;

import com.excilys.cdb.dto.DTOComputer;
import com.excilys.cdb.mapper.ComputerMapper;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.persistence.DAOComputer;

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
		pageComp.init(getCount());
	}
	
	public Page<Computer> selectAll() {
		return pageComp.filled(dao.findBatch(pageComp.getEntitiesPerPage(), pageComp.getIdxPage()));
	}
	
	public Page<Computer> getPageComp() {
		return pageComp;
	}
	
	public double getCount() {
		return dao.getCount();
	}
	
	public void nextPage() {
		pageComp.nextPage();
	}
	
	public void previousPage() {
		pageComp.previousPage();
	}
	
	public void selectPage(int index) {
		pageComp.selectPage(index);
	}
	
	public Computer selectById(Long id) {
		return dao.findById(id);
	}
	
	public void create(DTOComputer computerDTO) {
		dao.create(ComputerMapper.getInstance().mapFromValidDTO(computerDTO));
	}
	
	public void create(Computer comp) {
		dao.create(comp);
	}
	
	public void update(Computer comp) {
		dao.update(comp);
	}
	
	public void update(DTOComputer computerDTO) {
		dao.update(ComputerMapper.getInstance().mapFromValidDTO(computerDTO));
	}
	
	public void delete(Computer comp) {
		dao.delete(comp);
	}
}
