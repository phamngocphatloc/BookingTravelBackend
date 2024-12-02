package com.example.BookingTravelBackend.payload.respone;

import com.example.BookingTravelBackend.entity.Menu;
import lombok.Getter;
import lombok.Setter;
import com.example.BookingTravelBackend.entity.Restaurant;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RestaurantResponse {
    private int id;
    HotelRespone hotelRestaurant;
    private String restaurantName;
    private boolean active;
    private List<MenuRestaurantResponse> listMenu = new ArrayList<>();
    private String restaurantImg;
    private boolean authentic;

    public RestaurantResponse (Restaurant restaurant){
        this.id = restaurant.getId();
        this.hotelRestaurant = new HotelRespone(restaurant.getHotelRestaurant());
        this.restaurantName = restaurant.getRestaurantName();
        this.active = restaurant.isActive();
        restaurant.getListMenu().stream().forEach(item -> {
            listMenu.add(new MenuRestaurantResponse(item));
        });
        this.restaurantImg = restaurant.getRestaurantImg();
        this.authentic = restaurant.isAuthentic();
    }
}
