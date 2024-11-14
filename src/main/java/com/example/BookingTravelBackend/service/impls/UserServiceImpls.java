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
import com.example.BookingTravelBackend.payload.respone.JwtResponse;
import com.example.BookingTravelBackend.payload.respone.UserDetailsResponse;
import com.example.BookingTravelBackend.service.EmailService;
import com.example.BookingTravelBackend.service.JwtService;
import com.example.BookingTravelBackend.service.UserService;
import com.example.BookingTravelBackend.service.VerificationTokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.BookingTravelBackend.payload.respone.PostResponse;
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
        String message = "Click the link to verify your account: " + WebConfig.url+"/" + confirmationUrl;
        try {
            emailService.sendEmail(recipientAddress, subject, message);
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
            throw new IllegalArgumentException("Đã Follower");
        }

    }
}
