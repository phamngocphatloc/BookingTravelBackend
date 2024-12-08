package com.example.BookingTravelBackend.controllers;

import com.example.BookingTravelBackend.payload.Request.WithdrawRequest;
import com.example.BookingTravelBackend.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping ("/wallet")
public class WalletController {
    private final WalletService walletService;

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestBody WithdrawRequest request) {
            walletService.processWithdrawal(request);
            return ResponseEntity.ok("Yêu cầu rút tiền đã được xử lý thành công.");
    }
}
