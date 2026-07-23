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
        adminMenu();
    }

    private void adminMenu() {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\n----------------- ADMIN MENU -----------------");
            System.out.println("1. View / Manage Donors");
            System.out.println("2. Manage Blood Inventory");
            System.out.println("3. Process Blood Requests");
            System.out.println("4. View Transaction History");
            System.out.println("5. Sort Donor / Inventory Records");
            System.out.println("6. Branch / Distribution Network");
            System.out.println("7. Logout");
            System.out.println("------------------------------------------------");
            int choice = readInt("Enter your choice: ", 1, 7);
            switch (choice) {
                case 1 -> manageDonorsMenu();
                case 2 -> manageInventoryMenu();
                case 3 -> processRequestsMenu();
                case 4 -> viewTransactionHistory();
                case 5 -> sortMenu();
                case 6 -> branchNetworkMenu();
                case 7 -> loggedIn = false;
            }
        }
    }

    // ---- placeholders: each becomes a real feature later ----
    private void manageDonorsMenu() {
        System.out.println("[View/Manage Donors] Not implemented yet.");
    }

    private void manageInventoryMenu() {
        System.out.println("[Manage Blood Inventory] Not implemented yet.");
    }

    private void processRequestsMenu() {
        System.out.println("[Process Blood Requests] Not implemented yet.");
    }

    private void viewTransactionHistory() {
        System.out.println("[View Transaction History] Not implemented yet.");
    }

    private void sortMenu() {
        System.out.println("[Sort Donor/Inventory Records] Not implemented yet.");
    }

    private void branchNetworkMenu() {
        System.out.println("[Branch/Distribution Network] Not implemented yet.");
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