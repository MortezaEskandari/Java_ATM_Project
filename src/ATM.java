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
			System.out.println(" 1) Create a new bank account with a Bank of your choice.");
			System.out.println(" 2) Login to an existing bank account.");
			System.out.println(" 3) Quit.");
			option = scanner.nextInt();
			scanner.nextLine(); // moves cursor to nextLine
			
			if(!(option >= 1 && option <= 3)) {
				System.out.println("Invalid option. Please choose 1-3.");
			}
			if(option == 2 && this.banks.isEmpty()) {
				System.out.println("You currently have no existing accounts in any banks. Please first open an account with a bank.\n");
				option = 1;
			}
		} while(!(option >= 1 && option <= 3));
		
		// process the option with the processLoginOption method, passing the option and scanner
		processLoginOption(option, scanner);
	}
	
	/**
	 * Processes the option chosen in the loginPrompt method
	 * @param option	the option chosen in loginPrompt method
	 * @param scanner	the scanner object for getting user input
	 */
	public void processLoginOption(int option, Scanner scanner) {
		
		// process the option
		switch(option) {
		
		case 1:
			// prompt user to enter which bank they want to create an account with.
			System.out.print("Enter the name of the Bank you'd like to open an account with: ");
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
				System.out.print("Please enter a unique user ID to be used for login to your bank account: ");
				newUserID = scanner.nextLine();
				
				if(newBank.existingUserID(newUserID)) {
					System.out.println("\nThis user ID already exists, please try something else.\n");
				}
			} while(newBank.existingUserID(newUserID));
			
			// ask for user pin number
			String newPin = "";
			boolean invalidPin = false;
			do {
				System.out.print("Enter your 4-digit pin number: ");
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
			break;
			
		case 2:
			// Display
			// ask user to enter the Bank that their account is located at
			String bankName;
			do {
				System.out.print("Enter the Bank your account is registerd in: ");
				bankName = scanner.nextLine();
				
				// check if the bank exists in the HashMap for when new accounts are made
				if(!(this.banks.containsKey(bankName))) {
					System.out.printf("No accounts exists in %s Bank, please try again. Make sure to enter the Bank your account is registered in.\n", bankName);
				}
			} while(!(this.banks.containsKey(bankName)));
			
			Bank bank = this.banks.get(bankName);
			
			// ask user to enter their user ID and pin to login to their account
			System.out.print("Enter userID: ");
			String userID = scanner.nextLine();
			System.out.print("Enter pin: ");
			String pin = scanner.nextLine();
		
			// if user ID or pin is invalid, ask the user some options
			User user = bank.userLogin(userID, pin);
			if(user == null) {
				boolean loop = true;
				boolean sameBank = true;
				while(loop) {
					if(sameBank) {
						do {
							System.out.println("Invalid userID or pin or account exists in another Bank and not in this one.");
							System.out.println("Please select an option below.");
							System.out.println(" 1) Try again.");
							System.out.println(" 2) Choose a different Bank.");
							System.out.println(" 3) Quit.");
							option = scanner.nextInt();
							scanner.nextLine(); // moves cursor to nextLine
							
							if(!(option >= 1 && option <= 3)) {
								System.out.println("Invalid option. Please choose 1-3.");
							}
						} while(!(option >= 1 && option <= 3));
					}
					
					if(option == 1) {
						System.out.print("Enter userID: ");
						userID = scanner.nextLine();
						System.out.print("Enter pin: ");
						pin = scanner.nextLine();
						user = bank.userLogin(userID, pin);
						if(user != null) {
							loop = false;
						}
						sameBank = true;
					}
					else if(option == 2) {
						do {
							System.out.print("Enter the Bank your account is registerd in: ");
							bankName = scanner.nextLine();
							
							// check if the bank exists in the HashMap for when new accounts are made
							if(!(this.banks.containsKey(bankName))) {
								System.out.printf("No accounts exists in %s Bank, please try again. Make sure to enter the Bank your account is registered in.\n", bankName);
							}
						} while(!(this.banks.containsKey(bankName)));
						
						bank = this.banks.get(bankName);
						sameBank = false;
						option = 1;
					}
					else {
						System.out.println("Quitting Application...");
						System.exit(1);
					}
				}
			}
			this.printUserMenu(bankName, bank, user, userID, pin, scanner);
			break;
			
		case 3:
			System.out.println("Quitting Application...");
			System.exit(1);
		}
		
		loginPrompt();
		
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
