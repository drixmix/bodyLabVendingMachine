package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import infrastructure.DBProductIF;
import model.Product;

public class DBProduct implements DBProductIF {
	
	// Klasse variabel
	private static DBProduct instance;
	// Instans variabel
	private Connection connection;
	
	// Konstrukt�ren er privat for at sikre at kun en instans af klassen oprettes
	private DBProduct() {
		connection = DBConnection.getInstance().getConnection();
	}
	
	// Metoden returnerer eller opretter en instans af klassen alt efter om den eksisterer
	public static DBProduct getInstance() {
		if (instance == null) {
			instance = new DBProduct();
		}
		return instance;
	}
	
	
	@Override
	public void insertProduct(Product product) throws PersistensException {
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
			PersistensException pe = new PersistensException(e, "Kunne ikke inds�tte produkt");
			throw pe;
		}

	}

	@Override
	public Product findProductById(int id) throws PersistensException {
		final String findProductById = "select * from Product where id = ?";
		Product p = null;
		try (PreparedStatement findById = connection.prepareStatement(findProductById)) {
			findById.setInt(1, id);
			ResultSet rs = findById.executeQuery();
			while (rs.next()) {
				p = buildProductObject(rs);
			}
		} catch (SQLException e) {
			PersistensException pe = new PersistensException(e, "Kunne ikke gennmf�rer s�gning");
			throw pe;
		}
		return p;
	}

	public List<Product> findProductsInVM(int VendingMachineId) throws PersistensException {
		List<Product> productList = null;
		final String findMachineProduct = "SELECT * FROM MachineProduct, Product as product WHERE productId = product.id AND vendingMachineId = ?";
		try {
			PreparedStatement statement = connection.prepareStatement(findMachineProduct);
			statement.setInt(1, VendingMachineId);
			ResultSet rs = statement.executeQuery();
			productList = buildProductList(rs);
		} catch (SQLException e) {
			PersistensException pe = new PersistensException(e, "Kunne ikke gennemf�rer s�gning");
			throw pe;
		}

		return productList;
	}

	public static List<Product> buildProductList(ResultSet rs) throws PersistensException {
		List<Product> productList = new LinkedList<Product>();
		try {
			while (rs.next()) {
				productList.add(buildProductObject(rs));
			}
		} catch (SQLException e) {
			PersistensException pe = new PersistensException(e, "Kunne ikke bygge Mach");
			throw pe;
		}
		return productList;
	}

	private static Product buildProductObject(ResultSet rs) throws PersistensException {
		int id;
		Product product = null;
		try {
			id = rs.getInt("id");
			String productNo = rs.getString("productNo");
			String name = rs.getString("name");
			String description = rs.getString("description");
			double stockValue = rs.getFloat("stockValue");
			product = new Product(id, productNo, name, description, stockValue);
		} catch (SQLException e) {
			PersistensException pe = new PersistensException(e, "Kunne ikke bygge produkt objekt");
		}
		return product;
	}

}
