package com.excilys.computerDB.persistence;

import java.util.List;

import com.excilys.computerDB.mapper.ComputerMapper;
import com.excilys.computerDB.model.Computer;

public class DAOComputer extends DAO<Computer> {

	private DAOComputer() {
		this.mapper = ComputerMapper.getInstance();
	}
	
	@Override
	public DAO<Computer> getInstance() {
		if (this.singleInstance == null) {
			this.singleInstance = new DAOComputer();
		}
		return this.singleInstance;
	}

	@Override
	public Computer findById(Long id) {
		return mapper.map(QE.findComputerById(id));
	}

	@Override
	public List<Computer> findBatch(int batchSize, int index) {
		return mapper.mapBatch(QE.retrieveComputers(batchSize, index));
	}
	
	@Override
	public void create(Computer computer) {
		QE.createComputer(computer.getName(), computer.getIntroduced(),
				computer.getDiscontinued(), computer.getCompanyId());	
	}

	@Override
	public void update(Computer computer) {
		QE.updateComputer(computer.getId(), computer.getName(),
				computer.getIntroduced(), computer.getDiscontinued(),
				computer.getCompanyId());
		
	}

	@Override
	public void delete(Computer computer) {
		QE.deleteComputer(computer.getId());
	}

	@Override
	public double getCount() {
		return QE.computerCount();
	}

}
