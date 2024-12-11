package com.example.BookingTravelBackend.service;

import com.example.BookingTravelBackend.payload.Request.WithdrawRequest;
import com.example.BookingTravelBackend.payload.respone.HttpRespone;

public interface WalletService {
    public HttpRespone getWalletByPartner (int partnerId);
    public HttpRespone getTransactioWallet (String transactionType);
    public void processWithdrawal(WithdrawRequest request);
    public HttpRespone WalletTransion(int id);
}
