package com.excilys.computerDB.model;

import java.util.ArrayList;
import java.util.List;

public class Page<T> {

	private List<T> entities;
	private int entitiesPerPage;
	private int idxCurrentPage;
	private int idxMaxPage;
	private String header;
	
	public Page(String header) {
		idxCurrentPage = 0;
		entitiesPerPage = 20;
		entities = new ArrayList<T>();
		this.header = header;
	}
	
	public Page<T> filled(List<T> entities) {
		this.entities = entities;
		return this;
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
	
	public void init(double count) {
		idxMaxPage = (int) (Math.ceil(count / entitiesPerPage) - 1);
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
		StringBuilder output = new StringBuilder("Page n°" + (this.idxCurrentPage + 1) +
				"/" + (this.idxMaxPage + 1) + "%n" + header);
		for (T entity : entities) {
			output.append(entity.toString() + "%n");
		}
		return output.toString();
	}
}
