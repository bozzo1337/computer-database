package com.excilys.cdb.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.cdb.config.ApplicationContextServlet;
import com.excilys.cdb.dto.DTOComputer;
import com.excilys.cdb.exception.IncorrectDiscDateException;
import com.excilys.cdb.exception.IncorrectIDException;
import com.excilys.cdb.exception.IncorrectIntroDateException;
import com.excilys.cdb.exception.IncorrectNameException;
import com.excilys.cdb.exception.IncorrectTemporalityException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;
import com.excilys.cdb.validation.Validator;

/**
 * Servlet implementation class CreateServlet
 */
@WebServlet(name = "createServlet", urlPatterns = "/create")
public class CreateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CompanyService cas;
	private ComputerService cs;
	private boolean firstCallCreate = true;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateServlet() {
        super();	
        this.cas = ApplicationContextServlet.getCompanyService();
        this.cs = ApplicationContextServlet.getComputerService();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Company> listCompanies = cas.selectAll().getEntities();
		firstCallCreate = Boolean.parseBoolean(request.getParameter("firstCallCreate"));
		request.setAttribute("firstCallCreate", firstCallCreate);
		request.setAttribute("listCompanies", listCompanies);
		RequestDispatcher rd = request.getRequestDispatcher("/create.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		firstCallCreate = false;
		boolean creationOK = false;
		boolean validDTO = true;
		DTOComputer computerDTO = new DTOComputer.Builder()
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
			cs.create(computerDTO);
			creationOK = true;
		}
		request.setAttribute("creationOK", creationOK);
		request.setAttribute("errMessage", errMessage);
		doGet(request, response);
	}

}
