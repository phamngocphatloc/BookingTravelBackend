package com.example.BookingTravelBackend.service.impls;

import com.example.BookingTravelBackend.Configuration.WebConfig;
import com.example.BookingTravelBackend.Repository.FollowRepository;
import com.example.BookingTravelBackend.Repository.RoleRepository;
import com.example.BookingTravelBackend.Repository.UserRepository;
import com.example.BookingTravelBackend.entity.*;
import com.example.BookingTravelBackend.exception.NotFoundException;
import com.example.BookingTravelBackend.payload.Request.ChangePasswordRequest;
import com.example.BookingTravelBackend.payload.Request.LoginRequest;
import com.example.BookingTravelBackend.payload.Request.UserInfoRequest;
import com.example.BookingTravelBackend.payload.Request.UserRequest;
import com.example.BookingTravelBackend.payload.respone.*;
import com.example.BookingTravelBackend.service.EmailService;
import com.example.BookingTravelBackend.service.JwtService;
import com.example.BookingTravelBackend.service.UserService;
import com.example.BookingTravelBackend.service.VerificationTokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.BookingTravelBackend.Repository.PostRepository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpls implements UserService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    private final VerificationTokenService tokenService;

    private final EmailService emailService;
    private final PostRepository postRepository;
    private final FollowRepository followRepository;

    @Override
    public JwtResponse login(LoginRequest loginRequest) {


        UsernamePasswordAuthenticationToken authen = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                loginRequest.getPassword());
        Authentication authentication = authenticationManager.authenticate(authen);
        List<String> listRoles = authentication.getAuthorities()
                .stream().map((authority) -> authority.getAuthority())
                .collect(Collectors.toList());

        return jwtService.generateJwtResponse(loginRequest.getEmail(), listRoles);
    }

    @Override
    public void register(UserRequest userRequest) {
        String encryptedPassword = passwordEncoder.encode(userRequest.getPassword());
        Role role = roleRepository.findByRoleName("ROLE_USER");
        User users = mapUserRequestToUser(userRequest, role, encryptedPassword);

        if (!userRepository.findByEmail(users.getEmail()).isEmpty()){
            throw new IllegalStateException("Email Đã Tồn Tại");
        }
        if (!userRepository.findByPhone(users.getPhone()).isEmpty()){
            throw new IllegalStateException("Số Điện Thoại Đã Tồn Tại");
        }
        users.setVerify(false);
        users.setAuthentic(false);
        //set avatar mac dinh
        users.setAvatar("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQRMjq1f6zrvoO_vb_oRwMOsWm59Ux_7Ky9FQ&s");
        userRepository.save(users);
        VerificationToken token = tokenService.createVerificationToken(users);
        String recipientAddress = users.getEmail();
        String subject = "Account Verification";
        String confirmationUrl = "#!/verify?token=" + token.getToken();
        String htmlContent = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            background-color: #f9f9f9;\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "        }\n" +
                "        .email-container {\n" +
                "            max-width: 600px;\n" +
                "            margin: 20px auto;\n" +
                "            background-color: #ffffff;\n" +
                "            border: 1px solid #dddddd;\n" +
                "            border-radius: 8px;\n" +
                "            overflow: hidden;\n" +
                "            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);\n" +
                "        }\n" +
                "        .header {\n" +
                "            background-color: #007BFF;\n" +
                "            color: white;\n" +
                "            text-align: center;\n" +
                "            padding: 20px;\n" +
                "            font-size: 24px;\n" +
                "        }\n" +
                "        .content {\n" +
                "            padding: 20px;\n" +
                "            line-height: 1.6;\n" +
                "            color: #333333;\n" +
                "        }\n" +
                "        .content a {\n" +
                "            color: #007BFF;\n" +
                "            text-decoration: none;\n" +
                "        }\n" +
                "        .footer {\n" +
                "            background-color: #f1f1f1;\n" +
                "            text-align: center;\n" +
                "            padding: 10px;\n" +
                "            font-size: 14px;\n" +
                "            color: #666666;\n" +
                "        }\n" +
                "        .button {\n" +
                "            display: inline-block;\n" +
                "            padding: 10px 20px;\n" +
                "            margin: 20px 0;\n" +
                "            background-color: #007BFF;\n" +
                "            color: white;\n" +
                "            text-decoration: none;\n" +
                "            border-radius: 4px;\n" +
                "            font-size: 16px;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"email-container\">\n" +
                "        <div class=\"header\">\n" +
                "            Chào mừng đến với Travel Book!\n" +
                "        </div>\n" +
                "        <div class=\"content\">\n" +
                "            <p>Xin chào <strong>{{name}}</strong>,</p>\n" +
                    "            <p>Cảm ơn bạn đã đăng ký tài khoản! Để hoàn tất việc đăng ký, vui lòng xác minh địa chỉ email của bạn bằng cách nhấn vào nút dưới đây:</p>\n" +
                    "            <p style=\"text-align: center;\">\n" +
                    "                <a href=\""+WebConfig.url+"/" + confirmationUrl+"\" class=\"button\">Xác minh email</a>\n" +
                    "            </p>\n" +
                    "            <p>Nếu bạn không tạo tài khoản, hãy bỏ qua email này.</p>\n" +
                    "            <p>Trân trọng,<br>Đội ngũ [Tên công ty/ứng dụng]</p>\n" +
                    "        </div>\n" +
                "        <div class=\"footer\">\n" +
                "            &copy; 2024 Travelbook. Mọi quyền được bảo lưu.\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>\n"; // Copy nội dung HTML template ở trên
        String message = "Click the link to verify your account: " + WebConfig.url+"/" + confirmationUrl;
        try {
            emailService.sendEmail(recipientAddress, subject, htmlContent);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private User mapUserRequestToUser(UserRequest userRequest, Role role, String encryptedPassword) {
        User user = new User();
        user.setFullName(userRequest.getFullName());
        user.setEmail(userRequest.getEmail());
        user.setAddress(userRequest.getAddress());
        user.setCity(userRequest.getCity());
        user.setDistrict(userRequest.getDistrict());
        user.setPhone(userRequest.getPhone());
        user.setRole(role);
        user.setPassword(encryptedPassword);
        user.setWard(userRequest.getWard());
        return user;
    }

    @Override
    public User findByEmail(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty())
            throw new NotFoundException("No user with this email");
        return optionalUser.get();
    }

    @Override
    public User findById(int id) {
        Optional<User> optional = userRepository.findById(id);
        if (optional.isEmpty()) throw new NotFoundException("No user with this id: " + id);
        return optional.get();
    }

    @Override
    public Boolean changePassword(ChangePasswordRequest changePasswordRequest) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String email = userDetails.getUsername();
        String oldPassword = changePasswordRequest.getOldPassword();
        String newPassword = changePasswordRequest.getNewPassword();
        String confirmPassword = changePasswordRequest.getConfirmPassword();

        User user = findByEmail(email);
        if (passwordEncoder.matches(oldPassword, user.getPassword()) && newPassword.equals(confirmPassword)) {
            String encryptedPassword = passwordEncoder.encode(newPassword);
            user.setPassword(encryptedPassword);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public void update(int id, UserInfoRequest userInfoRequest) {
        User user = userRepository.findById(id).get();
        user.setAddress(userInfoRequest.getAddress());
        user.setPhone(userInfoRequest.getPhone());
        user.setCity(userInfoRequest.getCity());
        user.setDistrict(userInfoRequest.getDistrict());
        user.setWard(userInfoRequest.getWard());
        user.setAvatar(userInfoRequest.getAvatar());
        user.setFullName(userInfoRequest.getFullname());
        userRepository.save(user);
    }

    @Override
    public List<UserDetailsResponse> selectAll() {
        List<UserDetailsResponse> response = new ArrayList<>();
        userRepository.findAll().stream().forEach(item -> {
            response.add(new UserDetailsResponse(item));
        });
        return response;
    }

    @Override
    @Transactional
    public UserDetailsResponse updateAdmin(int userId, String roleName) {
        User user = findById(userId);
        Role role = roleRepository.findByRoleName(roleName);
        user.setRole(role);
        User userSaved = userRepository.save(user);
        return new UserDetailsResponse(userSaved);
    }

    @Override
    public UserDetailsResponse findUserById(int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        User userFind = findById(id);
        UserDetailsResponse response = new UserDetailsResponse(userFind);
        List<Post> allPost = postRepository.findAllPostByUserId(id);
        List<PostResponse> allPostResponse = new ArrayList<>();
        response.setAllFolowers(followRepository.AllFollowersByUser(id));
        if (!allPost.isEmpty()){
            allPost.stream().forEach(item -> {
                allPostResponse.add(new PostResponse(item));
            });
        }
        boolean followers = false;
        if (followRepository.CheckFollow(id,principal.getId())==1){
            followers = true;
        }
        response.setAllPost(allPostResponse);
        response.setFollowersProfile(followers);
        return response;
    }

    @Override
    @Transactional
    public int Follow(int userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        if (followRepository.CheckFollow(userId,principal.getId())==0) {
            User userLogin = findById(principal.getId());
            User userFollow = findById(userId);
            Follow follow = new Follow();
            follow.setFollower(userLogin);
            follow.setFollowedAt(new Timestamp(System.currentTimeMillis()));
            follow.setFollowedUser(userFollow);
            followRepository.save(follow);
            return followRepository.AllFollowersByUser(userId);
        }else{
            Follow follow = followRepository.findFollowByUserAndUserFollow(userId,principal.getId());
            followRepository.delete(follow);
            return followRepository.AllFollowersByUser(userId);
        }

    }

    @Override
    public List<UserInfoResponse> AllFollowingByUser(int userId) {
        List<User> listUserFollowing = userRepository.selectFollowingByUser(userId);
        List<UserInfoResponse> listUser = new ArrayList<>();
        listUserFollowing.stream().forEach(item -> {
            listUser.add(new UserInfoResponse(item));
        });
        return listUser;
    }

    @Override
    public HttpRespone GetAllUser() {
        List<User> listUser = userRepository.findAll();
        List<UserInfoResponse> listReponse  =  new ArrayList<>();
        if(!listUser.isEmpty()){
            listUser.stream().forEach(item->{
                listReponse.add(new UserInfoResponse(item));
            });
        }
        return new HttpRespone(HttpStatus.OK.value(), "success", listReponse);
    }
}
