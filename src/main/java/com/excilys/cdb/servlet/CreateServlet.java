package com.excilys.cdb.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.cdb.dto.DTOComputer;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;
import com.excilys.cdb.ui.Validator;

import exception.IncorrectDiscDateException;
import exception.IncorrectIDException;
import exception.IncorrectIntroDateException;
import exception.IncorrectNameException;
import exception.IncorrectTemporalityException;

/**
 * Servlet implementation class CreateServlet
 */
@WebServlet(name = "createServlet", urlPatterns = "/create")
public class CreateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CompanyService cas = CompanyService.getInstance();
	private ComputerService cs = ComputerService.getInstance();
	private Validator validator = new Validator();
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
		boolean creationOK = false;
		boolean validDTO = true;
		String name = request.getParameter("computerNameInput");
		String intro = request.getParameter("introduced");
		String disc = request.getParameter("discontinued");
		String compId = request.getParameter("companyId");
		DTOComputer computerDTO = new DTOComputer(name, intro, disc, compId);
		try {
			validator.validateDTO(computerDTO);
		} catch (IncorrectNameException e) {
			validDTO = false;
		} catch (IncorrectIntroDateException e) {
			validDTO = false;
		} catch (IncorrectDiscDateException e) {
			validDTO = false;
		} catch (IncorrectIDException e) {
			validDTO = false;
		} catch (IncorrectTemporalityException e) {
			validDTO = false;
		}
		if (validDTO) {
			cs.create(computerDTO);
			creationOK = true;
		}
		request.setAttribute("creationOK", creationOK);
		doGet(request, response);
	}

}
