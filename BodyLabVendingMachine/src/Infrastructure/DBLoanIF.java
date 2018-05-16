package Infrastructure;

import java.sql.SQLException;
import java.util.List;

import Database.PersistensException;
import Model.Customer;
import Model.Loan;
import Model.VendingMachine;

public interface DBLoanIF {
	List<Loan> findLoansForCustomer(Customer cu, boolean retrieveAssociation) throws PersistensException;
	int insertLoan(Loan l, Customer customer);
	
}
