package com.example.BookingTravelBackend.payload.respone;

import com.example.BookingTravelBackend.entity.Menu;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
public class MenuRestaurantResponse {
    private int id;
    private String productName;
    private String imgProduct;
    private String description;
    private int price;
    private List<MenuRestaurantReviewResponse> menuRestaurantReviews = new ArrayList<>();
    private List<MenuDetailsResponse> listItems = new ArrayList<>();

    public MenuRestaurantResponse(Menu restaurant){
        this.id = restaurant.getId();
        this.productName = restaurant.getProductName();
        this.imgProduct = restaurant.getImgProduct();
        this.description = restaurant.getDescription();
        this.price = restaurant.getPrice();
        restaurant.getMenuRestaurantReviews().stream().forEach(item -> {
            this.menuRestaurantReviews.add(new MenuRestaurantReviewResponse(item));
        });
        restaurant.getListItems().stream().forEach(item -> {
            this.listItems.add(new MenuDetailsResponse(item));
        });
    }
}
