import java.io.*;
import java.util.*;

// =========================
// CO4: Interface
// =========================
interface TriageService {
    String determinePriority();
}

// =========================
// CO4: Abstract Class
// =========================
abstract class Person {
    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public abstract void displayDetails();
}

// =========================
// CO4: Inheritance
// =========================
class Patient extends Person implements TriageService {

    private int patientId;
    private String symptoms;
    private int severity;
    private int arrivalNumber;

    public Patient(int patientId, String name, int age,
                   String symptoms, int severity, int arrivalNumber) {

        super(name, age);
        this.patientId = patientId;
        this.symptoms = symptoms;
        this.severity = severity;
        this.arrivalNumber = arrivalNumber;
    }

    public int getPatientId() {
        return patientId;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public int getSeverity() {
        return severity;
    }

    public int getArrivalNumber() {
        return arrivalNumber;
    }

    // CO4: Polymorphism
    @Override
    public String determinePriority() {

        if (severity >= 9) {
            return "CRITICAL";
        } else if (severity >= 7) {
            return "URGENT";
        } else if (severity >= 4) {
            return "MODERATE";
        } else {
            return "LOW";
        }
    }

    @Override
    public void displayDetails() {

        System.out.println("--------------------------------");
        System.out.println("Patient ID      : " + patientId);
        System.out.println("Name            : " + getName());
        System.out.println("Age             : " + getAge());
        System.out.println("Symptoms        : " + symptoms);
        System.out.println("Severity        : " + severity);
        System.out.println("Priority        : " + determinePriority());
        System.out.println("Arrival Number  : " + arrivalNumber);
    }

    @Override
    public String toString() {
        return patientId + "," +
               getName() + "," +
               getAge() + "," +
               symptoms + "," +
               severity + "," +
               determinePriority();
    }
}

// =========================
// CO4: Custom Exception
// =========================
class InvalidPatientException extends Exception {

    public InvalidPatientException(String message) {
        super(message);
    }
}

// =========================
// Hospital Triage System
// =========================
public class HospitalTriageSystem {

    // CO3: Array
    static Patient[] patients = new Patient[100];

    static int patientCount = 0;
    static int arrivalCounter = 1;

    // =========================
    // CO3: Method
    // =========================
    public static void addPatient(Scanner sc)
            throws InvalidPatientException {

        try {

            System.out.print("Enter Patient ID: ");
            int id = Integer.parseInt(sc.nextLine());

            System.out.print("Enter Patient Name: ");
            String name = sc.nextLine().trim();

            System.out.print("Enter Age: ");
            int age = Integer.parseInt(sc.nextLine());

            System.out.print("Enter Symptoms: ");
            String symptoms = sc.nextLine().trim();

            System.out.print("Enter Severity (1-10): ");
            int severity = Integer.parseInt(sc.nextLine());

            // CO2: Selection
            if (id <= 0) {
                throw new InvalidPatientException(
                        "Patient ID must be positive.");
            }

            if (name.isEmpty()) {
                throw new InvalidPatientException(
                        "Patient name cannot be empty.");
            }

            if (age <= 0 || age > 120) {
                throw new InvalidPatientException(
                        "Invalid age.");
            }

            if (symptoms.isEmpty()) {
                throw new InvalidPatientException(
                        "Symptoms cannot be empty.");
            }

            if (severity < 1 || severity > 10) {
                throw new InvalidPatientException(
                        "Severity must be between 1 and 10.");
            }

            // Check duplicate patient ID
            for (int i = 0; i < patientCount; i++) {

                if (patients[i].getPatientId() == id) {
                    throw new InvalidPatientException(
                            "Patient ID already exists.");
                }
            }

            patients[patientCount] =
                    new Patient(
                            id,
                            name,
                            age,
                            symptoms,
                            severity,
                            arrivalCounter
                    );

            patientCount++;
            arrivalCounter++;

            System.out.println("\nPatient added successfully.");
            System.out.println(
                    "Priority: " +
                    patients[patientCount - 1].determinePriority());

        } catch (NumberFormatException e) {

            throw new InvalidPatientException(
                    "Please enter valid numeric values.");
        }
    }

    // =========================
    // CO3: Display Method
    // =========================
    public static void displayPatients() {

        if (patientCount == 0) {
            System.out.println("\nNo patients in emergency room.");
            return;
        }

        System.out.println("\n===== PATIENT LIST =====");

        // CO2: Iteration
        for (int i = 0; i < patientCount; i++) {
            patients[i].displayDetails();
        }
    }

