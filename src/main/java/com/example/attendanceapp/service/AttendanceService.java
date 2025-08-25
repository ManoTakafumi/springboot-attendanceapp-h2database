package com.example.attendanceapp.service;

import com.example.attendanceapp.entity.Attendance;
import com.example.attendanceapp.entity.User;
import com.example.attendanceapp.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AttendanceService {
    @Autowired
    private AttendanceRepository attendanceRepository;

    public void saveAttendance(Attendance attendance) {
        attendanceRepository.save(attendance);
        deleteOldAttendance(attendance.getUser());
    }

    private void deleteOldAttendance(User user) {
        //ユーザーの勤怠記録を日付の降順(新しい順)で取得
        List<Attendance> attendances = attendanceRepository.findByUserOrderByDateDesc(user);

        //最新５件より古いものがあれば削除
        if (attendances.size() > 5) {
            List<Attendance> toDelete = attendances.subList(5, attendances.size());
            attendanceRepository.deleteAll(toDelete);
        }
    }

    public void punchIn(User user) {
        LocalDate today = LocalDate.now();

        //今日の勤怠が存在しない場合だけ出勤を記録
        // if (attendanceRepository.findByUserAndDate(user, today).isEmpty()) {}
            Attendance attendance = new Attendance();
            attendance.setUser(user);
            attendance.setDate(today);
            attendance.setStartTime(LocalDateTime.now());

            attendanceRepository.save(attendance);
            deleteOldAttendance(user);  //punchIn後にも最新５件保持ロジックを適用
    }

    public void punchOut(User user) {
        LocalDate today = LocalDate.now();

        //今日の最新の勤怠を取得して通勤時間を記録
        List<Attendance> todayAttendances = attendanceRepository.findByUserAndDateOrderByStartTimeDesc(user, today);

        todayAttendances
            .stream()
            .filter(a -> a.getEndTime() == null)
            .findFirst()
            .ifPresent(att -> {
                att.setEndTime(LocalDateTime.now());
                attendanceRepository.save(att);
            });

        if(!todayAttendances.isEmpty()) {
            Attendance latest = todayAttendances.get(0); //一番新しい打刻
            latest.setEndTime(LocalDateTime.now());
            attendanceRepository.save(latest);
        }
        //まだ退勤していない最新の出勤を取得
        // List<Attendance> attendances = attendanceRepository.findByUserOrderByDateDesc(user);
        // attendances.stream()
        //     .filter(a -> a.getEndTime() == null)
        //     .findFirst()
        //     .ifPresent(att -> {
        //         att.setEndTime(LocalDateTime.now());
        //         attendanceRepository.save(att);
        //     });
        // LocalDate today = LocalDate.now();

        // //今日の勤怠が存在すれば退勤時間を記録
        // Attendance attendance = attendanceRepository.findByUserAndDate(user, today).orElse(null);
        // if (attendance != null && attendance.getEndTime() == null) {
        //     attendance.setEndTime(LocalDateTime.now());
        //     attendanceRepository.save(attendance);
        // }
    }

    public List<Attendance> getAttendanceList(User user) {
        List<Attendance> attendances = attendanceRepository.findByUserOrderByDateDesc(user);

        //古いのを削除して最新５件にする
        if (attendances.size() > 5) {
            List<Attendance> toDelete = attendances.subList(5, attendances.size());
            attendanceRepository.deleteAll(toDelete);
            attendances = attendances.subList(0, 5);
        }
        return attendances;
        // return all.size() > 5 ? all.subList(0, 5) : all;
    }
}