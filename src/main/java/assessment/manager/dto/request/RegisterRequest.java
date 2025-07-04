package assessment.manager.dto.request;

import assessment.manager.enums.UserRole;

public class RegisterRequest {

    private UserRole role;
    private String email;
    private String password;

    public RegisterRequest() {}

    public RegisterRequest(String email, String password, UserRole role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
