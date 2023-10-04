package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
	public void insert(Seller seller) {		
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			preparedStatement = connection.prepareStatement(
					"insert into seller(Name, Email, BirthDate, BaseSalary, DepartmentId)"
					+ " values(?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS
			);
			
			preparedStatement.setString(1, seller.getName());
			preparedStatement.setString(2, seller.getEmail());
			preparedStatement.setDate(3, new java.sql.Date(seller.getBirthDate().getTime()));
			preparedStatement.setDouble(4, seller.getBaseSalary());
			preparedStatement.setInt(5, seller.getDepartment().getId());
			
			int rowsAffected = preparedStatement.executeUpdate();
			
			if(rowsAffected > 0) {
				resultSet = preparedStatement.getGeneratedKeys();
				if(resultSet.next()) {
					int id = resultSet.getInt(1);
					seller.setId(id);
				}
			} else {
				throw new DbException("Erro inesperado, nenhuma linha foi alterada");
			}
		} catch(SQLException sqle) {
			throw new DbException(sqle.getMessage());
		} finally {
			DB.closeResultSet(resultSet);
			DB.closeStatement(preparedStatement);
		}
	}

	@Override
	public void update(Seller seller) {
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
		
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			preparedStatement = connection.prepareStatement(
					"select seller.*, department.Name as DepName from seller"
					+ " inner join department on seller.DepartmentId = department.id"
					+ " order by Name"					
					);
						
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
	
	@Override
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			preparedStatement = connection.prepareStatement(
					"select seller.*, department.Name as DepName from seller"
					+ " inner join department on seller.DepartmentId = department.id"
					+ " where department.id = ?"
					+ " order by Name"
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
