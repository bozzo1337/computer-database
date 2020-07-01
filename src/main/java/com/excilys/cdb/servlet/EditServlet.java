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
import com.excilys.cdb.exception.IncorrectDiscDateException;
import com.excilys.cdb.exception.IncorrectIDException;
import com.excilys.cdb.exception.IncorrectIntroDateException;
import com.excilys.cdb.exception.IncorrectNameException;
import com.excilys.cdb.exception.IncorrectTemporalityException;
import com.excilys.cdb.mapper.ComputerMapper;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;
import com.excilys.cdb.validation.Validator;

/**
 * Servlet implementation class EditServlet
 */
@WebServlet(name = "editServlet", urlPatterns = "/edit")
public class EditServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CompanyService cas = CompanyService.getInstance();
	private ComputerService cs = ComputerService.getInstance();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Company> listCompanies = cas.selectAll();
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
			computer = ComputerMapper.getInstance().mapToDTO(cs.selectById(computerId));
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
		String id = request.getParameter("computerId");
		String name = request.getParameter("computerNameInput");
		String intro = request.getParameter("introduced");
		String disc = request.getParameter("discontinued");
		String compId = request.getParameter("companyId");
		String errMessage = null;
		DTOComputer computerDTO = new DTOComputer(id, name, intro, disc, compId);
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
