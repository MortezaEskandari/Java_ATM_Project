import java.util.HashMap;
import java.util.Scanner;

public class ATM {
	
	private HashMap<String,Bank> banks;
	
	public ATM() {
		this.banks = new HashMap<String,Bank>();
	}
	
	/**
	 * First method called when running the program, gets user input for the ATM login prompt
	 */
	public void loginPrompt() {
		
		Scanner scanner = new Scanner(System.in);
		
		// Prompt user some options to choose from
		int option;
		System.out.println("Welcome to the ATM, please select an option below.");
		do {
			System.out.println(" 1) Login.");
			System.out.println(" 2) Create account.");
			System.out.println(" 3) Quit.");
			System.out.print("Select option 1-3: ");
			option = scanner.nextInt();
			scanner.nextLine(); // moves cursor to nextLine
			
			if(!(option >= 1 && option <= 3)) {
				System.out.println("Invalid option. Please choose 1-3.");
			}
		} while(!(option >= 1 && option <= 3));
		
		// Process the option chosen by the user
		if(option == 1) {
			loginATM(scanner);
		}
		else if(option == 2) {
			createAccount(scanner);
		}
		else if(option == 3) {
			System.out.println("Quitting Application . . .");
		}
	}
	
	/*
	 * Method used to login to the ATM and gain access to User Account
	 * User will input which bank they are using
	 * User will input their login credentials (user ID + pin)
	 * @param scanner	the scanner object for getting user input
	 */
	public void loginATM(Scanner scanner) {
		
		boolean enterBank = true;	// Boolean condition for asking user to enter bank name or not
		String bankName = "";	// Store bank name given by user input
		String userID = "";	// Store user ID given by user input
		String pin = "";	// Store 4 digit pin given by user input
		int option = 1; // Used to get user input
		
		// Ask user to enter bank name Account is located at and login credentials (user ID + pin) to login
		do {
			// Ask user to enter bank their account is located at
			if(enterBank) {
				System.out.print("\nEnter bank name: ");
				bankName = scanner.nextLine();
			}
			
			// Ask for user ID
			System.out.print("Enter user ID: ");
			userID = scanner.nextLine();
			
			// Ask for 4-digit pin, loop until valid 4 digit pin
			boolean invalidPin = false; // used for the loop, set true when invalid pin entered, false when valid
			do {
				System.out.print("Enter 4-digit pin: ");
				pin = scanner.nextLine();
				
				invalidPin = false;
				try{
		            int intPin = Integer.parseInt(pin);
		            if(pin.length() != 4 || intPin >= 10000 || intPin < 0) {
		            	System.out.println("\nInvalid pin number. Please enter only 1 digit number per digit. Example pin (1234,0000,4321).\n");
		            	invalidPin = true;
		            }
		        }
		        catch (NumberFormatException ex){
		            System.out.println("\nThe pin you entered was invalid. Make sure to only enter a 4-digit pin. Each digit is a number between 0-9.\n");
		            invalidPin = true;
		        }
			} while(invalidPin);
			
			// Check if Account exists in Bank, Enter statement if Bank object doesn't exist or if Bank object exists but Account doesn't
			if(this.banks.get(bankName) == null || this.banks.get(bankName).existingUserID(userID) == false) {
				System.out.printf("\nAccount doesn't exist with %s bank.\n\n", bankName);
				
				// Loop until user chooses an option 1-3 to proceed
				do {
					System.out.println("1) Try again.");
					System.out.println("2) Go back to menu.");
					System.out.println("3) Quit.");
					System.out.print("Select option 1-3: ");
					option = scanner.nextInt();
					scanner.nextLine(); // moves cursor to nextLine
					enterBank = true; // If user chooses option 1 to try again, ask user to enter bankName again + userID + pin
					
					if(!(option >= 1 && option <= 3)) {
						System.out.println("Invalid option. Please choose 1-3.");
					}
				} while(!(option >= 1 && option <= 3));
			}
			else if(this.banks.get(bankName).userLogin(userID, pin) == null) {
				System.out.println("Invalid user ID or pin.");
				
				do {
					System.out.println("1) Try again.");
					System.out.println("2) Go back to menu.");
					System.out.println("3) Quit.");
					System.out.print("Select option 1-3: ");
					option = scanner.nextInt();
					scanner.nextLine(); // moves cursor to nextLine
					enterBank = false; // If user chooses option 1 to try again, don't ask user to enter bankName again, just userID + pin
					
					if(!(option >= 1 && option <= 3)) {
						System.out.println("Invalid option. Please choose 1-3.");
					}
				} while(!(option >= 1 && option <= 3));
			}
			else { // Enter this statement if Account exists and proper login credentials given
				User user = this.banks.get(bankName).userLogin(userID, pin);
				this.printUserMenu(bankName, this.banks.get(bankName), user, userID, pin, scanner);
				option = 2;
			}
			
			if(option == 3) {
				System.out.println("Quitting Application . . .");
			}
			else if(option == 2) {
				System.out.println();
				this.loginPrompt();
			}
		} while(option == 1);
	}
	
