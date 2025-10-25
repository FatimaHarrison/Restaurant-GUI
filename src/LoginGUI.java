//Fatima Harrison, CEN 3024C, 10/21/25
// In this project users would be able to interact with live GUIs panels based from a famous restaurant theme Terralina.
// User will be able to access their role based menus and are prompted to login and access files throughout the process.
//Overall, instead of using the CLI, this project is a digital screen popup feature that is programed for easy usages.
// Each menu options consist action buttons for users to load files, use CRUD functions and for manager role allow it to access the custom action.
//If errors occurs, exceptions would notify users whether a file can't be loaded or a invalid entry has been detected.
// If everything passes, the output would display the loaded files and all CRUD functions.
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//Declaring primary class
public class LoginGUI extends JFrame {
    //Declaring class attributes
    //Declaring HostMenuGUI layout functions
    private JPanel LoginGUI;
    private JTextField userRole;
    private JPasswordField employId;
    private JButton btnSubmit;
    private JLabel Password;
    private JLabel Username;
    //GUI Setter methods
    public LoginGUI() {
        setTitle("Compass Group Employee Login"); //Title
        setContentPane(LoginGUI);//Set content to display layout
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Stop run
        setSize(350, 250);//Setting size
        setLocationRelativeTo(null);//Location of popup menu
        setVisible(true);//Display panel

        //Load login data once
        DMS.loadFromFile();
        //Action button to submit
        btnSubmit.addActionListener(new ActionListener() {
            @Override //Wired function from panel
            public void actionPerformed(ActionEvent e) {
                String role = userRole.getText().trim(); //Username
                String idInput = new String(employId.getPassword()).trim();//Password

                //Role validation
                if (!role.equalsIgnoreCase("Host") && !role.equalsIgnoreCase("Manager")) {
                    JOptionPane.showMessageDialog(null, "Access Denied: Unauthorized User.");
                    return;
                }

                //ID format validation
                if (!idInput.matches("\\d{7}")) {
                    JOptionPane.showMessageDialog(null, "Invalid ID format. Must be 7 digits.");
                    return;
                }

                int employeeId = Integer.parseInt(idInput);

                // Login using DMS validation
                if (!DMS.validateLogin(employeeId, role)) {
                    JOptionPane.showMessageDialog(null, "Access Denied: ID or Role do not match system records.");
                    return;
                }

                // System launch appropriate dashboard based on user login.
                JOptionPane.showMessageDialog(null, "Logged in as: " + role);
                if (role.equalsIgnoreCase("Host")) {
                    new HostMenuGUI(); //Host Menu
                } else {
                    new ManaMenuGUI(); //Management Menu
                }
                dispose(); // Close login window
            }
        });
    }
   //(Main) file runner
    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginGUI::new);
    }
}
