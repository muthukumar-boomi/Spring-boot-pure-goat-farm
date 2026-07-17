package springboot.Pure.Goat.Farms;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class customer_register_con {
    
    @Autowired
    private customer_register_repo customerRepo;
    
    @GetMapping("/")
    public String welcomepage() {
        return "welcomepage"; 
    }
    
    @GetMapping("/view_details")
    public String viewDetails() {
        return "view_details"; 
    }
    
    @GetMapping("/mainpage")
    public String mainpage() {
        return "mainpage"; 
    }
    
    @GetMapping("/customer_list")
    public String customerListRedirect() {
        return "redirect:/customer/list";
    }

    @GetMapping("/customer/list")
    public String customerList(Model model) {
        List<customer_register_model> customers = customerRepo.findAll();
        model.addAttribute("customers", customers);
        return "customer_list";
    }
    
    @GetMapping("/customer_register")
    public String customerRegisterGet() {
        return "customer_register";
    }
    
    @GetMapping("/customer/register")
    public String customer_register() { 
        return "customer_register"; 
    }
    
    @PostMapping("/customer_register")
    public String insert_customer_register(
        @RequestParam("name") String name, 
        @RequestParam("mobile") String mobile, 
        @RequestParam("email") String email, 
        @RequestParam("address") String address, 
        Model m
    ) {
        try {
            Optional<customer_register_model> userExists = customerRepo.findById(name);
            if(userExists.isPresent()) {
                m.addAttribute("err", "Username already exists");
                return "customer_register";
            }
            
            customer_register_model customer = new customer_register_model(name, mobile, email, address);
            customerRepo.save(customer);
            
            m.addAttribute("msg", "Registration successful");
            return "redirect:/customer_register";
        } catch(Exception e) {
            m.addAttribute("err", "Registration failed: " + e.getMessage());
            return "customer_register";
        }
    }
    
    @GetMapping("/customer/edit/{name}")
    public String customerEdit(@PathVariable("name") String name, Model model) {
        Optional<customer_register_model> customer = customerRepo.findById(name);
        if(customer.isPresent()) {
            model.addAttribute("customer", customer.get());
            return "customer_edit";
        } else {
            model.addAttribute("err", "Customer not found");
            return "redirect:/customer/list";
        }
    }
    
    @PostMapping("/customer/update")
    public String customerUpdate(
        @RequestParam("name") String name,
        @RequestParam("mobile") String mobile,
        @RequestParam("email") String email,
        @RequestParam("address") String address,
        RedirectAttributes ra
    ) {
        try {
            Optional<customer_register_model> existingCustomer = customerRepo.findById(name);
            if(!existingCustomer.isPresent()) {
                ra.addFlashAttribute("err", "Customer not found");
                return "redirect:/customer/list";
            }
            
            customer_register_model customer = existingCustomer.get();
            customer.setMobile(mobile);
            customer.setEmail(email);
            customer.setAddress(address);
            
            customerRepo.save(customer);
            ra.addFlashAttribute("msg", "Customer updated successfully");
        } catch(Exception e) {
            ra.addFlashAttribute("err", "Update failed: " + e.getMessage());
        }
        return "redirect:/customer/list";
    }
    
    @PostMapping("/customer/delete")
    public String customerDelete(@RequestParam("name") String name, RedirectAttributes ra) {
        try {
            customerRepo.deleteById(name);
            ra.addFlashAttribute("msg", "Customer deleted successfully");
        } catch(Exception e) {
            ra.addFlashAttribute("err", "Delete failed: " + e.getMessage());
        }
        return "redirect:/customer/list";
    }
    
    // ✅ NEW REST endpoint for auto-fill by mobile number
    @GetMapping("/getCustomerByMobile")
    @ResponseBody
    public customer_register_model getCustomerByMobile(@RequestParam("mobile") String mobile) {
        System.out.println("Searching for mobile: " + mobile);  // Debug log
        customer_register_model customer = customerRepo.findByMobile(mobile);
        System.out.println("Found: " + (customer != null ? customer.getName() : "null"));
        return customer;
    }
}