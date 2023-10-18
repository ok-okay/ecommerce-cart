package models;

import java.time.LocalDateTime;

public class Pojo_User {
    private long userId;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String sessionId;
    private LocalDateTime expiryTime;

    public Pojo_User(long userId, String email, String firstName, String lastName, String password, String sessionId, LocalDateTime expiryTime) {
        this.userId = userId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.sessionId = sessionId;
        this.expiryTime = expiryTime;
    }

    public long getUserId() {
        return userId;
    }
    public String getEmail() {
        return email;
    }
    public String getFirstName(){return firstName;}
    public String getLastName() {return lastName;}
    public String getPassword() {
        return password;
    }
    public String getSessionId() {return sessionId;}
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
    public LocalDateTime getExpiryTime() {
        return expiryTime;
    }
    public void setExpiryTime(LocalDateTime expiryTime) {
        this.expiryTime = expiryTime;
    }
}
