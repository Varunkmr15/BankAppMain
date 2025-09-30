package ui;

import service.UserService;
import service.BankService;
import admin.AdminService;

import java.util.Scanner;

public class Main {
    // ANSI color codes for console
    private static final String RESET = "\u001B[0m";
    private static final String CYAN = "\u001B[36m";
    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String MAGENTA = "\u001B[35m";

    public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    java.io.Console console = System.console();
        service.UserService userService = new service.UserService();
        service.BankService bankService = new service.BankService();
        admin.AdminService adminService = new admin.AdminService();

        System.out.println();
        System.out.println(CYAN + "========================================" + RESET);
        System.out.println(BLUE + "      Welcome to AMDOCS Bank Console     " + RESET);
        System.out.println(CYAN + "========================================" + RESET);
        System.out.println();

        while (true) {
            System.out.println(YELLOW + "----------- Main Menu -----------" + RESET);
            System.out.println(GREEN + "1. Register" + RESET);
            System.out.println(GREEN + "2. Login" + RESET);
            System.out.println(GREEN + "3. Admin Login" + RESET);
            System.out.println(RED + "0. Exit" + RESET);
            System.out.print(MAGENTA + "Select option: " + RESET);
            String choice = scanner.nextLine();
            System.out.println(CYAN + "----------------------------------------" + RESET);
            switch (choice) {
                case "1": {
                    String username;
                    while (true) {
                        System.out.print("Enter username (alphanumeric, 4-16 chars): ");
                        username = scanner.nextLine();
                        if (username.matches("^[a-zA-Z0-9]{4,16}$")) break;
                        System.out.println(RED + "Invalid username. Try again." + RESET);
                    }
                    String password;
                    while (true) {
                        if (console != null) {
                            System.out.print("Enter password (min 8 chars): ");
                            char[] pwdArr = console.readPassword();
                            password = new String(pwdArr);
                        } else {
                            System.out.print("Enter password (min 8 chars, input visible): ");
                            password = scanner.nextLine();
                        }
                        if (password.length() >= 8) break;
                        System.out.println(RED + "Password must be at least 8 characters." + RESET);
                    }
                    String phoneNumber;
                    while (true) {
                        System.out.print("Phone number (10 digits): ");
                        phoneNumber = scanner.nextLine();
                        if (phoneNumber.matches("^[0-9]{10}$")) break;
                        System.out.println(RED + "Invalid phone number. Try again." + RESET);
                    }
                    System.out.print("Full name: ");
                    String fullName = scanner.nextLine();
                    System.out.print("Email: ");
                    String email = scanner.nextLine();
                    model.User newUser = new model.User(0, username, "", fullName, email, phoneNumber, false, false);
                    boolean reg = userService.registerUser(newUser, password);
                    System.out.println(reg ? GREEN + "Registration successful!" + RESET : RED + "Registration failed." + RESET);
                    break;
                }
                case "2":
                    System.out.print("Username: ");
                    String user = scanner.nextLine();
                    String pass;
                    if (console != null) {
                        System.out.print("Password: ");
                        char[] pwdArr = console.readPassword();
                        pass = new String(pwdArr);
                    } else {
                        System.out.print("Password (input will be visible): ");
                        pass = scanner.nextLine();
                    }
                    model.User loggedIn = userService.login(user, pass);
                    if (loggedIn != null) {
                        System.out.println(GREEN + "\nWelcome, " + loggedIn.getFullName() + "!" + RESET);
                        userMenu(scanner, loggedIn, bankService, userService);
                    } else {
                        System.out.println(RED + "Login failed." + RESET);
                    }
                    break;
                case "3":
                    System.out.print("Admin username: ");
                    String adminUser = scanner.nextLine();
                    String adminPass;
                    if (console != null) {
                        System.out.print("Password: ");
                        char[] pwdArr = console.readPassword();
                        adminPass = new String(pwdArr);
                    } else {
                        System.out.print("Password (input will be visible): ");
                        adminPass = scanner.nextLine();
                    }
                    model.User admin = userService.login(adminUser, adminPass);
                    if (admin != null && admin.isAdmin()) {
                        System.out.println(GREEN + "\nWelcome Admin!" + RESET);
                        adminMenu(scanner, adminService);
                    } else {
                        System.out.println(RED + "Admin login failed." + RESET);
                    }
                    break;
                case "0":
                    System.out.println(BLUE + "Thank you for using MyBank. Goodbye!" + RESET);
                    scanner.close();
                    return;
                default:
                    System.out.println(RED + "Invalid option. Try again." + RESET);
            }
            System.out.println();
        }
    }

    private static void userMenu(Scanner scanner, model.User user, service.BankService bankService, service.UserService userService) {
        while (true) {
            System.out.println(YELLOW + "\n----------- User Menu -----------" + RESET);
            System.out.println(GREEN + "1. View Profile" + RESET);
            System.out.println(GREEN + "2. Deposit" + RESET);
            System.out.println(GREEN + "3. Withdraw" + RESET);
            System.out.println(GREEN + "4. Transfer" + RESET);
            System.out.println(GREEN + "5. Check Balance" + RESET);
            System.out.println(GREEN + "6. Transaction History" + RESET);
            System.out.println(GREEN + "7. Update Profile" + RESET);
            System.out.println(RED + "8. Delete Account" + RESET);
            System.out.println(RED + "0. Logout" + RESET);
            System.out.print(MAGENTA + "Select option: " + RESET);
            String choice = scanner.nextLine();
            System.out.println(CYAN + "----------------------------------------" + RESET);
            switch (choice) {
                case "1":
                    model.User profile = userService.viewProfile(user.getUsername());
                    System.out.println(BLUE + profile + RESET);
                    break;
                case "2": {
                    model.Account account = new dao.AccountDAO().getAccountByUserId(user.getId());
                    if (account == null) {
                        System.out.println(RED + "No account found for user." + RESET);
                        break;
                    }
                    System.out.print("Amount to deposit: ");
                    double dep = Double.parseDouble(scanner.nextLine());
                    boolean depRes = bankService.deposit(account.getAccountId(), dep);
                    System.out.println(depRes ? GREEN + "Deposit successful." + RESET : RED + "Deposit failed." + RESET);
                    break;
                }
                case "3": {
                    model.Account account = new dao.AccountDAO().getAccountByUserId(user.getId());
                    if (account == null) {
                        System.out.println(RED + "No account found for user." + RESET);
                        break;
                    }
                    System.out.print("Amount to withdraw: ");
                    double wd = Double.parseDouble(scanner.nextLine());
                    boolean wdRes = bankService.withdraw(account.getAccountId(), wd);
                    System.out.println(wdRes ? GREEN + "Withdraw successful." + RESET : RED + "Withdraw failed." + RESET);
                    break;
                }
                case "4": {
                    model.Account account = new dao.AccountDAO().getAccountByUserId(user.getId());
                    if (account == null) {
                        System.out.println(RED + "No account found for user." + RESET);
                        break;
                    }
                    System.out.print("Recipient account ID: ");
                    int toAcc = Integer.parseInt(scanner.nextLine());
                    System.out.print("Amount to transfer: ");
                    double tf = Double.parseDouble(scanner.nextLine());
                    boolean tfRes = bankService.transfer(account.getAccountId(), toAcc, tf);
                    System.out.println(tfRes ? GREEN + "Transfer successful." + RESET : RED + "Transfer failed." + RESET);
                    break;
                }
                case "5": {
                    model.Account account = new dao.AccountDAO().getAccountByUserId(user.getId());
                    System.out.println(BLUE + account + RESET);
                    if (account == null) {
                        System.out.println(RED + "No account found for user." + RESET);
                        break;
                    }
                    double bal = bankService.getBalance(account.getAccountId());
                    System.out.println(GREEN + "Current Balance: " + bal + RESET);
                    break;
                }
                case "6": {
                    model.Account account = new dao.AccountDAO().getAccountByUserId(user.getId());
                    if (account == null) {
                        System.out.println(RED + "No account found for user." + RESET);
                        break;
                    }
                    java.util.List<model.Transaction> txs = bankService.getTransactionHistory(account.getAccountId());
                    System.out.println(YELLOW + "--- Transaction History ---" + RESET);
                    for (model.Transaction tx : txs) {
                        System.out.println(BLUE + tx + RESET);
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
                    System.out.println(upRes ? GREEN + "Profile updated." + RESET : RED + "Update failed." + RESET);
                    break;
                case "8":
                    boolean delRes = userService.deleteAccount(user.getUsername());
                    System.out.println(delRes ? GREEN + "Account deleted." + RESET : RED + "Delete failed." + RESET);
                    if (delRes) return;
                    break;
                case "0":
                    System.out.println(BLUE + "Logged out." + RESET);
                    return;
                default:
                    System.out.println(RED + "Invalid option. Try again." + RESET);
            }
        }
    }

    private static void adminMenu(Scanner scanner, admin.AdminService adminService) {
        while (true) {
            System.out.println(YELLOW + "\n----------- Admin Menu -----------" + RESET);
            System.out.println(GREEN + "1. View All Accounts" + RESET);
            System.out.println(GREEN + "2. Search Account" + RESET);
            System.out.println(GREEN + "3. Delete/Freeze Account" + RESET);
            System.out.println(GREEN + "4. Generate Reports" + RESET);
            System.out.println(RED + "0. Logout" + RESET);
            System.out.print(MAGENTA + "Select option: " + RESET);
            String choice = scanner.nextLine();
            System.out.println(CYAN + "----------------------------------------" + RESET);
            switch (choice) {
                case "1":
                    java.util.List<model.Account> accounts = adminService.viewAllAccounts();
                    System.out.println(YELLOW + "--- All Accounts ---" + RESET);
                    for (model.Account acc : accounts) {
                        System.out.println(BLUE + acc + RESET);
                    }
                    break;
                case "2":
                    System.out.print("Account ID to search: ");
                    int accId = Integer.parseInt(scanner.nextLine());
                    model.Account acc = adminService.searchAccount(accId);
                    System.out.println(acc != null ? BLUE + acc + RESET : RED + "Account not found." + RESET);
                    break;
                case "3":
                    System.out.print("Account ID: ");
                    int delId = Integer.parseInt(scanner.nextLine());
                    System.out.print("Freeze? (y/n): ");
                    boolean freeze = scanner.nextLine().equalsIgnoreCase("y");
                    boolean res = adminService.deleteOrFreezeAccount(delId, freeze);
                    System.out.println(res ? (freeze ? YELLOW + "Account frozen." + RESET : GREEN + "Account deleted." + RESET) : RED + "Operation failed." + RESET);
                    break;
                case "4":
                    String report = adminService.generateReports();
                    System.out.println(BLUE + report + RESET);
                    break;
                case "0":
                    System.out.println(BLUE + "Admin logged out." + RESET);
                    return;
                default:
                    System.out.println(RED + "Invalid option. Try again." + RESET);
            }
        }
    }
}
