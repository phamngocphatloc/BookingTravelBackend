package com.example.BookingTravelBackend.service;

import com.example.BookingTravelBackend.entity.Wallet;
import com.example.BookingTravelBackend.payload.respone.HttpRespone;

public interface WalletService {
    public HttpRespone getWalletByPartner (int partnerId);
    public HttpRespone getTransactioWallet (String transactionType);
}
