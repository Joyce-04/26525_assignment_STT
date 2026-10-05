import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Employee Management System for Organization X.
 * Covers: employee management, payment processing, and reporting.
 *
 * Functional Requirements implemented:
 * FR1 - Add a new employee with a unique ID, name, position, salary, deductions.
 * FR2 - Reject adding an employee with a duplicate ID or negative salary/deductions.
 * FR3 - Remove an existing employee record.
 * FR4 - Update an existing employee's salary and deductions.
 * FR5 - Process payment for an employee: net pay = salary - deductions (never negative).
 * FR6 - Return an error indicator (-1) when processing payment for a non-existent employee.
 * FR7 - Generate a report listing all employees with salary, deductions, net pay and total payroll.
 */
public class EmployeeManagementSystem {

    static class Employee {
        String id;
        String name;
        String position;
        double baseSalary;
        double deductions;

        Employee(String id, String name, String position, double baseSalary, double deductions) {
            this.id = id;
            this.name = name;
            this.position = position;
            this.baseSalary = baseSalary;
            this.deductions = deductions;
        }
    }

    private final Map<String, Employee> employees = new LinkedHashMap<>();

    // FR1 / FR2: Add employee
    public boolean addEmployee(String id, String name, String position, double baseSalary, double deductions) {
        if (employees.containsKey(id)) {
            return false; // duplicate ID
        }
        if (baseSalary < 0 || deductions < 0) {
            return false; // invalid data
        }
        employees.put(id, new Employee(id, name, position, baseSalary, deductions));
        return true;
    }

    // FR3: Remove employee
    public boolean removeEmployee(String id) {
        if (!employees.containsKey(id)) {
            return false;
        }
        employees.remove(id);
        return true;
    }

    // FR4: Update employee
    public boolean updateEmployee(String id, double newSalary, double newDeductions) {
        Employee e = employees.get(id);
        if (e == null) {
            return false;
        }
        if (newSalary < 0 || newDeductions < 0) {
            return false;
        }
        e.baseSalary = newSalary;
        e.deductions = newDeductions;
        return true;
    }

    // FR5 / FR6: Process payment
    public double processPayment(String id) {
        Employee e = employees.get(id);
        if (e == null) {
            return -1;
        }
        double net = e.baseSalary - e.deductions;
        if (net < 0) {
            net = 0; // net pay cannot be negative
        }
        return net;
    }

    // FR7: Generate report
    public String generateReport() {
        StringBuilder sb = new StringBuilder();
        double totalPayroll = 0;
        sb.append(String.format("%-6s %-15s %-13s %10s %10s %10s%n",
                "ID", "Name", "Position", "Salary", "Deduct.", "NetPay"));
        for (Employee e : employees.values()) {
            double net = processPayment(e.id);
            totalPayroll += net;
            sb.append(String.format("%-6s %-15s %-13s %10.2f %10.2f %10.2f%n",
                    e.id, e.name, e.position, e.baseSalary, e.deductions, net));
        }
        sb.append(String.format("Total Payroll: %.2f%n", totalPayroll));
        return sb.toString();
    }

    // Reads a double from the console; keeps asking until the input is a valid number.
    private static double readDouble(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            try {
                return Double.parseDouble(line);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    public static void main(String[] args) {
        EmployeeManagementSystem system = new EmployeeManagementSystem();
        Scanner sc = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println();
            System.out.println("===== Employee Management System - Organization X =====");
            System.out.println("1. Add employee");
            System.out.println("2. Remove employee");
            System.out.println("3. Update employee");
            System.out.println("4. Process payment for an employee");
            System.out.println("5. Generate report");
            System.out.println("6. Exit");
            System.out.print("Choose an option (1-6): ");
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1": {
                    System.out.print("Employee ID: ");
                    String id = sc.nextLine().trim();
                    System.out.print("Name: ");
                    String name = sc.nextLine().trim();
                    System.out.print("Position: ");
                    String position = sc.nextLine().trim();
                    double salary = readDouble(sc, "Base salary: ");
                    double deductions = readDouble(sc, "Deductions: ");
                    boolean ok = system.addEmployee(id, name, position, salary, deductions);
                    System.out.println(ok ? "Employee added successfully."
                            : "Could not add employee (duplicate ID or negative salary/deductions).");
                    break;
                }
                case "2": {
                    System.out.print("Employee ID to remove: ");
                    String id = sc.nextLine().trim();
                    boolean ok = system.removeEmployee(id);
                    System.out.println(ok ? "Employee removed." : "Employee ID not found.");
                    break;
                }
                case "3": {
                    System.out.print("Employee ID to update: ");
                    String id = sc.nextLine().trim();
                    double salary = readDouble(sc, "New base salary: ");
                    double deductions = readDouble(sc, "New deductions: ");
                    boolean ok = system.updateEmployee(id, salary, deductions);
                    System.out.println(ok ? "Employee updated." : "Update failed (ID not found or invalid values).");
                    break;
                }
                case "4": {
                    System.out.print("Employee ID: ");
                    String id = sc.nextLine().trim();
                    double net = system.processPayment(id);
                    if (net == -1) {
                        System.out.println("Employee ID not found.");
                    } else {
                        System.out.printf("Net pay for %s: %.2f%n", id, net);
                    }
                    break;
                }
                case "5": {
                    System.out.println();
                    System.out.print(system.generateReport());
                    break;
                }
                case "6":
                    running = false;
                    System.out.println("Goodbye.");
                    break;
                default:
                    System.out.println("Invalid option, please choose 1-6.");
            }
        }
        sc.close();
    }
}