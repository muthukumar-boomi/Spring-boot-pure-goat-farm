package springboot.Pure.Goat.Farms;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "product_catalog")
public class product_catalog_model {
    
    @Id
    @Column(name = "name")
    private String name;
    
    @Column(name = "code")
    private String code;
    
    @Column(name = "purpose")
    private String purpose;
    
    @Column(name = "gender")
    private String gender;
    
    @Column(name = "weight")
    private String weight;
    
    @Column(name = "height")
    private String height;
    
    @Column(name = "storage")
    private String storage;
    
    @Column(name = "price")
    private String price;
    
    @Column(name = "total")
    private String total;
    
    @Column(name = "grade")
    private String grade;
    
    @Column(name = "stack")
    private String stack;
    
    // Constructors
    public product_catalog_model() {}
    
    public product_catalog_model(String name, String code, String purpose, 
            String gender, String weight,String height, String storage, String price, 
            String total, String grade, String stack) {
        this.name = name;
        this.code = code;
        this.purpose = purpose;
        this.gender = gender;
        this.weight = weight;
        this.height = height;
        this.storage = storage;
        this.price = price;
        this.total = total;
        this.grade = grade;
        this.stack = stack;
    }

    // 🔥 FIX: Add this helper method for the template
    @Transient
    public int getTotalAsInt() {
        if (total == null || total.trim().isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(total.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getStack() {
        return stack;
    }

    public void setStack(String stack) {
        this.stack = stack;
    }
}