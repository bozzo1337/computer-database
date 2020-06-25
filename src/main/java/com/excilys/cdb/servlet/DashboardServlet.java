package com.excilys.cdb.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.cdb.model.Computer;
import com.excilys.cdb.service.ComputerService;

/**
 * Servlet implementation class DashboardServlet
 */
@WebServlet(name = "dashboardServlet", urlPatterns = "/dashboard")
public class DashboardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ComputerService cs = ComputerService.getInstance();
	private int currentPage = 0;
	private int maxPage;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DashboardServlet() {
        super();
        cs.resetPages();
        maxPage = cs.getPageComp().getIdxMaxPage();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("maxPage", new Long(maxPage));
		Integer paramPage = null;
		if (request.getParameter("page") != null) {
			paramPage = Integer.parseInt(request.getParameter("page"));
		}
		if (paramPage != null) {
			currentPage = paramPage.intValue();
			currentPage = Math.max(currentPage, 0);
			currentPage = Math.min(currentPage, maxPage);
		}
		cs.selectPage(currentPage);
		List<Computer> listComp = cs.selectAll().getEntities();
		request.setAttribute("currentPage", currentPage);
		request.setAttribute("listComp", listComp);
		request.setAttribute("compCount", (int) cs.getCount());
		RequestDispatcher rd = request.getRequestDispatcher("/dashboard.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		doGet(request, response);
	}

}