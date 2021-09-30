package com.example.demo.dto;

import com.example.demo.model.User;

public class UserAuthenticationDto {
    private boolean isAuthenticated;
    private User user;

    public UserAuthenticationDto() {
        this.isAuthenticated = false;
    }

    public UserAuthenticationDto(boolean isAuthenticated) {
        this.isAuthenticated = isAuthenticated;
    }

    public UserAuthenticationDto(boolean isAuthenticated, User user) {
        this.isAuthenticated = isAuthenticated;
        this.user = user;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        isAuthenticated = authenticated;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

