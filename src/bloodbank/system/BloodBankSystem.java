package bloodbank.system;

import java.util.Scanner;

// Main class that runs the Blood Bank Management System
public class BloodBankSystem {

    // Scanner for reading user input
    private final Scanner sc = new Scanner(System.in);

    // Start the system and show the main menu in a loop
    public void run() {
        System.out.println("======================================================");
        System.out.println("   WELCOME TO THE BLOOD BANK MANAGEMENT SYSTEM");
        System.out.println("======================================================");

        boolean running = true;
        while (running) {
            showMainMenu();
            int choice = readInt("Enter your choice: ", 1, 4);
            switch (choice) {
                case 1 -> donorRegistrationFlow(); // Register a donor
                case 2 -> adminLoginFlow();         // Admin login
                case 3 -> bloodRequestFlow();       // Request blood
                case 4 -> running = false;          // Exit
            }
        }
        System.out.println("All data saved. Goodbye!");
    }

    // Display the main menu options
    private void showMainMenu() {
        System.out.println("\n----------------- MAIN MENU -----------------");
        System.out.println("1. Donor Registration");
        System.out.println("2. Login as Admin");
        System.out.println("3. Blood Request");
        System.out.println("4. Exit");
        System.out.println("-----------------------------------------------");
    }

    // Placeholder - donor registration (not implemented yet)
    private void donorRegistrationFlow() {
        System.out.println("[Donor Registration] Not implemented yet.");
    }

    // Placeholder - admin login (not implemented yet)
    private void adminLoginFlow() {
        System.out.println("[Admin Login] Not implemented yet.");
    }

    // Placeholder - blood request (not implemented yet)
    private void bloodRequestFlow() {
        System.out.println("[Blood Request] Not implemented yet.");
    }

    // Read a line of text from the user
    private String readLine(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    // Read an integer from the user within a given range
    private int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            try {
                int val = Integer.parseInt(input);
                if (val >= min && val <= max) return val; // Valid input
                System.out.println("Please enter a number between " + min + " and " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("Invalid number, please try again.");
            }
        }
    }
}