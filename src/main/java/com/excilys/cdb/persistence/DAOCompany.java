package com.excilys.cdb.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.excilys.cdb.exception.NullMappingSourceException;
import com.excilys.cdb.exception.UnknownMappingSourceException;
import com.excilys.cdb.mapper.CompanyMapper;
import com.excilys.cdb.model.Company;

public class DAOCompany {

	private static DAOCompany singleInstance = null;
	private DBConnector dbc;
	private CompanyMapper mapperCompany;

	private DAOCompany() {
		this.dbc = DBConnector.getInstance();
		this.mapperCompany = CompanyMapper.getInstance();
	}

	public static DAOCompany getInstance() {
		if (singleInstance == null) {
			singleInstance = new DAOCompany();
		}
		return singleInstance;
	}

	public Company findById(Long id) {
		Company company = null;
		ResultSet results = null;
		try (Connection conn = dbc.getConn();
				PreparedStatement ps = conn.prepareStatement(SQLRequest.SELECT_ONE_COMPANY.toString())) {
			ps.setLong(1, id);
			results = ps.executeQuery();
			if (results.next()) {
				company = mapperCompany.map(results);
			}
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NullMappingSourceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownMappingSourceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return company;
	}
	
	public List<Company> findAll() {
		List<Company> companies = new ArrayList<Company>();
		ResultSet results = null;
		try (Connection conn = dbc.getConn();
				PreparedStatement ps = conn.prepareStatement(SQLRequest.SELECT_COMPANIES.toString())) {
			results = ps.executeQuery();
			while (results.next()) {
				companies.add(mapperCompany.map(results));
			}
			conn.commit();
			return companies;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NullMappingSourceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownMappingSourceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void deleteCompany(Long id) {
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
				e.printStackTrace();
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public double getCount() {
		return count();
	}

	public List<Company> findBatch(int batchSize, int index) {
		List<Company> companies = new ArrayList<Company>();
		ResultSet results = null;
		try (Connection conn = dbc.getConn();
				PreparedStatement ps = conn.prepareStatement(SQLRequest.SELECT_BATCH_COMPANY.toString())) {
			ps.setInt(1, index * batchSize);
			ps.setInt(2, batchSize);
			results = ps.executeQuery();
			while (results.next()) {
				companies.add(mapperCompany.map(results));
			}
			conn.commit();
			return companies;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NullMappingSourceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownMappingSourceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public double count() {
		ResultSet results = null;
		double compCount = 0;
		try (Connection conn = dbc.getConn();
				PreparedStatement ps = conn.prepareStatement(SQLRequest.COUNT_COMPANY.toString())) {
			results = ps.executeQuery();
			if (results.next()) {
				compCount = results.getDouble("count");
			}
		} catch (SQLException e) {
			// TODO
			e.printStackTrace();
		}
		return compCount;
	}
}
