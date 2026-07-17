package springboot.Pure.Goat.Farms;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "customer_register")
public class customer_register_model {
    
    @Id
    @Column(name = "name")
    private String name;
    
    @Column(name = "mobile")
    private String mobile;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "address")
    private String address;
    
    // Constructors
    public customer_register_model() {}
    
    public customer_register_model(String name, String mobile, String email, String address) {
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.address = address;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}