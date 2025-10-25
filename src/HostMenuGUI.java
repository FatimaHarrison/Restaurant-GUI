import javax.swing.*;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
//Declaring primary class
public class HostMenuGUI extends JFrame {
    //GUI layout functions
    private JButton loadDataButton;
    private JButton displayDataButton;
    private JButton newReservationButton;
    private JButton cancelReservationButton;
    private JButton updateReservationButton;
    private JButton logoutButton;
    private JTextArea display2;
    private JList list2;
    private JPanel HostMenuGUI;
    private File loadFile = null;
    //To read over the records array list for loading the file
    private final List<Record> records = new ArrayList<>();

      //Setting GUI layouts
    public HostMenuGUI() {
        setTitle("Host Dashboard");//Tile
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//Stop run
        setSize(500, 550);//Set size
        setLocationRelativeTo(null);//Set location popup
        setContentPane(HostMenuGUI);//Getting the content
        setVisible(true); //Display the panel

        // Load file and parse into records
        loadDataButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select file:");
            //Opens CPU file dialog
            int result = fileChooser.showOpenDialog(HostMenuGUI);
            if (result == JFileChooser.APPROVE_OPTION) {
                loadFile = fileChooser.getSelectedFile();
                records.clear();
               //Restrict user from opening other files other than records.
                try (Scanner scanner = new Scanner(loadFile)) {
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine().trim();
                        String[] parts = line.split("-"); //File seperator
                        //List of arrays required to pass in file.
                        if (parts.length >= 7) {
                            int id = Integer.parseInt(parts[0]);
                            String name = parts[1].trim();
                            int partySize = Integer.parseInt(parts[2].trim());
                            String email = parts[3].trim();
                            String phone = parts[4].trim();
                            String date = parts[5].trim();
                            String time = parts[6].trim();
                            String notes = (parts.length > 7) ? parts[7].trim() : "";
                            //Stores in file information
                            int generatedId = 1000000 + records.size(); //7-digit ID
                            Record r = new Record(generatedId, name, email, phone, partySize, date, time, notes);
                            records.add(r);
                        }
                    }//Notify user whether it passed or failed giving a error or success message.
                    JOptionPane.showMessageDialog(null, "File loaded successfully:\n" + loadFile.getName());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Access Denied\n");
                }
            }
        });

        // Display current records
        displayDataButton.addActionListener(e -> {
            if (records.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No reservation records loaded.");
                return;
            }
            StringBuilder builder = new StringBuilder();
            for (Record r : records) {
                builder.append(r.toString()).append("\n");
            }
            display2.setText(builder.toString()); // Displays in text box
        });

        // Add new reservation and save
        newReservationButton.addActionListener(e -> {
            if (loadFile == null) { //Notify user that there is no file loaded
                JOptionPane.showMessageDialog(null, "No file loaded.");
                return;
            }
            try { //Prompt user to enter a new ID number.
                String idInput = JOptionPane.showInputDialog("Enter Reservation ID:");
                if (idInput == null || !idInput.matches("\\d{7}")) { //Must be 7 digits or else get an error message.
                    JOptionPane.showMessageDialog(null, "Invalid ID format.");
                    return;
                }
                int id = Integer.parseInt(idInput.trim());
                //Check if input is a copy of a existing ID
                boolean exists = records.stream().anyMatch(r -> r.getId().equals(id));
                if (exists) { //Error message
                    JOptionPane.showMessageDialog(null, "Reservation ID already exists.");
                    return;
                }
                //If passed, move on to the next prompts.
                String name = JOptionPane.showInputDialog("Name:");
                String email = JOptionPane.showInputDialog("Email:");
                String phone = JOptionPane.showInputDialog("Phone Number:");
                String partySizeInput = JOptionPane.showInputDialog("Party Size:");
                String date = JOptionPane.showInputDialog("Date (YYYY/MM/DD):");
                String time = JOptionPane.showInputDialog("Time (hh:mm AM/PM):");
                String notes = JOptionPane.showInputDialog("Notes:");

                int partySize = Integer.parseInt(partySizeInput.trim());

                Record newRecord = new Record(id, name.trim(), email.trim(), phone.trim(),
                        partySize, date.trim(), time.trim(), notes != null ? notes.trim() : "");

                records.add(newRecord);
                saveRecordsToFile();
                JOptionPane.showMessageDialog(null, "Reservation created:\n" + newRecord.toString());

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error creating reservation: " + ex.getMessage());
            }
        });

        // Cancel reservation and save
        cancelReservationButton.addActionListener(e -> {
            if (loadFile == null) { //Error message
                JOptionPane.showMessageDialog(null, "No file loaded.");
                return;
            }//Prompts user to enter a ID number to remove
            String idStr = JOptionPane.showInputDialog("Enter Reservation ID to cancel:");
            if (idStr == null || !idStr.matches("\\d{7}")) { //Invalided ID format checker
                JOptionPane.showMessageDialog(null, "Invalid ID format.");
                return;
            }

            int id = Integer.parseInt(idStr.trim());
            boolean removed = records.removeIf(r -> r.getId().equals(id));
            if (removed) {
                saveRecordsToFile(); //Validation passed message
                JOptionPane.showMessageDialog(null, "Reservation removed.");
            } else { //Validation failed message
                JOptionPane.showMessageDialog(null, "Reservation not found.");
            }
        });

        // Update reservation and save
        updateReservationButton.addActionListener(e -> {
            if (loadFile == null) { // Check if a file is loaded before proceeding
                JOptionPane.showMessageDialog(null, "No file loaded.");
                return;
            }
            // Prompt user for reservation ID to update
            String idStr = JOptionPane.showInputDialog("Enter Reservation ID to update:");
            if (idStr == null || !idStr.matches("\\d{7}")) {
                JOptionPane.showMessageDialog(null, "Invalid ID format.");
                return;
            }
            // Parse the ID and find the matching record
            int id = Integer.parseInt(idStr.trim());
            Record target = records.stream()
                    .filter(r -> r.getId().equals(id))
                    .findFirst()
                    .orElse(null);
//Show error if no matching record is found
            if (target == null) {
                JOptionPane.showMessageDialog(null, "Reservation not found.");
                return;
            }
            // Prompting user for the field to update
            String field = JOptionPane.showInputDialog("Field to update (name, email, phone number, date, time, notes):");
            if (field == null || field.trim().isEmpty()) return;

            String value = JOptionPane.showInputDialog("New value for " + field + ":");
            if (value == null || value.trim().isEmpty()) return;

            try { // Update the appropriate field based on user input
                switch (field.trim().toLowerCase()) {
                    case "name": target.setName(value); break;
                    case "email": target.setEmail(value); break;
                    case "phone number": target.setPhoneNumber(value); break;
                    case "date": target.setDate(value); break;
                    case "time": target.setTime(value); break;
                    case "notes": target.setNotes(value); break;
                    default:
                        JOptionPane.showMessageDialog(null, "Invalid field.");
                        return;
                }
                //Saves the new update
                saveRecordsToFile();
                JOptionPane.showMessageDialog(null, "Reservation updated:\n" + target.toString());
            } catch (Exception ex) { //Error message
                JOptionPane.showMessageDialog(null, "Update failed: " + ex.getMessage());
            }
        });

        logoutButton.addActionListener(e -> System.exit(0));
    }

    // Save current records to file
    private void saveRecordsToFile() {
        try (PrintWriter writer = new PrintWriter(loadFile)) {
            for (Record r : records) {
                writer.println(String.join("-",
                        r.getName(),
                        String.valueOf(r.getPartySize()),
                        r.getEmail(),
                        r.getPhoneNumber(),
                        r.getDate(),
                        r.getTime(),
                        r.getNotes()));
            }
        } catch (Exception ex) { //error message
            JOptionPane.showMessageDialog(null, "Error saving file: " + ex.getMessage());
        }
    }

    }

