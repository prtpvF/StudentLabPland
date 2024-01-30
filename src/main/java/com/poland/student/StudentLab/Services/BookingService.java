package com.poland.student.StudentLab.Services;

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
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingService{
    private final PersonRepo personRepo;
    private final RoomRepo roomRepo;
    private final BookingRepo bookingRepo;

    public void create( int roomId, LocalDateTime timeOfBooking) throws PersonNotFoundException, RoomNotFoundException, RoomIsAlreadyTakenException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        Person person = personDetails.getPerson();

        Optional<Room> room = roomRepo.findById(roomId);
        if (room.isEmpty())
            throw new RoomNotFoundException("room is not found");

        if(bookingRepo.existsByRoomAndDate(roomId, timeOfBooking)){
            throw new RoomIsAlreadyTakenException("you cant booking this room on this date cause it's already taken");
        } else {
            Booking booking = new Booking();
            booking.setRoom(room.get());
            booking.setPerson(person);
            booking.setDate(timeOfBooking);
            bookingRepo.save(booking);
        }
    }

    public Booking getInfo(int id){
        Optional<Booking> booking = bookingRepo.findById(id);
        return booking.get();
    }

    public void deleteBooking(int id){
        bookingRepo.deleteById(id);
    }

    public void updateBooking(int id, Booking updatedBooking) throws RoomIsAlreadyTakenException {
        Optional<Booking> bookingToBeUpdatedOpt = bookingRepo.findById(id);
        if (bookingToBeUpdatedOpt.isEmpty()) {
            throw new IllegalArgumentException("Booking with id " + id + " not found");
        }
        Booking bookingToBeUpdated = bookingToBeUpdatedOpt.get();
        updatedBooking.setId(bookingToBeUpdated.getId());
        updatedBooking.setPerson(bookingToBeUpdated.getPerson());
        updatedBooking.setRoom(bookingToBeUpdated.getRoom());

        Booking existingBooking = bookingRepo.findByRoomAndDate(bookingToBeUpdated.getRoom().getId(), updatedBooking.getDate());
        if (existingBooking != null && existingBooking.getId() != bookingToBeUpdated.getId()) {
            throw new RoomIsAlreadyTakenException("you cant booking this room on this date cause it's already taken");
        }
        bookingRepo.save(updatedBooking);
    }



}