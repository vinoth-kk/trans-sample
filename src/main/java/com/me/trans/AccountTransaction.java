package com.me.trans;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;

import com.me.trans.constants.Constant;
import com.me.trans.model.RelativeAccountBalance;
import com.me.trans.model.Transaction;
import com.me.trans.model.TransactionType;
import com.me.trans.utility.TransactionUtility;

public class AccountTransaction {
	public static void main(String[] args) {
		Scanner in = null;
		try {
			in = new Scanner(System.in);

			System.out.println("Enter Account Number: ");
			String accountNo = in.nextLine();
			System.out.println("Enter From Date: ");
			String start = in.nextLine();
			System.out.println("Enter To Date: ");
			String end = in.nextLine();

			printOutput(accountNo, start, end);

		} catch (IOException ex ) {
			System.out.println("Error reading file");
			ex.printStackTrace();
		} finally {
			if (in != null) {
				in.close();
			}

		}

	}

	private static void printOutput(String accountNo, String start, String end) throws IOException {
		RelativeAccountBalance relativeAccountBalance = fetchRelativeBalanceDetailsForTransaction(accountNo, start,
				end);

		DecimalFormat decF = new DecimalFormat("$#.00");
		System.out.println(
				"Relative balance for the period is: " + decF.format(relativeAccountBalance.getRelativeBalance().doubleValue()));

		System.out.println("Number of transactions included is: " + relativeAccountBalance.getTransactionCount());
	}

	public static RelativeAccountBalance fetchRelativeBalanceDetailsForTransaction(String accountNo, String start,
			String end) throws IOException {
		File csvFile = new File(Constant.CSV_PATH);
		List<Transaction> collectedTransaction = null;

		collectedTransaction = TransactionUtility.fetchTransactionsFromCSVFile(csvFile);

		LocalDateTime startDate = TransactionUtility.parseDateTime(start);
		LocalDateTime endDate = TransactionUtility.parseDateTime(end);

		List<Transaction> finalTransaction = processAccountTransactionForTimeFrame(accountNo, collectedTransaction,
				startDate, endDate);

		BigDecimal relativeAccountBalance = finalTransaction.stream().map(Transaction::getAmount)
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		return new RelativeAccountBalance(relativeAccountBalance, finalTransaction.size());

	}



	private static List<Transaction> processAccountTransactionForTimeFrame(String accountNo,
			List<Transaction> collectedTransaction, LocalDateTime startDate, LocalDateTime endDate) {

		List<Transaction> finalTransactions = null;
		if (CollectionUtils.isEmpty(collectedTransaction)) {
			return finalTransactions;
		}

		Predicate<Transaction> isBetweenTimeFrame = trans -> (trans.getCreatedAt().isAfter(startDate)
				|| trans.getCreatedAt().equals(startDate))
				&& (trans.getCreatedAt().isBefore(endDate) || trans.getCreatedAt().equals(endDate));

		Predicate<Transaction> isAccountPartOfTransaction = trans -> accountNo.equals(trans.getFromAccountId())
				|| accountNo.equals(trans.getToAccountId());

		Predicate<Transaction> isReversingTranaction = trans -> trans.getTransactionType() == TransactionType.REVERSAL;

		finalTransactions = collectedTransaction.stream()
				.filter(isReversingTranaction.negate().and(isBetweenTimeFrame.and(isAccountPartOfTransaction)))
				.map(trans -> {
					if (accountNo.equals(trans.getFromAccountId())) {
						trans.setAmount(trans.getAmount().negate());
					}
					return trans;
				}).collect(Collectors.toList());

		return finalTransactions;
	}



}
