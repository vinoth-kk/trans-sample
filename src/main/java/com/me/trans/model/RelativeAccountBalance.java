package com.me.trans.model;

import java.math.BigDecimal;

public class RelativeAccountBalance {
	private BigDecimal relativeBalance;
	private int transactionCount;

	public RelativeAccountBalance(BigDecimal relativeBalance, int transactionCount) {
		super();
		this.relativeBalance = relativeBalance;
		this.transactionCount = transactionCount;
	}

	public BigDecimal getRelativeBalance() {
		return relativeBalance;
	}

	public void setRelativeBalance(BigDecimal relativeBalance) {
		this.relativeBalance = relativeBalance;
	}

	public int getTransactionCount() {
		return transactionCount;
	}

	public void setTransactionCount(int transactionCount) {
		this.transactionCount = transactionCount;
	}

}