package com.excilys.cdb.controller;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.excilys.cdb.controller.attributes.DashboardAttributes;
import com.excilys.cdb.dto.DTOComputer;
import com.excilys.cdb.service.ComputerService;

@RestController
@RequestMapping("/")
public class RestDashboardController {

	private static final Logger LOGGER = LoggerFactory.getLogger(RestDashboardController.class);
	private ComputerService computerService;
	private DashboardAttributes dashboardAttributes;
	

	@Autowired
	public RestDashboardController(ComputerService computerService) {
		this.computerService = computerService;
		dashboardAttributes = new DashboardAttributes();
	}

	@GetMapping
	public ResponseEntity<List<DTOComputer>> setGetRequestParams(Model model, @RequestParam(required = false) String page,
			@RequestParam(required = false) String search, @RequestParam(required = false) String order) {
		
		return new ResponseEntity<List<DTOComputer>>(setUpDashboard(page, search, order), HttpStatus.OK) ;
	}

	public String display(Model model) {
		model.addAttribute(dashboardAttributes);
		return "dashboard";
	}

	@PostMapping
	public String setPostRequestParams(Model model, @RequestParam(required = false) String search,
			@RequestParam(required = false) String order, @RequestParam(required = false) String button10,
			@RequestParam(required = false) String button50, @RequestParam(required = false) String button100) {
		dashboardAttributes.setSearch(search);
		dashboardAttributes.setOrder(order);
		dashboardAttributes.setCurrentPage(0);
		if (button10 != null) {
			dashboardAttributes.setEntitiesPerPage(10);
		} else if (button50 != null) {
			dashboardAttributes.setEntitiesPerPage(50);
		} else if (button100 != null) {
			dashboardAttributes.setEntitiesPerPage(100);
		}
		return setEntitiesPerPage(model);
	}

	public String setEntitiesPerPage(Model model) {
		computerService.setEntitiesPerPage(dashboardAttributes.getEntitiesPerPage());
		setUpListComp("0");
		return display(model);
	}

	@PostMapping("/delete")
	public String deleteComputers(Model model, @RequestParam String selection) {
		List<String> idsComputersToDelete = Arrays.asList(selection.split(","));
		idsComputersToDelete.stream().mapToLong(Long::parseLong)
				.forEach(id -> computerService.delete(computerService.selectById(Long.valueOf(id))));
		return "redirect:/";
	}

	private List<DTOComputer> setUpDashboard(String page, String search, String order) {
		handleOrder(order);
		handleSearch(search);
		return setUpListComp(page);
	}

	private void handleOrder(String order) {
		String[] orderType = { "computer", "computerdesc", "introduced", "introduceddesc", "discontinued",
				"discontinueddesc", "company", "companydesc" };
		if (Arrays.asList(orderType).contains(order)) {
			dashboardAttributes.setOrder(order);
			dashboardAttributes.setCurrentPage(0);
			computerService.selectPage(0);
		}
	}

	private void handleSearch(String search) {
		if (search != null && !search.trim().isEmpty()) {
			search = search.contains("\\") ? search.replace("\\", "") : search;
			search = search.contains("_") ? search.replace("_", "\\_") : search;
			search = search.contains("%") ? search.replace("%", "\\%") : search;
		}
		dashboardAttributes.setSearch(search);
	}

	private List<DTOComputer> setUpListComp(String page) {
		String search = dashboardAttributes.getSearch();
		String order = dashboardAttributes.getOrder();
		computerService.resetPages(search);
		dashboardAttributes.setCompCount((int) computerService.getPageComp().getNbEntities());
		dashboardAttributes.setEntitiesPerPage(computerService.getPageComp().getEntitiesPerPage());
		dashboardAttributes.setMaxPage(computerService.getPageComp().getIdxMaxPage());
		dashboardAttributes.setFirstCallCreate(true);
		setUpCurrentPage(page);
		List<DTOComputer> listComp;
		if (search != null && !search.isEmpty() && order != null && !order.isEmpty()) {
			listComp = computerService.orderedSearchComp(search, order).getEntities();
		} else if (search != null && !search.isEmpty()) {
			listComp = computerService.searchComp(search).getEntities();
		} else if (order != null && !order.isEmpty()) {
			listComp = computerService.orderComp(order).getEntities();
		} else {
			listComp = computerService.selectAll().getEntities();
		}
		return listComp;
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
		currentPage = Math.min(currentPage, dashboardAttributes.getMaxPage());
		dashboardAttributes.setCurrentPage(currentPage);
		computerService.selectPage(currentPage);
	}
}
