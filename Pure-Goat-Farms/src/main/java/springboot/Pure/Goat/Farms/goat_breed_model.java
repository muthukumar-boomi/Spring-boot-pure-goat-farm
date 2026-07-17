package springboot.Pure.Goat.Farms;

import jakarta.persistence.*;

@Entity
@Table(name = "goat_breed")
public class goat_breed_model {
    
    @Id
    @Column(name = "name")
    private String name;
    
    @Column(name = "code")
    private String code;
    
    @Column(name = "orgin")
    private String orgin;
    
    @Column(name = "purpose")
    private String purpose;
    
    @Column(name = "weight")
    private String weight;
    
    @Column(name = "height")
    private String height;
    
    @Column(name = "stack")
    private String stack;
    
    // Constructors
    public goat_breed_model() {}
    
    public goat_breed_model(String name, String code, String orgin, String purpose, 
                            String weight, String height, String stack) {
        this.name = name;
        this.code = code;
        this.orgin = orgin;
        this.purpose = purpose;
        this.weight = weight;
        this.height = height;
        this.stack = stack;
    }
    
    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    
    public String getOrgin() { return orgin; }
    public void setOrgin(String orgin) { this.orgin = orgin; }
    
    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }
    
    public String getWeight() { return weight; }
    public void setWeight(String weight) { this.weight = weight; }
    
    public String getHeight() { return height; }
    public void setHeight(String height) { this.height = height; }
    
    public String getStack() { return stack; }
    public void setStack(String stack) { this.stack = stack; }
}