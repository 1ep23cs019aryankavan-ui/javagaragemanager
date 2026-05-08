import java.sql.*;

import java.util.Scanner;

public class VehicleServiceSystem {

    // Database credentials

    static final String URL = "jdbc:mysql://localhost:3306/garage_db";

    static final String USER = "root"; // Change to your MySQL username

    static final String PASS = "Aryan@003"; // Change to your MySQL password

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        while (true) {

            System.out.println("\n--- Garage Service Booking System ---");

            System.out.println("1. Customer: Book a Service");

            System.out.println("2. Mechanic: View & Update Appointments");

            System.out.println("3. Exit");

            System.out.print("Select an option: ");

            int choice = scanner.nextInt();

            scanner.nextLine(); // Consume newline

            switch (choice) {

                case 1:
                    bookAppointment();
                    break;

                case 2:
                    mechanicDashboard();
                    break;

                case 3:
                    System.exit(0);

                default:
                    System.out.println("Invalid choice!");

            }

        }

    }

    // --- CUSTOMER MODULE ---

    private static void bookAppointment() {

        System.out.print("Enter Your Name: ");

        String name = scanner.nextLine();

        System.out.print("Enter Vehicle Model: ");

        String model = scanner.nextLine();

        System.out.print("Enter Service Required (e.g., Oil Change, Repair): ");

        String service = scanner.nextLine();

        String query = "INSERT INTO appointments (customer_name, vehicle_model, service_type) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);

                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, name);

            pstmt.setString(2, model);

            pstmt.setString(3, service);

            pstmt.executeUpdate();

            System.out.println("Success: Service booked successfully!");

        } catch (SQLException e) {

            System.err.println("Database Error: " + e.getMessage());

        }

    }

    // --- MECHANIC MODULE ---

    private static void mechanicDashboard() {

        System.out.println("\n--- Active Appointments ---");

        String selectQuery = "SELECT * FROM appointments WHERE status != 'Completed'";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);

                Statement stmt = conn.createStatement();

                ResultSet rs = stmt.executeQuery(selectQuery)) {

            while (rs.next()) {

                System.out.printf("ID: %d | Customer: %s | Vehicle: %s | Service: %s | Status: %s\n",

                        rs.getInt("id"), rs.getString("customer_name"),

                        rs.getString("vehicle_model"), rs.getString("service_type"),

                        rs.getString("status"));

            }

            System.out.print("\nEnter ID to update status (or 0 to go back): ");

            int id = scanner.nextInt();

            if (id == 0)
                return;

            System.out.print("Enter New Status (In Progress / Completed): ");

            scanner.nextLine(); // consume newline

            String newStatus = scanner.nextLine();

            updateStatus(id, newStatus);

        } catch (SQLException e) {

            System.err.println("Database Error: " + e.getMessage());

        }

    }

    private static void updateStatus(int id, String status) {

        String query = "UPDATE appointments SET status = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);

                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, status);

            pstmt.setInt(2, id);

            int rows = pstmt.executeUpdate();

            if (rows > 0)
                System.out.println("Status updated successfully!");

            else
                System.out.println("Appointment ID not found.");

        } catch (SQLException e) {

            System.err.println("Error updating status: " + e.getMessage());

        }

    }

}