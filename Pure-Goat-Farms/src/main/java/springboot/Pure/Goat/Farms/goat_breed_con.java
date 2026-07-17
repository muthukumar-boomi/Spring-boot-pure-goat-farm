package springboot.Pure.Goat.Farms;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class goat_breed_con {
    
    @Autowired
    private goat_breed_repo repo;
    
    @GetMapping("/goat/breeds")
    public String listAllBreeds(Model model) {
        List<goat_breed_model> breeds = repo.findAll();
        model.addAttribute("breeds", breeds);
        return "goat_breeds_list";
    }
    
    @GetMapping("/goat_breeds_list")
    public String listAllBreedsAlt(Model model) {
        List<goat_breed_model> breeds = repo.findAll();
        model.addAttribute("breeds", breeds);
        return "goat_breeds_list";
    }
    
    @GetMapping("/goat_breed_form")
    public String goatBreedForm() {
        return "goat_breed_form";
    }
    
    @GetMapping("/goat/breed/new")
    public String showAddForm() {
        return "goat_breed_form";
    }
    
    @GetMapping("/goat_breed")
    public String showAddFormOld() {
        return "goat_breed_form";
    }
    
    @PostMapping("/goat/breed/insert")
    public String insertBreed(
        @RequestParam("name") String name, 
        @RequestParam("code") String code, 
        @RequestParam("orgin") String orgin, 
        @RequestParam("purpose") String purpose,
        @RequestParam("weight") String weight, 
        @RequestParam("height") String height, 
        @RequestParam("stack") String stack, 
        RedirectAttributes ra) {
        
        try {
            if(repo.existsById(name)) {
                ra.addFlashAttribute("err", "Goat breed already exists");
                return "redirect:/goat_breed_form";
            }
            
            goat_breed_model breed = new goat_breed_model(name, code, orgin, purpose, weight, height, stack);
            repo.save(breed);
            ra.addFlashAttribute("msg", "Goat breed added successfully");
            return "redirect:/goat_breed_form";
        } catch(Exception e) {
            ra.addFlashAttribute("err", "Failed to add goat breed: " + e.getMessage());
            return "redirect:/goat_breed_form.html";
        }
    }
    
    @GetMapping("/goat/breed/edit/{name}")
    public String showEditForm(@PathVariable("name") String name, Model model) {
        Optional<goat_breed_model> optional = repo.findById(name);
        if (optional.isEmpty()) {
            model.addAttribute("err", "Breed not found");
            return "redirect:/goat_breed_form";
        }
        model.addAttribute("breed", optional.get());
        return "goat_breed_form_edit";
    }
    
    @PostMapping("/goat/breed/update")
    public String updateBreed(
        @RequestParam("name") String name, 
        @RequestParam("code") String code, 
        @RequestParam("orgin") String orgin, 
        @RequestParam("purpose") String purpose,
        @RequestParam("weight") String weight, 
        @RequestParam("height") String height, 
        @RequestParam("stack") String stack, 
        RedirectAttributes ra) {
        
        try {
            goat_breed_model breed = new goat_breed_model(name, code, orgin, purpose, weight, height, stack);
            repo.save(breed);
            ra.addFlashAttribute("msg", "Breed updated successfully");
        } catch(Exception e) {
            ra.addFlashAttribute("err", "Update failed: " + e.getMessage());
        }
        return "redirect:/goat/breeds";
    }
    
    // GET delete - for backward compatibility (if needed)
    @GetMapping("/goat/breed/delete/{name}")
    public String deleteBreedGet(@PathVariable("name") String name, RedirectAttributes ra) {
        try {
            repo.deleteById(name);
            ra.addFlashAttribute("msg", "Breed deleted successfully");
        } catch(Exception e) {
            ra.addFlashAttribute("err", "Delete failed: " + e.getMessage());
        }
        return "redirect:/goat/breeds";
    }
    
    // POST delete - for form submissions (recommended)
    @PostMapping("/goat/breed/delete")
    public String deleteBreedPost(@RequestParam("name") String name, RedirectAttributes ra) {
        try {
            repo.deleteById(name);
            ra.addFlashAttribute("msg", "Breed deleted successfully");
        } catch(Exception e) {
            ra.addFlashAttribute("err", "Delete failed: " + e.getMessage());
        }
        return "redirect:/goat/breeds";
    }
    
    @GetMapping("/goat/breed/search")
    public String searchBreeds(
            @RequestParam(value = "name", required = false) String name,
            Model model) {
        
        List<goat_breed_model> allBreeds = repo.findAll();
        List<goat_breed_model> filteredBreeds;
        
        if (name != null && !name.trim().isEmpty()) {
            String searchTerm = name.trim().toLowerCase();
            filteredBreeds = new ArrayList<>();
            for (goat_breed_model breed : allBreeds) {
                if (breed.getName() != null && breed.getName().toLowerCase().contains(searchTerm)) {
                    filteredBreeds.add(breed);
                }
            }
        } else {
            filteredBreeds = allBreeds;
            // Removed erroneous line: re.addFlashAttribute(...)
        }
        
        model.addAttribute("breeds", filteredBreeds);
        model.addAttribute("searchTerm", name);
        return "goat_breeds_list";
    }
}