    // =========================
    // CO3: Searching
    // =========================
    public static void searchPatient(Scanner sc) {

        System.out.print("Enter Patient ID to search: ");
        int id = Integer.parseInt(sc.nextLine());

        boolean found = false;

        for (int i = 0; i < patientCount; i++) {

            if (patients[i].getPatientId() == id) {

                System.out.println("\nPatient Found:");
                patients[i].displayDetails();

                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Patient not found.");
        }
    }

    // =========================
    // CO5: Sorting
    // Highest severity first
    // =========================
    public static void sortBySeverity() {

        for (int i = 0; i < patientCount - 1; i++) {

            for (int j = 0; j < patientCount - i - 1; j++) {

                if (patients[j].getSeverity()
                        < patients[j + 1].getSeverity()) {

                    Patient temp = patients[j];
                    patients[j] = patients[j + 1];
                    patients[j + 1] = temp;
                }
            }
        }

        System.out.println(
                "\nPatients sorted according to emergency severity.");
    }

    // =========================
    // CO5: StringBuilder
    // =========================
    public static String generateReport() {

        StringBuilder report = new StringBuilder();

        report.append("\n===== HOSPITAL EMERGENCY REPORT =====\n");

        for (int i = 0; i < patientCount; i++) {

            report.append("Patient ID: ")
                  .append(patients[i].getPatientId())
                  .append(" | Name: ")
                  .append(patients[i].getName())
                  .append(" | Severity: ")
                  .append(patients[i].getSeverity())
                  .append(" | Priority: ")
                  .append(patients[i].determinePriority())
                  .append("\n");
        }

        return report.toString();
    }

    // =========================
    // CO5: File Writing
    // =========================
    public static void saveToFile() {

        try {

            FileWriter writer =
                    new FileWriter("hospital_triage_report.txt");

            writer.write(generateReport());

            writer.close();

            System.out.println(
                    "Report saved to hospital_triage_report.txt");

        } catch (IOException e) {

            System.out.println(
                    "Error while saving report: " +
                    e.getMessage());
        }
    }

    // =========================
    // CO5: File Reading
    // =========================
    public static void readFromFile() {

        try {

            File file =
                    new File("hospital_triage_report.txt");

            Scanner fileScanner =
                    new Scanner(file);

            System.out.println(
                    "\n===== SAVED REPORT =====");

            while (fileScanner.hasNextLine()) {

                System.out.println(
                        fileScanner.nextLine());
            }

            fileScanner.close();

        } catch (FileNotFoundException e) {

            System.out.println(
                    "Report file not found.");
        }
    }

    // =========================
    // Main Method
    // =========================
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        int choice = 0;

        do {

            System.out.println("\n");
            System.out.println("====================================");
            System.out.println(" HOSPITAL EMERGENCY-ROOM TRIAGE");
            System.out.println("====================================");
            System.out.println("1. Add Patient");
            System.out.println("2. Display All Patients");
            System.out.println("3. Search Patient");
            System.out.println("4. Sort by Emergency Severity");
            System.out.println("5. Generate Report");
            System.out.println("6. Save Report to File");
            System.out.println("7. Read Report from File");
            System.out.println("8. Exit");
            System.out.println("====================================");

            try {

                System.out.print("Enter your choice: ");
                choice = Integer.parseInt(sc.nextLine());

                switch (choice) {

                    case 1:
                        try {
                            addPatient(sc);
                        } catch (InvalidPatientException e) {
                            System.out.println(
                                    "Error: " + e.getMessage());
                        }
                        break;

                    case 2:
                        displayPatients();
                        break;

                    case 3:
                        searchPatient(sc);
                        break;

                    case 4:
                        sortBySeverity();
                        displayPatients();
                        break;

                    case 5:
                        System.out.println(
                                generateReport());
                        break;

                    case 6:
                        saveToFile();
                        break;

                    case 7:
                        readFromFile();
                        break;

                    case 8:
                        System.out.println(
                                "Exiting Hospital Triage System...");
                        break;

                    default:
                        System.out.println(
                                "Invalid choice. Try again.");
                }

            } catch (NumberFormatException e) {

                System.out.println(
                        "Please enter a valid menu number.");

            } catch (Exception e) {

                System.out.println(
                        "Unexpected error: " +
                        e.getMessage());
            }

        } while (choice != 8);

        sc.close();
    }
}
