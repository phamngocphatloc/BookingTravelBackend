package com.example.BookingTravelBackend.service.impls;

import com.example.BookingTravelBackend.Repository.*;
import com.example.BookingTravelBackend.entity.*;
import com.example.BookingTravelBackend.payload.Request.BookingRequest;
import com.example.BookingTravelBackend.payload.respone.*;
import com.example.BookingTravelBackend.service.BillService;
import com.example.BookingTravelBackend.service.PartnerNotificationService;
import com.example.BookingTravelBackend.util.HandleSort;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BillServiceImpl implements BillService {
    private final BillRepository billRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final PartnersRepository hotelPartnersRepository;
    private final HotelRepository hotelRepository;
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final PartnerNotificationService partnerNotificationService;
    private final InvoiceRepository invoiceRepository;
    @Transactional
    @Override
    public BillResponse Booking(BookingRequest request, String status) {
        // Check available rooms
        int quantityStill = roomRepository.selectCountRoomByCheckInCheckOutAndType(
                request.getHotelId(), request.getCheckIn(), request.getCheckOut(), request.getTypeRoom());
        if (quantityStill < request.getQuantityBook()) {
            throw new IllegalArgumentException("Chỉ còn " + quantityStill + " phòng trống, không đủ để đặt " + request.getQuantityBook() + " phòng.");
        }

        // Check date validity
        if (!request.getCheckOut().after(request.getCheckIn())) {
            throw new IllegalArgumentException("Ngày trả phòng phải sau ngày nhận phòng");
        }
        if (hotelRepository.findById(request.getHotelId()).get().isDelete() ==true){
            throw new IllegalArgumentException("Khách Sạn Đã Đóng Cửa");
        }

        // Create a new booking
        Booking bill = new Booking();
        bill.setPrice(request.getPrice());
        bill.setPhone(request.getPhone());
        bill.setLastName(request.getLastName());
        bill.setFirstName(request.getFirstName());
        bill.setStatus(status);
        bill.setCheckIn(request.getCheckIn());
        bill.setCheckOut(request.getCheckOut());
        bill.setCreatedAt(new Date());

        // Get the logged-in user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userLogin = userRepository.findById(((User) authentication.getPrincipal()).getId())
                .orElseThrow(() -> new IllegalArgumentException("Người dùng không hợp lệ"));
        bill.setUserBooking(userLogin);

        // Fetch available rooms and create booking details
        List<Room> listRoomStill = roomRepository.selectAllRoomByCheckInCheckOutAndType(
                request.getHotelId(), request.getCheckIn(), request.getCheckOut(), request.getTypeRoom());

        if (listRoomStill.isEmpty()) {
            throw new IllegalArgumentException("Không tìm thấy phòng khả dụng");
        }

        List<BookingDetails> bookingDetailsList = new ArrayList<>();
        int quantityBook = request.getQuantityBook();

        for (int i = 0; i < quantityBook && i < listRoomStill.size(); i++) {
            BookingDetails bookingDetails = new BookingDetails();
            bookingDetails.setRoomBooking(listRoomStill.get(i));
            bookingDetails.setBooking(bill);
            bookingDetailsList.add(bookingDetails);
        }

        // Attach booking details to the booking
        bill.setListDetails(bookingDetailsList);

        // Save the booking to the database
        Booking billCheck = billRepository.save(bill);

        // Return response
        return new BillResponse(billCheck);
    }

    @Override
    public Booking findById(int id) {
        return billRepository.findById(id).get();
    }


    @Override
    @Transactional
    public void updateStatusBill(Booking bill, String status) {
        bill.setStatus(status);
        billRepository.save(bill);
    }

    @Transactional
    public void cancelUnpaidOrders() {
        Date thirtyMinutesAgo = new Date(System.currentTimeMillis() - 30 * 60 * 1000);
        List<Booking> bills = billRepository.findByStatusPendingAndCreatedAtBefore(thirtyMinutesAgo);

        for (Booking bill : bills) {
            updateStatusBill(bill,"Cancel");
        }
    }

    @Override
    public PaginationResponse selectBillByUser(User user, int pageNum) {
        Sort sort = HandleSort.buildSortProperties("booking_id", "desc");
        Pageable pageable = PageRequest.of(pageNum, 10,sort);
        Page<Booking> pageBill = billRepository.findBookingByUser(user.getId(),pageable);
        List<BillResponse> listResponse = new ArrayList<>();
        pageBill.getContent().stream().forEach(item -> {
            listResponse.add(new BillResponse(item));
        });
        PaginationResponse paginationResponse = new PaginationResponse(pageNum,pageBill.getSize(),pageBill.getTotalElements(),pageBill.isLast(),pageBill.getTotalPages(),listResponse);
        return paginationResponse;
    }

    @Override
    public BillResponse selectBillById(int id) {
        BillResponse response = new BillResponse(findById(id));
        return response;
    }

    @Override
    public List<RevenueResponse> getRevenua(int hotelId, Date dateFrom, Date dateTo) {
        List<RevenueResponse> list = new ArrayList<>();
        billRepository.selectRevenueByHotelAndDate(hotelId,dateFrom,dateTo).stream().forEach(item -> {
            list.add(new RevenueResponse(item,dateFrom,dateTo));
        });
        return list;
    }



    @Override
    @Transactional
    public void updateStatusBill(String status, int billId) {
        Booking bill = billRepository.findById(billId).get();
        bill.setStatus(status);
        billRepository.save(bill);
    }

    @Override
    public List<BillResponse> SelectBookingByHotelIdAndStatus(int hotelId, String status, String phone) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userLogin = (User) authentication.getPrincipal();

        // Kiểm tra quyền
        if (hotelPartnersRepository.checkHotelPartnersByUser(userLogin.getId(), hotelId) == 0) {
            throw new IllegalArgumentException("Bạn Không Phải Đối Tác Này");
        }

        // Lấy danh sách booking
        List<Booking> listBooking = billRepository.listBookingByHotelAndStatus(hotelId, status,phone);

        // Chuyển đổi sang BillResponse
        return listBooking.stream()
                .map(BillResponse::new)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional
    public HttpRespone updateBillId(int bookingId, String status) {
        Invoice invoice = null;
        // Lấy người dùng đã đăng nhập
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userLogin = (User) authentication.getPrincipal();

        // Lấy booking và kiểm tra nếu tồn tại
        Booking bookingEntity = billRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Không Tìm Thấy Đơn Đặt Hàng Này"));

        // Kiểm tra có chi tiết phòng trong booking không
        if (bookingEntity.getListDetails().isEmpty() || bookingEntity.getListDetails().get(0).getRoomBooking() == null) {
            throw new IllegalArgumentException("Không có chi tiết phòng trong đơn đặt hàng.");
        }

        Hotel hotel = bookingEntity.getListDetails().get(0).getRoomBooking().getHotelRoom();

        // Kiểm tra quyền cập nhật của người dùng
        if (!hasPermissionToUpdateBooking(userLogin, hotel)) {
            throw new IllegalArgumentException("Bạn Không Có Quyền Update Đơn Đặt Hàng Này");
        }

        // Chỉ tạo và xử lý hóa đơn nếu booking đang ở trạng thái "active"
        if (status.equalsIgnoreCase("success") && bookingEntity.getStatus().equalsIgnoreCase("active")) {
            // Ngăn việc tạo hóa đơn trùng lặp bằng cách kiểm tra xem booking đã có hóa đơn chưa
            if (bookingEntity.getInvoice().isEmpty()) { // Giả sử Booking có trường Invoice
                // Tạo và lưu hóa đơn
                invoice = createInvoice(userLogin, bookingEntity);

                // Xử lý giao dịch
                processTransaction(userLogin, invoice, bookingEntity);
            } else {
                throw new IllegalArgumentException("Đơn đặt hàng này đã có hóa đơn.");
            }
        }

        // Cập nhật trạng thái booking
        bookingEntity.setStatus(status);
        Booking bookingSaved = billRepository.save(bookingEntity);
        if (invoice != null) {
            return new HttpRespone(HttpStatus.OK.value(), "success", new InvoiceResponse(invoice));
        }else{
            return new HttpRespone(HttpStatus.OK.value(), "success", new BillResponse(bookingSaved));
        }
    }

    @Override
    public HttpRespone findInvoiceById(int invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow(() -> {
           throw new IllegalArgumentException("Không Tìm Thấy Hoá Đơn");
        });
        return new HttpRespone(HttpStatus.OK.value(), "success",new InvoiceResponse(invoice));
    }


    private boolean hasPermissionToUpdateBooking(User userLogin, Hotel hotel) {
        return hotelPartnersRepository.checkHotelPartnersByUser(userLogin.getId(), hotel.getHotelId()) > 0;
    }




    private Invoice createInvoice(User userLogin, Booking bookingSaved) {
        Invoice invoice = new Invoice();
        invoice.setCreataAt(new Date());
        invoice.setUserCreate(userLogin);
        invoice.setBooking(bookingSaved);
        invoice.setDescription("Hoá Đơn Dịch Vụ Tại Khách Sạn: "
                + bookingSaved.getListDetails().get(0).getRoomBooking().getHotelRoom().getPartner().getHotelName());
        invoice.setPaymentMethod("vnpay");
        invoice.setDiscount(BigDecimal.ZERO);
        invoice.setStatus(Invoice.InvoiceStatus.PAID);
        invoice.setTax(BigDecimal.ZERO);

        int totalAmount = calculateTotalAmount(bookingSaved);
        invoice.setAmount(new BigDecimal(totalAmount));

        return invoiceRepository.save(invoice);  // Giả sử có repository cho invoice
    }

    private int calculateTotalAmount(Booking bookingSaved) {
        int totalAmount = bookingSaved.getPrice();
        for (RestaurantOrder item : bookingSaved.getListOrderFood()) {
            if (item.getStatus().equals("success")) {
                totalAmount += item.getTotalPrice();
            }
        }
        return totalAmount;
    }

    private void processTransaction(User userLogin, Invoice invoice, Booking bookingSaved) {
        Optional<Wallet> walletOpt = walletRepository.findWalletById(bookingSaved.getListDetails().get(0)
                .getRoomBooking().getHotelRoom().getPartner().getId());

        walletOpt.ifPresent(wallet -> {
            int amount = invoice.getAmount().multiply(new BigDecimal("0.90")).intValue();
            Transaction transaction = createTransaction(userLogin, wallet, amount);
            transactionRepository.save(transaction);

            // Cập nhật số dư ví
            Wallet walletSave = wallet;
            walletSave.setBalance(walletSave.getBalance().add(new BigDecimal(amount)));
            walletRepository.save(walletSave);

            // Thông báo cho đối tác
            partnerNotificationService.addNotification(
                    walletSave.getPartner().getId(),
                    "Biến Động Số Dư",
                    "Bạn Đã Được Nhận Tiền Từ: " + invoice.getDescription() + " Số Dư Hiện Tại: " + walletSave.getBalance(),
                    "#!/wallet"
            );
        });
    }

    private Transaction createTransaction(User userLogin, Wallet wallet, int amount) {
        Transaction transaction = new Transaction();
        transaction.setTransactionDate(new Date());
        transaction.setTransactionType(Transaction.TransactionType.DEPOSIT);
        transaction.setAmount(new BigDecimal(amount));
        transaction.setDescription("Thanh Toán Tiền Hoá Đơn");
        transaction.setWallet(wallet);
        transaction.setFee(BigDecimal.ZERO);
        transaction.setStatus(Transaction.TransactionStatus.COMPLETED);
        transaction.setInitiator(userLogin);
        transaction.setUserForPartner(userLogin);
        return transaction;
    }


    @Override
    public List<Object[]> getRevenueByMonth(int year, int hotelId) {
        return invoiceRepository.getTotalRevenueByMonth(year, hotelId);
    }


}
