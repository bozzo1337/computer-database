package com.excilys.cdb.service;

import java.util.stream.Collectors;

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
	private Page<DTOComputer> pageComp;
	private DAOComputer dao;
	private final String pageHeader = "ID | Name | Intro | Disc | CompID\n";
	
	@Autowired
	public ComputerService(DAOComputer dao) {
		this.dao = dao;
		pageComp = new Page<DTOComputer>(pageHeader);
		LOGGER.info("ComputerService instantiated");
	}
	
	public void resetPages(String search) {
		if (search != null && !search.trim().isEmpty()) {
			pageComp.init(getSearchCount(search));
		} else {
			pageComp.init(getCount());
		}
	}
	
	public Page<DTOComputer> selectAll() {
		try {
			return pageComp.filled(dao.findBatch(pageComp.getEntitiesPerPage(), pageComp.getIdxPage())
					.stream()
					.map(c -> dao.mapToDTO(c))
					.collect(Collectors.toList()));
		} catch (PersistenceException e) {
			LOGGER.error("Error during selectAll in service", e);
		}
		return pageComp;
	}
	
	public Page<DTOComputer> getPageComp() {
		return pageComp;
	}
	
	public Page<DTOComputer> searchComp(String search) {
		try {
			return pageComp.filled(dao.searchBatch(search, pageComp.getEntitiesPerPage(), pageComp.getIdxPage())
					.stream()
					.map(c -> dao.mapToDTO(c))
					.collect(Collectors.toList()));
		} catch (PersistenceException e) {
			LOGGER.error("Error during selectAll in service", e);
		}
		return pageComp;
	}
	
	public Page<DTOComputer> orderComp(String orderType) {
		try {
			return pageComp.filled(dao.orderBatch(orderType, pageComp.getEntitiesPerPage(), pageComp.getIdxPage())
					.stream()
					.map(c -> dao.mapToDTO(c))
					.collect(Collectors.toList()));
		} catch (PersistenceException e) {
			LOGGER.error("Error during selectAll in service", e);
		}
		return pageComp;
	}
	
	public Page<DTOComputer> orderedSearchComp(String search, String orderType) {
		try {
			return pageComp.filled(dao.orderedSearch(search, orderType, pageComp.getEntitiesPerPage(), pageComp.getIdxPage())
					.stream()
					.map(c -> dao.mapToDTO(c))
					.collect(Collectors.toList()));
		} catch (PersistenceException e) {
			LOGGER.error("Error during selectAll in service", e);
		}
		return pageComp;
	}
	
	public double getCount() {
		try {
			return dao.count();
		} catch (PersistenceException e) {
			LOGGER.error("Error during selectAll in service", e);
		}
		return 0;
	}
	
	public double getSearchCount(String search) {
		try {
			return dao.searchCount(search);
		} catch (PersistenceException e) {
			LOGGER.error("Error during selectAll in service", e);
		}
		return 0;
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
		try {
			return dao.mapToDTO(dao.findById(id));
		} catch (PersistenceException e) {
			LOGGER.error("Error during selectAll in service", e);
		}
		return null;
	}
	
	public void create(DTOComputer computerDTO) {
		try {
			Computer computer = ComputerMapper.map(computerDTO);
			dao.create(computer);
		} catch (NullMappingSourceException | UnknownMappingSourceException | PersistenceException e) {
			LOGGER.error("Error during selectAll in service", e);
		}	
	}
	
	public void update(DTOComputer computerDTO) {
		try {
			Computer computer = ComputerMapper.map(computerDTO); 
			dao.update(computer);
		} catch (NullMappingSourceException | UnknownMappingSourceException | PersistenceException e) {
			LOGGER.error("Error during selectAll in service", e);
		}
	}
	
	public void delete(DTOComputer computerDTO) {
		try {
			dao.delete(Long.valueOf(computerDTO.getId()));
		} catch (NumberFormatException | PersistenceException e) {
			LOGGER.error("Error during selectAll in service", e);
		}
	}
}
