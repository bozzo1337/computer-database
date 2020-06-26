package com.excilys.cdb.servlet;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;
import com.excilys.cdb.ui.Validator;

/**
 * Servlet implementation class CreateServlet
 */
@WebServlet(name = "createServlet", urlPatterns = "/create")
public class CreateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CompanyService cas = CompanyService.getInstance();
	private ComputerService cs = ComputerService.getInstance();
	private Computer compToCreate;
	private Validator validator = new Validator();
	private boolean creationOK = false;
	private boolean firstCallCreate = true;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Company> listCompanies = cas.selectAll();
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
		String name = request.getParameter("computerName");
		String intro = request.getParameter("introduced");
		String disc = request.getParameter("discontinued");
		String compId = request.getParameter("companyId");
		boolean nameOK = false;
		boolean temporalityOK;
		LocalDate introduced = null;
		LocalDate discontinued = null;
		Long companyId = null;
		if (name != null) {
			nameOK = validator.validateName(name);
		}
		if (intro != null) {
			introduced = validator.validateDate(intro);
		}
		if (disc != null) {
			discontinued = validator.validateDate(disc);
		}
		if (!compId.equals("0") ) {
			companyId = validator.validateID(compId);
		}
		temporalityOK = validator.validateTemporality(introduced, discontinued);
		if (nameOK && temporalityOK) {
			compToCreate = new Computer(name.trim(), introduced, discontinued, companyId, cas.selectById(companyId));
			cs.create(compToCreate);
			creationOK = true;
		} else {
			creationOK = false;
		}
		request.setAttribute("creationOK", creationOK);
		doGet(request, response);
	}

}
