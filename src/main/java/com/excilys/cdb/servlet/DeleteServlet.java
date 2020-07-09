package com.excilys.cdb.servlet;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.cdb.service.ComputerService;
import com.excilys.cdb.spring.ApplicationContextServlet;

/**
 * Servlet implementation class DeleteServlet
 */
@WebServlet(name = "deleteServlet", urlPatterns = "/delete")
public class DeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ComputerService cs;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteServlet() {
        super();
        this.cs = ApplicationContextServlet.getComputerService();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("/dashboard");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<String> idsComputersToDelete = Arrays.asList(request.getParameter("selection").split(","));
		idsComputersToDelete
		.stream()
		.mapToLong(Long::parseLong)
		.forEach(id -> cs.delete(cs.selectById(Long.valueOf(id))));
		doGet(request, response);
	}

}
