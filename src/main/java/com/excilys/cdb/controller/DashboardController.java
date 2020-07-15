package com.excilys.cdb.controller;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.excilys.cdb.config.WebAttributes;
import com.excilys.cdb.dto.DTOComputer;
import com.excilys.cdb.service.ComputerService;

@Controller
@RequestMapping(value = "/dashboard")
public class DashboardController {

	private static final Logger LOGGER = LoggerFactory.getLogger(DashboardController.class);
	private ComputerService computerService;
	@Autowired
	private WebAttributes webAttributes;

	public void setWebAttributes(WebAttributes webAttributes) {
		this.webAttributes = webAttributes;
	}

	@Autowired
	public DashboardController(ComputerService computerService) {
		this.computerService = computerService;
	}

	private void setUpDashboard(String page, String search, String order) {
		handleOrder(order);
		handleSearch(search);
		setUpListComp(page);
	}

	@GetMapping(value = "")
	public String display(Model model, @RequestParam(required = false) String page, @RequestParam(required = false) String search, @RequestParam(required = false) String order) {
		setUpDashboard(page, search, order);
		model.addAttribute(webAttributes);
		return "dashboard";
	}

	private void handleOrder(String order) {
		String[] orderType = { "computer", "computerdesc", "introduced", "introduceddesc", "discontinued",
				"discontinueddesc", "company", "companydesc" };
		if (Arrays.asList(orderType).contains(order)) {
			webAttributes.setOrder(order);
			webAttributes.setCurrentPage(0);
			computerService.selectPage(0);
		}
	}
	
	private void handleSearch(String search) {
		if (search != null && !search.trim().isEmpty()) {
			search = search.contains("\\") ? search.replace("\\", "") : search;
			search = search.contains("_") ? search.replace("_", "\\_") : search;
			search = search.contains("%") ? search.replace("%", "\\%") : search;			
		}
		webAttributes.setSearch(search);
	}
	
	private void setUpListComp(String page) {
		String search = webAttributes.getSearch();
		String order = webAttributes.getOrder();
		computerService.resetPages(search);
		webAttributes.setCompCount((int) computerService.getPageComp().getNbEntities());
		webAttributes.setEntitiesPerPage(computerService.getPageComp().getEntitiesPerPage());
		webAttributes.setMaxPage(computerService.getPageComp().getIdxMaxPage());
		webAttributes.setFirstCallCreate(true);
		setUpCurrentPage(page);
		List<DTOComputer> listComp;
		if (search != null && !search.isEmpty() && order != null) {
			listComp = computerService.orderedSearchComp(search, order).getEntities();
		} else if (search != null && !search.isEmpty()) {
			listComp = computerService.searchComp(search).getEntities();
		} else if (order != null) {
			listComp = computerService.orderComp(order).getEntities();
		} else {
			listComp = computerService.selectAll().getEntities();
		}
		webAttributes.setListComp(listComp);
	}
	
	private void setUpCurrentPage(String page) {
		int currentPage = 0;
		if (page != null) {
			try {
				currentPage = Integer.parseInt(page);
			} catch (NumberFormatException e) {
				LOGGER.debug("Wrong page number format");
			}
		}
		currentPage = Math.max(currentPage, 0);
		currentPage = Math.min(currentPage, webAttributes.getMaxPage());
		webAttributes.setCurrentPage(currentPage);
		computerService.selectPage(currentPage);
	}
}
