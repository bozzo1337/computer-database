package com.excilys.cdb.persistence;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import com.excilys.cdb.mapper.ComputerMapper;
import com.excilys.cdb.model.Computer;

public class DAOComputer extends DAO<Computer> {
	
	private static DAOComputer singleInstance = null;
	private static final String SELECT_ONE = "SELECT * FROM computer LEFT JOIN company ON computer.company_id = company.id WHERE computer.id = ?;";
	private static final String SELECT_BATCH = "SELECT * FROM computer LEFT JOIN company ON computer.company_id = company.id LIMIT ?, ?;";
	private static final String SEARCH_BATCH = "SELECT * FROM computer LEFT JOIN company ON computer.company_id = company.id "
			+ "WHERE computer.name LIKE ? OR company.name LIKE ? ORDER BY CASE "
			+ "WHEN computer.name LIKE ? OR company.name LIKE ? THEN 0 "
			+ "WHEN computer.name LIKE ? OR company.name LIKE ? THEN 1 "
			+ "WHEN computer.name LIKE ? OR company.name LIKE ? THEN 3 "
			+ "WHEN computer.name LIKE ? OR company.name LIKE ? THEN 2 "
			+ "ELSE 4 "
			+ "END LIMIT ?, ? ;";
	private static final String INSERT_COMPUTER = "INSERT INTO computer(name, introduced, discontinued, company_id) VALUES(?, ?, ?, ?);";
	private static final String UPDATE_COMPUTER = "UPDATE computer SET name=?, introduced=?, discontinued=?, company_id=? WHERE id=?;";
	private static final String DELETE_COMPUTER = "DELETE FROM computer WHERE id=?;";
	private static final String COUNT_COMPUTER = "SELECT COUNT(id) AS count FROM computer;";
	private static final String COUNT_SEARCH = "SELECT COUNT(computer.id) AS count FROM computer LEFT JOIN company ON computer.company_id = company.id "
			+ "WHERE computer.name LIKE ? OR company.name LIKE ? ;";
	private static final String ORDER_COMPUTER_ASC = "SELECT * FROM computer LEFT JOIN company ON computer.company_id = company.id "
			+ "ORDER BY computer.name ASC LIMIT ?, ?;";
	private static final String ORDER_INTRO_ASC = "SELECT * FROM computer LEFT JOIN company ON computer.company_id = company.id "
			+ "ORDER BY computer.introduced ASC LIMIT ?, ?;";
	private static final String ORDER_DISC_ASC = "SELECT * FROM computer LEFT JOIN company ON computer.company_id = company.id "
			+ "ORDER BY computer.discontinued ASC LIMIT ?, ?;";
	private static final String ORDER_COMPANY_ASC = "SELECT * FROM computer LEFT JOIN company ON computer.company_id = company.id "
			+ "ORDER BY company.name ASC LIMIT ?, ?;";
	private static final String ORDER_COMPUTER_DESC = "SELECT * FROM computer LEFT JOIN company ON computer.company_id = company.id "
			+ "ORDER BY computer.name DESC LIMIT ?, ?;";
	private static final String ORDER_INTRO_DESC = "SELECT * FROM computer LEFT JOIN company ON computer.company_id = company.id "
			+ "ORDER BY computer.introduced DESC LIMIT ?, ?;";
	private static final String ORDER_DISC_DESC = "SELECT * FROM computer LEFT JOIN company ON computer.company_id = company.id "
			+ "ORDER BY computer.discontinued DESC LIMIT ?, ?;";
	private static final String ORDER_COMPANY_DESC = "SELECT * FROM computer LEFT JOIN company ON computer.company_id = company.id "
			+ "ORDER BY company.name DESC LIMIT ?, ?;";
	private static final String SEARCH_ORDER_COMPUTER_ASC = "SELECT * FROM computer LEFT JOIN company ON computer.company_id = company.id "
			+ "WHERE computer.name LIKE ? OR company.name LIKE ? ORDER BY computer.name ASC LIMIT ?, ?;";
	private static final String SEARCH_ORDER_INTRO_ASC = "SELECT * FROM computer LEFT JOIN company ON computer.company_id = company.id "
			+ "WHERE computer.name LIKE ? OR company.name LIKE ? ORDER BY computer.introduced ASC LIMIT ?, ?;";
	private static final String SEARCH_ORDER_DISC_ASC = "SELECT * FROM computer LEFT JOIN company ON computer.company_id = company.id "
			+ "WHERE computer.name LIKE ? OR company.name LIKE ? ORDER BY computer.discontinued ASC LIMIT ?, ?;";
	private static final String SEARCH_ORDER_COMPANY_ASC = "SELECT * FROM computer LEFT JOIN company ON computer.company_id = company.id "
			+ "WHERE computer.name LIKE ? OR company.name LIKE ? ORDER BY company.name ASC LIMIT ?, ?;";
	private static final String SEARCH_ORDER_COMPUTER_DESC = "SELECT * FROM computer LEFT JOIN company ON computer.company_id = company.id "
			+ "WHERE computer.name LIKE ? OR company.name LIKE ? ORDER BY computer.name DESC LIMIT ?, ?;";
	private static final String SEARCH_ORDER_INTRO_DESC = "SELECT * FROM computer LEFT JOIN company ON computer.company_id = company.id "
			+ "WHERE computer.name LIKE ? OR company.name LIKE ? ORDER BY computer.introduced DESC LIMIT ?, ?;";
	private static final String SEARCH_ORDER_DISC_DESC = "SELECT * FROM computer LEFT JOIN company ON computer.company_id = company.id "
			+ "WHERE computer.name LIKE ? OR company.name LIKE ? ORDER BY computer.discontinued DESC LIMIT ?, ?;";
	private static final String SEARCH_ORDER_COMPANY_DESC = "SELECT * FROM computer LEFT JOIN company ON computer.company_id = company.id "
			+ "WHERE computer.name LIKE ? OR company.name LIKE ? ORDER BY company.name DESC LIMIT ?, ?;";
	
	
	private DAOComputer() {
		this.mapper = ComputerMapper.getInstance();
	}
	
