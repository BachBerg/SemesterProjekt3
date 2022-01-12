package model;

public class User {
    private String username;
    private String password;
    private String auth;

    public User(String password, String username, String auth){
        this.password = password;
        this.auth = auth;
        this.username = username;
    }

    public User(LoginData loginData) {
        setUsername(loginData.getUsername());
    }

    public User() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }
}
