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
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;

    @ManyToOne
    @JoinColumn(name = "userCreate")
    private User userCreate;

    @Column(name = "CreateAt")
    private Date creataAt;

    @ManyToOne
    @JoinColumn(name = "BookingId")
    private Booking booking;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvoiceStatus status;  // Trạng thái hóa đơn

    @Column(nullable = false)
    private BigDecimal amount;  // Tổng số tiền

    @Column(name = "paymentDate")
    private Date paymentDate;  // Ngày thanh toán

    @Column(length = 500)
    private String description;  // Mô tả hóa đơn

    @Column(nullable = false)
    private BigDecimal tax = BigDecimal.ZERO;  // Thuế

    @Column(nullable = false)
    private BigDecimal discount = BigDecimal.ZERO;  // Chiết khấu

    @Column(name = "updateAt")
    private Date updateAt;  // Ngày cập nhật

    @Column(nullable = false)
    private String paymentMethod;  // Phương thức thanh toán

    // Enum trạng thái hóa đơn
    public enum InvoiceStatus {
        PENDING,    // Chưa thanh toán
        PAID,       // Đã thanh toán
        CANCELLED   // Hủy bỏ
    }
}
