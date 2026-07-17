package springboot.Pure.Goat.Farms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class employee_register_con {

    @Autowired
    private employee_register_repo repo;

    private boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("loggedInEmployee") != null;
    }

    @GetMapping("/employee_login")
    public String showLoginPage() {
        return "employee_login";
    }

    @GetMapping("/employee_register")
    public String showRegisterPage() {
        return "employee_register";
    }

    // ✅ NEW: REST endpoint to get role by email
    @GetMapping("/getRoleByEmail")
    @ResponseBody
    public String getRoleByEmail(@RequestParam("email") String email) {
        Optional<employee_register_model> emp = repo.findById(email);
        if (emp.isPresent()) {
            return emp.get().getRole();
        }
        return "notfound";
    }

    @PostMapping("/employee_login")
    public String processLogin(
            @RequestParam("email") String email,
            @RequestParam("pass") String password,
            @RequestParam(value = "role", required = false) String role, // now optional, we will fetch from DB
            @RequestParam(value = "redirect", required = false) String redirectUrl,
            HttpSession session,
            Model model) {

        try {
            // Fetch the employee from DB using email
            Optional<employee_register_model> empOpt = repo.findById(email);
            if (empOpt.isEmpty()) {
                model.addAttribute("err", "Email not registered");
                return "employee_login";
            }
            employee_register_model employee = empOpt.get();
            String dbRole = employee.getRole();

            // ✅ Only allow if role is "admin"
            if (!"admin".equalsIgnoreCase(dbRole)) {
                model.addAttribute("err", "Access denied. Only Admin can login.");
                return "employee_login";
            }

            String decryptedPass = en_de.decrypt(employee.getPass());
            if (decryptedPass != null && decryptedPass.equals(password)) {
                session.setAttribute("loggedInEmployee", employee);
                if (redirectUrl != null && !redirectUrl.isEmpty()) {
                    return "redirect:" + redirectUrl;
                } else {
                    return "redirect:/view_details";
                }
            } else {
                model.addAttribute("err", "Invalid password");
                return "employee_login";
            }
        } catch (Exception e) {
            model.addAttribute("err", "Login failed: " + e.getMessage());
            return "employee_login";
        }
    }

    // ---------- Other methods unchanged (register, list, edit, delete, search) ----------
    @PostMapping("/employee_register")
    public String processRegister(
            @RequestParam("email") String email,
            @RequestParam("mobile") String mobile,
            @RequestParam("pass") String password,
            @RequestParam("role") String role,
            Model model) {
        try {
            if (repo.existsById(email)) {
                model.addAttribute("err", "Email already registered");
                return "employee_register";
            }
            String encryptedPass = en_de.encrypt(password);
            repo.registerEmployee(email, mobile, encryptedPass, role);
            model.addAttribute("msg", "Registration successful");
            return "redirect:/employee_login";
        } catch (Exception e) {
            model.addAttribute("err", "Registration failed: " + e.getMessage());
            return "employee_register";
        }
    }

    @GetMapping("/employee_list")
    public String listAllEmployees(HttpSession session, HttpServletRequest request, Model model) {
        if (!isLoggedIn(session)) return "redirect:/employee_login?redirect=" + request.getRequestURI();
        List<employee_register_model> employees = repo.getAllEmployees();
        model.addAttribute("employees", employees);
        return "employee_list";
    }

    @GetMapping("/employee/edit/{email}")
    public String showEditForm(@PathVariable("email") String email, HttpSession session, HttpServletRequest request, Model model) {
        if (!isLoggedIn(session)) return "redirect:/employee_login?redirect=" + request.getRequestURI();
        Optional<employee_register_model> optional = repo.findById(email);
        if (optional.isEmpty()) {
            model.addAttribute("err", "Employee not found");
            return "redirect:/employee_list";
        }
        model.addAttribute("employee", optional.get());
        return "employee_edit";
    }

    @PostMapping("/employee/update")
    public String updateEmployee(@RequestParam("email") String email, @RequestParam("mobile") String mobile,
                                 @RequestParam(value = "pass", required = false) String password,
                                 @RequestParam("role") String role, HttpSession session,
                                 HttpServletRequest request, Model model) {
        if (!isLoggedIn(session)) return "redirect:/employee_login?redirect=" + request.getRequestURI();
        try {
            employee_register_model existing = repo.findById(email).orElse(null);
            if (existing == null) {
                model.addAttribute("err", "Employee not found");
                return "redirect:/employee_list";
            }
            String passToStore = existing.getPass();
            if (password != null && !password.isEmpty()) passToStore = en_de.encrypt(password);
            repo.updateEmployee(email, mobile, passToStore, role);
            model.addAttribute("msg", "Employee updated successfully");
        } catch (Exception e) {
            model.addAttribute("err", "Update failed: " + e.getMessage());
        }
        return "redirect:/employee_list";
    }

    @GetMapping("/employee/delete/{email}")
    public String deleteEmployee(@PathVariable("email") String email, HttpSession session, HttpServletRequest request, Model model) {
        if (!isLoggedIn(session)) return "redirect:/employee_login?redirect=" + request.getRequestURI();
        try {
            repo.deleteEmployee(email);
            model.addAttribute("msg", "Employee deleted successfully");
        } catch (Exception e) {
            model.addAttribute("err", "Delete failed: " + e.getMessage());
        }
        return "redirect:/employee_list";
    }

    @GetMapping("/employee/search")
    public String searchEmployees(@RequestParam(value = "keyword", required = false) String keyword,
                                  HttpSession session, HttpServletRequest request, Model model) {
        if (!isLoggedIn(session)) return "redirect:/employee_login?redirect=" + request.getRequestURI();
        List<employee_register_model> allEmployees = repo.getAllEmployees();
        List<employee_register_model> filtered = new ArrayList<>();
        if (keyword != null && !keyword.trim().isEmpty()) {
            String searchTerm = keyword.trim().toLowerCase();
            for (employee_register_model emp : allEmployees) {
                if (emp.getEmail().toLowerCase().contains(searchTerm) ||
                    emp.getRole().toLowerCase().contains(searchTerm)) {
                    filtered.add(emp);
                }
            }
        } else {
            filtered = allEmployees;
        }
        model.addAttribute("employees", filtered);
        model.addAttribute("keyword", keyword);
        return "employee_list";
    }
}