package FinalProject;
import javax.swing.*;
import java.io.*;
public class WellsFargoBankj {
    private static final StringBuilder sessionLog = new StringBuilder();
    public static void main(String[] args) {
        while (true) {
            try {
                /** Wells Fargo Bank System
                 * 1. Open an Account
                 * 2. Make a Bank Certified Check
                 * 3. Fund Transfer
                 * 4. Log out of system
                 * 5. Exit menu
                 */

                String[] roleOptions = {"Bank Staff", "Bank Customer", "Visitor"};
                int roleType = JOptionPane.showOptionDialog(null,
                        "Select your role:",
                        "Role Selection",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        roleOptions,
                        roleOptions[0]);

                if (roleType == -1) {
                    showSessionLog();
                    System.out.println("Exiting program.");
                    JOptionPane.showMessageDialog(null, "Exiting program.");
                    System.exit(0);
                }

                roleType += 1;
                String message = "Role Selected: " + roleOptions[roleType - 1];
                System.out.println(message);
                JOptionPane.showMessageDialog(null, message);
                sessionLog.append(message).append("\n");

                while (true) {
                    showMenu(roleType);
                    String menuOptionStr = JOptionPane.showInputDialog("Select the Bank Transaction Type:");
                    int menuOption;
                    try {
                        menuOption = Integer.parseInt(menuOptionStr);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid menu option entered.");
                        JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid menu option.");
                        sessionLog.append("Invalid menu option entered.\n");
                        continue;
                    }

                    if (validateInput(menuOption)) {
                        showsubmenu1(menuOption);
                    } else {
                        System.out.println("Invalid option selected.");
                        JOptionPane.showMessageDialog(null, "Invalid option selected. Please try again.");
                        sessionLog.append("Invalid option selected.\n");
                    }
                }
            } catch (IOException e) {
                System.out.println("An error occurred: " + e.getMessage());
                JOptionPane.showMessageDialog(null, "An error occurred: " + e.getMessage());
                sessionLog.append("Error occurred: ").append(e.getMessage()).append("\n");
                showSessionLog();
                System.exit(0);
            } catch (Exception e) {
                System.out.println("Unexpected error occurred: " + e.getMessage());
                JOptionPane.showMessageDialog(null, "Unexpected error occurred: " + e.getMessage());
                sessionLog.append("Unexpected error: ").append(e.getMessage()).append("\n");
                showSessionLog();
                System.exit(0);
            }
        }
    }

    private static void showMenu(int roleType) {
        StringBuilder menu = new StringBuilder("Wells Fargo Bank System Menu\n==========================================\n");

        switch (roleType) {
            case 1:
                menu.append("1. Open Account\n2. Make a Bank Certified Check\n3. Fund Transfer\n4. Log out of system\n5. Exit menu\n");
                break;

            case 2:
                menu.append("1. Open Account\n2. Fund Transfer\n3. Log out of system\n4. Exit menu\n");
                break;

            case 3:
                menu.append("1. Open Account\n2. Find Bank Branch\n3. Get knowledge about the Bank\n4. Exit menu\n");
                break;
        }

        System.out.println(menu.toString());
        JOptionPane.showMessageDialog(null, menu.toString());
    }

