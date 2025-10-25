//GUI and format imports
import javax.swing.*;
import java.io.File;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
//Declaring primary class
public class EmployeeRecordGUI extends JFrame {
    //Declaring class attributes
    //Declaring GUI layout functions
    private JList list3;
    private JButton loadButton;
    private JButton addNewInfoButton;
    private JButton deleteEmployeeButton;
    private JButton displyRecordsButton;
    private JTextArea recordsDisplay;
    private JPasswordField veriFy;
    private JButton mainMenuButton;
    private JPanel EmployeeRecordGUI;
    private JLabel UserLogin;
    //file loader
    private File loadedFile = null;
    //To get a specific file (employee records)
    private final List<EmployeeRecord> employeeRecords = new ArrayList<>();
    //GUI setters
    public EmployeeRecordGUI() {
        setTitle("Record Dashboard");//Title
        setContentPane(EmployeeRecordGUI);//Set content to display layout
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//Stop run
        setSize(500, 550);//Set size
        setVisible(true);//Display panel
        setLocationRelativeTo(null);//Location of popup menu

        //Load employee records from file
        loadButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select employee record file");
            //System prompts and allows user to load a file.
            int result = fileChooser.showOpenDialog(EmployeeRecordGUI);
            if (result == JFileChooser.APPROVE_OPTION) {
                loadedFile = fileChooser.getSelectedFile();
                employeeRecords.clear();//Searches and wires only for specific file.
                //Restricts manager from loading other files.
                try (Scanner scanner = new Scanner(loadedFile)) {
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine().trim();
                        String[] parts = line.split("-"); //file separator
                        //Array list
                        if (parts.length >= 6) {
                            int id = Integer.parseInt(parts[0].trim());
                            String name = parts[1].trim();
                            String gender = parts[2].trim();
                            String role = parts[3].trim();
                            String status = parts[4].trim();
                            LocalDate hireDate = LocalDate.parse(parts[5].trim(), DateTimeFormatter.ofPattern("yyyy/MM/dd"));
                            //Matches attributes to file to be verified and loaded.
                            EmployeeRecord emp = new EmployeeRecord(id, name, gender, role, status, hireDate);
                            employeeRecords.add(emp);
                        }
                    }
                    //Notifications for user
                    JOptionPane.showMessageDialog(null, "File loaded successfully:\n" + loadedFile.getName());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error reading file:\n" + ex.getMessage());
                }
            }
        });

        // Add new employee and save into the file.
        addNewInfoButton.addActionListener(e -> {
            if (loadedFile == null) { //error message
                JOptionPane.showMessageDialog(null, "No file loaded.");
                return;
            }
            try {//Prompting user to insert new employee info.
                String idInput = JOptionPane.showInputDialog("Enter New Employee ID:");
                if (idInput == null || !idInput.matches("\\d{7}")) { //Must be 7 digits
                    JOptionPane.showMessageDialog(null, "Employee ID must be 7 digits."); //Notify user invalid input
                    return;
                }
                int employId = Integer.parseInt(idInput.trim());
                //Shows invalid entry for existing ID number
                boolean exists = employeeRecords.stream().anyMatch(emp -> emp.getEmployID() == employId);
                if (exists) {
                    JOptionPane.showMessageDialog(null, "Employee record already exists.");
                    return;
                }
                //Prompts user to input other data.
                String name = JOptionPane.showInputDialog("Enter Employee Full Name:");
                String gender = JOptionPane.showInputDialog("Enter Gender:");
                String role = JOptionPane.showInputDialog("Enter Employee Role:");
                String status = JOptionPane.showInputDialog("Enter Working Status:");
                //Must choose between the two
                if (status == null || (!status.equalsIgnoreCase("FullTime") && !status.equalsIgnoreCase("PartTime"))) {
                    JOptionPane.showMessageDialog(null, "Status must be 'FullTime' or 'PartTime'."); //Error message
                    return;
                }
                //Date validation
                String dateInput = JOptionPane.showInputDialog("Enter Hire Date (yyyy/MM/dd):");
                LocalDate hireDate;
                try {
                    hireDate = LocalDate.parse(dateInput.trim(), DateTimeFormatter.ofPattern("yyyy/MM/dd"));
                } catch (DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid date format. Use yyyy/MM/dd.");
                    return;
                }
                //Goes over user input
                EmployeeRecord newEmp = new EmployeeRecord(employId, name.trim(), gender.trim(), role.trim(), status.trim(), hireDate);
                employeeRecords.add(newEmp);
                saveEmployeeRecordsToFile(); //Adds new employee record in file if passed.
                JOptionPane.showMessageDialog(null, "Employee record added:\n" + newEmp.toString());

            } catch (Exception ex) {//Gets error message if failed.
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            }
        });

        // Delete employee and save
        deleteEmployeeButton.addActionListener(e -> {
            if (loadedFile == null) { //Have to load a file before, or else get a error message.
                JOptionPane.showMessageDialog(null, "No file loaded.");
                return;
            } //Prompts user to input existing ID number.
            String idStr = JOptionPane.showInputDialog("Enter Employee ID:");
            if (idStr == null || !idStr.matches("\\d{7}")) { //Must be 7 digits.
                JOptionPane.showMessageDialog(null, "Invalid ID format.");
                return;
            }
            //Stored in user input
            int empId = Integer.parseInt(idStr.trim());
            boolean removed = employeeRecords.removeIf(emp -> emp.getEmployID() == empId);
            if (removed) { //If it's valid and found in file it gets removed
                saveEmployeeRecordsToFile();
                //Notify user once its removed.
                JOptionPane.showMessageDialog(null, "Employee record removed.");
            } else { //If not the error message.
                JOptionPane.showMessageDialog(null, "Employee record not found.");
            }
        });

        // Display current employee records
        displyRecordsButton.addActionListener(e -> {
            if (employeeRecords.isEmpty()) { //Shows message if there nothing there
                JOptionPane.showMessageDialog(null, "No employee records loaded.");
                return;
            }//displays the employee record file.
            StringBuilder builder = new StringBuilder();
            for (EmployeeRecord emp : employeeRecords) {
                builder.append(emp.toString()).append("\n");
            }
            recordsDisplay.setText(builder.toString());
        });

        // Return to manager menu
        mainMenuButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Returning to Manager Dashboard...");
            new ManaMenuGUI();
            dispose();
        });
    }

    // Save employee records to file
    private void saveEmployeeRecordsToFile() {
        try (PrintWriter writer = new PrintWriter(loadedFile)) {
            for (EmployeeRecord emp : employeeRecords) {
                writer.println(String.join("-", //Must have this file separator.
                        //Getter methods
                        String.valueOf(emp.getEmployID()),
                        emp.getEmployName(),
                        emp.getEmployGen(),
                        emp.getEmployRole(),
                        emp.getEmployStat(),
                        emp.getHireDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))));
            }
        } catch (Exception ex) { //Error message
            JOptionPane.showMessageDialog(null, "Error saving file: " + ex.getMessage());
        }
    }

    }

