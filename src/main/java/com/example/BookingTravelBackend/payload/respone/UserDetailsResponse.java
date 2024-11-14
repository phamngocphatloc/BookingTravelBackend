package com.example.BookingTravelBackend.payload.respone;

import com.example.BookingTravelBackend.entity.Post;
import com.example.BookingTravelBackend.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Getter
@RequiredArgsConstructor
public class UserDetailsResponse {
    private int userId;
    private String email;
    private String phone;
    private String address;
    private String city;
    private String district;
    private String ward;

    private String fullname;

    private String role;

    private boolean verify;

    private String avatar;

    private boolean authentic;
    List<PostResponse> allPost;
    private int allFolowers;

    private List<PartnersManagerResponse> listPartnersManager = new ArrayList<>();
    private boolean followersProfile;
    public UserDetailsResponse(User user) {
        this.userId = user.getId();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.address = user.getAddress();
        this.city = user.getCity();
        this.district = user.getDistrict();
        this.ward = user.getWard();
        this.fullname = user.getFullName();
        this.role = user.getRole().getRoleName();
        this.verify = user.isVerify();
        this.avatar = user.getAvatar();
        this.authentic = user.isAuthentic();
        user.getListPartnersManager().stream().forEach(item -> {
            this.listPartnersManager.add(new PartnersManagerResponse(item));
        });
    }

    public void setAllPost(List<PostResponse> allPost) {
        this.allPost = allPost;
    }

    public void setAllFolowers(int allFolowers) {
        this.allFolowers = allFolowers;
    }
    public void setFollowersProfile(boolean followersProfile) {
        this.followersProfile = followersProfile;
    }
}