package com.example.attendanceapp.controller;

import com.example.attendanceapp.entity.User;
import com.example.attendanceapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class TestController {

    @GetMapping("/test")
    public String testPage() {
        return "test";
    }
}