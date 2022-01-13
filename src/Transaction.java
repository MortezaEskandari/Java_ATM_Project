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
	
	/**
	 * Constructor class: generates a transaction without a memo
	 * @param amount
	 * @param account
	 */
	public Transaction(String type, double amount, double oldBalance, Account account) {
		
		this.type = type;
		this.amount = amount;
		this.account = account;
		this.timestamp = new Date();
		this.memo = "";
		this.oldBalance = oldBalance;
	}
	
	/**
	 * Constructor class overloaded: generates a transaction with the optional memo
	 * @param amount
	 * @param memo
	 * @param account
	 */
	public Transaction(String type, String memo, double amount, double oldBalance, Account account) {
		
		// call the 4 argument constructor first
		this(type,amount,oldBalance,account);
		
		// set the memo
		this.memo = memo;
	}
	
	public void printTransaction() {
		if(this.type == "withdraw") {
			
		}
		else if(this.type == "deposit") {
			
		}
		else if(this.type == "transfer") {
			
		}
		else {
			System.out.println("Transaction 'type' was not any of the following: withdraw, deposit, transfer.");
		}
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
