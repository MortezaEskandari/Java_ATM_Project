import java.util.ArrayList;
import java.util.Scanner;

public class Account {

	/*
	 *  Name of the account.
	 */
	private String name;
	
	/*
	 *  Current balance of the account.
	 */
	private double balance;
	
	/*
	 *  Account ID number.
	 */
	private String uuid;
	
	/*
	 *  User object that owns this account.
	 */
	private User user;
	
	/*
	 * The bank object this account belongs to
	 */
	private Bank bank;
	
	/*
	 *  List of transactions for this account.
	 */
	private ArrayList<Transaction> transactions;
	
	/*
	 * Constructor class, create's a new account
	 * creates empty list of transactions
	 * adds this account to the holder and bank lists
	 * @param name		the name of the account
	 * @param holder	the User object that holds this account
	 * @param bank		the bank that issues the account
	 */
	public Account(String name, User user, Bank bank) {
		
		// set the account name and holder
		this.name = name;
		this.user = user;
		this.balance = 0.0;
		this.bank = bank;
		
		// get new account UUID
		this.uuid = bank.getNewAccountUUID();
		
		// initialize the transactions
		this.transactions = new ArrayList<Transaction>();
	}
	
	/**
	 * Method called by the User class to perform the withdraw from this account
	 * After the withdraw is made, add this withdraw into the list of transactions made for this account
	 * @param scanner
	 */
	public void withdraw(Scanner scanner) {
		
		// see if the User wants to add a memo, returning an empty String "" means they didn't want a memo
		String memo = getMemo(scanner);
		
		// ask User the amount they want to withdraw
		double withdrawAmount;
		do {			
			this.getSummary();
			System.out.println("Enter amount you want to withdraw: ");
			withdrawAmount = scanner.nextDouble();
			scanner.nextLine(); // moves cursor to nextLine
			
			if(withdrawAmount > this.balance || withdrawAmount < 0) {
				System.out.println("Withdraw amount exceeds the account balance or you entered an invalid amount.");
			}
		} while(withdrawAmount > this.balance || withdrawAmount < 0);
		
		// process the withdraw
		double oldBalance = this.balance;
		this.balance -= withdrawAmount;
		System.out.printf("You have successfully withdrew: $%.2f\n", withdrawAmount);
		System.out.printf("Your new balance is now: $%.2f\n", this.balance);
		
		// add new transaction made to list of transactions
		Transaction trans;
		if(memo != "") { // if user wants to add memo
			trans = new Transaction("withdraw", memo, withdrawAmount, oldBalance, this.balance, this);
		}
		else { // else user didn't want to add memo
			trans = new Transaction("withdraw", withdrawAmount, oldBalance, this.balance, this);
		}
		this.transactions.add(trans);
	}
	
	/**
	 * Method called by the User class to perform the deposit onto this account
	 * After the deposit is made, add this deposit into the list of transactions made for this account
	 * @param scanner
	 */
	public void deposit(Scanner scanner) {
		
		// see if the User wants to add a memo, returning an empty String "" means they didn't want a memo
		String memo = getMemo(scanner);
		
		// ask User the amount they want to deposit
		double depositAmount;
		do {			
			this.getSummary();
			System.out.println("Enter amount you want to deposit: ");
			depositAmount = scanner.nextDouble();
			scanner.nextLine(); // moves cursor to nextLine
			
			if(depositAmount <= 0) {
				System.out.println("Deposit amount entered is either a negative amount or 0, please enter a positive value greater than 0.");
			}
		} while(depositAmount <= 0);
		
		// process the withdraw
		double oldBalance = this.balance;
		this.balance += depositAmount;
		System.out.printf("You have successfully deposited: $%.2f\n", depositAmount);
		System.out.printf("Your new balance is now: $%.2f\n", this.balance);
		
		// add new transaction made to list of transactions
		Transaction trans;
		if(memo != "") { // if user wants to add memo
			trans = new Transaction("deposit", memo, depositAmount, oldBalance, this.balance, this);
		}
		else { // else user didn't want to add memo
			trans = new Transaction("deposit", depositAmount, oldBalance, this.balance, this);
		}
		this.transactions.add(trans);
	}
	
