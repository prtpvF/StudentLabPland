package com.poland.student.StudentLab.Controllers;

import com.poland.student.StudentLab.Model.Room;
import com.poland.student.StudentLab.Services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
    public String createRoomPage(@ModelAttribute("room") Room room){
        return "/room/create";
    }
    @PostMapping("/create")
    public String createProduct(@RequestParam("file1") MultipartFile file1, @RequestParam("file2") MultipartFile file2,
                                @RequestParam("file3") MultipartFile file3, Room room) throws IOException {
        roomService.saveRoom(room, file1, file2, file3);
        return "redirect:/";
    }
}
