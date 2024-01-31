package com.poland.student.StudentLab.Controllers;

import com.poland.student.StudentLab.Model.Person;
import com.poland.student.StudentLab.Security.PersonDetails;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    @GetMapping("/home")
    public String getHomePage(){
        return "home";
    }
    @RequestMapping("/header")
    public String header(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userRole = authentication.getAuthorities().iterator().next().getAuthority();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        Person person = personDetails.getPerson();
        model.addAttribute("userRole", userRole);
        model.addAttribute("person", person);
        return "header";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            request.getSession().invalidate();
        }
        return "redirect:/auth/login";
    }
}
