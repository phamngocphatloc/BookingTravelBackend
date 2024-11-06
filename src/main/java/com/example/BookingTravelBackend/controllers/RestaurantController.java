package com.example.BookingTravelBackend.controllers;

import com.example.BookingTravelBackend.Configuration.VnpayConfig;
import com.example.BookingTravelBackend.Configuration.WebConfig;
import com.example.BookingTravelBackend.payload.Request.OrderFoodRequest;
import com.example.BookingTravelBackend.payload.respone.HttpRespone;
import com.example.BookingTravelBackend.payload.respone.OrderFoodResponse;
import com.example.BookingTravelBackend.service.RestaurantOrderService;
import com.example.BookingTravelBackend.service.RestaurantService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import com.example.BookingTravelBackend.entity.RestaurantOrder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping ("/restaurant")
public class RestaurantController {
    private final RestaurantService restaurantService;
    private final RestaurantOrderService restaurantOrderService;
    @GetMapping ("/getmenu")
    public HttpRespone getMenu (@RequestParam int orderId,@RequestParam  int pageNum,@RequestParam int pageSize){
        HttpRespone response = new HttpRespone(HttpStatus.OK.value(),"Success",restaurantService.LoadProductByOrderId(orderId,pageNum,pageSize));
        return response;
    }

    @GetMapping ("/detail")
    public HttpRespone getMenu (@RequestParam int orderId,@RequestParam  int foodId){
        HttpRespone response = new HttpRespone(HttpStatus.OK.value(),"Success",restaurantService.findById(orderId,foodId));
        return response;
    }
    @PostMapping("/order")
    @Transactional
    public HttpRespone order (@RequestBody OrderFoodRequest request){
        OrderFoodResponse response = restaurantOrderService.order(request);
        return new HttpRespone(HttpStatus.OK.value(), "Đặt Hàng Thành Công", response);
    }

    @GetMapping ("/order-detail")
    public HttpRespone OrderDetail (@RequestParam ("oid") int id, @RequestParam("bid") int bId){
        return new HttpRespone(HttpStatus.OK.value(), "Success", restaurantOrderService.OrderDetail(id,bId));
    }

    @GetMapping("/paying/{id}")
    public RedirectView paying(@PathVariable("id") int id,
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
                               @RequestParam(name = "vnp_BankTranNo", defaultValue = "") String tranno) throws UnsupportedEncodingException {
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
        List<String> fieldNames = new ArrayList(vnp_Params.keySet());
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
        RestaurantOrder order = restaurantOrderService.findById(id);
        if (order.getStatus().equalsIgnoreCase("active") || order.getStatus().equalsIgnoreCase("cancel")){
            return new RedirectView(WebConfig.url+"/restaurant/#!/order-detail/"+id);
        }
        if (vnp_SecureHash.equalsIgnoreCase(hash) && order.getId() == Integer.parseInt(txnref)) {
            if (status.equalsIgnoreCase("00")) {

                restaurantOrderService.updateStatusBill(order,"active");


                return new RedirectView(WebConfig.url+"/#!/booking/"+id);
            } else {
                restaurantOrderService.updateStatusBill(order,"cancel");
                return new RedirectView(WebConfig.url+"/#!/booking/"+id);
            }
        } else {
            return new RedirectView("https://sandbox.vnpayment.vn/paymentv2/Payment/Error.html?code=70");
        }
    }
}
