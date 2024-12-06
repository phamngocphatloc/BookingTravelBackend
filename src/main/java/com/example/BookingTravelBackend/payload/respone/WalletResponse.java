package com.example.BookingTravelBackend.payload.respone;

import com.example.BookingTravelBackend.entity.Wallet;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class WalletResponse {
    private int id;
    private BigDecimal balance;
    private String currency;
    private UserInfoResponse user;
    private HotelPartnersResponse partner;
    private String status;
    private Date createAt;
    private Date updateAt;
    private List<TransactionResponse> transactions; // List of TransactionResponse for related transactions

    // Constructor
    public WalletResponse(Wallet wallet) {
        this.id = wallet.getId();
        this.balance = wallet.getBalance();
        this.currency = wallet.getCurrency();
        if (wallet.getUser() != null) {
            this.user = new UserInfoResponse(wallet.getUser());
        }
        if (wallet.getPartner() !=null) {
            this.partner = new HotelPartnersResponse(wallet.getPartner());
        }
        this.status = wallet.getStatus().name();
        this.createAt = wallet.getCreateAt();
        this.updateAt = wallet.getUpdateAt();
        // Convert list of Transaction to list of TransactionResponse
        this.transactions = wallet.getListTransiton().stream()
                .map(TransactionResponse::new)
                .collect(Collectors.toList());
    }

}
