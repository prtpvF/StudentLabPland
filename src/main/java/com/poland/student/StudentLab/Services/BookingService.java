package com.poland.student.StudentLab.Services;

import com.poland.student.StudentLab.Exception.InvalidDate;
import com.poland.student.StudentLab.Exception.PersonNotFoundException;
import com.poland.student.StudentLab.Exception.RoomIsAlreadyTakenException;
import com.poland.student.StudentLab.Exception.RoomNotFoundException;
import com.poland.student.StudentLab.Model.Booking;
import com.poland.student.StudentLab.Model.Person;
import com.poland.student.StudentLab.Model.Room;
import com.poland.student.StudentLab.Repo.BookingRepo;
import com.poland.student.StudentLab.Repo.PersonRepo;
import com.poland.student.StudentLab.Repo.RoomRepo;
import com.poland.student.StudentLab.Security.PersonDetails;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingService{
    private final PersonRepo personRepo;
    private final RoomRepo roomRepo;
    private final BookingRepo bookingRepo;
    private final HttpSession httpSession;



    public void create( int roomId, int personId, Date timeOfBooking) throws RoomNotFoundException, RoomIsAlreadyTakenException, InvalidDate {

       Optional<Person> person = personRepo.findById(personId);
       Date date = new Date();

        Optional<Room> room = roomRepo.findById(roomId);
        if (room.isEmpty())
            throw new RoomNotFoundException("room is not found");
        if(timeOfBooking.toInstant().isBefore(date.toInstant()))
            throw new InvalidDate("date is invalid");

        if(bookingRepo.existsByRoomAndDate(roomId, timeOfBooking)){
            throw new RoomIsAlreadyTakenException("you cant booking this room on this date cause it's already taken");
        } else {
            Booking booking = new Booking();
            booking.setRoom(room.get());
            booking.setPerson(person.get());
            booking.setDate(timeOfBooking);
            bookingRepo.save(booking);
        }
    }

    public Booking getInfo(int id){
        Optional<Booking> booking = bookingRepo.findById(id);
        return booking.get();
    }


    public void deleteBooking(int id){
        Optional<Booking> booking = bookingRepo.findById(id);
        httpSession.removeAttribute("cache");
        bookingRepo.deleteById(id);
    }

    public void updateBooking(int id, Booking updatedBooking) throws RoomIsAlreadyTakenException, InvalidDate {
        Optional<Booking> bookingToBeUpdatedOpt = bookingRepo.findById(id);
        if (bookingToBeUpdatedOpt.isEmpty()) {
            throw new IllegalArgumentException("Booking with id " + id + " not found");
        }
        Date date = new Date();
        Booking bookingToBeUpdated = bookingToBeUpdatedOpt.get();
        updatedBooking.setId(bookingToBeUpdated.getId());
        updatedBooking.setPerson(bookingToBeUpdated.getPerson());
        updatedBooking.setRoom(bookingToBeUpdated.getRoom());

        Booking existingBooking = bookingRepo.findByRoomAndDate(bookingToBeUpdated.getRoom().getId(), updatedBooking.getDate());
        if (existingBooking != null && existingBooking.getId() != bookingToBeUpdated.getId()) {
            throw new RoomIsAlreadyTakenException("you cant booking this room on this date cause it's already taken");
        }
        if(updatedBooking.getDate().toInstant().isBefore(date.toInstant()))
            throw new InvalidDate("date is invalid");

        bookingRepo.save(updatedBooking);
    }
    public List<Booking> allBookings(){
       return  bookingRepo.findAll();
    }



}
