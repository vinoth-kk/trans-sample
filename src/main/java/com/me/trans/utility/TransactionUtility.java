package com.me.trans.utility;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.me.trans.constants.Constant;
import com.me.trans.model.Transaction;

public class TransactionUtility {
	
	public static List<Transaction> fetchTransactionsFromCSVFile(File csvFile) throws IOException {
		CsvMapper mapper = new CsvMapper();
		CsvSchema schema = CsvSchema.emptySchema().withHeader(); // use first row as header; otherwise defaults are fine
		List<Transaction> collectedTransaction = new ArrayList<>();
		MappingIterator<Transaction> transactions = mapper.readerFor(Transaction.class).with(schema)
				.readValues(csvFile);

		transactions.forEachRemaining(trans -> collectedTransaction.add(trans));
		return collectedTransaction;
	}
	
	public static LocalDateTime parseDateTime(String dateTime) {
		DateTimeFormatter datePattern = DateTimeFormatter.ofPattern(Constant.DATE_FORMAT_PATTERN);
		return LocalDateTime.parse(dateTime, datePattern);
	}

}
