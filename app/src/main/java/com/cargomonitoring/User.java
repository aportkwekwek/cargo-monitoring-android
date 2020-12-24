package com.cargomonitoring;

public class User {
    private String id;
    private String username;
    private String email;
    private String token;

    public String getId() {return id; }

    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getToken() { return token; }

    public void setToken(String token) { this.token = token; }
}
