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
		String query = "SELECT * FROM computer LEFT JOIN company ON computer.company_id = company.id "
				+ "WHERE computer.id = " + id + ";";
		try (Connection conn = DBC.getConn();
				PreparedStatement ps = conn.prepareStatement(query)) {
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
		String query = "SELECT * FROM computer LEFT JOIN company ON computer.company_id = company.id "
				+ "LIMIT ?, ?;";
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
	
	public List<Computer> searchBatch(String search, int batchSize, int index) {
		ResultSet results = null;
		String query = "SELECT * FROM computer LEFT JOIN company ON computer.company_id = company.id "
				+ "WHERE computer.name LIKE ? OR company.name LIKE ? ORDER BY CASE "
				+ "WHEN computer.name LIKE ? OR company.name LIKE ? THEN 0 "
				+ "WHEN computer.name LIKE ? OR company.name LIKE ? THEN 1 "
				+ "WHEN computer.name LIKE ? OR company.name LIKE ? THEN 3 "
				+ "WHEN computer.name LIKE ? OR company.name LIKE ? THEN 2 "
				+ "ELSE 4 "
				+ "END LIMIT ?, ? ;";
		try (Connection conn = DBC.getConn();
				PreparedStatement ps = conn.prepareStatement(query)) {
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

	public void createComputer(String name, LocalDate intro, LocalDate disc, Long compId) {
		String query = "INSERT INTO computer(name, introduced, discontinued, company_id)";
		query += " VALUES(?, ?, ?, ?);";
		try (Connection conn = DBC.getConn();
				PreparedStatement ps = conn.prepareStatement(query)){
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
		String query = "UPDATE computer SET name=?, introduced=?, discontinued=?, company_id=? WHERE id=?;";
		try (Connection conn = DBC.getConn();
				PreparedStatement ps = conn.prepareStatement(query)){
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
		String query = "DELETE FROM computer WHERE id=" + id + ";";
		try (Connection conn = DBC.getConn();
				PreparedStatement ps = conn.prepareStatement(query)) {
			ps.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			doRollBack();
		}	
	}
	
	public double count() {
		String query = "SELECT COUNT(id) AS count FROM computer;";
		ResultSet results = null;
		double compCount = 0;
		try (Connection conn = DBC.getConn();
				PreparedStatement ps = conn.prepareStatement(query)) {
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
		String query = "SELECT COUNT(computer.id) AS count FROM computer LEFT JOIN company ON computer.company_id = company.id "
				+ "WHERE computer.name LIKE ? OR company.name LIKE ? ;";
		try (Connection conn = DBC.getConn();
				PreparedStatement ps = conn.prepareStatement(query)) {
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
}
