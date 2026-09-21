import java.util.*;

class Patient {
    int id;
    String name;
    int age;
    String symptoms;
    int painLevel;
    int priority;

    Patient(int id, String name, int age, String symptoms, int painLevel) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.symptoms = symptoms;
        this.painLevel = painLevel;
        this.priority = calculatePriority();
    }

    // Calculate triage priority
    int calculatePriority() {
        if (painLevel >= 8) {
            return 1; // Critical
        } else if (painLevel >= 5) {
            return 2; // Urgent
        } else {
            return 3; // Non-Urgent
        }
    }

    String getPriorityName() {
        switch (priority) {
            case 1:
                return "CRITICAL";
            case 2:
                return "URGENT";
            case 3:
                return "NON-URGENT";
            default:
                return "UNKNOWN";
        }
    }

    void displayPatient() {
        System.out.println("----------------------------------------");
        System.out.println("Patient ID   : " + id);
        System.out.println("Name         : " + name);
        System.out.println("Age          : " + age);
        System.out.println("Symptoms     : " + symptoms);
        System.out.println("Pain Level   : " + painLevel + "/10");
        System.out.println("Priority     : " + getPriorityName());
        System.out.println("----------------------------------------");
    }
}

public class HospitalTriageSystem {

    static Scanner sc = new Scanner(System.in);

    // Priority Queue: smaller priority number = higher priority
    static PriorityQueue<Patient> patientQueue =
            new PriorityQueue<>(
                    Comparator.comparingInt(p -> p.priority)
            );

    static int patientId = 1001;

    // Add a new patient
    static void registerPatient() {

        System.out.println("\n========== PATIENT REGISTRATION ==========");

        System.out.print("Enter Patient Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Age: ");
        int age = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter Symptoms: ");
        String symptoms = sc.nextLine();

        System.out.print("Enter Pain Level (1-10): ");
        int painLevel = sc.nextInt();
        sc.nextLine();

        if (painLevel < 1 || painLevel > 10) {
            System.out.println("Invalid pain level!");
            return;
        }

        Patient patient =
                new Patient(patientId++, name, age, symptoms, painLevel);

        patientQueue.add(patient);

        System.out.println("\nPatient registered successfully!");
        System.out.println("Assigned Patient ID: " + patient.id);
        System.out.println("Priority: " + patient.getPriorityName());
    }

    // Display all patients
    static void displayQueue() {

        if (patientQueue.isEmpty()) {
            System.out.println("\nNo patients in the queue.");
            return;
        }

        System.out.println("\n========== EMERGENCY ROOM QUEUE ==========");

        ArrayList<Patient> patients =
                new ArrayList<>(patientQueue);

        patients.sort(Comparator.comparingInt(p -> p.priority));

        for (Patient p : patients) {
            p.displayPatient();
        }
    }

    // Treat the highest priority patient
    static void treatPatient() {

        if (patientQueue.isEmpty()) {
            System.out.println("\nNo patients waiting for treatment.");
            return;
        }

        Patient patient = patientQueue.poll();

        System.out.println("\n========== CURRENT PATIENT ==========");
        System.out.println("Patient selected for treatment:");
        patient.displayPatient();

        System.out.println("Status: Treatment started.");
    }

    // Search patient by ID
    static void searchPatient() {

        System.out.print("\nEnter Patient ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        for (Patient p : patientQueue) {
            if (p.id == id) {
                System.out.println("\nPatient Found!");
                p.displayPatient();
                return;
            }
        }

        System.out.println("Patient not found.");
    }

    // Main menu
    public static void main(String[] args) {

        int choice;

        System.out.println("==========================================");
        System.out.println("     HOSPITAL EMERGENCY-ROOM TRIAGE");
        System.out.println("==========================================");

        do {

            System.out.println("\n============== MENU ==============");
            System.out.println("1. Register Patient");
            System.out.println("2. Display Patient Queue");
            System.out.println("3. Treat Next Patient");
            System.out.println("4. Search Patient");
            System.out.println("5. Exit");
            System.out.println("==================================");

            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    registerPatient();
                    break;

                case 2:
                    displayQueue();
                    break;

                case 3:
                    treatPatient();
                    break;

                case 4:
                    searchPatient();
                    break;

                case 5:
                    System.out.println(
                            "\nThank you for using the Hospital Triage System!"
                    );
                    break;

                default:
                    System.out.println("Invalid choice. Try again.");
            }

        } while (choice != 5);

        sc.close();
    }
}