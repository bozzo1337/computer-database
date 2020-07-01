package com.excilys.cdb.servlet;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.cdb.service.ComputerService;

/**
 * Servlet implementation class DeleteServlet
 */
@WebServlet(name = "deleteServlet", urlPatterns = "/delete")
public class DeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ComputerService cs = ComputerService.getInstance();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect(request.getContextPath() + "/dashboard");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<String> idsComputersToDelete = Arrays.asList(request.getParameter("selection").split(","));
		for (String idCompToDelete : idsComputersToDelete) {
			if (!idCompToDelete.trim().isEmpty()) {
				cs.delete(cs.selectById(Long.valueOf(idCompToDelete)));
			}
		}
		doGet(request, response);
	}

}