    private static void showsubmenu1(int menuOption) throws IOException {
        double balance = 10000.00;

        switch (menuOption) {
            case 1:
                while (true) {
                    String name = JOptionPane.showInputDialog("Please enter your full name:");
                    String password = JOptionPane.showInputDialog("Please enter your password:");

                    if ("bimala".equalsIgnoreCase(name) && "bimala123".equals(password)) {
                        String welcomeMessage = "Welcome to Wells Fargo Bank, your Account Number: ACC176753 " + name;
                        System.out.println(welcomeMessage);
                        JOptionPane.showMessageDialog(null, welcomeMessage);
                        sessionLog.append("Successfully logged in as: ").append(name).append("\n");
                        writeToFile("Account accessed by: " + name);
                        break;
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid credentials. Please try again.");
                        sessionLog.append("Failed login attempt with name: ").append(name).append("\n");
                    }
                }
                break;

            case 2:
                simulateProcessing("Bank Certified Check");
                String ssn = JOptionPane.showInputDialog("Please enter your Social Security Number (SSN) for verification:");
                String phoneNumber = JOptionPane.showInputDialog("Please enter your phone number:");
                String transferAmountStr = JOptionPane.showInputDialog("Enter the amount you want to transfer:");
                double transferAmount;
                try {
                    transferAmount = Double.parseDouble(transferAmountStr);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid amount entered.");
                    JOptionPane.showMessageDialog(null, "Invalid amount entered.");
                    sessionLog.append("Failed fund transfer due to invalid input.\n");
                    writeToFile("Failed fund transfer attempt due to invalid input.");
                    return;
                }

                if (transferAmount > 0 && transferAmount <= balance) {
                    balance -= transferAmount;
                    String successMessage = "Transaction of $" + transferAmount + " has been processed. Remaining balance: $" + balance;
                    System.out.println(successMessage);
                    JOptionPane.showMessageDialog(null, successMessage);
                    sessionLog.append("Fund Transfer: $").append(transferAmount).append(" | Remaining Balance: $").append(balance).append("\n");
                    writeToFile("Fund Transfer: $" + transferAmount + " | Remaining Balance: $" + balance);
                } else if (transferAmount <= 0) {
                    System.out.println("Transfer amount must be greater than zero.");
                    JOptionPane.showMessageDialog(null, "Transfer amount must be greater than zero.");
                    sessionLog.append("Failed fund transfer: Amount less than or equal to zero.\n");
                } else {
                    System.out.println("Insufficient balance. Your balance is $" + balance);
                    JOptionPane.showMessageDialog(null, "Insufficient balance. Your balance is $" + balance);
                    sessionLog.append("Failed fund transfer due to insufficient balance.\n");
                    writeToFile("Failed fund transfer due to insufficient balance.");
                }
                break;

            case 3:
                readFromFile();
                break;

            default:
                System.out.println("Invalid menu option selected.");
                JOptionPane.showMessageDialog(null, "Invalid menu option.");
                sessionLog.append("Invalid menu option selected.\n");
                break;
        }
    }

    private static boolean validateInput(int input) {
        final int MAX_OPTIONS = 3;
        return input > 0 && input <= MAX_OPTIONS;
    }

    private static void writeToFile(String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("transaction_log.txt", true))) {
            writer.write(content + "\n");
        }
        System.out.println("Written to file: " + content);
    }

    private static void readFromFile() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader("transaction_log.txt"))) {
            StringBuilder logs = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                logs.append(line).append("\n");
            }
            System.out.println("Transaction Logs:\n" + logs);
            JOptionPane.showMessageDialog(null, logs.toString(), "Transaction Logs", JOptionPane.INFORMATION_MESSAGE);
            sessionLog.append("Viewed transaction logs.\n");
        }
    }

    private static void simulateProcessing(String taskName) {
        for (int i = 3; i > 0; i--) {
            String progressMessage = "Processing " + taskName + "... " + i + " seconds remaining";
            System.out.println(progressMessage);
            JOptionPane.showMessageDialog(null, progressMessage);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Error during processing " + taskName);
                JOptionPane.showMessageDialog(null, "Error during processing.");
                sessionLog.append("Error during processing ").append(taskName).append("\n");
            }
        }
        System.out.println(taskName + " completed successfully.");
        JOptionPane.showMessageDialog(null, taskName + " completed successfully.");
        sessionLog.append(taskName).append(" completed successfully.\n");
    }

    private static void showSessionLog() {
        System.out.println("=== Session Log ===");
        System.out.println(sessionLog.toString());
        JOptionPane.showMessageDialog(null, sessionLog.toString(), "Session Summary", JOptionPane.INFORMATION_MESSAGE);
    }
}
