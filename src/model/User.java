package model;

import java.io.Serializable;

public abstract class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userId;
    private String name;
    private String password;
    private String role;

    public User(String userId, String name, String password, String role) {
        this.userId = userId;
        this.name = name;
        this.password = password;
        this.role = role;
    }

    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getPassword() { return password; }
    public String getRole() { return role; }

    public boolean authenticate(String inputPassword) {
        return this.password.equals(inputPassword);
    }

    public abstract String getDashboardTitle();

    @Override
    public String toString() {
        return role + ":" + userId + ":" + name + ":" + password;
    }
}
