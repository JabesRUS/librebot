package com.jabes.librebot.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "notification_settings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "low_threshold", nullable = false)
    private BigDecimal lowThreshold;

    @Column(name = "high_threshold", nullable = false)
    private BigDecimal highThreshold;

    @Column(name = "notifications_enabled", nullable = false)
    private Boolean notificationsEnabled;

    @Column(name = "check_interval_minutes", nullable = false)
    private Integer checkIntervalMinutes;
}
