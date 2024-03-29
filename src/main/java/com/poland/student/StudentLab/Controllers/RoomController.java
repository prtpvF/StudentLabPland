package com.poland.student.StudentLab.Controllers;

import com.poland.student.StudentLab.Exception.RoomNotFoundException;
import com.poland.student.StudentLab.Model.Booking;
import com.poland.student.StudentLab.Model.Person;
import com.poland.student.StudentLab.Model.Room;
import com.poland.student.StudentLab.Security.PersonDetails;
import com.poland.student.StudentLab.Services.RoomService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/room")
public class RoomController {
    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }
    @GetMapping("/create/page")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String createRoomPage(@ModelAttribute("room") Room room){
        return "/room/create";
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String createRoom(@RequestParam("file1") MultipartFile file1, @RequestParam("file2") MultipartFile file2,
                             @RequestParam("file3") MultipartFile file3, @Valid Room room, BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()){
            return "redirect:/room/create/page";
        }
        roomService.saveRoom(room, file1, file2, file3);
        return "redirect:/room/all";
    }

    @GetMapping("/all")
    @CacheEvict
    public String allRoom(@ModelAttribute("room") Room room, Model model){
        model.addAttribute("rooms", roomService.findAll());
        model.addAttribute("imgs", room.getImages());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userRole = authentication.getAuthorities().iterator().next().getAuthority();
        model.addAttribute("userRole", userRole);
        return "room/all";
    }

    @GetMapping("/page/{id}")
    public String getInfo(@PathVariable("id") int id, Model model) throws RoomNotFoundException {
        Room room = roomService.findRoom(id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        Person guest = personDetails.getPerson();
        String userRole = authentication.getAuthorities().iterator().next().getAuthority();
        model.addAttribute("userRole", userRole);
        model.addAttribute("room", room);
        model.addAttribute("person", guest);
        return "room/info";
    }

    //test passed
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteRoom(@PathVariable("id") int id){
        roomService.deleteRoom(id);
        return "redirect:/room/all";
    }


}
