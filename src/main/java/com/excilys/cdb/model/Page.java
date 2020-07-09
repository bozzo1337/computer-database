package com.excilys.cdb.model;

import java.util.ArrayList;
import java.util.List;

public class Page<T> {

	private List<T> entities;
	private double nbEntities;
	private int entitiesPerPage;
	private int idxCurrentPage;
	private int idxMaxPage;
	private String header;
	private String search;
	private String order;

	public Page(String header) {
		idxCurrentPage = 0;
		entitiesPerPage = 10;
		entities = new ArrayList<T>();
		this.header = header;
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
	
	public void selectPage(int index) {
		if (index >= 0 && index <= idxMaxPage) {
			idxCurrentPage = index;
		}
	}
	
	public void init(double count) {
		nbEntities = count;
		if (count <= 0) {
			idxMaxPage = 0;
		} else {
			idxMaxPage = (nbEntities % entitiesPerPage == 0) ? (int) (nbEntities / entitiesPerPage) - 1 : (int) (Math.ceil(nbEntities / entitiesPerPage) - 1);
		}
		idxCurrentPage = 0;
	}
	
	public List<T> getEntities() {
		return entities;
	}
	
	public int getIdxCurrentPage() {
		return idxCurrentPage;
	}
	
	public void setIdxCurrentPage(int index) {
		this.idxCurrentPage = index;
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
				"/" + (this.idxMaxPage + 1) + "\n" + header);
		for (T entity : entities) {
			output.append(entity.toString() + "\n");
		}
		return output.toString();
	}

	public double getNbEntities() {
		return nbEntities;
	}

	public void setNbEntities(double nbEntities) {
		this.nbEntities = nbEntities;
	}
	
	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}
}
