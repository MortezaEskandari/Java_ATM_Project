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
	
	public void withdrawFunds(Scanner scanner) {
		
		String accountName;
		do {
			System.out.println("Which account do you want to make a withdrawl from?: ");
			accountName = scanner.nextLine();
			
			if(!(this.hashAccounts.containsKey(accountName))) {
				System.out.println("This account doesn't exist. Please enter the name of one of the accounts you own.");
				this.printAccountsSummary();
			}
		} while(!(this.hashAccounts.containsKey(accountName)));
		
		// process the withdrawl from the chosen account
		Account acc = this.hashAccounts.get(accountName);
		acc.withdraw(scanner);
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
		
		System.out.printf("\n\n%s's accounts summary\n", this.firstName);
		for(int a = 0; a < this.listAccounts.size(); a++) {
			System.out.printf("%d) %s\n", this.listAccounts.get(a).getSummary());
		}
		System.out.println();
	}

}
