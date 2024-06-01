package com.example.BookingTravelBackend.controllers;

import com.example.BookingTravelBackend.Configuration.VnpayConfig;
import com.example.BookingTravelBackend.Configuration.WebConfig;
import com.example.BookingTravelBackend.entity.Bill;
import com.example.BookingTravelBackend.payload.respone.paymentVnpayRespone;
import com.example.BookingTravelBackend.service.BillService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequiredArgsConstructor
public class PaymentVnpayController {
    private final BillService billService;
    @GetMapping("/createPaymentVnpay")
    public ResponseEntity<?> createPayment(HttpServletRequest request, @RequestParam("bId") int bid) throws UnsupportedEncodingException {
        Bill order = billService.findById(bid);
        String vnp_Returnurl = WebConfig.url+"/payment?id="+bid;
        if (order != null && order.getBooking().getStatus().equalsIgnoreCase("pending")) {
            long amount = (long)order.getPrice()* 100;
            System.out.println(amount);
            String vnp_TxnRef = String.valueOf(order.getId());

            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", "2.1.0");
            vnp_Params.put("vnp_Command", "pay");
            vnp_Params.put("vnp_TmnCode", VnpayConfig.vnp_TmnCode);
            vnp_Params.put("vnp_Amount", String.valueOf(amount));

            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String vnp_CreateDate = formatter.format(cld.getTime());
            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
            vnp_Params.put("vnp_CurrCode", "VND");
            vnp_Params.put("vnp_IpAddr", VnpayConfig.getIpAddress(request));
            vnp_Params.put("vnp_Locale", "vn");
            vnp_Params.put("vnp_OrderInfo", "Thanh Toan Don Hang:" + order.getId());
            vnp_Params.put("vnp_OrderType", "200000");
            vnp_Params.put("vnp_ReturnUrl", vnp_Returnurl);
            vnp_Params.put("vnp_TxnRef", vnp_TxnRef);

            cld.add(Calendar.MINUTE, 15);
            String vnp_ExpireDate = formatter.format(cld.getTime());
            vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

            List fieldNames = new ArrayList(vnp_Params.keySet());
            Collections.sort(fieldNames);
            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();
            Iterator itr = fieldNames.iterator();
            while (itr.hasNext()) {
                String fieldName = (String) itr.next();
                String fieldValue = (String) vnp_Params.get(fieldName);
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    //Build hash data
                    hashData.append(fieldName);
                    hashData.append('=');
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    //Build query
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                    query.append('=');
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    if (itr.hasNext()) {
                        query.append('&');
                        hashData.append('&');
                    }
                }
            }
            String queryUrl = query.toString();
            String vnp_SecureHash = VnpayConfig.hmacSHA512(VnpayConfig.vnp_HashSecret, hashData.toString());
            queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
            String paymentUrl = VnpayConfig.vnp_PayUrl + "?" + queryUrl;
            paymentVnpayRespone dto = new paymentVnpayRespone("ok", "sucessfully", paymentUrl);
            return ResponseEntity.status(HttpStatus.OK).body(dto);
        } else if (order.getBooking().getStatus().equalsIgnoreCase("active")) {
            paymentVnpayRespone dto = new paymentVnpayRespone("ok", "sucessfully", "đã thanh toán");
            return ResponseEntity.status(HttpStatus.OK).body(dto);
        }else{
            paymentVnpayRespone dto = new paymentVnpayRespone("ok", "error", "error");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);
        }
    }
}