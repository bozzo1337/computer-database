package com.excilys.cdb.controller.attributes;

import java.util.List;

import com.excilys.cdb.dto.DTOCompany;

public class AddAttributes {

	private boolean firstCallCreate;
	private boolean creationOK;
	private List<DTOCompany> listCompanies;
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

	public List<DTOCompany> getListCompanies() {
		return listCompanies;
	}

	public void setListCompanies(List<DTOCompany> listCompanies) {
		this.listCompanies = listCompanies;
	}

	public String getErrMessage() {
		return errMessage;
	}

	public void setErrMessage(String errMessage) {
		this.errMessage = errMessage;
	}
}
