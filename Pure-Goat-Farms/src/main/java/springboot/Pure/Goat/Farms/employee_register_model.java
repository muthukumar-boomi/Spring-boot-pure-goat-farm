package springboot.Pure.Goat.Farms;

import jakarta.persistence.*;

@Entity
@Table(name = "employee_register")
public class employee_register_model {

    @Id
    @Column(name = "email")
    private String email;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "pass")
    private String pass;

    @Column(name = "role")
    private String role;

    // No-arg constructor (required by JPA)
    public employee_register_model() {}

    // Parameterized constructor
    public employee_register_model(String email, String mobile, String pass, String role) {
        this.email = email;
        this.mobile = mobile;
        this.pass = pass;
        this.role = role;
    }

    // Getters and setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }

    public String getPass() { return pass; }
    public void setPass(String pass) { this.pass = pass; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}