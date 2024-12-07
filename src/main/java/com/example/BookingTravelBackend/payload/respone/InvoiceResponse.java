package com.example.BookingTravelBackend.payload.respone;

import com.example.BookingTravelBackend.entity.Invoice;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class InvoiceResponse {
    private int id;
    private UserInfoResponse userCreate;  // ID of the user who created the invoice
    private Date createAt;
    private BillResponse booking;  // Booking ID related to the invoice
    private Invoice.InvoiceStatus status;
    private Date paymentDate;
    private String description;
    private BigDecimal tax;
    private BigDecimal discount;
    private Date updateAt;
    private String paymentMethod;
    private List<OrderFoodResponse> orderFoodResponseList = new ArrayList<>();
    private BigDecimal totalPrice;

    public InvoiceResponse (Invoice invoice){
        this.id = invoice.getId();
        this.userCreate = new UserInfoResponse(invoice.getUserCreate());
        this.createAt = invoice.getCreataAt();
        this.booking = new BillResponse(invoice.getBooking());
        this.status = invoice.getStatus();
        this.paymentDate = invoice.getPaymentDate();
        this.description = invoice.getDescription();
        this.tax = invoice.getTax();
        this.discount = invoice.getDiscount();
        if (invoice.getUpdateAt() != null) {
            this.updateAt = invoice.getUpdateAt();
        }
        this.paymentMethod = invoice.getPaymentMethod();
        if (!invoice.getBooking().getListOrderFood().isEmpty()) {
            invoice.getBooking().getListOrderFood().stream().forEach(item -> {
                orderFoodResponseList.add(new OrderFoodResponse(item));
            });
        }
        this.totalPrice = invoice.getAmount();
    }
}
