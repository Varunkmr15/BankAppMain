package ui;

import service.UserService;
import service.BankService;
import admin.AdminService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        service.UserService userService = new service.UserService();
        service.BankService bankService = new service.BankService();
        admin.AdminService adminService = new admin.AdminService();

        System.out.println("\n==============================");
        System.out.println("   Welcome to AMDOCS Bank Console   ");
        System.out.println("==============================\n");

        while (true) {
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Admin Login");
            System.out.println("0. Exit");
            System.out.print("Select option: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    System.out.print("Enter username: ");
                    String username = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String password = scanner.nextLine();
                    System.out.print("Full name: ");
                    String fullName = scanner.nextLine();
                    System.out.print("Email: ");
                    String email = scanner.nextLine();
                    model.User newUser = new model.User(0, username, "", fullName, email, false, false);
                    boolean reg = userService.registerUser(newUser, password);
                    if (reg) {
                        // Create account for new user
                        model.Account account = new model.Account(0, newUser.getId(), java.math.BigDecimal.ZERO, false);
                        boolean accCreated = new dao.AccountDAO().createAccount(account);
                        System.out.println(accCreated ? "Account created!" : "Account creation failed.");
                    }
                    System.out.println(reg ? "Registration successful!" : "Registration failed.");
                    break;
                case "2":
                    System.out.print("Username: ");
                    String user = scanner.nextLine();
                    System.out.print("Password: ");
                    String pass = scanner.nextLine();
                    model.User loggedIn = userService.login(user, pass);
                    if (loggedIn != null) {
                        System.out.println("\nWelcome, " + loggedIn.getFullName() + "!");
                        userMenu(scanner, loggedIn, bankService, userService);
                    } else {
                        System.out.println("Login failed.");
                    }
                    break;
                case "3":
                    System.out.print("Admin username: ");
                    String adminUser = scanner.nextLine();
                    System.out.print("Password: ");
                    String adminPass = scanner.nextLine();
                    model.User admin = userService.login(adminUser, adminPass);
                    if (admin != null && admin.isAdmin()) {
                        System.out.println("\nWelcome Admin!");
                        adminMenu(scanner, adminService);
                    } else {
                        System.out.println("Admin login failed.");
                    }
                    break;
                case "0":
                    System.out.println("Thank you for using MyBank. Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
            System.out.println();
        }
    }

    private static void userMenu(Scanner scanner, model.User user, service.BankService bankService, service.UserService userService) {
        while (true) {
            System.out.println("\n--- User Menu ---");
            System.out.println("1. View Profile");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. Check Balance");
            System.out.println("6. Transaction History");
            System.out.println("7. Update Profile");
            System.out.println("8. Delete Account");
            System.out.println("0. Logout");
            System.out.print("Select option: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    model.User profile = userService.viewProfile(user.getUsername());
                    System.out.println(profile);
                    break;
                case "2": {
                    model.Account account = new dao.AccountDAO().getAccountByUserId(user.getId());
                    if (account == null) {
                        System.out.println("No account found for user.");
                        break;
                    }
                    System.out.print("Amount to deposit: ");
                    double dep = Double.parseDouble(scanner.nextLine());
                    boolean depRes = bankService.deposit(account.getAccountId(), dep);
                    System.out.println(depRes ? "Deposit successful." : "Deposit failed.");
                    break;
                }
                case "3": {
                    model.Account account = new dao.AccountDAO().getAccountByUserId(user.getId());
                    if (account == null) {
                        System.out.println("No account found for user.");
                        break;
                    }
                    System.out.print("Amount to withdraw: ");
                    double wd = Double.parseDouble(scanner.nextLine());
                    boolean wdRes = bankService.withdraw(account.getAccountId(), wd);
                    System.out.println(wdRes ? "Withdraw successful." : "Withdraw failed.");
                    break;
                }
                case "4": {
                    model.Account account = new dao.AccountDAO().getAccountByUserId(user.getId());
                    if (account == null) {
                        System.out.println("No account found for user.");
                        break;
                    }
                    System.out.print("Recipient account ID: ");
                    int toAcc = Integer.parseInt(scanner.nextLine());
                    System.out.print("Amount to transfer: ");
                    double tf = Double.parseDouble(scanner.nextLine());
                    boolean tfRes = bankService.transfer(account.getAccountId(), toAcc, tf);
                    System.out.println(tfRes ? "Transfer successful." : "Transfer failed.");
                    break;
                }
                case "5": {
                    model.Account account = new dao.AccountDAO().getAccountByUserId(user.getId());
                    if (account == null) {
                        System.out.println("No account found for user.");
                        break;
                    }
                    double bal = bankService.getBalance(account.getAccountId());
                    System.out.println("Current Balance: " + bal);
                    break;
                }
                case "6": {
                    model.Account account = new dao.AccountDAO().getAccountByUserId(user.getId());
                    if (account == null) {
                        System.out.println("No account found for user.");
                        break;
                    }
                    java.util.List<model.Transaction> txs = bankService.getTransactionHistory(account.getAccountId());
                    System.out.println("--- Transaction History ---");
                    for (model.Transaction tx : txs) {
                        System.out.println(tx);
                    }
                    break;
                }
                case "7":
                    System.out.print("New full name: ");
                    String newName = scanner.nextLine();
                    System.out.print("New email: ");
                    String newEmail = scanner.nextLine();
                    user.setFullName(newName);
                    user.setEmail(newEmail);
                    boolean upRes = userService.updateProfile(user);
                    System.out.println(upRes ? "Profile updated." : "Update failed.");
                    break;
                case "8":
                    boolean delRes = userService.deleteAccount(user.getUsername());
                    System.out.println(delRes ? "Account deleted." : "Delete failed.");
                    if (delRes) return;
                    break;
                case "0":
                    System.out.println("Logged out.");
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void adminMenu(Scanner scanner, admin.AdminService adminService) {
        while (true) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. View All Accounts");
            System.out.println("2. Search Account");
            System.out.println("3. Delete/Freeze Account");
            System.out.println("4. Generate Reports");
            System.out.println("0. Logout");
            System.out.print("Select option: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    java.util.List<model.Account> accounts = adminService.viewAllAccounts();
                    System.out.println("--- All Accounts ---");
                    for (model.Account acc : accounts) {
                        System.out.println(acc);
                    }
                    break;
                case "2":
                    System.out.print("Account ID to search: ");
                    int accId = Integer.parseInt(scanner.nextLine());
                    model.Account acc = adminService.searchAccount(accId);
                    System.out.println(acc != null ? acc : "Account not found.");
                    break;
                case "3":
                    System.out.print("Account ID: ");
                    int delId = Integer.parseInt(scanner.nextLine());
                    System.out.print("Freeze? (y/n): ");
                    boolean freeze = scanner.nextLine().equalsIgnoreCase("y");
                    boolean res = adminService.deleteOrFreezeAccount(delId, freeze);
                    System.out.println(res ? (freeze ? "Account frozen." : "Account deleted.") : "Operation failed.");
                    break;
                case "4":
                    String report = adminService.generateReports();
                    System.out.println(report);
                    break;
                case "0":
                    System.out.println("Admin logged out.");
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }
}
