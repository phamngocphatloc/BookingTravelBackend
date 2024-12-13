package com.example.BookingTravelBackend.controllers;

import com.example.BookingTravelBackend.payload.Request.*;
import com.example.BookingTravelBackend.payload.respone.HotelPartnersResponse;
import com.example.BookingTravelBackend.payload.respone.HttpRespone;
import com.example.BookingTravelBackend.payload.respone.RenuveResponse;
import com.example.BookingTravelBackend.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/partners")
@RequiredArgsConstructor
public class PartnersController {
    private final PartnersHotelService partnersHotelService;
    private final BillService billService;
    private final RestaurantService restaurantService;
    private final HotelService hotelService;
    private final RoomService roomService;
    private final PartnerNotificationService partnerNotificationService;
    private final WalletService walletService;

    @GetMapping("/get_all")
    public ResponseEntity<HttpRespone> ListAllPartnerResponse() {
        List<HotelPartnersResponse> response = partnersHotelService.listPartnersByUserId();
        return ResponseEntity.ok(new HttpRespone(HttpStatus.OK.value(), "success", response));
    }

    @GetMapping("/login_partner")
    public ResponseEntity<HttpRespone> loginHotel(int hotelId) {
        if (partnersHotelService.checkHotelPartnersByHotelId(hotelId) == false) {
            throw new IllegalArgumentException("Bạn Không Phải Quản Lý Của Khách Sạn Này");
        } else {
            return ResponseEntity.ok(new HttpRespone(HttpStatus.OK.value(), "success", "yes"));
        }
    }

    @GetMapping("list_type")
    public ResponseEntity<HttpRespone> ListTypeRoomByPartner(@RequestParam int partnerId) {
        return ResponseEntity.ok(new HttpRespone(HttpStatus.OK.value(), "success", partnersHotelService.selectAllTypeRoomByPartnersId(partnerId)));
    }

    @PostMapping("save_type")
    public ResponseEntity<HttpRespone> SaveTypeRoom(@RequestBody TypeRoomRequest request) {
        return ResponseEntity.ok(new HttpRespone(HttpStatus.OK.value(), "success", partnersHotelService.saveTypeRoom(request)));
    }

    @GetMapping("get_all_bill")
    public ResponseEntity<HttpRespone> getAllBill(@RequestParam int hotelId, @RequestParam(defaultValue = "") String status, @RequestParam(defaultValue = "") String phone) {
        return ResponseEntity.ok(new HttpRespone(HttpStatus.OK.value(), "success", billService.SelectBookingByHotelIdAndStatus(hotelId, status, phone)));
    }

    @PutMapping("update_booking")
    public ResponseEntity<HttpRespone> UpdateBooking(@RequestBody BookingUpdateRequest request) {
        return ResponseEntity.ok(new HttpRespone(HttpStatus.OK.value(), "success", billService.updateBillId(request.getBookingId(), request.getStatus())));
    }

    @GetMapping("check_restaurant")
    public ResponseEntity<HttpRespone> CheckRestaurant(@RequestParam int hotelId) {
        return ResponseEntity.ok(restaurantService.checkRestaurantByHotel(hotelId));
    }

    @PostMapping("create_restaurant")
    public ResponseEntity<HttpRespone> CreateRestaurant(@RequestBody RestaurantRequest request) {
        return ResponseEntity.ok(restaurantService.createRestaurant(request));
    }

    @PostMapping("add_menu")
    public ResponseEntity<HttpRespone> AddMenu(@RequestBody MenuRestaurantRequest request) {
        return ResponseEntity.ok(restaurantService.AddMenu(request));
    }

    @DeleteMapping("delete_room")
    public ResponseEntity<HttpRespone> AddMenu(@RequestParam int roomId) {
        return ResponseEntity.ok(new HttpRespone(HttpStatus.OK.value(), "success", roomService.deleteRoom(roomId)));
    }

    @GetMapping("get_hotel")
    public ResponseEntity<HttpRespone> GetHotel(@RequestParam int hotelId) {
        return ResponseEntity.ok(hotelService.findHotelById(hotelId));
    }

    @GetMapping("get_restaurant")
    public ResponseEntity<HttpRespone> getRestaurant(@RequestParam int restaurantId) {
        return ResponseEntity.ok(restaurantService.getRestaurantById(restaurantId));
    }

