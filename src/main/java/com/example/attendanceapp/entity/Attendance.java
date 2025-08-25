package com.example.attendanceapp.entity;

import jakarta.persistence.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "attendances")
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private boolean late;

    private boolean earlyLeave;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public boolean isLate() { return late; }
    public void setLate(boolean late) { this.late = late; }

    public boolean isEarlyLeave() { return earlyLeave; }
    public void setEarlyLeave(boolean earlyLeave) { this.earlyLeave = earlyLeave; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getWorkingDurationFormatted() {
        if (startTime != null && endTime != null) {
            Duration duration = Duration.between(startTime, endTime);
            long hours = duration.toHours();
            long minutes = duration.toMinutesPart();
            return String.format("%02d:%02d", hours, minutes);
        }
        return "-";
    }

    public String getLateStatus() {
        if (startTime != null) {
            LocalTime threshold = LocalTime.of(9, 0);
            if (startTime.toLocalTime().isAfter(threshold)) {
                return "遅刻";
            }
        }
        return "";
    }

    public String getLeaveEarlyStatus() {
        if (endTime != null) {
            LocalTime threshold = LocalTime.of(17, 0);
            if (endTime.toLocalTime().isBefore(threshold)) {
                return "早退";
            }
        }
        return "";
    }
}