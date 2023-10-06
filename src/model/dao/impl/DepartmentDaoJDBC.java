package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao {

private Connection connection;
	
	public DepartmentDaoJDBC(Connection connection) {
		this.connection = connection;
	}
	
	@Override
	public void insert(Department department) {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			preparedStatement = connection.prepareStatement(
				"insert into department(Name)"
				+ " values(?)",
				Statement.RETURN_GENERATED_KEYS
			);
						
			preparedStatement.setString(1, department.getName());
			
			int affectedRows = preparedStatement.executeUpdate();
			
			if(affectedRows > 0) {
				resultSet = preparedStatement.getGeneratedKeys();
				if(resultSet.next()) {
					int id = resultSet.getInt(1);
					department.setId(id);
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
	public void update(Department department) {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			preparedStatement = connection.prepareStatement(
				"update department"
				+ " set Name = ?"
				+ " where Id = ?",
				Statement.RETURN_GENERATED_KEYS
			);
						
			preparedStatement.setString(1, department.getName());
			preparedStatement.setInt(2, department.getId());
			
			int affectedRows = preparedStatement.executeUpdate();
			
			if(affectedRows > 0) {
				resultSet = preparedStatement.getGeneratedKeys();
				if(resultSet.next()) {
					int id = resultSet.getInt(1);
					department.setId(id);
				}
			} else {
				throw new DbException("The informed department does not exist");
			}
		} catch(SQLException sqle) {
			throw new DbException(sqle.getMessage());
		} finally {
			DB.closeResultSet(resultSet);
			DB.closeStatement(preparedStatement);
		}
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement preparedStatement = null;
		
		try {
			preparedStatement = connection.prepareStatement(
				"delete from department"
				+ " where Id = ?"	
			);
			
			preparedStatement.setInt(1, id);
			
			int rowsAffected = preparedStatement.executeUpdate();
			
			if(rowsAffected == 0) {
				throw new SQLException("the id "+id+" does not exist!");
			}
		} catch(SQLException sqle) {
			throw new DbException(sqle.getMessage());
		}
	}

	@Override
	public Department findById(Integer id) {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			preparedStatement = connection.prepareStatement(
				"select * from department"
				+ " where id = ?"
			);
			
			preparedStatement.setInt(1, id);
			resultSet = preparedStatement.executeQuery();
			
			if(resultSet.next()) {
				Department department = new Department(
						resultSet.getInt("Id"),
						resultSet.getString("Name"));
				
				return department;
			}
			
			return null;
			
		} catch(SQLException sqle) {
			throw new DbException(sqle.getMessage());
		} finally {
			DB.closeResultSet(resultSet);
			DB.closeStatement(preparedStatement);
		}
	}

	@Override
	public List<Department> findAll() {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			preparedStatement = connection.prepareStatement(
				"select * from department"				
			);
						
			resultSet = preparedStatement.executeQuery();
			
			List<Department> list = new ArrayList<>();
			while(resultSet.next()) {
				Department department = new Department(
						resultSet.getInt("Id"),
						resultSet.getString("Name"));
				
				list.add(department);
			}
			
			return list;						
			
		} catch(SQLException sqle) {
			throw new DbException(sqle.getMessage());
		} finally {
			DB.closeResultSet(resultSet);
			DB.closeStatement(preparedStatement);
		}
	}

}
