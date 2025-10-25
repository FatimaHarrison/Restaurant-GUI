import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//declaring class
public class AuditLog {
    // Default path where audit logs will be saved
    private static final String DEFAULT_DATA_PATH  = "Textual/Data";
    // Formatter for timestamp entries
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");

    // Log structured audit entry
    public static void log(int employId, String role, String actionType, String employName, String records) {
        LocalDateTime timestamp = LocalDateTime.now();
        // Construct a hyphen-separated log entry
        String entry = String.join("-",
                String.valueOf(employId),
                role,
                actionType,
                timestamp.format(FORMATTER),
                employName,
                records);
        // Append the entry to the audit log file
        try (PrintWriter writer = new PrintWriter(new FileWriter(DEFAULT_DATA_PATH , true))) {
            writer.println(entry);
        } catch (Exception e) {
            //Error message
            System.out.println("Audit log error: " + e.getMessage());
        }
    }

    // message-only log
    public static void log(String message) {
        LocalDateTime timestamp = LocalDateTime.now();
        try (PrintWriter writer = new PrintWriter(new FileWriter(DEFAULT_DATA_PATH , true))) {
            writer.println(timestamp.format(FORMATTER) + " - " + message);
        } catch (Exception e) {
            System.out.println("Audit log error: " + e.getMessage());
        }
    }
}
