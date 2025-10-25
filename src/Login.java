import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
//Declaring primary class
public class Login {
    //Static method fro validUser on LoginGUI file.
    public static Map<Integer, String> validUsers = new HashMap<>();
//Login file scanner retrieve information fro data file.
    public Login() {
        //Setting file path
        File dataFile = new File("Textual/Data"); // Adjust path if needed
        try (Scanner scanner = new Scanner(dataFile)) {
            while (scanner.hasNextLine()) { //Scans over file
                String line = scanner.nextLine().trim();
                String[] parts = line.split("-"); //Line splitter
                //Extracts only the first to arrays
                if (parts.length >= 2) {
                    try { //Only gets the ID and name from file
                        int id = Integer.parseInt(parts[0].trim());
                        String role = parts[1].trim();
                        validUsers.put(id, role);
                    } catch (NumberFormatException ignored) {
                    }
                }
            }//Shows error message.
        } catch (Exception e) {
            System.out.println("Error loading manager data: " + e.getMessage());
        }
    }

}
