package com.excilys.cdb.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.excilys.cdb.dao.DAOComputer;
import com.excilys.cdb.dao.mapper.ComputerMapper;
import com.excilys.cdb.dto.DTOComputer;
import com.excilys.cdb.exception.NullMappingSourceException;
import com.excilys.cdb.exception.PersistenceException;
import com.excilys.cdb.exception.UnknownMappingSourceException;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Page;

@Service
public class ComputerService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ComputerService.class);
	private Page<DTOComputer> pageCompDTO;
	private Page<Computer> pageComp;
	private DAOComputer dao;
	private final String pageHeader = "ID | Name | Intro | Disc | CompID\n";
	
	@Autowired
	public ComputerService(DAOComputer dao) {
		this.dao = dao;
		pageComp = new Page<Computer>(pageHeader);
		pageCompDTO = new Page<DTOComputer>(pageHeader);
		LOGGER.info("ComputerService instantiated");
	}
	
	public void resetPages(String search) {
		double count = -1;
		if (search != null && !search.trim().isEmpty()) {
			count = getSearchCount(search);
		} else {
			count = getCount();
		}
		pageComp.init(count);
		pageCompDTO.init(count);
	}
	
	public Page<DTOComputer> selectAll() {
		pageCompDTO.getEntities().clear();
		try {
			dao.findBatch(pageComp);
			pageComp.getEntities()
			.stream()
			.forEach(c -> pageCompDTO.getEntities().add(dao.mapToDTO(c)));
		} catch (PersistenceException e) {
			LOGGER.error("Error during selectAll in service", e);
		}
		return pageCompDTO;
	}
	
	public Page<DTOComputer> getPageComp() {
		return pageCompDTO;
	}
	
	public Page<DTOComputer> searchComp(String search) {
		pageCompDTO.getEntities().clear();
		pageComp.setSearch(search);
		try {
			dao.searchBatch(pageComp);
			pageComp.getEntities()
			.stream()
			.forEach(c -> pageCompDTO.getEntities().add(dao.mapToDTO(c)));
		} catch (PersistenceException e) {
			LOGGER.error("Error during search in service", e);
		}
		return pageCompDTO;
	}
	
	public Page<DTOComputer> orderComp(String orderType) {
		pageCompDTO.getEntities().clear();
		pageComp.setOrder(orderType);
		try {
			dao.orderBatch(pageComp);
			pageComp.getEntities()
			.stream()
			.forEach(c -> pageCompDTO.getEntities().add(dao.mapToDTO(c)));
		} catch (PersistenceException e) {
			LOGGER.error("Error during order in service", e);
		}
		return pageCompDTO;
	}
	
	public Page<DTOComputer> orderedSearchComp(String search, String orderType) {
		pageCompDTO.getEntities().clear();
		pageComp.setOrder(orderType);
		pageComp.setSearch(search);
		try {
			dao.orderedSearch(pageComp);
			pageComp.getEntities()
			.stream()
			.forEach(c -> pageCompDTO.getEntities().add(dao.mapToDTO(c)));
		} catch (PersistenceException e) {
			LOGGER.error("Error during search order in service", e);
		}
		return pageCompDTO;
	}
	
	public double getCount() {
		double count = -1;
		try {
			count = dao.count();
		} catch (PersistenceException e) {
			LOGGER.error("Error during default count in service", e);
		}
		return count;
	}
	
	public double getSearchCount(String search) {
		double count = -1;
		try {
			count = dao.searchCount(search);
		} catch (PersistenceException e) {
			LOGGER.error("Error during search count in service", e);
		}
		return count;
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
	
	public DTOComputer selectById(Long id) {
		DTOComputer computerDTO = new DTOComputer.Builder().build();
		try {
			computerDTO = dao.mapToDTO(dao.findById(id));
		} catch (PersistenceException e) {
			LOGGER.error("Error during selectById in service", e);
		}
		return computerDTO;
	}
	
	public void create(DTOComputer computerDTO) {
		try {
			Computer computer = ComputerMapper.map(computerDTO);
			dao.create(computer);
		} catch (NullMappingSourceException | UnknownMappingSourceException | PersistenceException e) {
			LOGGER.error("Error during create in service", e);
		}	
	}
	
	public void update(DTOComputer computerDTO) {
		try {
			Computer computer = ComputerMapper.map(computerDTO); 
			dao.update(computer);
		} catch (NullMappingSourceException | UnknownMappingSourceException | PersistenceException e) {
			LOGGER.error("Error during update in service", e);
		}
	}
	
	public void delete(DTOComputer computerDTO) {
		try {
			dao.delete(Long.valueOf(computerDTO.getId()));
		} catch (NumberFormatException | PersistenceException e) {
			LOGGER.error("Error during delete in service", e);
		}
	}
}
