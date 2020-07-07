package com.excilys.cdb.service;

import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.dao.DAOComputer;
import com.excilys.cdb.dao.mapper.ComputerMapper;
import com.excilys.cdb.dto.DTOComputer;
import com.excilys.cdb.exception.NullMappingSourceException;
import com.excilys.cdb.exception.PersistenceException;
import com.excilys.cdb.exception.UnknownMappingSourceException;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Page;

public class ComputerService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ComputerService.class);
	private Page<DTOComputer> pageComp;
	private DAOComputer dao;
	private String pageHeader = "ID | Name | Intro | Disc | CompID\n";
	
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
		} catch (PersistenceException | NullMappingSourceException | UnknownMappingSourceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Page<DTOComputer> getPageComp() {
		return pageComp;
	}
	
	public Page<DTOComputer> searchComp(String search) {
		return pageComp.filled(dao.searchBatch(search, pageComp.getEntitiesPerPage(), pageComp.getIdxPage())
				.stream()
				.map(c -> dao.mapToDTO(c))
				.collect(Collectors.toList()));
	}
	
	public Page<DTOComputer> orderComp(String orderType) {
		return pageComp.filled(dao.orderBatch(orderType, pageComp.getEntitiesPerPage(), pageComp.getIdxPage())
				.stream()
				.map(c -> dao.mapToDTO(c))
				.collect(Collectors.toList()));
	}
	
	public Page<DTOComputer> orderedSearchComp(String search, String orderType) {
		return pageComp.filled(dao.orderedSearch(search, orderType, pageComp.getEntitiesPerPage(), pageComp.getIdxPage())
				.stream()
				.map(c -> dao.mapToDTO(c))
				.collect(Collectors.toList()));
	}
	
	public double getCount() {
		return dao.count();
	}
	
	public double getSearchCount(String search) {
		return dao.searchCount(search);
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
		return dao.mapToDTO(dao.findById(id));
	}
	
	public void create(DTOComputer computerDTO) {
		try {
			Computer computer = ComputerMapper.getInstance().map(computerDTO);
			dao.create(computer);
		} catch (NullMappingSourceException | UnknownMappingSourceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public void update(DTOComputer computerDTO) {
		try {
			Computer computer = ComputerMapper.getInstance().map(computerDTO); 
			dao.update(computer);
		} catch (NullMappingSourceException | UnknownMappingSourceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void delete(DTOComputer computerDTO) {
		dao.delete(Long.valueOf(computerDTO.getId()));
	}
}
