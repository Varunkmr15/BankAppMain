# Java Bank Management System

A full-featured console-based banking application using Java, JDBC, and PostgreSQL.

## Features
- User Management: Registration, login (hashed passwords), profile view/update/delete
- Banking Operations: Deposit, withdraw, transfer, check balance, transaction history
- Admin Panel: View/search accounts, delete/freeze accounts, generate reports
- Secure authentication and OOP design
- Modular code: UI, DAO, models, services, authentication, admin, database

## Directory Structure
- `src/ui` - Console UI classes
- `src/model` - Data models (User, Account, Transaction, etc.)
- `src/dao` - Data Access Objects (JDBC logic)
- `src/service` - Business logic
- `src/auth` - Authentication and security
- `src/admin` - Admin features
- `src/database` - Database connection/configuration

## Setup
1. Install Java (JDK 8+)
2. Install PostgreSQL and create a database
3. Configure database connection in `src/database/DatabaseConfig.java`
4. Build and run the project

## How to Run
```
javac -d bin src/**/*.java
java -cp bin ui.Main
```

## Notes
- All passwords are securely hashed
- Modular and maintainable code
- Extendable for new features
