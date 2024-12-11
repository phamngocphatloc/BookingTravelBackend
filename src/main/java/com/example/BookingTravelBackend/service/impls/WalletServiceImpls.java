package com.example.BookingTravelBackend.service.impls;

import com.example.BookingTravelBackend.Repository.TransactionRepository;
import com.example.BookingTravelBackend.Repository.WalletRepository;
import com.example.BookingTravelBackend.Repository.UserRepository;
import com.example.BookingTravelBackend.entity.Transaction;
import com.example.BookingTravelBackend.entity.User;
import com.example.BookingTravelBackend.entity.Wallet;
import com.example.BookingTravelBackend.payload.Request.WithdrawRequest;
import com.example.BookingTravelBackend.payload.respone.HttpRespone;
import com.example.BookingTravelBackend.payload.respone.TransactionResponse;
import com.example.BookingTravelBackend.payload.respone.WalletResponse;
import com.example.BookingTravelBackend.service.NotificationService;
import com.example.BookingTravelBackend.service.PartnerNotificationService;
import com.example.BookingTravelBackend.service.WalletService;
import com.example.BookingTravelBackend.Repository.HotelPartnersRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
@RequiredArgsConstructor
public class WalletServiceImpls implements WalletService {
    private final HotelPartnersRepository hotelPartnersRepository;
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final PartnerNotificationService partnerNotificationService;
    private final NotificationService notificationService;

    @Override
    public HttpRespone getWalletByPartner(int partnerId) {
        // Lấy người dùng đã đăng nhập
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userLogin = (User) authentication.getPrincipal();

        if (hotelPartnersRepository.isManagerOfPartner(userLogin.getId(),partnerId)==0){
            throw new IllegalArgumentException("Bạn Không Phải Admin Partner Này");
        }

        Wallet wallet = walletRepository.findWalletById(partnerId).orElseThrow(() -> {
            throw new IllegalArgumentException("Không Tìm Thấy Ví Này");
        });


        return new HttpRespone(HttpStatus.OK.value(), "success", new WalletResponse(wallet));
    }
    @Override
    public HttpRespone getTransactioWallet(String transactionType) {
        List<Transaction> transactionList = transactionRepository.findAllWithdrawal(transactionType);
        List<TransactionResponse> transactionResponses = new ArrayList<>();
        if (!transactionList.isEmpty()){
            transactionList.stream().forEach(item -> {
                transactionResponses.add(new TransactionResponse(item));
            });
        }
        return new HttpRespone(HttpStatus.OK.value(), "success", transactionResponses);
    }

    @Override
    @Transactional
    public void processWithdrawal(WithdrawRequest request) {
        // Kiểm tra đối tác
        if (request.getPartnerId() != null) {
            processPartnerWithdrawal(request);
        } else {
            processUserWithdrawal(request);
        }
    }

    @Override
    public HttpRespone WalletTransion(int id) {
        Transaction transaction = transactionRepository.findById(id).orElseThrow(()->{
            throw new IllegalArgumentException("Không tìm thấy Transaction");

        });
        transaction.setStatus(Transaction.TransactionStatus.COMPLETED);
        return new HttpRespone(HttpStatus.OK.value(), "success",
                new TransactionResponse(transactionRepository.save(transaction)));
    }

    private void processPartnerWithdrawal(WithdrawRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userLogin = (User) authentication.getPrincipal();

        boolean partnerAdmin = userLogin.getListPartnersManager().stream()
                .anyMatch(item -> item.getId() == request.getPartnerId());
        if (!partnerAdmin) {
            throw new IllegalArgumentException("Bạn không phải đối tác này");
        }

        Wallet wallet = walletRepository.findWalletById(request.getPartnerId())
                .orElseThrow(() -> new IllegalArgumentException("Đối tác này chưa có ví"));

        processWithdrawalLogic(wallet, request);
    }

    private void processUserWithdrawal(WithdrawRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        User userLogin = userRepository.findById(principal.getId())
                .orElseThrow(() -> new IllegalArgumentException("Người dùng không tồn tại"));

        if (userLogin.getWallet() == null) {
            throw new IllegalArgumentException("Tài khoản chưa có ví điện tử");
        }

        Wallet wallet = userLogin.getWallet();
        processWithdrawalLogic(wallet, request);
    }

    private void processWithdrawalLogic(Wallet wallet, WithdrawRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userLogin = (User) authentication.getPrincipal();
        // Kiểm tra số dư trong ví
        if (wallet.getBalance().compareTo(request.getAmount()) < 0) {
            throw new IllegalArgumentException("Số dư không đủ để thực hiện giao dịch.");
        }

        // Kiểm tra thông tin tài khoản ngân hàng
        if (request.getBankAccountNumber() == null || request.getBankAccountNumber().isEmpty()
                || request.getBankName() == null || request.getBankName().isEmpty()
                || request.getAccountHolderName() == null || request.getAccountHolderName().isEmpty()) {
            throw new IllegalArgumentException("Thông tin tài khoản ngân hàng là bắt buộc.");
        }


        // Kiểm tra số tiền rút phải >= 100,000
        BigDecimal minimumAmount = new BigDecimal("100000");
        if (request.getAmount().compareTo(minimumAmount) < 0) {
            throw new IllegalArgumentException("Số tiền rút phải tối thiểu là 100,000 đồng.");
        }

        // Trừ số dư trong ví
        BigDecimal newAmount = wallet.getBalance().subtract(request.getAmount());
        wallet.setBalance(newAmount);

        // Tạo giao dịch rút tiền
        Transaction transaction = new Transaction();
        transaction.setTransactionType(Transaction.TransactionType.WITHDRAWAL);
        transaction.setStatus(Transaction.TransactionStatus.PENDING); // Trạng thái ban đầu là PENDING
        transaction.setAmount(request.getAmount());
        transaction.setWallet(wallet);
        transaction.setUserForPartner(userLogin);
        transaction.setInitiator(userLogin); // Người thực hiện giao dịch
        transaction.setTransactionDate(new Date());
        transaction.setDescription("Rút tiền tới tài khoản ngân hàng: " + request.getBankAccountNumber() + " tại " + request.getBankName());
        transaction.setBankAccountNumber(request.getBankAccountNumber());
        transaction.setBankName(request.getBankName());
        transaction.setAccountHolderName(request.getAccountHolderName());

        // Lưu giao dịch và cập nhật ví
        transactionRepository.save(transaction);
        Wallet saved = walletRepository.save(wallet);
        if (request.getPartnerId() != null){
            // Thông báo cho đối tác
            partnerNotificationService.addNotification(
                    request.getPartnerId(),
                    "Biến Động Số Dư",
                    "Bạn Đã Rút: " + request.getAmount() + "VNĐ" + " Số Dư Hiện Tại: " + saved.getBalance()+ "VNĐ" ,
                    "#!/wallet"
            );
        }else{
            // Thông báo cho đối tác
            notificationService.addNotification(
                    saved.getUser().getId(),
                    "Biến Động Số Dư",
                    "Bạn Đã Rút: " + request.getAmount()  + "VNĐ" + " Số Dư Hiện Tại: " + saved.getBalance(),
                    "#!/wallet"
            );
        }
    }

}
