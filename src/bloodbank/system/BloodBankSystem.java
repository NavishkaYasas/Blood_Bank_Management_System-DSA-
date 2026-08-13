package bloodbank.system;

import bloodbank.io.AdminFileManager;
import bloodbank.io.BloodUnitFileManager;
import bloodbank.io.DonorFileManager;
import bloodbank.model.Admin;
import bloodbank.model.BloodUnit;
import bloodbank.model.Donor;
import bloodbank.search.LinearSearch;
import bloodbank.structures.BloodUnitAVL;
import bloodbank.structures.BloodUnitBST;
import bloodbank.structures.DonorHashTable;
import bloodbank.structures.DonorLinkedList;
import bloodbank.structures.DonorSet;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class BloodBankSystem {

    private static final String DATA_DIR = "data/";
    private static final String ADMIN_FILE = DATA_DIR + "admin.txt";
    private static final String DONORS_FILE = DATA_DIR + "donors.txt";
    private static final String UNITS_FILE = DATA_DIR + "bloodUnits.txt";

    public static final String[] BLOOD_GROUPS = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};

    private final AdminFileManager adminFileManager = new AdminFileManager();
    private final DonorFileManager donorFileManager = new DonorFileManager();
    private final BloodUnitFileManager unitFileManager = new BloodUnitFileManager();
    private final DonorLinkedList donorList = new DonorLinkedList();
    private final DonorHashTable donorHash = new DonorHashTable();
    private final DonorSet donorSet = new DonorSet();
    private final BloodUnitBST inventoryBST = new BloodUnitBST();
    private final BloodUnitAVL inventoryAVL = new BloodUnitAVL();
    private Admin admin;
    private int donorCounter = 1;
    private int unitCounter = 1000;

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
            donorHash.put(d);
            donorSet.add(d.getId());
        }
        for (BloodUnit u : unitFileManager.loadFromFile(UNITS_FILE)) {
            inventoryBST.insert(u);
            inventoryAVL.insert(u);
        }
        donorCounter = donorList.size() + 1;
        unitCounter = 1000 + inventoryBST.inorder().size();
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
        String answers = "Weight>50kg:" + readLine("Are you over 50kg? (yes/no): ") +
                ";ChronicIllness:" + readLine("Any chronic illness? (yes/no): ") +
                ";RecentSurgery:" + readLine("Recent surgery in last 6 months? (yes/no): ") +
                ";OnMedication:" + readLine("Currently on medication? (yes/no): ");
        String lastDonation = readLine("Last donation date (yyyy-MM-dd, or 'none'): ");
        if (lastDonation.equalsIgnoreCase("none") || lastDonation.isBlank()) {
            lastDonation = "1970-01-01";
        }

        System.out.println("\n[Upload Medical Certificate (PDF)]");
        System.out.println("--> This button is a placeholder for a future feature and is not yet functional.");
        boolean certUploaded = false;

        // Member 6's Set ADT: duplicate prevention (checked before inserting anywhere)
        if (donorSet.contains(id)) {
            System.out.println("Registration failed: duplicate donor ID.");
            return;
        }

        Donor donor = new Donor(id, name, age, contact, address, bloodGroup, lastDonation, answers, certUploaded);
        donorSet.add(id);
        donorList.insert(donor);
        donorHash.put(donor);
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
        System.out.println("3. Search donor by ID (Hash Table - O(1) average)");
        System.out.println("4. Delete donor by ID");
        System.out.println("5. Back");
        int choice = readInt("Choice: ", 1, 5);
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
            case 3 -> {
                String id = readLine("Enter Donor ID: ");
                Donor d = donorHash.get(id);
                System.out.println(d != null ? d : "Donor not found.");
            }
            case 4 -> {
                String id = readLine("Enter Donor ID to delete: ");
                if (donorList.delete(id)) {
                    donorHash.remove(id);
                    donorFileManager.saveToFile(DONORS_FILE, donorList.traverse());
                    // NOTE: transaction logging (Stack) will be wired in once we
                    // build the Transaction History feature.
                    System.out.println("Donor deleted.");
                } else {
                    System.out.println("Donor not found.");
                }
            }
            case 5 -> { /* back to admin menu */ }
        }
    }

    private void manageInventoryMenu() {
        System.out.println("\n-- Manage Blood Inventory --");
        System.out.println("1. Add new blood unit");
        System.out.println("2. Search unit by ID");
        System.out.println("3. Delete unit by ID");
        System.out.println("4. View inventory (BST in-order = soonest expiry first)");
        System.out.println("5. View inventory traversals (pre-order / post-order)");
        System.out.println("6. View inventory count per blood group (Array)");
        System.out.println("7. Back");
        int choice = readInt("Choice: ", 1, 7);
        switch (choice) {
            case 1 -> addBloodUnit();
            case 2 -> {
                String id = readLine("Enter Unit ID: ");
                BloodUnit u = inventoryBST.search(id);
                System.out.println(u != null ? u : "Unit not found.");
            }
            case 3 -> {
                String id = readLine("Enter Unit ID to delete: ");
                BloodUnit u = inventoryBST.search(id);
                if (u != null) {
                    inventoryBST.delete(id);
                    inventoryAVL.delete(id);
                    unitFileManager.saveToFile(UNITS_FILE, inventoryBST.inorder());
                    // NOTE: transaction logging will be wired in once we build
                    // the Transaction History feature (Member 2).
                    System.out.println("Unit deleted.");
                } else {
                    System.out.println("Unit not found.");
                }
            }
            case 4 -> {
                List<BloodUnit> units = inventoryBST.inorder();
                if (units.isEmpty()) System.out.println("No inventory yet.");
                units.forEach(System.out::println);
            }
            case 5 -> {
                System.out.println("Pre-order (BST):");
                inventoryBST.preorder().forEach(System.out::println);
                System.out.println("Post-order (BST):");
                inventoryBST.postorder().forEach(System.out::println);
                System.out.println("AVL tree height: " + inventoryAVL.getTreeHeight()
                        + " (kept balanced automatically via rotations)");
            }
            case 6 -> {
                Map<String, Integer> counts = new LinkedHashMap<>();
                for (String bg : BLOOD_GROUPS) counts.put(bg, 0);
                for (BloodUnit u : inventoryBST.inorder()) {
                    if (u.getStatus().equals("Available")) {
                        counts.put(u.getBloodGroup(), counts.getOrDefault(u.getBloodGroup(), 0) + 1);
                    }
                }
                System.out.println("Available units per blood group:");
                for (String bg : BLOOD_GROUPS) {
                    System.out.println("  " + bg + " : " + counts.get(bg));
                }
            }
            case 7 -> { /* back to admin menu */ }
        }
    }

    private void addBloodUnit() {
        String unitId = "U" + (unitCounter++);
        String bloodGroup = chooseBloodGroup();
        String expiry = readLine("Expiry date (yyyy-MM-dd): ");
        String donorId = readLine("Donor ID (or 'N/A'): ");

        BloodUnit unit = new BloodUnit(unitId, bloodGroup, expiry, donorId, "Available");
        inventoryBST.insert(unit);
        inventoryAVL.insert(unit);
        unitFileManager.saveToFile(UNITS_FILE, inventoryBST.inorder());
        // NOTE: transaction logging will be wired in once we build
        // the Transaction History feature (Member 2).
        System.out.println("Unit " + unitId + " added to inventory.");
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