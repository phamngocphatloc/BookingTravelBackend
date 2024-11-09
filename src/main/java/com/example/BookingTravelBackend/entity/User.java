package com.example.BookingTravelBackend.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Entity
@Setter
@Getter
@Table (name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserId")
    private int id;
    @Column (name = "fullName", columnDefinition = "nvarchar(50)", length = 255)
    private String fullName;
    @Column(name = "Email", nullable = false, unique = true, length = 50)
    private String email;
    @Column(name = "Phone", nullable = false, unique = true, length = 50)
    private String phone;
    @Column(name = "Password", nullable = false)
    private String password;
    @ManyToOne
    @JoinColumn(name = "RoleId")
    private Role role;
    @Column(name = "Address", columnDefinition = "nvarchar(500)", nullable = false, length = 500)
    private String address;
    @Column(name = "City",columnDefinition = "nvarchar(50)", nullable = false, length = 50)
    private String city;
    @Column(name = "District", columnDefinition = "nvarchar(50)", nullable = false, length = 50)
    private String district;
    @Column(name = "Ward",columnDefinition = "nvarchar(50)", nullable = false, length = 50)
    private String ward;

    @OneToOne (mappedBy = "userToken")
    private VerificationToken Token;

    @Column (name = "Verify")
    private boolean verify;

    @Column (name = "avatar")
    private String avatar;

    @OneToMany (mappedBy = "userManager", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<PartnersManager> listPartnersManager = new ArrayList<>();

    @Column (name = "authentic")
    private boolean authentic;

    @OneToMany(mappedBy = "sender")
    private List<FriendRequest> sentRequests;  // Danh sách yêu cầu kết bạn mà người dùng đã gửi

    @OneToMany(mappedBy = "receiver")
    private List<FriendRequest> receivedRequests;  // Danh sách yêu cầu kết bạn mà người dùng đã nhận

    @OneToMany(mappedBy = "user1")
    private List<Friendship> friendshipsAsUser1;  // Danh sách kết bạn của người dùng (user1)

    @OneToMany(mappedBy = "user2")
    private List<Friendship> friendshipsAsUser2;  // Danh sách kết bạn của người dùng (user2)


}