	public static DAOComputer getInstance() {
		if (singleInstance == null) {
			singleInstance = new DAOComputer();
		}
		return singleInstance;
	}

	@Override
	public Computer findById(Long id) {
		ResultSet results = null;
		try (Connection conn = DBC.getConn();
				PreparedStatement ps = conn.prepareStatement(SELECT_ONE)) {
			ps.setLong(1, id);
			results = ps.executeQuery();
			conn.commit();
			return mapper.map(results);
		} catch (SQLException e) {
			e.printStackTrace();
			doRollBack();
		}
		return null;
	}

	@Override
	public List<Computer> findBatch(int batchSize, int index) {
		ResultSet results = null;
		try (Connection conn = DBC.getConn();
				PreparedStatement ps = conn.prepareStatement(SELECT_BATCH)) {
			ps.setInt(1, index * batchSize);
			ps.setInt(2, batchSize);
			results = ps.executeQuery();
			conn.commit();
			return mapper.mapBatch(results);
		} catch (SQLException e) {
			e.printStackTrace();
			doRollBack();
		}
		return null;
	}
	
	public List<Computer> searchBatch(String search, int batchSize, int index) {
		ResultSet results = null;
		try (Connection conn = DBC.getConn();
				PreparedStatement ps = conn.prepareStatement(SEARCH_BATCH)) {
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
			conn.commit();
			return mapper.mapBatch(results);
		} catch (SQLException e) {
			e.printStackTrace();
			doRollBack();
		}
		return null;
	}

	public List<Computer> orderBatch(String orderType, int batchSize, int index) {
		ResultSet results = null;
		String query;
		switch(orderType) {
		case "computer":
			query = ORDER_COMPUTER_ASC;
			break;
		case "computerdesc":
			query = ORDER_COMPUTER_DESC;
			break;
		case "introduced":
			query = ORDER_INTRO_ASC;
			break;
		case "introduceddesc":
			query = ORDER_INTRO_DESC;
			break;
		case "discontinued":
			query = ORDER_DISC_ASC;
			break;
		case "discontinueddesc":
			query = ORDER_DISC_DESC;
			break;
		case "company":
			query = ORDER_COMPANY_ASC;
			break;
		case "companydesc":
			query = ORDER_COMPANY_DESC;
			break;
		default:
			return null;
		}
		try (Connection conn = DBC.getConn();
				PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setInt(1, index * batchSize);
			ps.setInt(2, batchSize);
			results = ps.executeQuery();
			conn.commit();
			return mapper.mapBatch(results);
		} catch (SQLException e) {
			e.printStackTrace();
			doRollBack();
		}
		return null;
	}
	
