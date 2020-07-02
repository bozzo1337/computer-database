package com.excilys.cdb.persistence;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.excilys.cdb.mapper.CompanyMapper;
import com.excilys.cdb.model.Company;

public class DAOCompany extends DAO<Company> {
	
	private static DAOCompany singleInstance = null;
	
	private static final String SELECT_ONE = "SELECT * FROM company WHERE id=?;";
	private static final String ORDER_ALL = "SELECT * FROM company ORDER BY name;";
	private static final String SELECT_BATCH = "SELECT * FROM company LIMIT ?, ?;";
	private static final String COUNT_ALL = "SELECT COUNT(id) AS count FROM company;";
	private static final String SELECT_COMPUTERS_IN_COMPANY = "SELECT computer.id FROM computer WHERE computer.company_id=?;";
	private static final String DELETE_COMPANY = "DELETE FROM company WHERE id=?;";

	private DAOCompany() {
		this.mapper = CompanyMapper.getInstance();
	}
	
	public static DAOCompany getInstance() {
		if (singleInstance == null) {
			singleInstance = new DAOCompany();
		}
		return singleInstance;
	}

	@Override
	public Company findById(Long id) {
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
	public void create(Company pojo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Company pojo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Company company) {
		deleteCompany(company.getId());
	}
	
	private void deleteCompany(Long id) {
		ResultSet results = null;
		try (Connection conn = DBC.getConn();
				PreparedStatement psSelectComputers = conn.prepareStatement(SELECT_COMPUTERS_IN_COMPANY);
						PreparedStatement psDeleteComputers = conn.prepareStatement(DAOComputer.DELETE_COMPUTER);
				PreparedStatement psDeleteCompany = conn.prepareStatement(DELETE_COMPANY)) {
			psSelectComputers.setLong(1, id);
			results = psSelectComputers.executeQuery();
			while (results.next()) {
				DAOComputer.getInstance().deleteComputerTransaction(psDeleteComputers, results.getLong("computer.id"));
			}
			psDeleteComputers.executeBatch();
			psDeleteCompany.setLong(1, id);
			psDeleteCompany.executeUpdate();
			conn.commit();
		} catch (BatchUpdateException e) {
			e.printStackTrace();
			doRollBack();
		} catch (SQLException e) {
			e.printStackTrace();
			doRollBack();
		}		
	}

	@Override
	public double getCount() {
		return count();
	}
	
	public List<Company> findAll() {
		ResultSet results = null;
		try (Connection conn = DBC.getConn();
				PreparedStatement ps = conn.prepareStatement(ORDER_ALL)) {
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
	public List<Company> findBatch(int batchSize, int index) {
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
	
	public double count() {
		ResultSet results = null;
		double compCount = 0;
		try (Connection conn = DBC.getConn();
				PreparedStatement ps = conn.prepareStatement(COUNT_ALL)) {
			results = ps.executeQuery();
			if (results.next()) {
				compCount = results.getDouble("count");
			}
		} catch (SQLException e) {
			//TODO
			doRollBack();
		}
		return compCount;
	}
}
