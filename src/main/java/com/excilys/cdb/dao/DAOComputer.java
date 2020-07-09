package com.excilys.cdb.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.connector.DBConnector;
import com.excilys.cdb.dao.mapper.ComputerMapper;
import com.excilys.cdb.dto.DTOComputer;
import com.excilys.cdb.dto.mapper.DTOComputerMapper;
import com.excilys.cdb.exception.NullMappingSourceException;
import com.excilys.cdb.exception.PersistenceException;
import com.excilys.cdb.exception.UnknownMappingSourceException;
import com.excilys.cdb.model.Computer;

@Repository
public class DAOComputer {

	private static final Logger LOGGER = LoggerFactory.getLogger(DAOComputer.class);
	private DBConnector dbc;

	@Autowired
	public DAOComputer(DBConnector dbc) {
		this.dbc = dbc;
		LOGGER.info("DAOComputer instantiated");
	}
	
	public void setDBC(DBConnector dbc) {
		this.dbc = dbc;
	}

	public Computer findById(Long id) throws PersistenceException {
		Computer computer = new Computer.Builder().build();
		ResultSet results = null;
		try (Connection conn = dbc.getConn();
				PreparedStatement ps = conn.prepareStatement(SQLRequest.SELECT_ONE.toString())) {
			ps.setLong(1, id);
			results = ps.executeQuery();
			if (results.next()) {
				computer = ComputerMapper.map(results);
			}
			conn.commit();
		} catch (SQLException | NullMappingSourceException | UnknownMappingSourceException e) {
			LOGGER.error("Error during SELECT one computer", e);
			throw new PersistenceException("Error during SELECT one computer", e);
		}
		return computer;
	}

	public List<Computer> findBatch(int batchSize, int index) throws PersistenceException {
		List<Computer> computers = new ArrayList<Computer>();
		ResultSet results = null;
		try (Connection conn = dbc.getConn();
				PreparedStatement ps = conn.prepareStatement(SQLRequest.SELECT_BATCH.toString())) {
			ps.setInt(1, index * batchSize);
			ps.setInt(2, batchSize);
			results = ps.executeQuery();
			while (results.next()) {
				computers.add(ComputerMapper.map(results));
			}
			conn.commit();
		} catch (SQLException | NullMappingSourceException | UnknownMappingSourceException e) {
			LOGGER.error("Error during SELECT computers batch", e);
			throw new PersistenceException("Error during SELECT computer batch", e);
		}
		return computers;
	}

	public List<Computer> searchBatch(String search, int batchSize, int index) throws PersistenceException {
		List<Computer> computers = new ArrayList<Computer>();
		ResultSet results = null;
		try (Connection conn = dbc.getConn();
				PreparedStatement ps = conn.prepareStatement(SQLRequest.SEARCH_BATCH.toString())) {
			ps.setInt(11, index * batchSize);
			ps.setInt(12, batchSize);
			ps.setString(1, "%" + search + "%");
			ps.setString(2, "%" + search + "%");
			ps.setString(3, search);
			ps.setString(4, search);
			ps.setString(5, search + "%");
			ps.setString(6, search + "%");
			ps.setString(7, "%" + search + "%");
			ps.setString(8, "%" + search + "%");
			ps.setString(9, "%" + search);
			ps.setString(10, "%" + search);
			results = ps.executeQuery();
			while (results.next()) {
				computers.add(ComputerMapper.map(results));
			}
			conn.commit();
		} catch (SQLException | NullMappingSourceException | UnknownMappingSourceException e) {
			LOGGER.error("Error during SEARCH computers batch", e);
			throw new PersistenceException("Error during SEARCH computers batch", e);
		}
		return computers;
	}

	public List<Computer> orderBatch(String orderType, int batchSize, int index) throws PersistenceException {
		List<Computer> computers = new ArrayList<Computer>();
		ResultSet results = null;
		String query = formatQuery(SQLRequest.ORDER.toString(), orderType);
		try (Connection conn = dbc.getConn(); PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setInt(1, index * batchSize);
			ps.setInt(2, batchSize);
			results = ps.executeQuery();
			while (results.next()) {
				computers.add(ComputerMapper.map(results));
			}
			conn.commit();
		} catch (SQLException | NullMappingSourceException | UnknownMappingSourceException e) {
			LOGGER.error("Error during ORDERED computers batch", e);
			throw new PersistenceException("Error during ORDERED computers batch", e);
		}
		return computers;
	}

