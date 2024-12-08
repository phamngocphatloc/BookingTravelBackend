package com.example.BookingTravelBackend.payload.Request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class WithdrawRequest {
    private Integer partnerId;         // ID của ví thực hiện giao dịch (có thể null)
    private BigDecimal amount;        // Số tiền muốn rút
    private String bankAccountNumber; // Số tài khoản ngân hàng
    private String bankName;          // Tên ngân hàng
    private String accountHolderName; // Tên chủ tài khoản ngân hàng
}
