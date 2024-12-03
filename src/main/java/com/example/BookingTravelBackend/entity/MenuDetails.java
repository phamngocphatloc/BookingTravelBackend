package com.example.BookingTravelBackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "MenuDetails")
@Data
public class MenuDetails {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "MenuDetailsId")
    private int id;
    @Column (name = "Name",columnDefinition = "nvarchar(255)")
    private String name;
    @Column (name = "Size", columnDefinition = "nvarchar(255)")
    private String size;
    @Column (name = "price")
    private int price;
    @ManyToOne
    @JoinColumn (name = "MenuId")
    private Menu product;
    @Column (name = "IsDelete")
    public boolean isDelete;
}
