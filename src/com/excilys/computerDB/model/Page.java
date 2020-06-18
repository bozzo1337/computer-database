package com.excilys.computerDB.model;

import java.util.ArrayList;
import java.util.List;

import com.excilys.computerDB.persistence.DAO;

public class Page<T> {

	private DAO<T> dao;
	private List<T> entities;
	private int entitiesPerPage;
	private int idxCurrentPage;
	private int idxMaxPage;
	
	public Page() {
		idxCurrentPage = 0;
		entitiesPerPage = 20;
		entities = new ArrayList<T>();
	}
	
	public void fill() {
		entities = dao.findBatch(entitiesPerPage, idxCurrentPage);
	}
	
	public void previousPage() {
		if (idxCurrentPage != 0) {
			idxCurrentPage--;
		}
	}
	
	public void nextPage() {
		if (idxCurrentPage != idxMaxPage) {
			idxCurrentPage++;
		}
	}
	
	public void init() {
		idxMaxPage = (int) (Math.ceil(dao.getCount() / entitiesPerPage) - 1);
		idxCurrentPage = 0;
	}
	
	public int getIdxPage() {
		return idxCurrentPage;
	}
	
	public int getIdxMaxPage() {
		return idxMaxPage;
	}
	
	public int getEntitiesPerPage() {
		return entitiesPerPage;
	}

	public void setEntitiesPerPage(int entitiesPerPage) {
		this.entitiesPerPage = entitiesPerPage;
	}
	
	@Override
	public String toString() {
		StringBuilder output = new StringBuilder("Page " + this.idxCurrentPage +
				"/" + this.idxMaxPage + "%n" + dao.getHeader());
		for (T entity : entities) {
			output.append(entity.toString() + "%n");
		}
		return output.toString();
	}
}
