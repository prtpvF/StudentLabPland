package com.poland.student.StudentLab.Controllers;

import com.poland.student.StudentLab.Exception.PersonNotFoundException;
import com.poland.student.StudentLab.Model.Person;
import com.poland.student.StudentLab.Repo.PersonRepo;
import com.poland.student.StudentLab.Security.PersonDetails;
import com.poland.student.StudentLab.Services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/person")
public class PersonController {
    private final PersonService personService;
    private final PersonRepo personRepo;

    @Autowired
    public PersonController(PersonService personService, PersonRepo personRepo) {
        this.personService = personService;
        this.personRepo = personRepo;
    }

    @GetMapping("/{id}")
    public String getAccountPage(Model model){
       Person person = getAuth();
        try {
            personService.getPerson(person.getId());
        } catch (PersonNotFoundException e) {
            throw new RuntimeException(e);
        }
        model.addAttribute("person", person);
        return "/person/account";
    }
    @DeleteMapping("/delete")
    public String deletePerson(){
        Person person = getAuth();
        personService.deleteAccount(person.getId());
        return "redirect:/auth/login";
    }
    @GetMapping("/{id}/update/page")
    public String updatePage(Model model){
        Person person = getAuth();
        model.addAttribute("person", person);
        return "/person/update";
    }
    @PostMapping("/update/{id}")
    public String updatePerson(@ModelAttribute("person") Person updatedPerson, BindingResult bindingResult){
        Person person = getAuth();
        if (personRepo.existsByUsername(updatedPerson.getUsername())) {
            bindingResult.rejectValue("username", "error.username", "Username is already taken");
            return "/person/update";
        }
        personService.update(person.getId(),updatedPerson);

        return "redirect:/logout";
    }

    private Person getAuth(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        Person person = personDetails.getPerson();
        return person;
    }
}
