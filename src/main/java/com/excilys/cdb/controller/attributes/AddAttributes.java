package com.excilys.cdb.controller.attributes;

import java.util.List;

import com.excilys.cdb.model.Company;

public class AddAttributes {

	private boolean firstCallCreate;
	private boolean creationOK;
	private List<Company> listCompanies;
	private String errMessage;
	
	public boolean isFirstCallCreate() {
		return firstCallCreate;
	}
	public void setFirstCallCreate(boolean firstCallCreate) {
		this.firstCallCreate = firstCallCreate;
	}
	public boolean isCreationOK() {
		return creationOK;
	}
	public void setCreationOK(boolean creationOK) {
		this.creationOK = creationOK;
	}
	public List<Company> getListCompanies() {
		return listCompanies;
	}
	public void setListCompanies(List<Company> listCompanies) {
		this.listCompanies = listCompanies;
	}
	public String getErrMessage() {
		return errMessage;
	}
	public void setErrMessage(String errMessage) {
		this.errMessage = errMessage;
	}
}