	/**
	 * Create a new Account for Logging into the ATM and bank
	 * @param scanner	the scanner object for getting user input
	 */
	public void createAccount(Scanner scanner) {
		
		// prompt user to enter which bank they want to create an account with.
		System.out.print("\nEnter the name of the Bank you'd like to open an account with: ");
		String newBankName = scanner.nextLine();
		
		// Check if bank already exists in HashMap or not, if yes pull that Bank object out, else create new Bank object and add to HashMap
		Bank newBank;
		if(this.banks.containsKey(newBankName)) {
			newBank = this.banks.get(newBankName);
		}
		else {
			newBank = new Bank(newBankName);
			this.banks.put(newBankName, newBank);
		}
		
		// ask for user's first and last name
		System.out.print("Enter your first name: ");
		String firstName = scanner.nextLine();
		System.out.print("Enter your last name: ");
		String lastName = scanner.nextLine();
		
		// ask for the user to input a unique userID
		String newUserID;
		do {
			System.out.print("Create your userID: ");
			newUserID = scanner.nextLine();
			
			if(newBank.existingUserID(newUserID)) {
				System.out.println("\nThis user ID already exists, please try something else.\n");
			}
		} while(newBank.existingUserID(newUserID));
		
		// ask for user pin number
		String newPin = "";
		boolean invalidPin = false;
		do {
			System.out.print("Create your 4-digit pin number: ");
			newPin = scanner.nextLine();
			
			invalidPin = false;
	        try{
	            int intPin = Integer.parseInt(newPin);
	            if(newPin.length() != 4 || intPin >= 10000 || intPin < 0) {
	            	System.out.println("Invalid pin number. Please enter only 1 digit number per digit. Example pin (1234,0000,4321).");
	            	invalidPin = true;
	            }
	        }
	        catch (NumberFormatException ex){
	            System.out.println("The pin you entered was invalid. Make sure to only enter a 4-digit pin. Each digit is a number between 0-9.");
	            invalidPin = true;
	        }
			
		} while(invalidPin);
		
		// add user to the bank system
		newBank.addUser(firstName, lastName, newUserID, newPin);
		
		System.out.printf("\nCongratulations! Your new bank account with %s Bank is now active and ready for your login.\n\n", newBankName);	
			
		this.loginPrompt();
	}
	
	public void printUserMenu(String bankName, Bank bank, User user, String userID, String pin, Scanner scanner) {
		
		// print a summary of the user's accounts
		user.printAccountsSummary();
		
		// initialize
		int choice;
		
		// user menu
		do {
			System.out.printf("Welcome to %s Bank %s, what would you like to do?\n", bankName, user.getName());
			System.out.println(" 1) Show account transaction history.");
			System.out.println(" 2) Withdraw.");
			System.out.println(" 3) Deposit.");
			System.out.println(" 4) Transfer.");
			System.out.println(" 5) Create New Account");
			System.out.println(" 6) Logout.\n");
			System.out.print("Enter choice: ");
			choice = scanner.nextInt();
			scanner.nextLine(); // moves cursor to nextLine
			
			// print message to user if they chose an invalid choice and tell to try again.
			if(!(choice >= 1 && choice <= 6)) {
				System.out.println("Invalid choice. Please choose 1-6.");
			}
			
		} while(!(choice >= 1 && choice <= 6)); // continue to loop until a valid choice is given by user
		
		processUserChoice(choice, bankName, bank, user, userID, pin, scanner);
	}
	
	public void processUserChoice(int choice, String bankName, Bank bank, User user, String userID, String pin, Scanner scanner) {
		
		// process the choice
		switch(choice) {
		
		case 1:
			user.showTransHistory(scanner);
			break;
		case 2:
			user.withdrawFunds(scanner);
			break;
		case 3:
			user.depositFunds(scanner);
			break;
		case 4:
			user.transferFunds(scanner);
			break;
		case 5:
			user.createAccount(scanner);
		case 6:
			System.out.println("\nLogging out of account...\n");
			break;
		}
		
		if(choice != 6) {
			printUserMenu(bankName, bank, user, userID, pin, scanner);
		}
	}

}
