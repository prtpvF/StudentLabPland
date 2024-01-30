package com.poland.student.StudentLab.Services;

import com.poland.student.StudentLab.Exception.PersonNotFoundException;
import com.poland.student.StudentLab.Model.Person;
import com.poland.student.StudentLab.Repo.PersonRepo;
import com.poland.student.StudentLab.Security.PersonDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonService implements UserDetailsService {
    private final PersonRepo personRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<Person> person = personRepo.findByUsername(s);

        if (person.isEmpty())
            throw new UsernameNotFoundException("User not found");

        return new PersonDetails(person.get());
    }

    public void registration(Person person){
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setRole("ROLE_USER");
        personRepo.save(person);
    }

    public Person getPerson(int id) throws PersonNotFoundException {
        Optional<Person> person = personRepo.findById(id);
        if(person.isEmpty()){
            throw new PersonNotFoundException("person is not found");
        }
        return person.get();
    }

    public void deleteAccount(int id){
        personRepo.deleteById(id);
    }

    public void update(int id, Person updatedPerson){
        Optional<Person> personToBeUpdated = personRepo.findById(id);
        updatedPerson.setBookings(personToBeUpdated.get().getBookings());
        updatedPerson.setId(personToBeUpdated.get().getId());
        personRepo.save(updatedPerson);
    }

    public void changeRole(int id, Person person){
        Optional<Person> personToUpdatedRole = personRepo.findById(id);
        person.setId(personToUpdatedRole.get().getId());
        person.setBookings(personToUpdatedRole.get().getBookings());
        person.setRole("ROLE_ADMIN");
        personRepo.save(person);

    }

}
