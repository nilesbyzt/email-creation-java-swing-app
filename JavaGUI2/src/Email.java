import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;


public class Email extends JFrame implements ActionListener {
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField departmentField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton createButton;

    public Email() {
        super("Email Creation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 2));

        // Create form fields
        JLabel firstNameLabel = new JLabel("First Name:");
        firstNameField = new JTextField();
        JLabel lastNameLabel = new JLabel("Last Name:");
        lastNameField = new JTextField();
        JLabel departmentLabel = new JLabel("Department:");
        departmentField = new JTextField();
        JLabel emailLabel = new JLabel("Alternate Email:");
        emailField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        createButton = new JButton("Create Email");

        // Add form components to the frame
        add(firstNameLabel);
        add(firstNameField);
        add(lastNameLabel);
        add(lastNameField);
        add(departmentLabel);
        add(departmentField);
        add(emailLabel);
        add(emailField);
        add(passwordLabel);
        add(passwordField);
        add(createButton);

        // Register action listener for the create button
        createButton.addActionListener(this);

        // Set frame size and visibility
        setSize(400, 250);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Email());//Email constructor is properly invoked on the event dispatch thread.
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == createButton) {
            if(validateInput()){
                displayInfo();
                saveToDatabase(e);
            }
        }
    }

    private boolean validateInput(){
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String department = departmentField.getText();
        String password = passwordField.getText();

        // Perform validations
        if (firstName.isEmpty() || lastName.isEmpty() || department.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter all fields.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Password validation
        if (!password.matches(".*[A-Z].*")) {
            JOptionPane.showMessageDialog(this, "Password must contain at least one uppercase letter.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!password.matches(".*[!@#$%^&*()].*")) {
            JOptionPane.showMessageDialog(this, "Password must contain at least one special character.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private String generateEmail(String firstName, String lastName, String department) {
        String email = firstName+"."+lastName+"@"+ department +".abc";
        return email;
    }

    private void displayInfo() {
        // Display the email information in a dialog
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String department = departmentField.getText();
        String email = generateEmail(firstName, lastName, department);

        JOptionPane.showMessageDialog(this, "DISPLAY NAME: " + firstNameField.getText().toUpperCase() + " " + lastNameField.getText().toUpperCase() +
                "\nCOMPANY EMAIL: " + email +
                "\nMAILBOX CAPACITY: " + "500" + "mb");
    }

    private void saveToDatabase(ActionEvent event) {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String department = departmentField.getText();
        String email = generateEmail(firstName, lastName, department);


        // Connect to MySQL database
        String url = "jdbc:mysql://localhost:3306/email_database";
        String username = "root";
        String password = "newrootpassword";

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            String query = "INSERT INTO employees (first_name, last_name, department, email) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, department);
            stmt.setString(4, email);

            // // Execute the SQL statement
            // stmt.executeUpdate();

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Registration successful!");
            } else {
                System.out.println("Registration failed.");
            }

            // Close the database connection
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
