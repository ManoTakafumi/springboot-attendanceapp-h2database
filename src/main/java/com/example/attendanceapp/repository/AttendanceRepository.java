package com.example.attendanceapp.repository;

import com.example.attendanceapp.entity.Attendance;
import com.example.attendanceapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    //指定ユーザーと日付で１件だけ取得(主に出勤時チェック用)
    Optional<Attendance> findByUserAndDate(User user, LocalDate date);

    //指定ユーザーの勤怠履歴を日付降順で取得
    List<Attendance> findByUserOrderByDateDesc(User user);

    //同じ日付の勤怠を startTime 降順で取得(通勤時の最新出勤検索用)
    List<Attendance> findByUserAndDateOrderByStartTimeDesc(User user, LocalDate date);
}