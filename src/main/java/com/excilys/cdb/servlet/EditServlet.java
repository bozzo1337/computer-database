package com.excilys.cdb.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.cdb.config.ApplicationContextServlet;
import com.excilys.cdb.dto.DTOComputer;
import com.excilys.cdb.exception.validation.IncorrectDiscDateException;
import com.excilys.cdb.exception.validation.IncorrectIDException;
import com.excilys.cdb.exception.validation.IncorrectIntroDateException;
import com.excilys.cdb.exception.validation.IncorrectNameException;
import com.excilys.cdb.exception.validation.IncorrectTemporalityException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;
import com.excilys.cdb.validation.Validator;

public class EditServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CompanyService cas;
	private ComputerService cs;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditServlet() {
        super();
        this.cas = ApplicationContextServlet.getCompanyService();
        this.cs = ApplicationContextServlet.getComputerService();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Company> listCompanies = cas.selectAll().getEntities();
		String compId = request.getParameter("computerId");
		Boolean firstCall = Boolean.valueOf(request.getParameter("firstCall"));
		Long computerId = null;
		DTOComputer computer = null;
		if (compId != null) {
			try {
				computerId = Long.parseLong(compId);
			} catch (NumberFormatException e) {
				// TODO: handle exception
			}
		}
		if (computerId != null) {
			computer = cs.selectById(computerId);
		}
		request.setAttribute("firstCall", firstCall);
		request.setAttribute("computer", computer);
		request.setAttribute("listCompanies", listCompanies);
		RequestDispatcher rd = request.getRequestDispatcher("/edit.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		boolean updateOK = false;
		boolean validDTO = true;
		DTOComputer computerDTO = new DTOComputer.Builder()
				.withId(request.getParameter("computerId"))
				.withName(request.getParameter("computerNameInput"))
				.withIntroDate(request.getParameter("introduced"))
				.withDiscDate(request.getParameter("discontinued"))
				.withCompanyId(request.getParameter("companyId"))
				.build();
		String errMessage = null;
		try {
			Validator.validateDTO(computerDTO);
		} catch (IncorrectNameException | IncorrectIntroDateException | IncorrectDiscDateException |
				IncorrectIDException | IncorrectTemporalityException e) {
			validDTO = false;
			errMessage = e.getMessage();
		}
		if (validDTO) {
			cs.update(computerDTO);
			updateOK = true;
		}
		request.setAttribute("updateOK", updateOK);
		request.setAttribute("errMessage", errMessage);
		doGet(request, response);
	}

}
