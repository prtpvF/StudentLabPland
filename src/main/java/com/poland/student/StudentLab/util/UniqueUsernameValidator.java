package com.poland.student.StudentLab.util;

import com.poland.student.StudentLab.Repo.PersonRepo;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {


    private PersonRepo personRepository;

    public UniqueUsernameValidator() {
    }

    @Override
    public void initialize(UniqueUsername constraintAnnotation) {
        personRepository = ApplicationContextProvider.getContext().getBean(PersonRepo.class);
    }



    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        return username != null && !personRepository.existsByUsername(username);
    }
}