	/**
	 * Performs the actual transfer of money from this Account to the passed in Account (accTransTo)
	 * Called from the User Class, transferFunds Method
	 * @param scanner		Scanner object used to get user input
	 * @param accTransTo	Account object we are transferring money TO from this Account
	 */
	public void transfer(Scanner scanner, Account accTransTo) {
		
		// see if the User wants to add a memo, returning an empty String "" means they didn't want a memo
		String memo = getMemo(scanner);
		
		// ask User the amount they want to transfer
		double transferAmount;
		do {			
			this.getSummary();
			System.out.println("Enter amount you want to transfer: ");
			transferAmount = scanner.nextDouble();
			scanner.nextLine(); // moves cursor to nextLine
			
			if(transferAmount > this.balance || transferAmount < 0) {
				System.out.println("Withdraw amount exceeds the account balance or you entered an invalid amount.");
			}
		} while(transferAmount > this.balance || transferAmount < 0);
		
		// process the transfer
		double oldBalance = this.balance;
		this.balance -= transferAmount;
		accTransTo.transferDeposit(transferAmount); // this method will add the passed in balance to that accounts balance
		
		// Display user that the transfer was successful
		System.out.printf("You have successfully transferred: $%.2f\n", transferAmount);
		System.out.printf("Your new balance is now: $%.2f\n", this.balance);
		
		// add new transaction made to list of transactions
		Transaction trans;
		if(memo != "") { // if user wants to add memo
			trans = new Transaction("transfer", memo, transferAmount, oldBalance, this.balance, this);
		}
		else { // else user didn't want to add memo
			trans = new Transaction("transfer", transferAmount, oldBalance, this.balance, this);
		}
		this.transactions.add(trans);
	}
	
	public void transferDeposit(Double amount) {
		this.balance += amount;
	}
	
	/**
	 * Asks the user if they want to add a memo for their transaction, returns empty String if they chose not to add a memo, else returns the memo
	 * @param scanner
	 * @return String: the memo the user entered, or empty String if they chose not to enter a memo
	 */
	public String getMemo(Scanner scanner) {
		
		String memo = "";
		
		// ask User if they want to add a memo for this withdraw
		String memoOption;
		do {
			System.out.println("Would you like to add a memo for this withdraw? (Y or N)");
			memoOption = scanner.nextLine();
			
			if(memoOption != "Y" || memoOption != "N" || memoOption != "y" || memoOption != "n") {
				System.out.println("Invalid response entered. Please type Y for yes and N for no.");
			}
		} while(memoOption != "Y" || memoOption != "N" || memoOption != "y" || memoOption != "n");
		
		// process the memo option chosen if they chose Y or y for yes to add a memo
		if(memoOption == "Y" || memoOption == "y") {
			System.out.println("Please enter your memo:");
			memo = scanner.nextLine();
		}
		
		return memo;
	}
	
	/**
	 * Prints transaction history for this account, called from User class showTransHistory() method
	 */
	public void printTransactions() {
		
		System.out.printf("Transaction history for %s account:\n", this.name);
		for(int t = 0; t < this.transactions.size(); t++) {
			this.transactions.get(t).printTransaction();
		}
	}
	
	/**
	 * Returns the account's UUID
	 * @return 	String object this accounts UUID
	 */
	public String getUUID() {
		return this.uuid;
	}
	
	/**
	 * Returns which User owns this account
	 * @return	User object that owns this account
	 */
	public User getUser() {
		return this.user;
	}
	
	/**
	 * Returns which bank this account was issued from
	 * @return	Bank object that this account belongs to
	 */
	public Bank getBank() {
		return this.bank;
	}
	
	/**
	 * Returns this accounts current balance
	 * @return	double value of this accounts current balance
	 */
	public double getBalance() {
		return this.balance;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getSummary() {
		
		// format the summary line, depending on whether the balance is negative
		if(balance >= 0) {
			return String.format("%s : $%.02f : %s", this.uuid, this.balance, this.name);
		}
		else {
			return String.format("%s : $(%.02f) : %s", this.uuid, balance, this.name);
		}
	}

}
