package com.example.BookingTravelBackend.controllers;

import com.example.BookingTravelBackend.Configuration.VnpayConfig;
import com.example.BookingTravelBackend.entity.Bill;
import com.example.BookingTravelBackend.payload.Request.BillRequest;
import com.example.BookingTravelBackend.payload.Request.BookingRequest;
import com.example.BookingTravelBackend.payload.respone.BillResponse;
import com.example.BookingTravelBackend.payload.respone.HttpRespone;
import com.example.BookingTravelBackend.service.BillService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController
@RequestMapping("/booking")
@RequiredArgsConstructor
public class BookingController {
    private final BillService billService;
    @PostMapping ("/book")
    public ResponseEntity<HttpRespone> Booking (@RequestBody BillRequest request){
        return ResponseEntity.ok(new HttpRespone(HttpStatus.OK.value(), "success",
                billService.Booking(request)));
    }

    @GetMapping("/paying/{id}")
    public ResponseEntity<HttpRespone> paying(@PathVariable("id") int id,
                                                 @RequestParam("vnp_Amount") String amount,
                                                 @RequestParam("vnp_BankCode") String bankcode,
                                                 @RequestParam("vnp_CardType") String cardtype,
                                                 @RequestParam("vnp_OrderInfo") String orderInfo,
                                                 @RequestParam("vnp_PayDate") String date,
                                                 @RequestParam("vnp_ResponseCode") String code,
                                                 @RequestParam("vnp_TmnCode") String tmncode,
                                                 @RequestParam("vnp_TransactionNo") String transactionno,
                                                 @RequestParam("vnp_TransactionStatus") String status,
                                                 @RequestParam("vnp_TxnRef") String txnref,
                                                 @RequestParam("vnp_SecureHash") String hash,
                                                 @RequestParam(name = "vnp_BankTranNo", defaultValue = "") String tranno,
                                                 Model model,
                                                 HttpSession session) throws UnsupportedEncodingException, MessagingException {
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Amount", amount);
        vnp_Params.put("vnp_BankCode", bankcode);
        vnp_Params.put("vnp_CardType", cardtype);
        vnp_Params.put("vnp_OrderInfo", orderInfo);
        vnp_Params.put("vnp_PayDate", date);
        vnp_Params.put("vnp_ResponseCode", code);
        vnp_Params.put("vnp_TmnCode", tmncode);
        vnp_Params.put("vnp_TransactionNo", transactionno);
        vnp_Params.put("vnp_TransactionStatus", status);
        vnp_Params.put("vnp_TxnRef", txnref);
        if (!tranno.equalsIgnoreCase("")) {
            vnp_Params.put("vnp_BankTranNo", tranno);
        }
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
        System.out.println(vnp_SecureHash);
        System.out.println(hash);
        Bill order = billService.findById(id);
        if (vnp_SecureHash.equalsIgnoreCase(hash) && order.getId() == Integer.parseInt(txnref)) {
            if (status.equalsIgnoreCase("00")) {

                billService.updateStatusBill(order,"active");


                return ResponseEntity.status(HttpStatus.OK).body(new HttpRespone(HttpStatus.OK.value(),"success","Thanh Toán Thành Công"));
            } else {
                billService.updateStatusBill(order,"cancel");
                return ResponseEntity.status(HttpStatus.OK).body(new HttpRespone(HttpStatus.OK.value(),"success","Hủy Thành Công"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(new HttpRespone(HttpStatus.OK.value(),"success","https://sandbox.vnpayment.vn/paymentv2/Payment/Error.html?code=70"));
        }
    }

}
