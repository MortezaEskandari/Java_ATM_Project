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
	
	public void withdraw(Scanner scanner) {
		
		// see if the User wants to add a memo, returning an empty String "" means they didn't want a memo
		String memo = getMemo(scanner);
		
		// ask User the amount they want to withdraw
		double withdrawAmount;
		do {			
			this.getSummary();
			System.out.println("Enter amount you want to withdrawl: ");
			withdrawAmount = scanner.nextDouble();
			
			if(withdrawAmount > this.balance || withdrawAmount < 0) {
				System.out.println("Withdrawl amount exceeds the account balance or you entered an invalid amount.");
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
			trans = new Transaction("withdraw", memo, withdrawAmount, oldBalance, this);
		}
		else { // else user didn't want to add memo
			trans = new Transaction("withdraw", withdrawAmount, oldBalance, this);
		}
		this.transactions.add(trans);
	} //
	
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
