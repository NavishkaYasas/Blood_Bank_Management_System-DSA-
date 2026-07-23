package bloodbank.model;

/** Simple Admin credential holder. A single fixed admin account is used (loaded from admin.txt). */
public class Admin {
    private String username;
    private String password;

    public Admin(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public boolean login(String u, String p) {
        return username.equals(u) && password.equals(p);
    }

    public String getUsername() { return username; }
}