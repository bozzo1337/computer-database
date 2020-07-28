package com.excilys.cdb.controller.attributes;

import java.util.List;

import com.excilys.cdb.dto.DTOCompany;
import com.excilys.cdb.dto.DTOComputer;

public class EditAttributes {

	private boolean firstCall;
	private boolean updateOK;
	private List<DTOCompany> listCompanies;
	private DTOComputer computer;
	private String errMessage;

	public boolean isFirstCall() {
		return firstCall;
	}

	public void setFirstCall(boolean firstCall) {
		this.firstCall = firstCall;
	}

	public boolean isUpdateOK() {
		return updateOK;
	}

	public void setUpdateOK(boolean updateOK) {
		this.updateOK = updateOK;
	}

	public List<DTOCompany> getListCompanies() {
		return listCompanies;
	}

	public void setListCompanies(List<DTOCompany> listCompanies) {
		this.listCompanies = listCompanies;
	}

	public DTOComputer getComputer() {
		return computer;
	}

	public void setComputer(DTOComputer computer) {
		this.computer = computer;
	}

	public String getErrMessage() {
		return errMessage;
	}

	public void setErrMessage(String errMessage) {
		this.errMessage = errMessage;
	}
}
