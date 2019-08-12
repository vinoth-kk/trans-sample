package com.me.trans;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.me.trans.constants.Constant;
import com.me.trans.model.RelativeAccountBalance;
import com.me.trans.model.Transaction;
import com.me.trans.utility.TransactionUtility;

public class AccountTransactionTest {

	@Test
	public void testCSVReadWithCorrectPath() throws IOException {
		File csvFile = new File(Constant.CSV_PATH);
		List<Transaction> collectedTransaction = null;
		collectedTransaction = TransactionUtility.fetchTransactionsFromCSVFile(csvFile);
		assertEquals(collectedTransaction.get(0).getTransactionId(), "TX10001", "CSV Read successful");
	}

	@Test
	public void testCSVReadWithWrongPath() throws IOException {
		File csvFile = new File("resources/sample_data1.csv");
		assertThrows(FileNotFoundException.class, 
				() -> TransactionUtility.fetchTransactionsFromCSVFile(csvFile),
											"CSV Read failure due to wrong path");
	}

	@Test
	public void testForwrongFromDate() throws IOException {
		assertThrows(DateTimeParseException.class, () -> AccountTransaction.fetchRelativeBalanceDetailsForTransaction("ACC334455", "20-10-2018 12:00:00",
				"20/10/2018 19:00:00"), "From Date given in wrong format");
		
	}

	@Test
	public void testForwrongToDate() throws IOException {
		assertThrows(DateTimeParseException.class, () -> AccountTransaction.fetchRelativeBalanceDetailsForTransaction("ACC334455", "20/10/2018 12:00:00",
				"20-10-2018 19:00:00"), "To Date given in wrong format");
		
	}

	@Test
	public void testForCorrectDataWithReversal() throws IOException {
		RelativeAccountBalance relativeAccountBalance = AccountTransaction
				.fetchRelativeBalanceDetailsForTransaction("ACC334455", "20/10/2018 12:00:00", "20/10/2018 19:00:00");
		assertEquals(relativeAccountBalance.getRelativeBalance().toString(), "-30.50");
		assertEquals(relativeAccountBalance.getTransactionCount(), 3);
	}
	
	@Test
	public void testForCorrectDataWithoutReversal() throws IOException {
		RelativeAccountBalance relativeAccountBalance = AccountTransaction
				.fetchRelativeBalanceDetailsForTransaction("ACC998877", "20/10/2018 12:00:00", "20/10/2018 19:00:00");
		assertEquals(relativeAccountBalance.getRelativeBalance().toString(), "0.50");
		assertEquals(relativeAccountBalance.getTransactionCount(), 3);
	}

	@Test
	public void testForwrongAccountNumber() throws IOException {
		RelativeAccountBalance relativeAccountBalance = AccountTransaction
				.fetchRelativeBalanceDetailsForTransaction("123", "20/10/2018 12:00:00", "20/10/2018 19:00:00");
		assertEquals(relativeAccountBalance.getTransactionCount(), 0);
	}

}
