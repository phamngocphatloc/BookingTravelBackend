package com.example.BookingTravelBackend.payload.respone;

import com.example.BookingTravelBackend.entity.Transaction;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class TransactionResponse {
    private int id;
    private String transactionId;
    private String transactionType;
    private String status;
    private BigDecimal amount;
    private BigDecimal fee;
    private Date transactionDate;
    private String description;
    private UserInfoResponse initiator;
    private UserInfoResponse userForPartner;

    // Constructor
    public TransactionResponse(Transaction transaction) {
        this.id = transaction.getId();
        this.transactionId = transaction.getTransactionId();
        this.transactionType = transaction.getTransactionType().name();
        this.status = transaction.getStatus().name();
        this.amount = transaction.getAmount();
        this.fee = transaction.getFee();
        this.transactionDate = transaction.getTransactionDate();
        this.description = transaction.getDescription();
        this.initiator = new UserInfoResponse(transaction.getInitiator());
        this.userForPartner = new UserInfoResponse(transaction.getUserForPartner());
    }
}
