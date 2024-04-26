package esprit.lanforlife.demo.Controllers;

import esprit.lanforlife.demo.Service.UserService;
import esprit.lanforlife.demo.Entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddUserController implements Initializable {


    @FXML
    private VBox VBoxAddUser;

    @FXML
    private Button btnsignUp;

    @FXML
    private Label hello;

    @FXML
    private Label lbConfirmPassword;

    @FXML
    private Label lbDate;

    @FXML
    private Label lbEmail;

    @FXML
    private Label lbFullName;

    @FXML
    private Label lbNumber;

    @FXML
    private Label lbPassword;

    @FXML
    private PasswordField tfConfirmPassword;

    @FXML
    private DatePicker tfDate;

    @FXML
    private TextField tfEmail;

    @FXML
    private TextField tfNom;

    @FXML
    private PasswordField tfPassword;

    @FXML
    private TextField tfPrenom;

    private boolean bConfirmEmail=false,bEqualPassword = false, bConfirmPassword = false, bDate = false, bName = false, bEmail = false, bAdd = false, bTel = false, bPass = false, bConfPass = false;

    @Override
        public void initialize(URL url, ResourceBundle rb) {
        // Add event listeners to text fields
        tfEmail.setOnKeyReleased(this::validateEmail);
        tfPassword.setOnKeyReleased(this::validatePassword);
        tfConfirmPassword.setOnKeyReleased(this::validatePassword);

    }

    @FXML
    private void fnSignup(ActionEvent event) throws Exception {
        UserService ps = new UserService();

        // Clear previous error messages
        clearErrorLabels();

        // Validate each field and set corresponding boolean flags
        bName = !tfPrenom.getText().isEmpty();
        bEmail = isValidEmail(tfEmail.getText());

        bTel = validateNumero(tfNom.getText());
        String passwordError = isValidPassword(tfPassword.getText(), tfPrenom.getText());
        if (passwordError != null) {
            bPass = false;
            lbPassword.setText(passwordError);
        } else {
            bPass = true;
        }
        bConfPass = !tfConfirmPassword.getText().isEmpty();
        bEqualPassword = bConfPass && tfPassword.getText().equals(tfConfirmPassword.getText());
        bDate = validateDate(tfDate);

        // Check if all fields are valid
        if (bName && bEmail &&  bTel && bPass && bConfPass && bEqualPassword && bDate) {
            User user = new User();
            user.setNom(tfNom.getText());
            user.setPrenom(tfPrenom.getText());
            user.setEmail(tfEmail.getText());
            user.setPassword(tfPassword.getText());
            user.setDate_naissance(Date.valueOf(tfDate.getValue()));
            user.setRoles("[\"ROLE_ADMIN\"]");
            if (ps.signUpUser(user)) {
                // Assuming Main.fxml is the next view after signup
                Parent root = FXMLLoader.load(getClass().getResource("/esprit/lanforlife/demo/DashboardAdmin.fxml"));
                tfPrenom.getScene().setRoot(root);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Account created successfully!");
                alert.setHeaderText(null);
                alert.setTitle("Success");
                alert.show();
            } else {
                showErrorAlert("Email already exists in the database!!");
            }
        } else {
            showErrorAlert("All information should be valid!");
        }
    }

    // Method to clear error labels
    private void clearErrorLabels() {
        lbFullName.setText("");
        lbEmail.setText("");
        lbNumber.setText("");
        lbPassword.setText("");
        lbConfirmPassword.setText("");
        lbDate.setText("");
    }

    // Method to show error alert
    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.setTitle("Invalid Information");
        alert.setHeaderText(null);
        alert.show();

        // Set error messages for specific fields
        if (!bName) {
            lbFullName.setText("Please enter name");
        }
        if (!bEmail) {
            lbEmail.setText("Please enter a valid email");
        }
        if (!bTel) {
            lbNumber.setText("Please enter family name");
        }
        if (!bPass) {
            lbPassword.setText("Please enter a valid password");
        }
        if (!bConfPass) {
            lbConfirmPassword.setText("Please confirm your password");
        }
        if (!bEqualPassword) {
            lbConfirmPassword.setText("Passwords do not match");
        }
        if (!bDate) {
            lbDate.setText("Please choose a valid date (age should be at least 13 years)");
        }
    }

    // Method to validate email
    private void validateEmail(KeyEvent event) {
        String email = tfEmail.getText();
        if (isValidEmail(email)) {
            bEmail = true;
            lbEmail.setText("");
        } else {
            bEmail = false;
            lbEmail.setText("Please enter a valid email");
        }
    }

    // Method to validate confirm email



    // Method to validate password
    private void validatePassword(KeyEvent event) {
        String password = tfPassword.getText();
        String fullName = tfPrenom.getText();

        String passwordError = isValidPassword(password, fullName);
        if (passwordError != null) {
            bPass = false;
            lbPassword.setText(passwordError);
        } else {
            bPass = true;
            lbPassword.setText("");
        }
    }

    // Method to check if email is valid
    private boolean isValidEmail(String email) {
        // Implement your email validation logic here
        // You can use a regex pattern or any other validation method
        // For example, using a simple regex pattern:
        String emailPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailPattern);
    }

    // Method to check if password meets requirements and return specific error message if any
    private String isValidPassword(String password, String fullName) {
        // Password length check
        if (password.length() < 8) {
            return "Password must be at least 8 characters long";
        }
        // Check for uppercase, lowercase, and digit presence
        if (!password.matches(".*[A-Z].*")) {
            return "Password must contain at least one uppercase letter";
        }

        // Check for lowercase presence
        if (!password.matches(".*[a-z].*")) {
            return "Password must contain at least one lowercase letter";
        }

        // Check for digit presence
        if (!password.matches(".*\\d.*")) {
            return "Password must contain at least one digit";
        }
        // Check if the password contains the full name
        if (fullName != null && !fullName.isEmpty()) {
            if (password.toLowerCase().contains(fullName.toLowerCase())) {
                return "Password cannot contain your full name";
            }
        }
        // If password meets all requirements, return null
        return null;
    }

    private boolean validateNumero(String numeroText) {
        // Check if the text is null or empty
        if (numeroText == null || numeroText.isEmpty()) {
            return false;
        }

        // Define a regular expression to match 8 digits starting with 2, 5, or 9
        String regex = "^[2|4|5|9]\\d{7}$";

        // Compile the regular expression pattern
        Pattern pattern = Pattern.compile(regex);

        // Create a matcher to match the pattern against the input text
        Matcher matcher = pattern.matcher(numeroText);

        // Return true if the input text matches the pattern, false otherwise
        return matcher.matches();
    }

    private boolean validateDate(DatePicker datePicker) {
        // Check if the DatePicker is null
        if (datePicker.getValue() == null) {
            return false;
        }

        // Get the chosen date from the DatePicker
        LocalDate chosenDate = datePicker.getValue();

        // Get the current date
        LocalDate currentDate = LocalDate.now();

        // Calculate the minimum date 13 years ago
        LocalDate minDate = currentDate.minusYears(13);

        // Compare the chosen date with the minimum date
        return chosenDate.isBefore(minDate);
    }


}
