package bloodbank.model;

/**
 * Simple Admin credential holder.
 * A single fixed admin account is used (loaded from admin.txt).
 */
public class Admin {
    // Fields to store admin username and password
    private String username;
    private String password;

    // Constructor: initialize admin with given username and password
    public Admin(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Login method: checks if provided credentials match stored ones
    public boolean login(String u, String p) {
        return username.equals(u) && password.equals(p);
    }

    // Getter: allows access to the admin's username
    public String getUsername() {
        return username;
    }
}
