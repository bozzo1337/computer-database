package com.excilys.cdb.dao;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.dto.DTOComputer;
import com.excilys.cdb.dto.mapper.DTOComputerMapper;
import com.excilys.cdb.exception.PersistenceException;
import com.excilys.cdb.exception.mapping.MappingException;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Page;

@Repository
public class DAOComputer {

	private static final Logger LOGGER = LoggerFactory.getLogger(DAOComputer.class);
	private JdbcTemplate jdbcTemplate;
	private SessionFactory sessionFactory;

	@Autowired
	public DAOComputer(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
		LOGGER.info("DAOComputer instantiated");
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public Computer findById(Long id) throws PersistenceException {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Computer computer = session.createQuery(HQLRequest.SELECT_ONE.toString(), Computer.class).setParameter("id", id)
				.getSingleResult();
		if (computer == null) {
			throw new PersistenceException("Results empty");
		}
		session.getTransaction().commit();
		session.close();
		return computer;
	}

	public void findBatch(Page<Computer> page) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		List<Computer> computers = new ArrayList<Computer>();
		computers = session.createQuery(HQLRequest.SELECT_BATCH.toString(), Computer.class)
				.setFirstResult(page.getIdxCurrentPage() * page.getEntitiesPerPage())
				.setMaxResults(page.getEntitiesPerPage()).getResultList();
		page.setEntities(computers);
		session.getTransaction().commit();
		session.close();
	}

	public void searchBatch(Page<Computer> page) {
		String search = page.getSearch();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		List<Computer> computers = new ArrayList<Computer>();
		computers = session.createQuery(HQLRequest.SEARCH_BATCH.toString(), Computer.class)
				.setParameter("search", "%" + search + "%").setParameter("start", search + "%")
				.setParameter("exact", search).setParameter("end", "%" + search)
				.setFirstResult(page.getIdxCurrentPage() * page.getEntitiesPerPage())
				.setMaxResults(page.getEntitiesPerPage()).getResultList();
		page.setEntities(computers);
		session.getTransaction().commit();
		session.close();
	}

	public void orderBatch(Page<Computer> page) {
		String query = formatQuery(HQLRequest.ORDER.toString(), page.getOrder());
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		List<Computer> computers = new ArrayList<Computer>();
		computers = session.createQuery(query, Computer.class)
				.setFirstResult(page.getIdxCurrentPage() * page.getEntitiesPerPage())
				.setMaxResults(page.getEntitiesPerPage()).getResultList();
		page.setEntities(computers);
		session.getTransaction().commit();
		session.close();
	}

	public void orderedSearch(Page<Computer> page) {
		String query = formatQuery(HQLRequest.SEARCH_ORDER.toString(), page.getOrder());
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		List<Computer> computers = new ArrayList<Computer>();
		computers = session.createQuery(query, Computer.class).setParameter("search", "%" + page.getSearch() + "%")
				.setFirstResult(page.getIdxCurrentPage() * page.getEntitiesPerPage())
				.setMaxResults(page.getEntitiesPerPage()).getResultList();
		page.setEntities(computers);
		session.getTransaction().commit();
		session.close();
	}

	private String formatQuery(String query, String orderType) {
		switch (orderType) {
		case "computer":
			query = String.format(query, "computer.name ASC");
			break;
		case "computerdesc":
			query = String.format(query, "computer.name DESC");
			break;
		case "introduced":
			query = String.format(query, "computer.introduced ASC");
			break;
		case "introduceddesc":
			query = String.format(query, "computer.introduced DESC");
			break;
		case "discontinued":
			query = String.format(query, "computer.discontinued ASC");
			break;
		case "discontinueddesc":
			query = String.format(query, "computer.discontinued DESC");
			break;
		case "company":
			query = String.format(query, "company.name ASC");
			break;
		case "companydesc":
			query = String.format(query, "company.name DESC");
			break;
		default:
			return null;
		}
		return query;
	}

	public void create(Computer computer) {
		String name = computer.getName();
		Date intro = null;
		if (computer.getIntroduced() != null)
			intro = Date.valueOf(computer.getIntroduced());
		Date disc = null;
		if (computer.getDiscontinued() != null)
			disc = Date.valueOf(computer.getDiscontinued());
		Long compId = computer.getCompanyId();
		jdbcTemplate.update(SQLRequest.INSERT_COMPUTER.toString(), name, intro, disc, compId);
	}

	public void update(Computer computer) {
		String name = computer.getName();
		Date intro = null;
		if (computer.getIntroduced() != null)
			intro = Date.valueOf(computer.getIntroduced());
		Date disc = null;
		if (computer.getDiscontinued() != null)
			disc = Date.valueOf(computer.getDiscontinued());
		Long compId = computer.getCompanyId();
		Long id = computer.getId();
		jdbcTemplate.update(SQLRequest.UPDATE_COMPUTER.toString(), name, intro, disc, compId, id);

	}

	public void delete(Long id) {
		jdbcTemplate.update(SQLRequest.DELETE_COMPUTER.toString(), id);
	}

	public void deleteComputersOfCompany(Long id) {
		jdbcTemplate.update(SQLRequest.DELETE_COMPUTERS_OF_COMPANY.toString(), id);
	}

	public double count() {
		return jdbcTemplate.queryForObject(SQLRequest.COUNT_COMPUTER.toString(), Double.class);
	}

	public double searchCount(String search) {
		return jdbcTemplate.queryForObject(SQLRequest.COUNT_SEARCH.toString(), Double.class, "%" + search + "%",
				"%" + search + "%");
	}

	public DTOComputer mapToDTO(Computer computer) {
		DTOComputer computerDTO = new DTOComputer.Builder().build();
		try {
			computerDTO = DTOComputerMapper.map(computer);
		} catch (MappingException e) {
			LOGGER.error("Error during mapping to DTO", e);
		}
		return computerDTO;
	}
}
