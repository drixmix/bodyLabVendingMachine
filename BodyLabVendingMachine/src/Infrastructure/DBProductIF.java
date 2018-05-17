package Infrastructure;

import java.sql.SQLException;

import Model.Product;

public interface DBProductIF {


	void insertProduct(Product product) throws SQLException;

	Product findProductById(int id) throws SQLException;

}