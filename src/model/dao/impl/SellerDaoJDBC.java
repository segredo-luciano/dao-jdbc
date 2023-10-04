package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {

	private Connection connection;
	
	public SellerDaoJDBC(Connection connection) {
		this.connection = connection;
	}
	
	@Override
	public void insert(Seller department) {		
		
	}

	@Override
	public void update(Seller department) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			preparedStatement = connection.prepareStatement(
					"select seller.*, department.Name as DepName"
					+ " from seller inner join department on seller.Id = ?"
					);
			
			preparedStatement.setInt(1, id);
			resultSet = preparedStatement.executeQuery();
			
			if(resultSet.next()) {
				Department department = instantiateDepartment(resultSet);				
				Seller seller = instantiateSeller(resultSet, department);
				
				return seller;
			}
			
			return null;
		} catch(SQLException sqle) {
			throw new DbException(sqle.getMessage());
		} finally {
			DB.closeStatement(preparedStatement);
			DB.closeResultSet(resultSet);
		}
	}
	
	private Department instantiateDepartment(ResultSet resultSet) throws SQLException {
		Department department = new Department();
		department.setId(resultSet.getInt("DepartmentId"));
		department.setName(resultSet.getString("DepName"));
		return department;
	}
	
	private Seller instantiateSeller(ResultSet resultSet, Department department) throws SQLException {
		Seller seller = new Seller(
				resultSet.getInt("Id"),
				resultSet.getString("Name"),
				resultSet.getString("Email"),
				resultSet.getDate("BirthDate"),
				resultSet.getDouble("BaseSalary"),
				department
			);
		
		return seller;
	}

	@Override
	public List<Seller> findAll() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			preparedStatement = connection.prepareStatement(
					"select seller.*, department.Name as DepName from seller"
					+ " inner join department on seller.DepartmentId = department.id"
					+ " where department.id = ?"
					);
			
			preparedStatement.setInt(1, department.getId());
			resultSet = preparedStatement.executeQuery();
						
			List<Seller> sellerList = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();
			while(resultSet.next()) {
				Department departmentVerify = map.get(resultSet.getInt("DepartmentId"));
				
				if(departmentVerify == null)
				{
					departmentVerify = instantiateDepartment(resultSet);
					map.put(resultSet.getInt("DepartmentId"), departmentVerify);
				}
				Seller seller = instantiateSeller(resultSet, departmentVerify);
				
				sellerList.add(seller);								
			}
			
			return sellerList;
		} catch(SQLException sqle) {
			throw new DbException(sqle.getMessage());
		}
	}

}