	public List<Computer> orderedSearch(String search, String orderType, int batchSize, int index) {
		ResultSet results = null;
		String query;
		switch(orderType) {
		case "computer":
			query = SEARCH_ORDER_COMPUTER_ASC;
			break;
		case "computerdesc":
			query = SEARCH_ORDER_COMPUTER_DESC;
			break;
		case "introduced":
			query = SEARCH_ORDER_INTRO_ASC;
			break;
		case "introduceddesc":
			query = SEARCH_ORDER_INTRO_DESC;
			break;
		case "discontinued":
			query = SEARCH_ORDER_DISC_ASC;
			break;
		case "discontinueddesc":
			query = SEARCH_ORDER_DISC_DESC;
			break;
		case "company":
			query = SEARCH_ORDER_COMPANY_ASC;
			break;
		case "companydesc":
			query = SEARCH_ORDER_COMPANY_DESC;
			break;
		default:
			return null;
		}
		try (Connection conn = DBC.getConn();
				PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, "%" + search + "%");
			ps.setString(2, "%" + search + "%");
			ps.setInt(3, index * batchSize);
			ps.setInt(4, batchSize);
			results = ps.executeQuery();
			conn.commit();
			return mapper.mapBatch(results);
		} catch (SQLException e) {
			e.printStackTrace();
			doRollBack();
		}
		return null;
	}
	
	public void createComputer(String name, LocalDate intro, LocalDate disc, Long compId) {
		try (Connection conn = DBC.getConn();
				PreparedStatement ps = conn.prepareStatement(INSERT_COMPUTER)){
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
			//TODO
			e.printStackTrace();
			doRollBack();
		}
	}

	public void updateComputer(Long id, String name, LocalDate intro, LocalDate disc, Long compId) {
		try (Connection conn = DBC.getConn();
				PreparedStatement ps = conn.prepareStatement(UPDATE_COMPUTER)){
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
			//TODO
			doRollBack();
		}
	}

	public void deleteComputer(Long id) {
		try (Connection conn = DBC.getConn();
				PreparedStatement ps = conn.prepareStatement(DELETE_COMPUTER)) {
			ps.setLong(1, id);
			ps.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			doRollBack();
		}	
	}
	
	public double count() {
		ResultSet results = null;
		double compCount = 0;
		try (Connection conn = DBC.getConn();
				PreparedStatement ps = conn.prepareStatement(COUNT_COMPUTER)) {
			results = ps.executeQuery();
			if (results.next()) {
				compCount = results.getDouble("count");
			}
			conn.commit();
		} catch (SQLException e) {
			//TODO
			doRollBack();
		}
		return compCount;
	}
	
	public double searchCount(String search) {
		ResultSet results = null;
		double compCount = 0;
		try (Connection conn = DBC.getConn();
				PreparedStatement ps = conn.prepareStatement(COUNT_SEARCH)) {
			ps.setString(1, "%" + search + "%");
			ps.setString(2, "%" + search + "%");
			results = ps.executeQuery();
			if (results.next()) {
				compCount = results.getDouble("count");
			}
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			doRollBack();
		}
		return compCount;
	}
	
	@Override
	public void create(Computer computer) {
		createComputer(computer.getName(), computer.getIntroduced(),
				computer.getDiscontinued(), computer.getCompanyId());	
	}

	@Override
	public void update(Computer computer) {
		updateComputer(computer.getId(), computer.getName(),
				computer.getIntroduced(), computer.getDiscontinued(),
				computer.getCompanyId());
	}

	@Override
	public void delete(Computer computer) {
		deleteComputer(computer.getId());
	}

	@Override
	public double getCount() {
		return count();
	}
}
