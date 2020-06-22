package com.excilys.cdb.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.excilys.cdb.mapper.CompanyMapper;
import com.excilys.cdb.model.Company;

public class DAOCompany extends DAO<Company> {
	
	private static DAOCompany singleInstance = null;

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
		String query = "SELECT * FROM company WHERE id=" + id + ";";
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
	public void create(Company pojo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Company pojo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Company pojo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getCount() {
		return count();
	}

	@Override
	public List<Company> findBatch(int batchSize, int index) {
		ResultSet results = null;
		String query = "SELECT * FROM company LIMIT " + index * batchSize + ", " + batchSize +";";
		try (Connection conn = DBC.getConn();
				PreparedStatement ps = conn.prepareStatement(query)) {
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
		String query = "SELECT COUNT(id) AS count FROM company;";
		ResultSet results = null;
		double compCount = 0;
		try (Connection conn = DBC.getConn();
				PreparedStatement ps = conn.prepareStatement(query)) {
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
