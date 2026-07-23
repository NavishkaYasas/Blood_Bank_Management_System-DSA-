package bloodbank.system;

import bloodbank.io.AdminFileManager;
import bloodbank.model.Admin;

import java.util.Scanner;

public class BloodBankSystem {

    private static final String DATA_DIR = "data/";
    private static final String ADMIN_FILE = DATA_DIR + "admin.txt";

    private final AdminFileManager adminFileManager = new AdminFileManager();
    private Admin admin;

    private final Scanner sc = new Scanner(System.in);

    public void run() {
        loadAllData();

        System.out.println("======================================================");
        System.out.println("   WELCOME TO THE BLOOD BANK MANAGEMENT SYSTEM");
        System.out.println("======================================================");

        boolean running = true;
        while (running) {
            showMainMenu();
            int choice = readInt("Enter your choice: ", 1, 4);
            switch (choice) {
                case 1 -> donorRegistrationFlow();
                case 2 -> adminLoginFlow();
                case 3 -> bloodRequestFlow();
                case 4 -> running = false;
            }
        }
        System.out.println("All data saved. Goodbye!");
    }

    private void loadAllData() {
        admin = adminFileManager.loadAdmin(ADMIN_FILE);
    }

    private void showMainMenu() {
        System.out.println("\n----------------- MAIN MENU -----------------");
        System.out.println("1. Donor Registration");
        System.out.println("2. Login as Admin");
        System.out.println("3. Blood Request");
        System.out.println("4. Exit");
        System.out.println("-----------------------------------------------");
    }

    private void donorRegistrationFlow() {
        System.out.println("[Donor Registration] Not implemented yet.");
    }

    private void adminLoginFlow() {
        System.out.println("\n------------- ADMIN LOGIN -------------");
        String u = readLine("Username: ");
        String p = readLine("Password: ");
        if (!admin.login(u, p)) {
            System.out.println("Invalid credentials.");
            return;
        }
        System.out.println("Login successful. Welcome, " + admin.getUsername() + "!");
        // adminMenu() will go here once we build it in a future step
    }

    private void bloodRequestFlow() {
        System.out.println("[Blood Request] Not implemented yet.");
    }

    private String readLine(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    private int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            try {
                int val = Integer.parseInt(input);
                if (val >= min && val <= max) return val;
                System.out.println("Please enter a number between " + min + " and " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("Invalid number, please try again.");
            }
        }
    }
}