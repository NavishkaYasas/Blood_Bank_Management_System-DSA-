package bloodbank.system;

import bloodbank.io.AdminFileManager;
import bloodbank.model.*;
import bloodbank.modules.*;
import bloodbank.sort.*;

import java.util.*;

/**
 * Main controller class. Wires the three member modules together and drives
 * the three top-level menus (Donor Registration, Admin, Blood Request).
 * Each data structure is owned by exactly one module class (see the
 * bloodbank.modules package), so ownership matches the group's official
 * task distribution table.
 */
public class BloodBankSystem {

    // ---- file paths ----
    private static final String DATA_DIR = "data/";
    private static final String DONORS_FILE = DATA_DIR + "donors.txt";
    private static final String UNITS_FILE = DATA_DIR + "bloodUnits.txt";
    private static final String REQUESTS_FILE = DATA_DIR + "requests.txt";
    private static final String LOG_FILE = DATA_DIR + "transactions_log.txt";
    private static final String ADMIN_FILE = DATA_DIR + "admin.txt";

    public static final String[] BLOOD_GROUPS = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};

    // ---- member modules (Task 1, 2, 3 owners) ----
    private final DonorModule donorModule = new DonorModule(DONORS_FILE);                 // Member 1
    private final TransactionQueueModule txQueueModule =
            new TransactionQueueModule(LOG_FILE, REQUESTS_FILE);                          // Member 2
    private final BSTModule bstModule = new BSTModule(UNITS_FILE);                        // Member 3

    private final AdminFileManager adminFileManager = new AdminFileManager();
    private final List<BloodRequest> allRequests = new ArrayList<>();

    private Admin admin;
    private final Scanner sc = new Scanner(System.in);
    private int unitCounter = 1000;
    private int requestCounter = 1;
    private int donorCounter = 1;

    // ===================================================================
    // STARTUP / SHUTDOWN
    // ===================================================================

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

    private void showMainMenu() {
        System.out.println("\n----------------- MAIN MENU -----------------");
        System.out.println("1. Donor Registration");
        System.out.println("2. Login as Admin");
        System.out.println("3. Blood Request");
        System.out.println("4. Exit");
        System.out.println("-----------------------------------------------");
    }

    private void loadAllData() {
        // Member 1 (linked list) indexes donors
        donorModule.loadAll();

        // Member 3 (BST) indexes inventory
        bstModule.loadAll();

        txQueueModule.loadTransactions();

        allRequests.addAll(txQueueModule.loadRequests());
        for (BloodRequest r : allRequests) {
            if (r.getStatus().equals("Pending")) txQueueModule.enqueue(r);
        }
        admin = adminFileManager.loadAdmin(ADMIN_FILE);

        donorCounter = donorModule.size() + 1;
        unitCounter = 1000 + bstModule.inorder().size();
        requestCounter = allRequests.size() + 1;
    }

    // ===================================================================
    // 1. DONOR REGISTRATION MODULE (Member 1: Array + Linked List)
    // ===================================================================

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
            lastDonation = "1970-01-01"; // treated as no prior donation -> eligible
        }

        System.out.println("\n[Upload Medical Certificate (PDF)]");
        System.out.println("--> This button is a placeholder for a future feature and is not yet functional.");
        boolean certUploaded = false; // always false: fake / future-scope button

        Donor donor = new Donor(id, name, age, contact, address, bloodGroup, lastDonation, answers.toString(), certUploaded);
        donorModule.insert(donor);       // Member 1's linked list (also persists donors.txt)
        txQueueModule.logTransaction("ADD_DONOR", "Registered donor " + id + " (" + name + ")");

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

    // ===================================================================
    // 2. ADMIN MODULE
    // ===================================================================

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
            System.out.println("1. View / Manage Donors            (Member 1)");
            System.out.println("2. Manage Blood Inventory           (Member 3)");
            System.out.println("3. Process Blood Requests           (Member 2)");
            System.out.println("4. View Transaction History         (Member 2)");
            System.out.println("5. Sort Donor / Inventory Records   (Member 2, 3)");
            System.out.println("6. Logout");
            System.out.println("------------------------------------------------");
            int choice = readInt("Enter your choice: ", 1, 6);
            switch (choice) {
                case 1 -> manageDonorsMenu();
                case 2 -> manageInventoryMenu();
                case 3 -> processRequestsMenu();
                case 4 -> viewTransactionHistory();
                case 5 -> sortMenu();
                case 6 -> loggedIn = false;
            }
        }
    }

    // ---- 2.1 Manage Donors: Member 1 (Linked List, Linear Search) ----
    private void manageDonorsMenu() {
        System.out.println("\n-- View / Manage Donors --");
        System.out.println("1. View all donors (Linked List traversal - Member 1)");
        System.out.println("2. Search donor by name (Linear Search - Member 1)");
        System.out.println("3. Delete donor by ID");
        System.out.println("4. Back");
        int choice = readInt("Choice: ", 1, 4);
        switch (choice) {
            case 1 -> {
                List<Donor> donors = donorModule.traverse();
                if (donors.isEmpty()) System.out.println("No donors registered yet.");
                donors.forEach(System.out::println);
            }
            case 2 -> {
                String name = readLine("Enter Donor Name: ");
                Donor d = donorModule.searchByName(name);
                System.out.println(d != null ? d : "Donor not found.");
            }
            case 3 -> {
                String id = readLine("Enter Donor ID to delete: ");
                if (donorModule.delete(id)) {
                    txQueueModule.logTransaction("DELETE_DONOR", "Deleted donor " + id);
                    System.out.println("Donor deleted.");
                } else {
                    System.out.println("Donor not found.");
                }
            }
        }
    }

    // ---- 2.2 Manage Inventory: Member 3 (BST) ----
    private void manageInventoryMenu() {
        System.out.println("\n-- Manage Blood Inventory --");
        System.out.println("1. Add new blood unit");
        System.out.println("2. Search unit by ID (BST)");
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
                BloodUnit u = bstModule.search(id);
                System.out.println(u != null ? u : "Unit not found.");
            }
            case 3 -> {
                String id = readLine("Enter Unit ID to delete: ");
                BloodUnit u = bstModule.search(id);
                if (u != null) {
                    bstModule.delete(id);
                    txQueueModule.logTransaction("DELETE_UNIT", "Deleted unit " + id);
                    System.out.println("Unit deleted.");
                } else {
                    System.out.println("Unit not found.");
                }
            }
            case 4 -> {
                List<BloodUnit> units = bstModule.inorder();
                if (units.isEmpty()) System.out.println("No inventory yet.");
                units.forEach(System.out::println);
            }
            case 5 -> {
                System.out.println("Pre-order (BST - Member 3):");
                bstModule.preorder().forEach(System.out::println);
                System.out.println("Post-order (BST - Member 3):");
                bstModule.postorder().forEach(System.out::println);
            }
            case 6 -> {
                Map<String, Integer> counts = new LinkedHashMap<>();
                for (String bg : BLOOD_GROUPS) counts.put(bg, 0);
                for (BloodUnit u : bstModule.inorder()) {
                    if (u.getStatus().equals("Available")) {
                        counts.put(u.getBloodGroup(), counts.getOrDefault(u.getBloodGroup(), 0) + 1);
                    }
                }
                System.out.println("Available units per blood group:");
                for (String bg : BLOOD_GROUPS) {
                    System.out.println("  " + bg + " : " + counts.get(bg));
                }
            }
        }
    }

    private void addBloodUnit() {
        String unitId = "U" + (unitCounter++);
        String bloodGroup = chooseBloodGroup();
        String expiry = readLine("Expiry date (yyyy-MM-dd): ");
        String donorId = readLine("Donor ID (or 'N/A'): ");

        BloodUnit unit = new BloodUnit(unitId, bloodGroup, expiry, donorId, "Available");
        bstModule.insert(unit);   // Member 3
        txQueueModule.logTransaction("ADD_UNIT", "Added unit " + unitId + " (" + bloodGroup + ")");
        System.out.println("Unit " + unitId + " added to inventory.");
    }

    // ---- 2.3 Process Requests: Member 2 (Queue) + Member 3's BST search ----
    private void processRequestsMenu() {
        System.out.println("\n-- Process Blood Requests --");
        if (txQueueModule.queueIsEmpty()) {
            System.out.println("No pending requests.");
            return;
        }
        System.out.println("Next pending request: " + txQueueModule.peekQueue());
        String proceed = readLine("Process this request? (yes/no): ");
        if (!proceed.equalsIgnoreCase("yes")) return;

        BloodRequest req = txQueueModule.dequeue();
        BloodUnit match = findMatchingUnit(req.getRequester().getRequiredBloodGroup());

        if (match != null) {
            match.setStatus("Issued");
            req.fulfill(match.getUnitId());
            bstModule.persist();
            txQueueModule.logTransaction("ISSUE_UNIT", "Issued unit " + match.getUnitId() + " for request " + req.getRequestId());
            System.out.println("Request fulfilled with unit " + match.getUnitId());
        } else {
            req.setStatus("Waitlisted");
            System.out.println("No matching unit available. Request waitlisted.");
        }
        txQueueModule.saveRequests(allRequests);
    }

    /** Searches the BST in-order (earliest expiry first) for the first available, non-expired match. */
    private BloodUnit findMatchingUnit(String bloodGroup) {
        for (BloodUnit u : bstModule.inorder()) {
            if (u.getBloodGroup().equals(bloodGroup) && u.getStatus().equals("Available") && !u.isExpired()) {
                return u;
            }
        }
        return null;
    }

    // ---- 2.4 Transaction History: Member 2 (Stack) ----
    private void viewTransactionHistory() {
        System.out.println("\n-- Transaction History (most recent first) --");
        if (txQueueModule.historyIsEmpty()) {
            System.out.println("No transactions logged yet.");
            return;
        }
        txQueueModule.historySnapshot().forEach(System.out::println);

        String undo = readLine("\nUndo the most recent action? (yes/no): ");
        if (undo.equalsIgnoreCase("yes")) {
            Transaction t = txQueueModule.popLastTransaction();
            undoTransaction(t);
        }
    }

    /** Best-effort undo: only ADD_DONOR and ADD_UNIT can be cleanly reversed. */
    private void undoTransaction(Transaction t) {
        if (t == null) return;
        System.out.println("Undoing: " + t);
        if (t.getAction().equals("ADD_DONOR")) {
            String id = t.getDetails().split(" ")[2];
            donorModule.delete(id);
            System.out.println("Donor " + id + " removed (undo successful).");
        } else if (t.getAction().equals("ADD_UNIT")) {
            String id = t.getDetails().split(" ")[2];
            bstModule.delete(id);
            System.out.println("Unit " + id + " removed (undo successful).");
        } else {
            System.out.println("This action type cannot be automatically reversed in the current version.");
        }
    }

    // ---- 2.5 Sorting: Task 3 - Members 2 and 3 ----
    private void sortMenu() {
        System.out.println("\n-- Sort Donor / Inventory Records --");
        System.out.println("1. Sort Donors by Name");
        System.out.println("2. Sort Inventory by Expiry Date");
        System.out.println("3. Back");
        int target = readInt("Choice: ", 1, 3);
        if (target == 3) return;

        System.out.println("Choose algorithm:");
        System.out.println("1. Bubble Sort (Member 2)   2. Selection Sort (Member 3)");
        int algoChoice = readInt("Choice: ", 1, 2);

        if (target == 1) {
            List<Donor> donors = donorModule.traverse();
            SortStrategy<Donor> sorter = pickSorter(algoChoice);
            long start = System.nanoTime();
            sorter.sort(donors, Comparator.comparing(Donor::getName, String.CASE_INSENSITIVE_ORDER));
            long elapsed = System.nanoTime() - start;
            System.out.println("\nSorted donors using " + sorter.getName() + " (" + elapsed + " ns):");
            donors.forEach(System.out::println);
        } else {
            List<BloodUnit> units = bstModule.inorder();
            SortStrategy<BloodUnit> sorter = pickSorter(algoChoice);
            long start = System.nanoTime();
            sorter.sort(units, Comparator.comparing(BloodUnit::getExpiryDate));
            long elapsed = System.nanoTime() - start;
            System.out.println("\nSorted inventory using " + sorter.getName() + " (" + elapsed + " ns):");
            units.forEach(System.out::println);
        }
    }

    private <T> SortStrategy<T> pickSorter(int choice) {
        return switch (choice) {
            case 1 -> new BubbleSorter<>();
            case 2 -> new SelectionSorter<>();
            default -> new BubbleSorter<>();
        };
    }

    // ===================================================================
    // 3. BLOOD REQUEST MODULE (Member 2: Queue)
    // ===================================================================

    private void bloodRequestFlow() {
        System.out.println("\n------------- BLOOD REQUEST -------------");
        String id = "R" + String.format("%04d", requestCounter++);
        String name = readLine("Requester Name: ");
        int age = readInt("Age: ", 0, 120);
        String contact = readLine("Contact Number: ");
        String address = readLine("Address: ");
        String hospital = readLine("Hospital / Organisation Name: ");
        String bloodGroup = chooseBloodGroup();

        int qty = readInt("Quantity needed (units): ", 1, 20);
        String urgency = readLine("Urgency (Normal/Urgent): ");
        if (!urgency.equalsIgnoreCase("Urgent")) urgency = "Normal";

        Requester requester = new Requester(id, name, age, contact, address, hospital, bloodGroup, qty, urgency);
        BloodRequest request = new BloodRequest(id, requester, "Pending", BloodRequest.now());

        allRequests.add(request);
        if (urgency.equalsIgnoreCase("Urgent")) {
            txQueueModule.enqueueUrgent(request);
        } else {
            txQueueModule.enqueue(request);
        }
        txQueueModule.saveRequests(allRequests);

        System.out.println("\nRequest submitted! Your Request ID is: " + id);
        System.out.println("Track this ID to check status later.");
    }

    // ===================================================================
    // INPUT HELPERS
    // ===================================================================

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
