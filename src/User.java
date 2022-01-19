import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User {

	/*
	 *  First name of the user.
	 */
	private String firstName;
	
	/*
	 *  Last name of the user.
	 */
	private String lastName;
	
	/*
	 *  The ID number of the user.
	 */
	private String userID;
	
	/*
	 *  The MD5 hash of the user's pin number.
	 */
	private byte pinHash[];
	
	/*
	 * the Bank object that this User is a customer of
	 */
	private Bank bank;
	/*
	 *  ArrayList of accounts this User has
	 */
	private ArrayList<Account> listAccounts;
	
	/*
	 * HashMap of the accounts this User has
	 */
	private HashMap<String,Account> hashAccounts;
	
	/*
	 * Constructor class, create's a new user
	 * prints a log message showing the user's last and first name and UUID
	 * @param firstName		the user's first name
	 * @param lastName		the user's last name
	 * @param pin			the user's account pin number
	 * @param bank			the Bank object that the user is a customer of
	 */
	public User(String firstName, String lastName, String userID, String pin, Bank bank) {	
		
		// set user's name
		this.firstName = firstName;
		this.lastName = lastName;
		
		// store the user's ID for logins
		this.userID = userID;
		
		// set the Bank object
		this.bank = bank;
		
		// store the pin's MD5 hash, rather than the original value, for security purposes
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			this.pinHash = md.digest(pin.getBytes());
		} catch (NoSuchAlgorithmException e) {
			System.err.println("error, caught NoSuchAlgorithmException");
			e.printStackTrace();
			System.exit(1);
		}
		
		// create empty list of accounts and banks(instantiate the HashMap,ArrayList)
		this.listAccounts = new ArrayList<Account>();
		this.hashAccounts = new HashMap<String,Account>();
	}
	
	/*
	 * Add an account for the user
	 * @param account	the account to add
	 */
	public void addAccount(String uuid, Account account) {
		this.listAccounts.add(account);
		this.hashAccounts.put(uuid,account);
	}
	
	
	/**
	 * Getter Method to return the userID of this User
	 * @return String	this.uuid
	 */
	public String getUserID() {

		return this.userID;
	}
	
	/**
	 * Check whether a given pin matches the true User pin
	 * @param pin	the pin to check
	 * @return		whether the pin is valid (true) or not (false)
	 */
	public boolean validatePin(String pin) {
		
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			return MessageDigest.isEqual(md.digest(pin.getBytes()), this.pinHash);
		} catch (NoSuchAlgorithmException e) {
			System.err.println("error, caught NoSuchAlgorithmException");
			e.printStackTrace();
			System.exit(1);
		}
		
		return false;
	}
	
	/**
	 * Method called from ATM.java class, user chose to make a withdraw
	 * this method is used by the user to choose which account they want to withdraw from
	 * after we got the account object we want to make a withdraw from
	 * we call on the withdraw method from that account object to do the withdraw
	 * @param scanner
	 */
	public void withdrawFunds(Scanner scanner) {
		
		String accountName;
		do {
			System.out.print("Which account do you want to make a withdraw from?: ");
			accountName = scanner.nextLine();
			
			if(!(this.hashAccounts.containsKey(accountName)) || this.hashAccounts.get(accountName).getBalance() <= 0) {
				int option;
				do {
					// Print to user invalid account type to withdraw, either it doesn't exist or no balance, then display all accounts available to choose from
					System.out.println("\n\nThe entered account does not exist or the balance of the account is zero.\nPlease try a different account or go back to the main menu.");
					this.printAccountsSummary();
					
					// Give user options to choose from
					System.out.println("Choose one of the following options:");
					System.out.println("1) Try another account.");
					System.out.println("2) Go back to main menu.");
					System.out.print("Enter option: ");
					option = scanner.nextInt();
					scanner.nextLine();
					
					if(option != 1 && option != 2) {
						System.out.println("Please select either option 1 or 2.");
					}
				} while(option != 1 && option != 2);
				
				// process the option chosen by user
				if(option == 1) {
					this.printAccountsSummary();
				}
				else { // has to be option 2, go back to main menu
					return;
				}
			}
		} while(!(this.hashAccounts.containsKey(accountName)) || this.hashAccounts.get(accountName).getBalance() <= 0);
		
		// process the withdraw from the chosen account
		Account acc = this.hashAccounts.get(accountName);
		acc.withdraw(scanner);
	}
	
	/**
	 * Method called from ATM.java class, user chose to make a deposit
	 * this method is used by the user to choose which account they want to deposit to
	 * after we got the account object we want to make a deposit to
	 * we call on the deposit method from that account object to do the withdraw
	 * @param scanner
	 */
	public void depositFunds(Scanner scanner) {
		
		String accountName;
		do {
			System.out.println("Which account do you want to make a deposit to?: ");
			accountName = scanner.nextLine();
			
			if(!(this.hashAccounts.containsKey(accountName))) {
				System.out.println("This account doesn't exist. Please enter the name of one of the accounts you own.");
				this.printAccountsSummary();
			}
		} while(!(this.hashAccounts.containsKey(accountName)));
		
		// process the withdraw from the chosen account
		Account acc = this.hashAccounts.get(accountName);
		acc.deposit(scanner);
	}
	
	/**
	 * Method called from ATM.java class, user chose to make a transfer
	 * this method is used by the user to choose which account they want to transfer from
	 * after we got the account object we want to make a transfer from
	 * user chooses which account to transfer to as well
	 * we call on the transfer method from that account object to perform the transfer
	 * @param scanner
	 */
	public void transferFunds(Scanner scanner) {
		
		// Ask user to choose which account they want to transfer FROM
		String accNameTransFrom;
		do {
			System.out.println("Which account do you want to transfer from?: ");
			accNameTransFrom = scanner.nextLine();
			
			if(!(this.hashAccounts.containsKey(accNameTransFrom)) || this.hashAccounts.get(accNameTransFrom).getBalance() <= 0) {
				System.out.println("This account doesn't exist or it has a either a zero or negative balance. Please choose a different account.");
				this.printAccountsSummary();
			}
		} while(!(this.hashAccounts.containsKey(accNameTransFrom)) || this.hashAccounts.get(accNameTransFrom).getBalance() <= 0);
		
		// Get the account object from the ArrayList of accounts that we want to transfer FROM
		Account accTransFrom = this.hashAccounts.get(accNameTransFrom);
		
		// Ask user to choose which account they want to transfer TO
		String accNameTransTo;
		do {
			System.out.println("Which account do you want to transfer to?: ");
			accNameTransTo = scanner.nextLine();
			
			if(!(this.hashAccounts.containsKey(accNameTransTo)) || accNameTransTo == accNameTransFrom) {
				System.out.println("This account doesn't exist or you chose the same account you want to transfer from. Please try again.");
				this.printAccountsSummary();
				System.out.printf("You are transferring FROM your %s account.", accNameTransFrom);
			}
		} while(!(this.hashAccounts.containsKey(accNameTransTo)) || accNameTransTo != accNameTransFrom);
		
		// Get the account object from the ArrayList of accounts that we want to transfer TO
		Account accTransTo = this.hashAccounts.get(accNameTransTo);
		
		accTransFrom.transfer(scanner, accTransTo);
	}
	
	/**
	 * Method called from ATM.java class, user chose to see transaction history
	 * this method is used by the user to choose which account they want to see the transaction history of
	 * after we got the account object we want to see the transaction history of
	 * we call on the printTransactions method from that account object to print it to the screen
	 * @param scanner
	 */
	public void showTransHistory(Scanner scanner) {
		
		// Ask user which account to show the transaction history of
		String accountName;
		do {
			this.printAccountsSummary();
			System.out.print("Which account do you want to see the transaction history of?: ");
			accountName = scanner.nextLine();
			
			if(!(this.hashAccounts.containsKey(accountName))) {
				System.out.println("This account doesn't exist. Please enter the name of one of the accounts you own.");
				this.printAccountsSummary();
			}
		} while(!(this.hashAccounts.containsKey(accountName)));
		
		// display the transaction history for the chosen account
		Account acc = this.hashAccounts.get(accountName);
		acc.printTransactions();
	}
	
	/**
	 * 
	 * @param scanner
	 */
	public void createAccount(Scanner scanner) {
		
		// Ask user the name of the new account
		String accountName;
		do {
			this.printAccountsSummary();
			System.out.print("Enter new account name: ");
			accountName = scanner.nextLine();
			
			if(this.hashAccounts.containsKey(accountName)) {
				System.out.println("\nThis account exists. Please enter a unique account name.\n");
			}
		} while(this.hashAccounts.containsKey(accountName));
		
		// generate the account object
		Account newAccount = new Account(accountName, this, this.bank);
		
		// add it to the list of accounts for this user and for the bank
		this.listAccounts.add(newAccount);
		this.bank.addAccount(newAccount);
	}
	
	/**
	 * Getter method for getting users first and last name
	 * @return String: firstName + " " + lastName
	 */
	public String getName() {
		return this.firstName + " " + this.lastName;
	}
	
	/**
	 * Getter method for getting the Bank object this User is a customer of
	 * @return Bank object: this.bank
	 */
	public Bank getBank() {
		return this.bank;
	}
	
	/**
	 * Prints the list of accounts this User has, "UUID, Balance, Account Name"
	 */
	public void printAccountsSummary() {
		
		System.out.printf("\n%s's accounts summary\n", this.firstName);
		for(int a = 0; a < this.listAccounts.size(); a++) {
			System.out.printf("ACCOUNT %d) %s\n", a+1, this.listAccounts.get(a).getSummary());
		}
		System.out.println();
	}

}
