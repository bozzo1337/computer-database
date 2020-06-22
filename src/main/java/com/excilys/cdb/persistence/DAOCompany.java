package com.excilys.cdb.persistence;

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
		return mapper.map(QE.findCompanyById(id));
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
		return QE.count("company");
	}

	@Override
	public List<Company> findBatch(int batchSize, int index) {
		return mapper.mapBatch(QE.retrieveCompanies(batchSize, index));
	}

}
