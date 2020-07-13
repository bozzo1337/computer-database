package com.excilys.cdb.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.cdb.connector.MyDataSource;
import com.excilys.cdb.dao.mapper.CompanyMapper;
import com.excilys.cdb.exception.PersistenceException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Page;

@Repository
public class DAOCompany {

	private static final Logger LOGGER = LoggerFactory.getLogger(DAOCompany.class);
	private JdbcTemplate jdbcTemplate;
	private CompanyMapper mapper;

	@Autowired
	public DAOCompany(MyDataSource dataSource, CompanyMapper mapper) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.mapper = mapper;
		LOGGER.info("DAOCompany instantiated");
	}

	public Company findById(Long id) throws PersistenceException {
		List<Company> listResult = jdbcTemplate.query(SQLRequest.SELECT_ONE_COMPANY.toString(), mapper, id);
		if (listResult.size() == 0) {
			throw new PersistenceException("Results empty");
		}
		return listResult.get(0);
	}

	public void findAll(Page<Company> page) {
		page.setEntities(jdbcTemplate.query(SQLRequest.SELECT_COMPANIES.toString(), mapper));
	}

	@Transactional
	public void delete(Long id) {
		jdbcTemplate.update(SQLRequest.DELETE_COMPUTERS_OF_COMPANY.toString(), id);
		jdbcTemplate.update(SQLRequest.DELETE_COMPANY.toString(), id);
	}

	public void findBatch(Page<Company> page) {
		page.setEntities(jdbcTemplate.query(SQLRequest.SELECT_BATCH_COMPANY.toString(), mapper,
				page.getIdxCurrentPage() * page.getEntitiesPerPage(), page.getEntitiesPerPage()));
	}

	public double count() {
		return jdbcTemplate.queryForObject(SQLRequest.COUNT_COMPANY.toString(), Double.class);
	}
}
