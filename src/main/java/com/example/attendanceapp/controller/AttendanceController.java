package com.example.attendanceapp.controller;

import com.example.attendanceapp.entity.User;
import com.example.attendanceapp.entity.Attendance;
import com.example.attendanceapp.service.AttendanceService;
import com.example.attendanceapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/attendance")
public class AttendanceController {
    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private UserService userService;

    //打刻ページの表示
    @GetMapping("/punch")
    public String showPunchPage() {
        return "attendance/punch";
    }

    //出勤打刻処理
    @PostMapping("/punch/in")
    public String punchIn(@AuthenticationPrincipal User user) {
        attendanceService.punchIn(user);
        return "redirect:/attendance/list";
    }

    //退勤打刻処理
    @PostMapping("/punch/out")
    public String punchOut(@AuthenticationPrincipal User user) {
        attendanceService.punchOut(user);
        return "redirect:/attendance/list";
    }

    //勤怠一覧ページの表示
    @GetMapping("/list")
    public String attendanceList(@AuthenticationPrincipal User user, Model model) {
        List<Attendance> attendances = attendanceService.getAttendanceList(user);
        model.addAttribute("attendances", attendances);
        return "attendance/list";
    }

    //勤怠手動保存(フォームなどからの登録用)
    @PostMapping("/save")
    public String saveAttendance(@ModelAttribute Attendance attendance, Principal principal) {
        //Principalから現在ログイン中のユーザー名を取得
        String username = principal.getName();
        User user = userService.findByUsername(username);
        attendance.setUser(user);
        attendanceService.saveAttendance(attendance);
        return "redirect:/attendance/list";
    }
}