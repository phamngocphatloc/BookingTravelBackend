package com.example.BookingTravelBackend.controllers;

import com.example.BookingTravelBackend.Configuration.WebConfig;
import com.example.BookingTravelBackend.Repository.FollowRepository;
import com.example.BookingTravelBackend.entity.User;
import com.example.BookingTravelBackend.entity.VerificationToken;
import com.example.BookingTravelBackend.payload.Request.*;
import com.example.BookingTravelBackend.payload.respone.*;
import com.example.BookingTravelBackend.service.BillService;
import com.example.BookingTravelBackend.service.EmailService;
import com.example.BookingTravelBackend.service.UserService;
import com.example.BookingTravelBackend.service.VerificationTokenService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Transactional
public class UserController {
    private final UserService userService;

    private final VerificationTokenService tokenService;
    private final BillService billService;
    private final FollowRepository followRepository;
    private final EmailService emailService;
    @PostMapping("login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        JwtResponse jwtResponse = userService.login(loginRequest);
        return ResponseEntity.status(HttpStatus.OK).body(jwtResponse);
    }

    @PostMapping("register")
    @Transactional
    public ResponseEntity<HttpRespone> register(@Valid @RequestBody UserRequest userRequest) {
        userService.register(userRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new HttpRespone(HttpStatus.OK.value(),
                "Register successfully",null));
    }

    @GetMapping("getUserByEmail/{email}")
    public ResponseEntity<UserDetailsResponse> findUserByEmail(@PathVariable String email){
        User user = userService.findByEmail(email);
        UserDetailsResponse userDetailsResponse = new UserDetailsResponse(user);
        return ResponseEntity.status(HttpStatus.OK).body(userDetailsResponse);
    }


    @PostMapping("changePassword")
    public ResponseEntity<HttpRespone> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        Boolean changePassword = userService.changePassword(changePasswordRequest);
        if(changePassword){
            return ResponseEntity.status(HttpStatus.OK).body(new HttpRespone(HttpStatus.OK.value(),
                    "Change password successfully",null));
        }
        else {
            return ResponseEntity.status(HttpStatus.OK).body(new HttpRespone(HttpStatus.OK.value(),
                    "Wrong old password or the new password and confirm password do not match",null));
        }
    }

    @PutMapping("update")
    public ResponseEntity<HttpRespone> update(@RequestBody UserInfoRequest userInfoRequest){
        try{
            User user = userService.findById(userInfoRequest.getUserId());

            if(user == null){
                return ResponseEntity.status(HttpStatus.OK).body(new HttpRespone(HttpStatus.OK.value(),
                        "User not found",null));
            }
            userService.update(userInfoRequest.getUserId(), userInfoRequest);
            return ResponseEntity.status(HttpStatus.OK).body(new HttpRespone(HttpStatus.OK.value(),
                    "Update successfully",null));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new HttpRespone(HttpStatus.BAD_REQUEST.value(),
                    "Update failed",null));
        }
    }



    @GetMapping ("authorization")
    public ResponseEntity<UserDetailsResponse> getRoleUset (){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        User userLogin = userService.findById(principal.getId());
        RoleResponse role = new RoleResponse(userLogin.getEmail(),userLogin.getRole().getRoleName());
        UserDetailsResponse response = new UserDetailsResponse(userLogin);
        response.setAllFolowers(followRepository.AllFollowersByUser(principal.getId()));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping ("verify")
    public ResponseEntity<UserDetailsResponse> verifyUser (@RequestParam ("token") String token){
        User user = tokenService.verifyUser(token);
        RoleResponse role = new RoleResponse(user.getEmail(),user.getRole().getRoleName());
        return ResponseEntity.status(HttpStatus.OK).body(new UserDetailsResponse(user));
    }

    @GetMapping ("getbill")
    public ResponseEntity<HttpRespone> getBill (@RequestParam("page")Optional<Integer> page){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        User userLogin = userService.findById(principal.getId());
        PaginationResponse response = billService.selectBillByUser(userLogin,page.orElse(0));
        return ResponseEntity.status(HttpStatus.OK).body(new HttpRespone(HttpStatus.OK.value(),"success",response));
    }

    @GetMapping ("getBillUser/{id}")
    public ResponseEntity<HttpRespone> getBillById (@PathVariable ("id") int id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        User userLogin = userService.findById(principal.getId());
        BillResponse response = billService.selectBillById(id);
        if (userLogin.getId() == response.getUserCreate().getUserId()){
            return ResponseEntity.ok(new HttpRespone(HttpStatus.OK.value(), "success", response));
        }else{
            return ResponseEntity.ok(new HttpRespone(HttpStatus.OK.value(), "không tìm thấy id này trong tài khoản", null));
        }
    }

    @PostMapping ("forget_password")
    public ResponseEntity<HttpRespone> forgetPasssword (String email){
        User user = userService.findByEmail(email);
        VerificationToken token = tokenService.createVerificationToken(user);
        String recipientAddress = user.getEmail();
        String subject = "Yêu Cầu Đặt Lại Mật Khẩu";
        String confirmationUrl = "/" + token.getToken()+"/"+email;
        String message = "Click the link to verify your account: " + WebConfig.url+"#!/forget_pass" + confirmationUrl;
        try {
            emailService.sendEmail(recipientAddress, subject, message);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.ok(new HttpRespone(HttpStatus.OK.value(), "success", "vui lòng nhấn vào link được gửi đến email của bạn"));
    }

    @GetMapping ("forget_password")
    public ResponseEntity<HttpRespone> checkTokenForget (String email, String token){
        User user = userService.findByEmail(email);
        if (tokenService.checkTokenValidity(token,email)){
            return ResponseEntity.ok(new HttpRespone(HttpStatus.OK.value(), "success", "yes"));
        }
        return ResponseEntity.ok(new HttpRespone(HttpStatus.OK.value(), "success", "token không chính xác hoặc đã hết hạn"));
    }

    @PutMapping ("forget_password")
    public ResponseEntity<HttpRespone> forgetPassword (@RequestBody ForgetPasswordRequest request){
        User user = tokenService.forgetPassword(request);
        return ResponseEntity.ok(new HttpRespone(HttpStatus.OK.value(), "success", new UserInfoResponse(user)));
    }
}