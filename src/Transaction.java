import java.util.Date;

public class Transaction {
	
	/*
	 * Type of transaction: withdraw, deposit, transfer
	 */
	private String type;
	
	/*
	 *  Amount of this transaction.
	 */
	private double amount;
	
	/*
	 *  Time and date of this transaction.
	 */
	private Date timestamp;
	
	/*
	 *  Optional memo for this transaction, if there is one. Empty String "" means no memo.
	 */
	private String memo;
	
	/*
	 *  Account in which the transaction was performed.
	 */
	private Account account;
	
	/*
	 * Old balance of the account before the transaction was made.
	 */
	private double oldBalance;
	
	/*
	 * Old balance of the account before the transaction was made.
	 */
	private double newBalance;
	
	/**
	 * Constructor class: generates a transaction without a memo
	 * @param amount
	 * @param account
	 */
	public Transaction(String type, double amount, double oldBalance, double newBalance, Account account) {
		
		this.type = type;
		this.amount = amount;
		this.account = account;
		this.timestamp = new Date();
		this.memo = "";
		this.oldBalance = oldBalance;
		this.newBalance = newBalance;
	}
	
	/**
	 * Constructor class overloaded: generates a transaction with the optional memo
	 * @param amount
	 * @param memo
	 * @param account
	 */
	public Transaction(String type, String memo, double amount, double oldBalance, double newBalance, Account account) {
		
		// call the 4 argument constructor first
		this(type, amount, oldBalance, newBalance, account);
		
		// set the memo
		this.memo = memo;
	}
	
	public void printTransaction() {
		System.out.printf("Time of transaction: %s\n", this.timestamp.toString());
		System.out.printf("Type of transaction: %s\n", this.type);
		System.out.printf("Transaction amount: %.02f\n", this.amount);
		System.out.printf("Balance BEFORE transaction: %.02f\n", this.oldBalance);
		System.out.printf("Balance AFTER transaction: %.02f\n", this.newBalance);
		System.out.println("------------------------------------------------------------------------------------------");
	}
	
	/**
	 * Getter method for getting the amount this transaction was. The amount withdrew, deposited, or transferred.
	 * @return	this.amount (double)
	 */
	public double getAmount() {
		return this.amount;
	}
	
	/**
	 * Getter method for getting the type of transaction that was performed (withdraw, deposit, transfer)
	 * @return	this.type (String)
	 */
	public String getType() {
		return this.type;
	}
	
	/**
	 * Getter method for getting the optional memo for this transaction if there was one, empty string "" means no memo.
	 * @return	this.memo (String)
	 */
	public String getMemo() {
		return this.memo;
	}
	
	/**
	 * Getter method for getting the value of the old balance before this transaction was performed.
	 * @return	this.oldBalance (double)
	 */
	public double getOldBalance() {
		return this.oldBalance;
	}
	
	/**
	 * Getter method for getting the Account object that this transaction was made in
	 * @return	this.account (Account object)
	 */
	public Account getAccount() {
		return this.account;
	}
}
