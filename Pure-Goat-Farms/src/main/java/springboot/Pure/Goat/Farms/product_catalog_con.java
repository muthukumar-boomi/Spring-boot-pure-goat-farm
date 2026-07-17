package springboot.Pure.Goat.Farms;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class product_catalog_con {

    @Autowired
    private product_catalog_repo repo;

    // List all products
    @GetMapping("/product/list")
    public String listAllProducts(Model model) {
        List<product_catalog_model> products = repo.get_all();
        model.addAttribute("products", products);
        return "product_catalog_list";
    }

    // ✅ FIXED: Use JPQL query instead of stored procedure
    @GetMapping("/product/byCode/{code}")
    @ResponseBody
    public product_catalog_model getProductByCode(@PathVariable("code") String code) {
        // Direct JPQL query – no stored procedure needed
        return repo.findByCode(code);
    }

    // Alternative list URL - redirect to main list
    @GetMapping("/product_catalog_list")
    public String listAllProductsAlt() {
        return "redirect:/product/list";
    }

    // Redirect old add form URL to new one
    @GetMapping("/product_catalog")
    public String redirectOldAddForm() {
        return "redirect:/product/new";
    }

    // Search products by name
    @GetMapping("/product/search")
    public String searchProducts(@RequestParam(value = "name", required = false) String name, Model model) {
        List<product_catalog_model> allProducts = repo.get_all();
        List<product_catalog_model> filtered;

        if (name != null && !name.trim().isEmpty()) {
            String searchTerm = name.trim().toLowerCase();
            filtered = new ArrayList<>();
            for (product_catalog_model p : allProducts) {
                if (p.getName() != null && p.getName().toLowerCase().contains(searchTerm)) {
                    filtered.add(p);
                }
            }
        } else {
            filtered = allProducts;
        }
        model.addAttribute("products", filtered);
        model.addAttribute("searchTerm", name);
        return "product_catalog_list";
    }

    // Show add form
    @GetMapping("/product/new")
    public String showAddForm() {
        return "product_catalog";
    }

    // Insert new product
    @PostMapping("/product/insert")
    public String insertProduct(
            @RequestParam("name") String name,
            @RequestParam("code") String code,
            @RequestParam("purpose") String purpose,
            @RequestParam("gender") String gender,
            @RequestParam("weight") String weight,
            @RequestParam("height") String height,
            @RequestParam("storage") String storage,
            @RequestParam("price") String price,
            @RequestParam("total") String total,
            @RequestParam("grade") String grade,
            @RequestParam("stack") String stack,
            RedirectAttributes ra) {
        try {
            if (repo.existsById(name)) {
                ra.addFlashAttribute("err", "Product already exists");
                return "redirect:/product/new";
            }
            repo.insert(name, code, purpose, gender, weight, height, storage,
                       price, total, grade, stack);
            ra.addFlashAttribute("msg", "Product added successfully");
            return "redirect:/product/new";
        } catch (Exception e) {
            ra.addFlashAttribute("err", "Failed to add product: " + e.getMessage());
            return "redirect:/product/new";
        }
    }

    // Show edit form
    @GetMapping("/product/edit/{name}")
    public String showEditForm(@PathVariable("name") String name, Model model, RedirectAttributes ra) {
        product_catalog_model product = repo.get_by_name(name);
        if (product == null) {
            ra.addFlashAttribute("err", "Product not found");
            return "redirect:/product/list";
        }
        model.addAttribute("product", product);
        return "product_catalog_edit";
    }

    // Update product
    @PostMapping("/product/update")
    public String updateProduct(
            @RequestParam("name") String name,
            @RequestParam("code") String code,
            @RequestParam("purpose") String purpose,
            @RequestParam("gender") String gender,
            @RequestParam("weight") String weight,
            @RequestParam("height") String height,
            @RequestParam("storage") String storage,
            @RequestParam("price") String price,
            @RequestParam("total") String total,
            @RequestParam("grade") String grade,
            @RequestParam("stack") String stack,
            RedirectAttributes ra) {
        try {
            repo.update(name, code, purpose, gender, weight, height, storage,
                       price, total, grade, stack);
            ra.addFlashAttribute("msg", "Product updated successfully");
        } catch (Exception e) {
            ra.addFlashAttribute("err", "Update failed: " + e.getMessage());
        }
        return "redirect:/product/list";
    }

    // GET delete (backward compatibility)
    @GetMapping("/product/delete/{name}")
    public String deleteProductGet(@PathVariable("name") String name, RedirectAttributes ra) {
        try {
            repo.delete(name);
            ra.addFlashAttribute("msg", "Product deleted successfully");
        } catch (Exception e) {
            ra.addFlashAttribute("err", "Delete failed: " + e.getMessage());
        }
        return "redirect:/product/list";
    }

    // POST delete
    @PostMapping("/product/delete")
    public String deleteProductPost(@RequestParam("name") String name, RedirectAttributes ra) {
        try {
            repo.delete(name);
            ra.addFlashAttribute("msg", "Product deleted successfully");
        } catch (Exception e) {
            ra.addFlashAttribute("err", "Delete failed: " + e.getMessage());
        }
        return "redirect:/product/list";
    }
}