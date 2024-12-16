package com.example.BookingTravelBackend.payload.respone;

import com.example.BookingTravelBackend.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoResponse {
    private int userId;
    private String fullname;

    private boolean verify;

    private String avatar;

    private boolean authentic;
    private String phone;
    private boolean blocked = false;

    public UserInfoResponse(User user) {
        this.userId = user.getId();
        this.fullname = user.getFullName();
        this.verify = user.isVerify();
        this.avatar = user.getAvatar();
        this.authentic = user.isAuthentic();
        this.phone = user.getPhone();
        this.setBlocked(user.isLocked());
    }
}
