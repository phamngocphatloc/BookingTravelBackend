package com.example.BookingTravelBackend.payload.respone;

import com.example.BookingTravelBackend.entity.Menu;
import com.example.BookingTravelBackend.entity.MenuDetails;
import com.example.BookingTravelBackend.entity.MenuRestaurantReview;
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
    private List<MenuRestaurantReviewResponse> menuRestaurantReviews;
    private List<MenuDetailsResponse> listItems;

    // Không cần trả về listRelated nếu không cần thiết, hoặc có thể để nó là null
    private List<MenuRestaurantResponse> listRelated;

    private boolean admin;

    // Constructor tối ưu
    public MenuRestaurantResponse(Menu restaurant) {
        this.id = restaurant.getId();
        this.productName = restaurant.getProductName();
        this.imgProduct = restaurant.getImgProduct();
        this.description = restaurant.getDescription();
        this.price = restaurant.getPrice();

        // Chỉ chuyển đổi các đối tượng cần thiết thành DTOs
        this.menuRestaurantReviews = new ArrayList<>();
        for (MenuRestaurantReview review : restaurant.getMenuRestaurantReviews()) {
            this.menuRestaurantReviews.add(new MenuRestaurantReviewResponse(review));
        }

        this.listItems = new ArrayList<>();
        for (MenuDetails item : restaurant.getListItems()) {
            this.listItems.add(new MenuDetailsResponse(item));
        }

        // Khởi tạo listRelated nếu cần thiết, có thể là null nếu không cần
        this.listRelated = new ArrayList<>();
    }
}
