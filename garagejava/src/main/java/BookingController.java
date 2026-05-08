
import org.springframework.web.bind.annotation.*;
import java.sql.*;
import java.util.*;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "*")
public class BookingController {

    static final String URL = "jdbc:mysql://localhost:3306/garage_db";
    static final String USER = "root";
    static final String PASS = "Aryan@003";

    // 1. BOOK A SERVICE (Post data to MySQL)
    @PostMapping("/book")
    public String book(@RequestBody Map<String, String> data) {
        String query = "INSERT INTO appointments (customer_name, vehicle_model, service_type, status) VALUES (?, ?, ?, 'Pending')";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, data.get("name"));
            pstmt.setString(2, data.get("model"));
            pstmt.setString(3, data.get("service"));
            pstmt.executeUpdate();
            return "Success";
        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    // 2. VIEW APPOINTMENTS (Get data from MySQL)
    @GetMapping("/view")
    public List<Map<String, Object>> view() {
        List<Map<String, Object>> list = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM appointments")) {
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("id", rs.getInt("id"));
                row.put("name", rs.getString("customer_name"));
                row.put("model", rs.getString("vehicle_model"));
                row.put("service", rs.getString("service_type"));
                row.put("status", rs.getString("status"));
                list.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}