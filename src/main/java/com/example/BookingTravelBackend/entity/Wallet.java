package com.example.BookingTravelBackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // Số dư ví sử dụng BigDecimal thay vì double
    @Column(nullable = false)
    private BigDecimal balance;

    // Tiền tệ của ví (ví dụ: USD, EUR)
    @Column(nullable = false)
    private String currency;

    // Quan hệ OneToOne với User, có thể null
    @OneToOne
    @JoinColumn(name = "userId", nullable = true) // nullable = true cho phép user có thể null
    private User user;

    // Quan hệ OneToOne với HotelPartners, có thể null
    @OneToOne
    @JoinColumn(name = "partnerId", nullable = true) // nullable = true cho phép partner có thể null
    private HotelPartners partner;

    // Trạng thái của ví (Hoạt động, Tạm ngừng, Đã đóng)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WalletStatus status;

    // Thời gian tạo ví
    @Column(name = "CreateAt", nullable = false, updatable = false)
    private Date createAt;

    // Thời gian cập nhật ví lần cuối (có thể null nếu chưa cập nhật)
    @Column(name = "UpdateAt")
    private Date updateAt;

    @OneToMany (mappedBy = "wallet", fetch = FetchType.EAGER)
    List<Transaction> listTransiton;

    // Enum trạng thái ví
    public enum WalletStatus {
        ACTIVE,
        SUSPENDED,
        CLOSED
    }

    // Hàm callback để tự động cập nhật thời gian tạo ví trước khi persist
    @PrePersist
    public void prePersist() {
        if (createAt == null) {
            createAt = new Date();
        }
    }

    // Hàm callback để tự động cập nhật thời gian sửa ví trước khi persist
    @PreUpdate
    public void preUpdate() {
        updateAt = new Date();
    }
}
