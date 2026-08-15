package bloodbank.system;

import bloodbank.model.*;
import bloodbank.modules.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class SystemTest {
    private static int failures = 0;
    private static final String TEST_DIR = "data/";
    private static final String DONORS_FILE = TEST_DIR + "donors.txt";
    private static final String UNITS_FILE = TEST_DIR + "bloodUnits.txt";
    private static final String LOG_FILE = TEST_DIR + "transactions_log.txt";
    private static final String REQUESTS_FILE = TEST_DIR + "requests.txt";

    public static void main(String[] args) {
        System.out.println("Starting Blood Bank System Comprehensive Tests...\\n");

        clearTestData();

        testMember1();
        testMember2();
        testMember3();
        testAdminAndSorting();
        testUndoAndEligibility();
        testIntegrationAndReboot();

        System.out.println("\\n========================================");
        if (failures == 0) {
            System.out.println("ALL TESTS PASSED SUCCESSFULLY! ");
        } else {
            System.out.println("TESTS FAILED: " + failures + " errors found. ");
        }
        System.out.println("========================================");

        if (failures > 0) System.exit(1);
    }

    private static void assertCondition(boolean condition, String message) {
        if (condition) {
            System.out.println("[PASS] " + message);
        } else {
            System.out.println("[FAIL] " + message);
            failures++;
        }
    }

    private static void clearTestData() {
        System.out.println("Cleaning test data...");
        try {
            Files.deleteIfExists(Paths.get(DONORS_FILE));
            Files.deleteIfExists(Paths.get(UNITS_FILE));
            Files.deleteIfExists(Paths.get(LOG_FILE));
            Files.deleteIfExists(Paths.get(REQUESTS_FILE));
        } catch (IOException e) {
            System.out.println("Error cleaning data: " + e.getMessage());
        }
    }

    private static void testMember1() {
        System.out.println("\\n--- Testing Member 1: Donor Management ---");
        DonorModule dm = new DonorModule(DONORS_FILE);

        Donor d1 = new Donor("D001", "Alice Smith", 30, "123", "Addr1", "A+", "2024-01-01", "Yes;No;Yes", true);
        Donor d2 = new Donor("D002", "Bob Jones", 35, "456", "Addr2", "O-", "2024-02-01", "No;Yes;No", false);

        dm.insert(d1);
        dm.insert(d2);

        assertCondition(dm.size() == 2, "Donor list size should be 2");
        assertCondition(dm.searchByName("Alice Smith") != null, "Search by name should find Alice");
        assertCondition(dm.searchByName("Bob Jones") != null, "Search by name should find Bob");

        dm.delete("D001");
        assertCondition(dm.size() == 1, "Size should be 1 after deletion");
        assertCondition(dm.searchById("D001") == null, "D001 should be deleted");

        // Check persistence
        try {
            String content = Files.readString(Paths.get(DONORS_FILE));
            assertCondition(content.contains("Bob Jones") && !content.contains("Alice Smith"), "File should only contain Bob");
        } catch (IOException e) {
            assertCondition(false, "Failed to read donors.txt");
        }
    }

    private static void testMember2() {
        System.out.println("\\n--- Testing Member 2: Requests & Transactions ---");
        TransactionQueueModule tqm = new TransactionQueueModule(LOG_FILE, REQUESTS_FILE);

        // Test Priority Queue
        Requester r1 = new Requester("R1", "Alice", 30, "1", "A1", "H1", "A+", 2, "Normal");
        Requester r2 = new Requester("R2", "Bob", 40, "2", "A2", "H2", "O-", 1, "Urgent");

        BloodRequest br1 = new BloodRequest("BReq1", r1, "Pending", BloodRequest.now());
        BloodRequest br2 = new BloodRequest("BReq2", r2, "Pending", BloodRequest.now());

        tqm.enqueue(br1);
        tqm.enqueueUrgent(br2); // Urgent should be at the front

        BloodRequest dequeued = tqm.dequeue();
        assertCondition(dequeued.getRequestId().equals("BReq2"), "Urgent request (BReq2) should be dequeued first");

        // Test Transaction Log
        tqm.logTransaction("TEST_OP", "Testing logs");
        try {
            String content = Files.readString(Paths.get(LOG_FILE));
            assertCondition(content.contains("TEST_OP"), "Log file should contain TEST_OP");
        } catch (IOException e) {
            assertCondition(false, "Failed to read log file");
        }

        // Test Undo (Stack)
        tqm.logTransaction("OP1", "First");
        tqm.logTransaction("OP2", "Second");
        Transaction popped = tqm.popLastTransaction();
        assertCondition(popped != null && popped.getAction().equals("OP2"), "Undo should pop the most recent action (OP2)");
    }

    private static void testMember3() {
        System.out.println("\\n--- Testing Member 3: Blood Inventory (BST) ---");
        BSTModule bst = new BSTModule(UNITS_FILE);

        BloodUnit u1 = new BloodUnit("U001", "A+", "2026-12-31", "D001", "Available");
        BloodUnit u2 = new BloodUnit("U002", "O-", "2026-01-01", "D002", "Available");
        BloodUnit u3 = new BloodUnit("U003", "B+", "2026-06-15", "D003", "Available");

        bst.insert(u1);
        bst.insert(u2);
        bst.insert(u3);

        List<BloodUnit> sorted = bst.inorder();
        assertCondition(sorted.get(0).getExpiryDate().equals("2026-01-01"), "Soonest expiry should be first");
        assertCondition(sorted.get(2).getExpiryDate().equals("2026-12-31"), "Latest expiry should be last");

        assertCondition(bst.search("U001") != null, "Should find unit U001");
        bst.delete("U001");
        assertCondition(bst.search("U001") == null, "U001 should be deleted");

        // Check persistence
        try {
            String content = Files.readString(Paths.get(UNITS_FILE));
            assertCondition(!content.contains("U001"), "File should not contain deleted unit U001");
        } catch (IOException e) {
            assertCondition(false, "Failed to read units file");
        }
    }

    private static void testAdminAndSorting() {
        System.out.println("\\n--- Testing Admin & Sorting (Members 1, 2, 3) ---");

        // 1. Test Admin Login
        Admin admin = new Admin("admin", "password123");
        assertCondition(admin.login("admin", "password123"), "Admin login should succeed with correct credentials");
        assertCondition(!admin.login("admin", "wrongpass"), "Admin login should fail with wrong password");

        // 2. Test Sorting Donors
        DonorModule dm = new DonorModule(DONORS_FILE);
        Donor d1 = new Donor("S001", "Zack", 30, "1", "A1", "A+", "2024-01-01", "Yes", true);
        Donor d2 = new Donor("S002", "Alice", 30, "2", "A2", "A+", "2024-01-01", "Yes", true);
        Donor d3 = new Donor("S003", "Charlie", 30, "3", "A3", "A+", "2024-01-01", "Yes", true);
        dm.insert(d1);
        dm.insert(d2);
        dm.insert(d3);

        List<Donor> donors = dm.traverse();

        // Bubble Sort
        bloodbank.sort.SortStrategy<Donor> bubble = new bloodbank.sort.BubbleSorter<>();
        bubble.sort(donors, java.util.Comparator.comparing(Donor::getName, String.CASE_INSENSITIVE_ORDER));
        assertCondition(donors.get(0).getName().equals("Alice"), "Bubble sort should put Alice first");

        // Selection Sort
        bloodbank.sort.SortStrategy<Donor> selection = new bloodbank.sort.SelectionSorter<>();
        selection.sort(donors, java.util.Comparator.comparing(Donor::getName, String.CASE_INSENSITIVE_ORDER));
        assertCondition(donors.get(0).getName().equals("Alice"), "Selection sort should put Alice first");

        // 3. Test Inventory Sorting & BST Traversals
        BSTModule bst = new BSTModule(UNITS_FILE);
        BloodUnit u1 = new BloodUnit("U1", "A+", "2026-12-01", "D1", "Available");
        BloodUnit u2 = new BloodUnit("U2", "A+", "2026-01-01", "D2", "Available");
        BloodUnit u3 = new BloodUnit("U3", "A+", "2026-06-01", "D3", "Available");
        bst.insert(u1);
        bst.insert(u2);
        bst.insert(u3);

        List<BloodUnit> inventory = bst.inorder();
        bloodbank.sort.SortStrategy<BloodUnit> unitSorter = new bloodbank.sort.BubbleSorter<>();
        unitSorter.sort(inventory, java.util.Comparator.comparing(BloodUnit::getExpiryDate));
        assertCondition(inventory.get(0).getExpiryDate().equals("2026-01-01"), "Inventory sort should put soonest expiry first");

        // BST Traversals
        List<BloodUnit> preorder = bst.preorder();
        List<BloodUnit> postorder = bst.postorder();
        assertCondition(!preorder.isEmpty() && !postorder.isEmpty(), "BST traversals should return units");
    }

    private static void testUndoAndEligibility() {
        System.out.println("\\n--- Testing Undo & Eligibility (Members 1, 2) ---");

        // 1. Test Donor Eligibility (90 days rule)
        java.time.LocalDate now = java.time.LocalDate.now();
        String eligibleDate = now.minusDays(91).toString();
        String ineligibleDate = now.minusDays(45).toString();

        Donor dEligible = new Donor("E001", "Eligible Donor", 30, "1", "A1", "A+", eligibleDate, "Yes", true);
        Donor dIneligible = new Donor("E002", "Ineligible Donor", 30, "2", "A2", "A+", ineligibleDate, "Yes", true);

        assertCondition(dEligible.isEligible(), "Donor who donated 91 days ago should be eligible");
        assertCondition(!dIneligible.isEligible(), "Donor who donated 45 days ago should be ineligible");

        // 2. Test Undo (simulating BloodBankSystem.undoTransaction)
        DonorModule dm = new DonorModule(DONORS_FILE);
        Donor dUndo = new Donor("UNDO1", "Undo Me", 30, "1", "A1", "A+", "2024-01-01", "Yes", true);
        dm.insert(dUndo);
        assertCondition(dm.searchById("UNDO1") != null, "Donor should exist before undo");

        // Simulate undo: DELETE_DONOR
        dm.delete("UNDO1");
        assertCondition(dm.searchById("UNDO1") == null, "Donor should be removed after simulated undo");

        BSTModule bst = new BSTModule(UNITS_FILE);
        BloodUnit uUndo = new BloodUnit("UUNDO1", "A+", "2026-12-01", "D1", "Available");
        bst.insert(uUndo);
        assertCondition(bst.search("UUNDO1") != null, "Unit should exist before undo");

        // Simulate undo: DELETE_UNIT
        bst.delete("UUNDO1");
        assertCondition(bst.search("UUNDO1") == null, "Unit should be removed after simulated undo");
    }

    private static void testIntegrationAndReboot() {
        System.out.println("\\n--- Testing Integration & System Reboot ---");

        // 1. Setup a full scenario using a new system instance
        BloodBankSystem system = new BloodBankSystem();

        // Mocking a flow:
        // Donor -> Unit -> Request -> Fulfillment

        // Manually use the internal modules via a "hack" or simulate the logic
        // Since BloodBankSystem doesn't expose modules, we use them separately but through the logic
        DonorModule dm = new DonorModule(DONORS_FILE);
        BSTModule bst = new BSTModule(UNITS_FILE);
        TransactionQueueModule tqm = new TransactionQueueModule(LOG_FILE, REQUESTS_FILE);

        Donor d = new Donor("D100", "John Test", 40, "555", "Test Ave", "O+", "2023-01-01", "Yes;Yes;No", true);
        dm.insert(d);

        BloodUnit u = new BloodUnit("U100", "O+", "2027-01-01", "D100", "Available");
        bst.insert(u);

        Requester reqR = new Requester("R100", "Hospital X", 0, "0", "Hosp Way", "City Hosp", "O+", 1, "Urgent");
        BloodRequest br = new BloodRequest("BReq100", reqR, "Pending", BloodRequest.now());
        tqm.enqueueUrgent(br);

        // Fulfillment Logic
        BloodRequest activeReq = tqm.dequeue();
        BloodUnit matchingUnit = null;
        for (BloodUnit unit : bst.inorder()) {
            if (unit.getBloodGroup().equals(activeReq.getRequester().getRequiredBloodGroup()) && unit.getStatus().equals("Available")) {
                matchingUnit = unit;
                break;
            }
        }

        if (matchingUnit != null) {
            matchingUnit.setStatus("Issued");
            bst.persist();
            activeReq.fulfill(matchingUnit.getUnitId());
            tqm.logTransaction("ISSUE_UNIT", "Issued unit " + matchingUnit.getUnitId() + " for request " + activeReq.getRequestId());
            tqm.saveRequests(Collections.singletonList(activeReq));
        }

        assertCondition(activeReq.getStatus().equals("Fulfilled"), "Request should be fulfilled");
        assertCondition(u.getStatus().equals("Issued"), "Unit should be issued");

        // 2. REBOOT TEST
        System.out.println("Simulating system reboot...");
        BloodBankSystem rebootedSystem = new BloodBankSystem();

        // BloodBankSystem has a run() method that calls loadAllData() internally.
        // Since we can't easily call loadAllData() because it's private,
        // we rely on the fact that it's called during run().
        // But for tests, let's check the files directly.

        try {
            String donorFile = Files.readString(Paths.get(DONORS_FILE));
            String unitFile = Files.readString(Paths.get(UNITS_FILE));
            String logFile = Files.readString(Paths.get(LOG_FILE));

            assertCondition(donorFile.contains("John Test"), "Donor should persist after reboot");
            assertCondition(unitFile.contains("Issued"), "Unit status 'Issued' should persist");
            assertCondition(logFile.contains("ISSUE_UNIT"), "Transaction 'ISSUE_UNIT' should persist");
        } catch (IOException e) {
            assertCondition(false, "Failed to verify reboot persistence");
        }
    }
}
