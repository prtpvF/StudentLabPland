package com.poland.student.StudentLab.Controllers;

import com.poland.student.StudentLab.Exception.PersonNotFoundException;
import com.poland.student.StudentLab.Exception.RoomIsAlreadyTakenException;
import com.poland.student.StudentLab.Exception.RoomNotFoundException;
import com.poland.student.StudentLab.Model.Booking;
import com.poland.student.StudentLab.Model.Person;
import com.poland.student.StudentLab.Model.Room;
import com.poland.student.StudentLab.Security.PersonDetails;
import com.poland.student.StudentLab.Services.BookingService;
import com.poland.student.StudentLab.Services.PersonService;
import com.poland.student.StudentLab.Services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/booking")
public class BookingController {
    private final BookingService bookingService;
    private final RoomService roomService;
    private final PersonService personService;

    @Autowired
    public BookingController(BookingService bookingService, RoomService roomService, PersonService personService) {
        this.bookingService = bookingService;
        this.roomService = roomService;
        this.personService = personService;
    }
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
    @PostMapping("/create/{id}/{id2}")
    public String createBooking(@RequestParam("date") @DateTimeFormat(pattern="yyyy-MM-dd") Date timeOfBooking,
                                @PathVariable("id") int id,
                                @PathVariable("id2") int id2, Booking booking, Model model) throws RoomNotFoundException, PersonNotFoundException {
        try {
            bookingService.create(id, id2, timeOfBooking);
        }catch (RoomIsAlreadyTakenException e){
            System.out.println(e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "/error-page";
        }
        model.addAttribute("successMessage", "room successfully booked!");
        return "/success-page";
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

    @DeleteMapping("/delete/{id}")
    public String deleteBooking(@PathVariable("id") int id){
        bookingService.deleteBooking(id);
        return "redirect:/room/all";
    }

    @GetMapping("/all/person/bookings")
    public String allPersonBookings( Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        Person person = personDetails.getPerson();
        List<Booking> bookingsList = personService.allPersonBookings(person);
        model.addAttribute("bookingsList", bookingsList);
        model.addAttribute("userRole", person.getRole());
        return "/booking/allPerson";
    }

    @GetMapping("/all/bookings")
    public String allBookings(Model model){
        List<Booking> bookingList = bookingService.allBookings();
        model.addAttribute("bookingList", bookingList);
        return "/booking/all";
    }
    @GetMapping("/{id}/updatePage")
    public String updateBooking(@PathVariable("id") int id, Model model){
        Booking booking = bookingService.getInfo(id);
        model.addAttribute("booking", booking);
        return "/booking/updatePage";
    }

}
