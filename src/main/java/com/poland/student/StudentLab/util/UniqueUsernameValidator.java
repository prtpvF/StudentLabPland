package com.poland.student.StudentLab.util;

import com.poland.student.StudentLab.Repo.PersonRepo;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

    private final PersonRepo personRepository;

    public UniqueUsernameValidator(PersonRepo personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public void initialize(UniqueUsername constraintAnnotation) {
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        return username != null && !personRepository.existsByUsername(username);
    }
}