    @GetMapping("get_menu")
    public ResponseEntity<HttpRespone> GetMenuRestaurant(@RequestParam int restaurantId, @RequestParam int hotelId) {
        return ResponseEntity.ok(restaurantService.findAllMenuByRestaurantId(restaurantId, hotelId));
    }

    @PostMapping("add_menu_detail")
    public ResponseEntity<HttpRespone> AddMenuDetail(@RequestBody OrderFoodDetailPartnerRequest request) {
        return ResponseEntity.ok(restaurantService.AddmenuDetails(request));
    }

    @GetMapping("get_all_menudetail")
    public ResponseEntity<HttpRespone> GetAllMenuDetail(@RequestParam int menuId, @RequestParam int hotelId) {
        return ResponseEntity.ok(restaurantService.getAllMenuDetailsByMenuId(menuId, hotelId));
    }

    @DeleteMapping("delete_menu_detail")
    public ResponseEntity<HttpRespone> DeleteMenuDetail(@RequestParam int foodId, @RequestParam int hotelId) {
        return ResponseEntity.ok(restaurantService.DeleteMenuDetail(foodId, hotelId));
    }

    @GetMapping("get_all_orderfood")
    public ResponseEntity<HttpRespone> GetAllOrderfood(@RequestParam int hotelId) {
        return ResponseEntity.ok(restaurantService.FindAllRestaurantOrderByHotelId(hotelId));
    }

    @PostMapping("handle_order_food")
    public ResponseEntity<HttpRespone> HandleOrderFood(@RequestParam int hotelId, @RequestParam int orderId,
                                                       @RequestParam String Status) {
        return ResponseEntity.ok(restaurantService.HandleOrderFood(hotelId, orderId, Status));
    }

    @GetMapping("get_all_notification")
    public ResponseEntity<HttpRespone> GetAllNotification(@RequestParam int partnerId) {
        return ResponseEntity.ok(partnerNotificationService.GetAllNotificationByPartner(partnerId));
    }

    @DeleteMapping("delete_notification")
    public ResponseEntity<HttpRespone> DeleteNotification(@RequestParam int notificationId) {
        return ResponseEntity.ok(partnerNotificationService.DeleteNotification(notificationId));
    }

    @GetMapping("wallet")
    public ResponseEntity<HttpRespone> GetWallet(@RequestParam int partnerId) {
        return ResponseEntity.ok(walletService.getWalletByPartner(partnerId));
    }

    @GetMapping("room_reservation_number")
    public ResponseEntity<HttpRespone> RoomReservationNumber(@RequestParam int hotelId) {
        return ResponseEntity.ok(partnersHotelService.RoomReservationNumber(hotelId));
    }

    @GetMapping("get_all_invoice")
    public ResponseEntity<HttpRespone> GetAllInvoice(@RequestParam int hotelId) {
        return ResponseEntity.ok(partnersHotelService.FindAllInvoiceByHotelId(hotelId));
    }
    @GetMapping("get_invoice_id")
    public ResponseEntity<HttpRespone> GetInvoiceById(@RequestParam int invoiceId) {
        return ResponseEntity.ok(billService.findInvoiceById(invoiceId));
    }
    @GetMapping("/revenue/{year}/{hotelId}")
    public ResponseEntity<HttpRespone> getRevenueByMonth(@PathVariable int year, @PathVariable int hotelId) {
        List<RenuveResponse> listResponse = new ArrayList<>();
        List<Object[]> listRenuve = billService.getRevenueByMonth(year,hotelId);
        if (!listRenuve.isEmpty()) {
            billService.getRevenueByMonth(year, hotelId).stream().forEach(item -> {
                listResponse.add(new RenuveResponse(item));
            });
        }
        return ResponseEntity.ok(new HttpRespone(HttpStatus.OK.value(), "success", listResponse));
    }
    @PostMapping("create_partner")
    public ResponseEntity<HttpRespone> CreatePartner(@RequestBody CreatePartnerRequest request) {
        partnersHotelService.RequestCreatePartners(request);
        return ResponseEntity.ok(new HttpRespone(HttpStatus.OK.value(), "success", "thành công"));
    }
}
