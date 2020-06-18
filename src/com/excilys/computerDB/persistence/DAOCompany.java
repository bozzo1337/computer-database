package com.excilys.computerDB.persistence;

import java.util.List;

import com.excilys.computerDB.mapper.CompanyMapper;
import com.excilys.computerDB.model.Company;

public class DAOCompany extends DAO<Company> {

	private DAOCompany() {
		this.mapper = CompanyMapper.getInstance();
	}
	
	@Override
	public DAO<Company> getInstance() {
		if (this.singleInstance == null) {
			this.singleInstance = new DAOCompany();
		}
		return this.singleInstance;
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
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Company> findBatch(int batchSize, int index) {
		// TODO Auto-generated method stub
		return null;
	}

}
