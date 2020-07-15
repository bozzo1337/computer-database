package com.excilys.cdb.config;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.excilys.cdb.dto.DTOComputer;

@Component
@Scope("prototype")
public class WebAttributes {

	private int currentPage = 0;
	private int compCount;
	private int entitiesPerPage = 10;
	private int maxPage;
	private String search;
	private String order;
	private List<DTOComputer> listComp;
	private boolean firstCallCreate = true;
	
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getCompCount() {
		return compCount;
	}
	public void setCompCount(int compCount) {
		this.compCount = compCount;
	}
	public int getEntitiesPerPage() {
		return entitiesPerPage;
	}
	public void setEntitiesPerPage(int entitiesPerPage) {
		this.entitiesPerPage = entitiesPerPage;
	}
	public int getMaxPage() {
		return maxPage;
	}
	public void setMaxPage(int maxPage) {
		this.maxPage = maxPage;
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
	public List<DTOComputer> getListComp() {
		return listComp;
	}
	public void setListComp(List<DTOComputer> listComp) {
		this.listComp = listComp;
	}
	public boolean isFirstCallCreate() {
		return firstCallCreate;
	}
	public void setFirstCallCreate(boolean firstCallCreate) {
		this.firstCallCreate = firstCallCreate;
	}
}
