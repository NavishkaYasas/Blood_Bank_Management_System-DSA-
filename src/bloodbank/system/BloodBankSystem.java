package bloodbank.system;

import java.util.Scanner;

public class BloodBankSystem {

    private final Scanner sc = new Scanner(System.in);

    public void run() {
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

    private void showMainMenu() {
        System.out.println("\n----------------- MAIN MENU -----------------");
        System.out.println("1. Donor Registration");
        System.out.println("2. Login as Admin");
        System.out.println("3. Blood Request");
        System.out.println("4. Exit");
        System.out.println("-----------------------------------------------");
    }

    // ---- placeholders: we'll build these out feature by feature ----
    private void donorRegistrationFlow() {
        System.out.println("[Donor Registration] Not implemented yet.");
    }

    private void adminLoginFlow() {
        System.out.println("[Admin Login] Not implemented yet.");
    }

    private void bloodRequestFlow() {
        System.out.println("[Blood Request] Not implemented yet.");
    }

    // ---- input helpers ----
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