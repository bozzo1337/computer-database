package com.excilys.cdb.servlet;

import java.io.IOException;
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
		String compId = request.getParameter("compId");
		Long computerId = null;
		Computer computer = new Computer();
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
		request.setAttribute("computer", computer);
		request.setAttribute("listCompanies", listCompanies);
		RequestDispatcher rd = request.getRequestDispatcher("/edit.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
