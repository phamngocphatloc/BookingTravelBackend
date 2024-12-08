package com.example.BookingTravelBackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Getter
@Setter
@Table (name = "TransactionWallet")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // Loại giao dịch: Chuyển tiền, Rút tiền, Nạp tiền
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType transactionType;

    // Trạng thái giao dịch: PENDING, COMPLETED, FAILED, CANCELLED
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;

    // Mã giao dịch (có thể sử dụng UUID hoặc một chuỗi duy nhất)
    @Column(nullable = false, unique = true)
    private String transactionId;

    // Số tiền giao dịch
    @Column(nullable = false)
    private BigDecimal amount;

    // Phí giao dịch (nếu có)
    @Column(nullable = true)
    private BigDecimal fee;

    // Thời gian giao dịch
    @Column(nullable = false)
    private Date transactionDate;

    // Mô tả giao dịch (nếu có)
    @Column(nullable = true)
    private String description;

    // Liên kết với ví (Wallet) liên quan đến giao dịch này
    @ManyToOne
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;

    // Người thực hiện giao dịch (người yêu cầu giao dịch)
    @ManyToOne
    @JoinColumn(name = "initiator_id", nullable = false)
    private User initiator;

    // Thông tin tài khoản ngân hàng cho giao dịch
    @Column(nullable = true)
    private String bankAccountNumber;

    @Column(nullable = true)
    private String bankName;

    @Column (name = "AccountHolderName")
    private String accountHolderName;

    // Người tạo giao dịch nếu ví là của partner
    @ManyToOne
    @JoinColumn(name = "user_id_for_partner", nullable = true)
    private User userForPartner;

    // Enum loại giao dịch
    public enum TransactionType {
        TRANSFER,   // Chuyển tiền
        WITHDRAWAL, // Rút tiền
        DEPOSIT
    }


    // Enum trạng thái giao dịch
    public enum TransactionStatus {
        PENDING,    // Giao dịch đang chờ xử lý
        COMPLETED,  // Giao dịch đã hoàn tất
        FAILED,     // Giao dịch thất bại
        CANCELLED   // Giao dịch bị hủy
    }

    // Hàm callback để tự động thiết lập thời gian giao dịch khi persist
    @PrePersist
    public void prePersist() {
        if (transactionDate == null) {
            transactionDate = new Date();
        }
        if (transactionId == null) {
            transactionId = generateTransactionId();
        }

        // Kiểm tra nếu ví thuộc partner, người tạo giao dịch là một user có quyền
        if (wallet.getPartner() != null) {
            if (userForPartner == null) {
                throw new IllegalArgumentException("Giao dịch của partner phải được tạo bởi user");
            }
        }
    }

    // Hàm tạo mã giao dịch duy nhất
    private String generateTransactionId() {
        // Có thể sử dụng UUID hoặc bất kỳ cách thức nào để tạo mã duy nhất
        return "TX-" + System.currentTimeMillis();
    }

    // Hàm callback để tự động cập nhật thời gian sửa giao dịch khi persist
    @PreUpdate
    public void preUpdate() {
        transactionDate = new Date();
    }
}
