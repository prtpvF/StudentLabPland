package com.poland.student.StudentLab.Controllers;

import com.poland.student.StudentLab.Exception.PersonNotFoundException;
import com.poland.student.StudentLab.Exception.RoomIsAlreadyTakenException;
import com.poland.student.StudentLab.Exception.RoomNotFoundException;
import com.poland.student.StudentLab.Model.Booking;
import com.poland.student.StudentLab.Model.Person;
import com.poland.student.StudentLab.Model.Room;
import com.poland.student.StudentLab.Security.PersonDetails;
import com.poland.student.StudentLab.Services.BookingService;
import com.poland.student.StudentLab.Services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/booking")
public class BookingController {
    private final BookingService bookingService;
    private final RoomService roomService;

    @Autowired
    public BookingController(BookingService bookingService, RoomService roomService) {
        this.bookingService = bookingService;
        this.roomService = roomService;
    }
    @PostMapping("/create/{id}/{id2}")
    public String createBooking(@RequestParam("date")LocalDateTime timeOfBooking,
                                @PathVariable("id") int id,
                                @PathVariable("id2") int id2, Booking booking, Model model) throws RoomNotFoundException, PersonNotFoundException {
        try {
            bookingService.create(id, id2, timeOfBooking);
        }catch (RoomIsAlreadyTakenException e){
            System.out.println(e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "/error-page";
        }
        return "redirect: /room/all";
    }

    @GetMapping("/{id}")
    public String gerBookingPage(@PathVariable("id") int id, Model model, Booking booking) throws RoomNotFoundException {
        Room room = roomService.findRoom(id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        Person person = personDetails.getPerson();
        String userRole = authentication.getAuthorities().iterator().next().getAuthority();
        model.addAttribute("userRole", userRole);
        model.addAttribute("room", room);
        model.addAttribute("person", person);
        model.addAttribute("booking", booking);
        return "/booking/page";
    }
}
