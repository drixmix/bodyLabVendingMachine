package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import Infrastructure.DBProductIF;
import Model.Product;

public class DBProduct implements DBProductIF {
	private static DBProduct instance = null;
	private Connection connection;

	private DBProduct() {
		connection = DBConnection.getInstance().getConnection();
	}

	public static DBProduct getInstance(){
		if (instance == null) {
			instance = new DBProduct();
		}
		return instance;
	}

	@Override
	public void insertProduct(Product product) throws SQLException {
		PreparedStatement insert;
		String insertProduct = "insert into Product (productNo, name, description, stockValue) values (?,?,?,?,?) ";
		try {
			insert = connection.prepareStatement(insertProduct);
			insert.setString(1, product.getProductNo());
			insert.setString(2, product.getName());
			insert.setString(3, product.getDescription());
			insert.setDouble(4, product.getStockValue());
			insert.execute();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}
	@Override
	public Product findProductById(int id) throws SQLException {
		final String findProductById = "select * from Product where id = ?";
		Product p = null;
			try	(PreparedStatement findById = connection.prepareStatement(findProductById)){
			findById.setInt(1, id);
			
			ResultSet rs = findById.executeQuery();
			
			while(rs.next()) {
				p = buildProductObject(rs);
			}}
			catch(SQLException e) {
				
			}
			
			System.out.println(p);
		return p;
	}
	
	public List<Product> findProductsInVM(int VendingMachineId) throws PersistensException {
		List<Product> productList = null;
		
		final String findMachineProduct = "SELECT * FROM MachineProduct, Product as product WHERE productId = product.id AND vendingMachineId = ?";
		try {
			PreparedStatement statement = connection.prepareStatement(findMachineProduct);
			statement.setInt(1, VendingMachineId);

			ResultSet rs = statement.executeQuery();
			
			productList = buildMachineProductObject(rs);
			
			System.out.println(productList);
			
		} 
		catch (SQLException e) {
			PersistensException pe = new PersistensException(e, "Could not find all");
			pe.printStackTrace();
			throw pe;
			
		}
		
		return productList;
	}
	
	private List<Product> buildMachineProductObject(ResultSet rs) throws SQLException {
		List<Product> productList = new LinkedList<Product>();
		
		while (rs.next()) {
			productList.add(buildProductObject(rs));
		}
		return productList;
	}
	

	private Product buildProductObject(ResultSet rs) throws SQLException {
		int id = rs.getInt("id");
		String productNo = rs.getString("productNo");
		String name = rs.getString("name");
		String description = rs.getString("description");
		double stockValue = rs.getFloat("stockValue");
		

		Product product = new Product(id, productNo, name, description, stockValue);
		
		return product;
	}

}
