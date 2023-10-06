package application;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class Program2 {

	public static void main(String[] args) {
		
		DepartmentDao departmentDao = DaoFactory.createDepartmentDao();
		
		System.out.println("=== TEST 1 find department by id ===");		
		System.out.println(departmentDao.findById(3));
		
		System.out.println();
		
		System.out.println("=== TEST 2 find department all ===");		
		System.out.println(departmentDao.findAll());
		
		System.out.println();
		
//		System.out.println("=== TEST 3 insert department ===");		
//		Department department = new Department(null, "Games");
//		departmentDao.insert(department);
//		System.out.println("inserted id: " + department.getId());
		
//		System.out.println("=== TEST 4 insert department ===");				
//		departmentDao.update(new Department(7, "Food"));		
//		System.out.println("department updated!");
		
		System.out.println();
		
		departmentDao.deleteById(6);
		System.out.println("succesfully deleted");

	}

}
