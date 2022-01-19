import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Bank {
	
	/*
	 *  Name of the bank.
	 */
	private String name;
	
	/*
	 *  List of users using this bank.
	 *  Key: (String) unique userID for each (User)
	 *  Value: (User) user object
	 */
	private HashMap<String,User> users;
	
	/*
	 *  List of accounts for each user.
	 */
	private ArrayList<Account> accounts;
	
	/**
	 * Constructor method: Create a new Bank object with empty lists of users and accounts
	 * @param name		the name of the bank
	 */
	public Bank(String name) {
		
		this.name = name;
		this.users = new HashMap<String,User>();
		this.accounts = new ArrayList<Account>();
	}
	
	/**
	 * Generate a new universally unique ID (UUID) for an account
	 * @return String	the uuid
	 */
	public String getNewAccountUUID() {
		
		// initialize
		String uuid;
		Random rng = new Random();
		int length = 10;
		boolean nonUnique;
		
		// continue looping until we get a unique ID
		do {
			
			// generate the number
			uuid = "";
			for(int c = 0; c < length; c++) {
				uuid += ((Integer)rng.nextInt(10)).toString();
			}
			
			// check to make sure it's unique
			nonUnique = false;
			for(Account a : this.accounts) {
				if(uuid.compareTo(a.getUUID()) == 0) {
					
					nonUnique = true;
					break;
				}
			}
			
		} while(nonUnique);
		
		return uuid;
	}

	/**
	 * Adds an account to the accounts ArrayList
	 * @param account
	 */
	public void addAccount(Account account) {
		this.accounts.add(account);
	}
	
	/**
	 *  Create a new user for this bank
	 * @param firstName		the user's first name
	 * @param lastName		the user's last name
	 * @param pin			the user's pin
	 * @return				the new User object
	 */
	public User addUser(String firstName, String lastName, String userID, String pin) {
		
		// create a new User object and add it to our list of Users
		User newUser = new User(firstName, lastName, userID, pin, this);
		this.users.put(userID,newUser);
		
		// create a savings account for the user and add to User and Bank accounts lists
		Account newAccount = new Account("Savings", newUser, this);
		//String uuid = newAccount.getUUID();
		newUser.addAccount("Savings",newAccount);
		this.addAccount(newAccount);
		
		return newUser;
	}
	
	/**
	 * Check if the user exists in this bank by using the userID + pin combo
	 * @param userID	the userID to find the User object
	 * @param pin		the pin used to find the User object
	 * @return			the User object if true, and null if false
	 */
	public User userLogin(String userID, String pin) {
		
		// if user ID key exists in the users HashMap AND the pin for that User is valid then return the User
		if(this.users.containsKey(userID) && this.users.get(userID).validatePin(pin)) {
			return this.users.get(userID);
		}
		
		return null;
	}
	
	public boolean existingUserID(String userID) {
		return this.users.containsKey(userID);
	}
	
	/**
	 * Getter method to get the name of this bank
	 * @return		the name of the bank (String)
	 */
	public String getName() {
		return this.name;
	}
	
}
