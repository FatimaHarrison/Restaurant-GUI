import javax.swing.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;
import java.awt.event.*;
import java.io.*;

//Declaring class extended to the JFrame
//Declaring GUI layouts
public class ManaMenuGUI extends JFrame {
    private JButton Load;
    private JButton RemoveData;
    private JButton AuditLog;
    private JButton EmployeeRecords;
    private JButton Logout;
    private JButton Display;
    private JTextArea logDisplay;

    private JPanel ManaMenuGUI;
    private JList list1;
    private List<String> logLines = new ArrayList<>();
    private File loadedFile = null;// replaces 'logFiles'
  //Setting GUI layouts
    public ManaMenuGUI() {
        DMS.loadFromFile(); // Load roles from Data.txt
        setTitle("Manager Dashboard"); //Title
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//Stop run
        setSize(500, 550);//Set size
        setLocationRelativeTo(null);//Disply pop up
        setContentPane(ManaMenuGUI); // Use the field, not a new panel
        setVisible(true);//Display panel

        // Action listener for the "Load" button
        Load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open a file chooser dialog for selecting a log file
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Select file:");

                // Show the dialog and capture the result
                int result = fileChooser.showOpenDialog(ManaMenuGUI);
                if (result == JFileChooser.APPROVE_OPTION) {
                    // Store the selected file and clear any previously loaded log lines
                    loadedFile = fileChooser.getSelectedFile();
                    logLines.clear();

                    // Reading the file line-by-line and storing each line
                    try (Scanner scanner = new Scanner(loadedFile)) {
                        while (scanner.hasNextLine()) {
                            logLines.add(scanner.nextLine());
                        }
                        // Notify user of successful file load
                        JOptionPane.showMessageDialog(null, "File loaded successfully:\n" + loadedFile.getName());
                    } catch (Exception ex) {
                        // Show error if file reading fails
                        JOptionPane.showMessageDialog(null, "Error reading file:\n" + ex.getMessage());
                    }
                }
            }
        });

// Action listener to display the data
        Display.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Check if log data is available
                if (logLines.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No logs loaded.");
                    return;
                }

                // Build a string from all log lines and display it
                StringBuilder builder = new StringBuilder();
                for (String line : logLines) {
                    builder.append(line).append("\n");
                }
                logDisplay.setText(builder.toString());
            }
        });

// Action listener to remove data
        RemoveData.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ensure a file is loaded before attempting removal
                if (loadedFile == null) {
                    JOptionPane.showMessageDialog(null, "No file loaded. Please load a file first.");
                    return;
                }

                // Prompt user for a keyword or ID to remove
                String keyword = JOptionPane.showInputDialog("Enter keyword or ID to remove:");
                if (keyword == null || keyword.trim().isEmpty()) return;

                // Filter out lines containing the keyword
                List<String> updated = new ArrayList<>();
                boolean found = false;
                for (String line : logLines) {
                    if (line.contains(keyword)) {
                        found = true;
                        continue; // skip matching line
                    }
                    updated.add(line);
                }

                // If a match was found, overwrite the file with updated lines
                if (found) {
                    try (PrintWriter writer = new PrintWriter(loadedFile)) {
                        for (String line : updated) {
                            writer.println(line);
                        }
                        logLines = updated; // update in memory log
                        JOptionPane.showMessageDialog(null, "Log entry removed.");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Error updating log file: " + ex.getMessage());
                    }
                } else {
                    // Notify user if no matching entry was found
                    JOptionPane.showMessageDialog(null, "No matching entry found.");
                }
            }
        });

        // Action listener for displaying the AuditLog
        AuditLog.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Reuse the display logic by clicking the Display button
                Display.doClick();
            }
        });

       // Action listener to direct to the EmployeeRecords
        EmployeeRecords.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Prompt for manager ID to authorize access
                String authIdInput = JOptionPane.showInputDialog("Authorized Manager ID:");
                if (authIdInput != null && !authIdInput.trim().isEmpty()) {
                    try {
                        int authId = Integer.parseInt(authIdInput.trim());
                        // Check if ID exists and Manager role
                        if (DMS.validUsers.containsKey(authId) &&
                                DMS.validUsers.get(authId).equalsIgnoreCase("Manager")) {

                            JOptionPane.showMessageDialog(null, "Access granted.");
                            new EmployeeRecordGUI(); // Launch employee record interface
                        } else {
                            JOptionPane.showMessageDialog(null, "Access denied. Invalid Manager ID.");
                        }
                    } catch (NumberFormatException ex) {
                        // Handle invalid numeric input
                        JOptionPane.showMessageDialog(null, "Invalid ID format.");
                    }
                }
            }
        });
        // Action listener to exit menu
        Logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Terminate the application
                System.exit(0);
            }
        });
    }
    // File runner
    public static void main(String[] args) {
        SwingUtilities.invokeLater(ManaMenuGUI::new);
    }
}