package infrastructure;

import java.util.List;

import database.PersistensException;
import model.Customer;
import model.Loan;
import model.VendingMachine;

public interface CtrLoanIF {
	void insertLoan(Loan loan, Customer customer) throws PersistensException;
	List<Loan> findLoansForCustomer(Customer customer) throws PersistensException;
	Loan createLoan(int customerId) throws PersistensException;
}
