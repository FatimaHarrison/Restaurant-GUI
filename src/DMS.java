import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class DMS {
    private static final String DEFAULT_DATA_PATH = "Textual/Data"; // GUI-safe default path
    public static Map<Integer, String> validUsers = new HashMap<>();

    //  Load user roles from file
    public static void loadFromFile() {
        validUsers.clear();
        File dataFile = new File(DEFAULT_DATA_PATH);

        if (!dataFile.exists()) {
            JOptionPane.showMessageDialog(null, "Login file not found: " + DEFAULT_DATA_PATH);
            return;
        }

        try (Scanner scanner = new Scanner(dataFile)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                String[] parts = line.split("-", 3); // ID-Role-Action-...

                if (parts.length >= 2 && parts[0].matches("\\d{7}")) {
                    int id = Integer.parseInt(parts[0]);
                    String role = parts[1].trim();
                    validUsers.put(id, role);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error loading login file:\n" + e.getMessage());
        }
    }

    //  Validate login for GUI use
    public static boolean validateLogin(int id, String role) {
        return validUsers.containsKey(id) &&
                validUsers.get(id).equalsIgnoreCase(role);
    }

    //GUI-prompt
    public static boolean promptLogin(String expectedRole) {
        String input = JOptionPane.showInputDialog("Enter your Authorized " + expectedRole + " ID:");
        if (input == null || !input.matches("\\d{7}")) {
            JOptionPane.showMessageDialog(null, "Invalid ID format. Must be 7 digits.");
            return false;
        }

        int id = Integer.parseInt(input.trim());
        if (validateLogin(id, expectedRole)) {
            JOptionPane.showMessageDialog(null, "Access granted.");
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Access denied. Invalid " + expectedRole + " ID.");
            return false;
        }
    }

    public void loadFromFile(String path) {

    }
}
