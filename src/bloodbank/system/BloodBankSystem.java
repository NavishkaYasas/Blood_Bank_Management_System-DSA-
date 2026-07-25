package bloodbank.system;

import bloodbank.io.AdminFileManager;
import bloodbank.io.DonorFileManager;
import bloodbank.model.Admin;
import bloodbank.model.Donor;
import bloodbank.search.LinearSearch;
import bloodbank.structures.DonorLinkedList;

import java.util.List;
import java.util.Scanner;

public class  BloodBankSystem {

    private static final String DATA_DIR = "data/";
    private static final String ADMIN_FILE = DATA_DIR + "admin.txt";
    private static final String DONORS_FILE = DATA_DIR + "donors.txt";

    public static final String[] BLOOD_GROUPS = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};

    private final AdminFileManager adminFileManager = new AdminFileManager();
    private final DonorFileManager donorFileManager = new DonorFileManager();
    private final DonorLinkedList donorList = new DonorLinkedList();
    private Admin admin;
    private int donorCounter = 1;

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
        for (Donor d : donorFileManager.loadFromFile(DONORS_FILE)) {
            donorList.insert(d);
        }
        donorCounter = donorList.size() + 1;
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
        System.out.println("\n------------- DONOR REGISTRATION -------------");
        String id = "D" + String.format("%04d", donorCounter++);
        String name = readLine("Full Name: ");
        int age = readInt("Age: ", 16, 70);
        String contact = readLine("Contact Number: ");
        String address = readLine("Address: ");

        String bloodGroup = chooseBloodGroup();

        System.out.println("\n-- Health Questionnaire --");
        StringBuilder answers = new StringBuilder();
        answers.append("Weight>50kg:").append(readLine("Are you over 50kg? (yes/no): "));
        answers.append(";ChronicIllness:").append(readLine("Any chronic illness? (yes/no): "));
        answers.append(";RecentSurgery:").append(readLine("Recent surgery in last 6 months? (yes/no): "));
        answers.append(";OnMedication:").append(readLine("Currently on medication? (yes/no): "));
        String lastDonation = readLine("Last donation date (yyyy-MM-dd, or 'none'): ");
        if (lastDonation.equalsIgnoreCase("none") || lastDonation.isBlank()) {
            lastDonation = "1970-01-01";
        }

        System.out.println("\n[Upload Medical Certificate (PDF)]");
        System.out.println("--> This button is a placeholder for a future feature and is not yet functional.");
        boolean certUploaded = false;

        // NOTE: duplicate-ID checking (Set ADT) will be wired in once we build
        // the Hash Table / Set feature - not needed yet since IDs are auto-generated.

        Donor donor = new Donor(id, name, age, contact, address, bloodGroup, lastDonation, answers.toString(), certUploaded);
        donorList.insert(donor);
        donorFileManager.saveToFile(DONORS_FILE, donorList.traverse());

        System.out.println("\nRegistration successful! Your Donor ID is: " + id);
        System.out.println("Eligibility status: " + (donor.isEligible()
                ? "Eligible to donate" : "Not yet eligible (must wait 90 days from last donation)"));
    }

    private String chooseBloodGroup() {
        System.out.println("Select Blood Group:");
        for (int i = 0; i < BLOOD_GROUPS.length; i++) {
            System.out.println((i + 1) + ". " + BLOOD_GROUPS[i]);
        }
        int bgChoice = readInt("Choice: ", 1, BLOOD_GROUPS.length);
        return BLOOD_GROUPS[bgChoice - 1];
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

    private void manageDonorsMenu() {
        System.out.println("\n-- View / Manage Donors --");
        System.out.println("1. View all donors (Linked List traversal)");
        System.out.println("2. Search donor by name (Linear Search)");
        System.out.println("3. Back");
        int choice = readInt("Choice: ", 1, 3);
        switch (choice) {
            case 1 -> {
                List<Donor> donors = donorList.traverse();
                if (donors.isEmpty()) System.out.println("No donors registered yet.");
                donors.forEach(System.out::println);
            }
            case 2 -> {
                String name = readLine("Enter Donor Name: ");
                Donor d = LinearSearch.searchByName(donorList.traverse(), name);
                System.out.println(d != null ? d : "Donor not found.");
            }
            case 3 -> { /* back to admin menu */ }
        }
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