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
import com.excilys.cdb.service.ComputerService;

/**
 * Servlet implementation class DashboardServlet
 */
@WebServlet(name = "dashboardServlet", urlPatterns = "/dashboard")
public class DashboardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ComputerService computerService;
	private int currentPage = 0;
	private int maxPage;
	private String search;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DashboardServlet() {
		super();
		this.computerService = ApplicationContextServlet.getComputerService();	
	}

	private void handleSearch() {
		if (search != null && !search.trim().isEmpty()) {
			search = search.contains("\\") ? search.replace("\\", "") : search;
			search = search.contains("_") ? search.replace("_", "\\_") : search;
			search = search.contains("%") ? search.replace("%", "\\%") : search;
		}
	}

	private String handleOrder(HttpServletRequest request) {
		String orderType = request.getParameter("order");
		if (orderType != null) {
			switch (orderType) {
			case "computer":
				currentPage = 0;
				break;
			case "computerdesc":
				currentPage = 0;
				break;
			case "introduced":
				currentPage = 0;
				break;
			case "introduceddesc":
				currentPage = 0;
				break;
			case "discontinued":
				currentPage = 0;
				break;
			case "discontinueddesc":
				currentPage = 0;
				break;
			case "company":
				currentPage = 0;
				break;
			case "companydesc":
				currentPage = 0;
				break;
			default:
				orderType = null;
			}
		}
		request.setAttribute("order", orderType);
		return orderType;
	}

	private void setUpDashboard(HttpServletRequest request) {
		computerService.resetPages(search);
		String orderType = handleOrder(request);
		setUpCurrentPage(request);
		List<DTOComputer> listComp;
		if (search != null && !search.isEmpty() && orderType != null) {
			listComp = computerService.orderedSearchComp(search, orderType).getEntities();
		} else if (search != null && !search.isEmpty()) {
			listComp = computerService.searchComp(search).getEntities();
		} else if (orderType != null) {
			listComp = computerService.orderComp(orderType).getEntities();
		} else {
			listComp = computerService.selectAll().getEntities();
		}
		request.setAttribute("listComp", listComp);
	}

	private void setUpCurrentPage(HttpServletRequest request) {
		maxPage = computerService.getPageComp().getIdxMaxPage();
		currentPage = currentPage > maxPage ? maxPage : currentPage;
		Integer paramPage = null;
		if (request.getParameter("page") != null) {
			try {
				paramPage = Integer.parseInt(request.getParameter("page"));
				currentPage = paramPage.intValue();
			} catch (NumberFormatException e) {
				paramPage = null;
			}
		}
		currentPage = Math.max(currentPage, 0);
		currentPage = Math.min(currentPage, maxPage);
		computerService.selectPage(currentPage);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		search = request.getParameter("search");
		handleSearch();
		setUpDashboard(request);
		request.setAttribute("search", search);
		request.setAttribute("entitiesPerPage", computerService.getPageComp().getEntitiesPerPage());
		request.setAttribute("maxPage", new Long(maxPage));
		request.setAttribute("currentPage", currentPage);
		request.setAttribute("compCount", (int) computerService.getPageComp().getNbEntities());
		request.setAttribute("firstCallCreate", true);
		RequestDispatcher rd = request.getRequestDispatcher("/dashboard.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		currentPage = 0;
		if (request.getParameter("button10") != null) {
			computerService.getPageComp().setEntitiesPerPage(10);
		} else if (request.getParameter("button50") != null) {
			computerService.getPageComp().setEntitiesPerPage(50);
		} else if (request.getParameter("button100") != null) {
			computerService.getPageComp().setEntitiesPerPage(100);
		}
		doGet(request, response);
	}
}
