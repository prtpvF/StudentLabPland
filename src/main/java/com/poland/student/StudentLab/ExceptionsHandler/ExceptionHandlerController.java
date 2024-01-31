package com.poland.student.StudentLab.ExceptionsHandler;

import com.poland.student.StudentLab.Exception.RoomIsAlreadyTakenException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {
    @ExceptionHandler(RoomIsAlreadyTakenException.class)
    public String handleRoomIsAlreadyTakenException(RoomIsAlreadyTakenException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error-page";
    }

    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDeniedException(AccessDeniedException ex) {
        return "redirect:/error/403";
    }
}