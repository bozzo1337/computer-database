package com.excilys.cdb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.connector.DBConnector;
import com.excilys.cdb.dao.mapper.CompanyMapper;
import com.excilys.cdb.exception.NullMappingSourceException;
import com.excilys.cdb.exception.PersistenceException;
import com.excilys.cdb.exception.UnknownMappingSourceException;
import com.excilys.cdb.model.Company;

@Repository
public class DAOCompany {

	private static final Logger LOGGER = LoggerFactory.getLogger(DAOCompany.class);
	private DBConnector dbc;

	@Autowired
	public DAOCompany(DBConnector dbc) {
		this.dbc = dbc;
		LOGGER.info("DAOCompany instantiated");
	}
	
	public void setDBC(DBConnector dbc) {
		this.dbc = dbc;
	}
	
	public Company findById(Long id) throws PersistenceException {
		Company company = new Company();
		ResultSet results = null;
		try (Connection conn = dbc.getConn();
				PreparedStatement ps = conn.prepareStatement(SQLRequest.SELECT_ONE_COMPANY.toString())) {
			ps.setLong(1, id);
			results = ps.executeQuery();
			if (results.next()) {
				company = CompanyMapper.map(results);
			}
			conn.commit();
		} catch (SQLException | NullMappingSourceException | UnknownMappingSourceException e) {
			LOGGER.error("Error during SELECT one company", e);
			throw new PersistenceException("Error during SELECT one company", e);
		}
		return company;
	}

	public List<Company> findAll() throws PersistenceException {
		List<Company> companies = new ArrayList<Company>();
		ResultSet results = null;
		try (Connection conn = dbc.getConn();
				PreparedStatement ps = conn.prepareStatement(SQLRequest.SELECT_COMPANIES.toString())) {
			results = ps.executeQuery();
			while (results.next()) {
				companies.add(CompanyMapper.map(results));
			}
			conn.commit();
		} catch (SQLException | NullMappingSourceException | UnknownMappingSourceException e) {
			LOGGER.error("Error during SELECT all companies", e);
			throw new PersistenceException("Error during SELECT all companies", e);
		}
		return companies;
	}

	public void delete(Long id) throws PersistenceException {
		try (Connection conn = dbc.getConn()) {
			try (PreparedStatement psDeleteComputers = conn
					.prepareStatement(SQLRequest.DELETE_COMPUTERS_OF_COMPANY.toString());
					PreparedStatement psDeleteCompany = conn.prepareStatement(SQLRequest.DELETE_COMPANY.toString())) {
				psDeleteComputers.setLong(1, id);
				psDeleteComputers.executeUpdate();
				psDeleteCompany.setLong(1, id);
				psDeleteCompany.executeUpdate();
				conn.commit();
			} catch (SQLException e) {
				conn.rollback();
				LOGGER.error("Error during DELETE Statement", e);
				throw new PersistenceException("Error during DELETE Statement", e);
			}
		} catch (SQLException e1) {
			LOGGER.error("Error with Connection during DELETE", e1);
			throw new PersistenceException("Error with Connection during DELETE", e1);
		}
	}

	public List<Company> findBatch(int batchSize, int index) throws PersistenceException {
		List<Company> companies = new ArrayList<Company>();
		ResultSet results = null;
		try (Connection conn = dbc.getConn();
				PreparedStatement ps = conn.prepareStatement(SQLRequest.SELECT_BATCH_COMPANY.toString())) {
			ps.setInt(1, index * batchSize);
			ps.setInt(2, batchSize);
			results = ps.executeQuery();
			while (results.next()) {
				companies.add(CompanyMapper.map(results));
			}
			conn.commit();
		} catch (SQLException | NullMappingSourceException | UnknownMappingSourceException e) {
			LOGGER.error("Error during SELECT batch companies", e);
			throw new PersistenceException("Error during SELECT batch companies", e);
		}
		return companies;
	}

	public double count() throws PersistenceException {
		ResultSet results = null;
		double compCount = 0;
		try (Connection conn = dbc.getConn();
				PreparedStatement ps = conn.prepareStatement(SQLRequest.COUNT_COMPANY.toString())) {
			results = ps.executeQuery();
			if (results.next()) {
				compCount = results.getDouble("count");
			}
		} catch (SQLException e) {
			LOGGER.error("Error during SELECT batch companies", e);
			throw new PersistenceException("Error during SELECT batch companies", e);
		}
		return compCount;
	}
}
