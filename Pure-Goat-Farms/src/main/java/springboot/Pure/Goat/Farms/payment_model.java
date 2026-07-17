package springboot.Pure.Goat.Farms;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "payment") // Removed space before "payment"
public class payment_model {
    
    @Id
    @Column(name = "name")
    private String name;
    
     
    @Column(name = "mobile")
    private String mobile;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "address")
    private String address;
    
    @Column(name = "purpose") // Changed from "use" to "purpose"
    private String purpose;
    
    @Column(name = "gender")
    private String gender;
    
    
    @Column(name = "qty")
    private String qty;
    
    @Column(name = "ctype")
    private String ctype;
    
    @Column(name = "otype")
    private String otype;
    
    @Column(name = "pmethod")
    private String pmethod;
    
    @Column(name = "odate")
    private String odate;
    
    @Column(name = "otime")
    private String otime;
    
    @Column(name = "ddate")
    private String ddate;
    
    @Column(name = "feedback")
    private String feedback;
    
    @Column(name = "total")
    private String total;
    
    // Constructors
    public payment_model() {}
    
    public payment_model(String name, String mobile, 
            String email, String address, String purpose, 
            String gender, String qty, String ctype, String otype, 
            String pmethod, String odate, String otime, String ddate, String feedback, 
            String total) {
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.address = address;
        this.purpose = purpose;
        this.gender = gender;
        this.qty = qty;
        this.ctype = ctype;
        this.otype = otype;
        this.pmethod = pmethod;
        this.odate = odate;
        this.otime = otime;
        this.ddate = ddate;
        this.feedback = feedback;
        this.total = total;
    }

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

	public String getQty() {
		return qty;
	}

	public void setQty(String qty) {
		this.qty = qty;
	}

	public String getCtype() {
		return ctype;
	}

	public void setCtype(String ctype) {
		this.ctype = ctype;
	}

	public String getOtype() {
		return otype;
	}

	public void setOtype(String otype) {
		this.otype = otype;
	}

	public String getPmethod() {
		return pmethod;
	}

	public void setPmethod(String pmethod) {
		this.pmethod = pmethod;
	}

	public String getOdate() {
		return odate;
	}

	public void setOdate(String odate) {
		this.odate = odate;
	}

	public String getOtime() {
		return otime;
	}

	public void setOtime(String otime) {
		this.otime = otime;
	}

	public String getDdate() {
		return ddate;
	}

	public void setDdate(String ddate) {
		this.ddate = ddate;
	}

	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

   
}