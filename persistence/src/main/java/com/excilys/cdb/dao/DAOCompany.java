package com.excilys.cdb.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.dto.DTOCompany;
import com.excilys.cdb.dto.mapper.DTOCompanyMapper;
import com.excilys.cdb.exception.mapping.MappingException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Page;

@Repository
public class DAOCompany {

	private static final Logger LOGGER = LoggerFactory.getLogger(DAOCompany.class);
	private DAOComputer daoComputer;
	private SessionFactory sessionFactory;

	@Autowired
	public DAOCompany(SessionFactory sessionFactory, DAOComputer daoComputer) {
		this.daoComputer = daoComputer;
		this.sessionFactory = sessionFactory;
		LOGGER.info("DAOCompany instantiated");
	}

	public Company findById(Long id) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Company company = session.createQuery(HQLRequest.SELECT_ONE_COMPANY.toString(), Company.class)
				.setParameter("id", id).getSingleResult();
		session.getTransaction().commit();
		session.close();
		return company;
	}

	public void findAll(Page<Company> page) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		List<Company> companies = session.createQuery(HQLRequest.SELECT_COMPANIES.toString(), Company.class)
				.getResultList();
		page.setEntities(companies);
		session.getTransaction().commit();
		session.close();
	}

	public void delete(Company company) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		daoComputer.deleteComputersOfCompany(company);
		LOGGER.info("Computers of Company deleted");
		session.delete(company);
		LOGGER.info("Company deleted");
		session.getTransaction().commit();
		session.close();
	}

	public void findBatch(Page<Company> page) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		List<Company> companies = session.createQuery(HQLRequest.SELECT_BATCH_COMPANY.toString(), Company.class)
				.setFirstResult(page.getIdxCurrentPage() * page.getEntitiesPerPage())
				.setMaxResults(page.getEntitiesPerPage()).getResultList();
		page.setEntities(companies);
		session.getTransaction().commit();
		session.close();
	}

	public double count() {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Long count = session.createQuery(HQLRequest.COUNT_COMPANY.toString(), Long.class).getSingleResult();
		session.getTransaction().commit();
		session.close();
		return count.doubleValue();
	}

	public DTOCompany mapToDTO(Company company) {
		DTOCompany companyDTO = new DTOCompany("");
		try {
			companyDTO = DTOCompanyMapper.map(company);
		} catch (MappingException e) {
			LOGGER.error("Error during mapping to DTO", e);
		}
		return companyDTO;
	}
}