	public List<Computer> orderedSearch(String search, String orderType, int batchSize, int index)
			throws PersistenceException {
		List<Computer> computers = new ArrayList<Computer>();
		ResultSet results = null;
		String query = formatQuery(SQLRequest.SEARCH_ORDER.toString(), orderType);
		try (Connection conn = dbc.getConn(); PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, "%" + search + "%");
			ps.setString(2, "%" + search + "%");
			ps.setInt(3, index * batchSize);
			ps.setInt(4, batchSize);
			results = ps.executeQuery();
			while (results.next()) {
				computers.add(ComputerMapper.map(results));
			}
			conn.commit();
		} catch (SQLException | NullMappingSourceException | UnknownMappingSourceException e) {
			LOGGER.error("Error during ORDERED SEARCH computers", e);
			throw new PersistenceException("Error during ORDERED SEARCH computers", e);
		}
		return computers;
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
			query = String.format(query, "computer.company_id ASC");
			break;
		case "companydesc":
			query = String.format(query, "computer.company_id DESC");
			break;
		default:
			return null;
		}
		return query;
	}

	public void create(Computer computer) throws PersistenceException {
		create(computer.getName(), computer.getIntroduced(), computer.getDiscontinued(), computer.getCompanyId());
	}

	public void create(String name, LocalDate intro, LocalDate disc, Long compId) throws PersistenceException {
		try (Connection conn = dbc.getConn();
				PreparedStatement ps = conn.prepareStatement(SQLRequest.INSERT_COMPUTER.toString())) {
			ps.setString(1, name);
			if (intro != null)
				ps.setDate(2, Date.valueOf(intro));
			else
				ps.setNull(2, java.sql.Types.DATE);
			if (disc != null)
				ps.setDate(3, Date.valueOf(disc));
			else
				ps.setNull(3, java.sql.Types.DATE);
			if (compId != null)
				ps.setLong(4, compId);
			else
				ps.setNull(4, java.sql.Types.BIGINT);
			ps.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			LOGGER.error("Error during INSERT computer", e);
			throw new PersistenceException("Error during INSERT computer", e);
		}
	}

	public void update(Computer computer) throws PersistenceException {
		update(computer.getId(), computer.getName(), computer.getIntroduced(), computer.getDiscontinued(),
				computer.getCompanyId());

	}

	public void update(Long id, String name, LocalDate intro, LocalDate disc, Long compId) throws PersistenceException {
		try (Connection conn = dbc.getConn();
				PreparedStatement ps = conn.prepareStatement(SQLRequest.UPDATE_COMPUTER.toString())) {
			ps.setString(1, name);
			if (intro != null)
				ps.setDate(2, Date.valueOf(intro));
			else
				ps.setNull(2, java.sql.Types.DATE);
			if (disc != null)
				ps.setDate(3, Date.valueOf(disc));
			else
				ps.setNull(3, java.sql.Types.DATE);
			if (compId != null)
				ps.setLong(4, compId);
			else
				ps.setNull(4, java.sql.Types.BIGINT);
			ps.setLong(5, id);
			ps.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			LOGGER.error("Error during UPDATE computer", e);
			throw new PersistenceException("Error during UPDATE computer", e);
		}
	}

	public void delete(Long id) throws PersistenceException {
		try (Connection conn = dbc.getConn();
				PreparedStatement ps = conn.prepareStatement(SQLRequest.DELETE_COMPUTER.toString())) {
			ps.setLong(1, id);
			ps.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			LOGGER.error("Error during DELETE computer", e);
			throw new PersistenceException("Error during DELETE computer", e);
		}
	}

	public void deleteComputersOfCompany(Long id) throws PersistenceException {
		try (Connection conn = dbc.getConn();
				PreparedStatement ps = conn.prepareStatement(SQLRequest.DELETE_COMPUTERS_OF_COMPANY.toString())) {
			ps.setLong(1, id);
			ps.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			LOGGER.error("Error during DELETE computers of company", e);
			throw new PersistenceException("Error during DELETE computers of company", e);
		}
	}

	public double count() throws PersistenceException {
		ResultSet results = null;
		double compCount = 0;
		try (Connection conn = dbc.getConn();
				PreparedStatement ps = conn.prepareStatement(SQLRequest.COUNT_COMPUTER.toString())) {
			results = ps.executeQuery();
			if (results.next()) {
				compCount = results.getDouble("count");
			}
			conn.commit();
		} catch (SQLException e) {
			LOGGER.error("Error during COUNT computers", e);
			throw new PersistenceException("Error during COUNT computers", e);
		}
		return compCount;
	}

	public double searchCount(String search) throws PersistenceException {
		ResultSet results = null;
		double compCount = 0;
		try (Connection conn = dbc.getConn();
				PreparedStatement ps = conn.prepareStatement(SQLRequest.COUNT_SEARCH.toString())) {
			ps.setString(1, "%" + search + "%");
			ps.setString(2, "%" + search + "%");
			results = ps.executeQuery();
			if (results.next()) {
				compCount = results.getDouble("count");
			}
			conn.commit();
		} catch (SQLException e) {
			LOGGER.error("Error during COUNT SEARCH computers", e);
			throw new PersistenceException("Error during COUNT SEARCH computers", e);
		}
		return compCount;
	}

	public DTOComputer mapToDTO(Computer computer) {
		DTOComputer computerDTO = new DTOComputer.Builder().build();
		try {
			computerDTO = DTOComputerMapper.map(computer);
		} catch (NullMappingSourceException | UnknownMappingSourceException e) {
			LOGGER.error("Error during mapping to DTO", e);
		}
		return computerDTO;
	}
}
