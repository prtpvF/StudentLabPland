package com.poland.student.StudentLab.Controllers;

import com.poland.student.StudentLab.Model.Person;
import com.poland.student.StudentLab.Repo.PersonRepo;
import com.poland.student.StudentLab.Services.PersonService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final PersonService personService;
    private final PersonRepo personRepo;

    @Autowired
    public AuthController(PersonService personService, PersonRepo personRepo) {
        this.personService = personService;
        this.personRepo = personRepo;
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("person") Person person) {
        return "/auth/registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/auth/registration";
        }
        if (personRepo.existsByUsername(person.getUsername())) {
            bindingResult.rejectValue("username", "error.username", "Username is already taken");
            return "/auth/registration";
        }
        personService.registration(person);
        return "redirect:/auth/login";
    }

    @RequestMapping(value = "/login", method = {RequestMethod.GET, RequestMethod.POST})
    public String loginPage(Model model, Person person, CsrfToken csrfToken) {
        model.addAttribute("_csrf", csrfToken);
        model.addAttribute("userRole", person.getRole());
        return "/auth/login";
    }


}
