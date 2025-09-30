package service;

import dao.UserDAO;
import model.User;

public class UserService {
    private UserDAO userDAO = new UserDAO();

    /**
     * Register a new user with hashed password and salt
     */
    public boolean registerUser(User user, String password) {
        String salt = auth.PasswordUtils.generateSalt();
        String hash = auth.PasswordUtils.hashPassword(password, salt);
        user.setPasswordHash(hash + ":" + salt);
        return userDAO.createUser(user);
    }

    /**
     * Login user by verifying password
     */
    public User login(String username, String password) {
        User user = userDAO.getUserByUsername(username);
        if (user == null) return null;
        String[] parts = user.getPasswordHash().split(":");
        if (parts.length != 2) return null;
        String hash = parts[0];
        String salt = parts[1];
        if (auth.PasswordUtils.verifyPassword(password, salt, hash)) {
            return user;
        }
        return null;
    }

    /**
     * View user profile
     */
    public User viewProfile(String username) {
        return userDAO.getUserByUsername(username);
    }

    /**
     * Update user profile
     */
    public boolean updateProfile(User user) {
        return userDAO.updateUser(user);
    }

    /**
     * Delete user account
     */
    public boolean deleteAccount(String username) {
        return userDAO.deleteUser(username);
    }
    
}
