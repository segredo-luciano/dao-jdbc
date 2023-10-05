package application;

import java.util.Date;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {				
		
		SellerDao sellerDao = DaoFactory.createSellerDao();
		System.out.println("=== TEST 1: sellerFindById ===");
		Seller seller = sellerDao.findById(3);		
		System.out.println(seller);
		
		System.out.println();
		
		System.out.println("=== TEST 2: sellerFindByDepartment ===");
		Department department = new Department(2, null);
		List<Seller> list = sellerDao.findByDepartment(department);
		
		for(Seller obj : list) {
			System.out.println(obj);
		}
		
		System.out.println();
		
		System.out.println("=== TEST 3: sellerFindAll===");		
		list = sellerDao.findAll();
		
		for(Seller obj : list) {
			System.out.println(obj);
		}
		
		System.out.println();
		
//		System.out.println("=== TEST 4: sellerinsert===");		
//		Seller newSeller = new Seller(null, "Greg", "greg@gmail.com", new Date(), 4000.0, department);
//		
//		sellerDao.insert(newSeller);
//		System.out.println("Inserted! new Id: " + newSeller.getId());
		
		System.out.println("=== TEST 5: sellerupdate===");		
		Seller updateSeller = new Seller(9, "Greg Orange", "greg@gmail.com", new Date(), 4000.0, new Department(3, null));
		
		sellerDao.update(updateSeller);
		System.out.println("Updated seller of id: " + updateSeller.getId());

	}

}
