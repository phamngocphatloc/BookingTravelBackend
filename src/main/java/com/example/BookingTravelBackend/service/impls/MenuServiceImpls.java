package com.example.BookingTravelBackend.service.impls;

import com.example.BookingTravelBackend.Repository.MenuRepository;
import com.example.BookingTravelBackend.entity.Menu;
import com.example.BookingTravelBackend.entity.User;
import com.example.BookingTravelBackend.entity.ReviewFoodImg;
import com.example.BookingTravelBackend.payload.Request.FoodReviewRequest;
import com.example.BookingTravelBackend.payload.Request.ReplyFoodReviewRequest;
import com.example.BookingTravelBackend.service.MenuService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.example.BookingTravelBackend.Repository.HotelRepository;
import com.example.BookingTravelBackend.Repository.MenuRestaurantReviewRepository;
import com.example.BookingTravelBackend.Repository.ReviewFoodImgRepository;
import com.example.BookingTravelBackend.Repository.RestaurantOrderRepository;
import com.example.BookingTravelBackend.Repository.UserRepository;
import com.example.BookingTravelBackend.entity.MenuRestaurantReview;
@Service
@RequiredArgsConstructor
public class MenuServiceImpls implements MenuService {
    private final MenuRepository menuRepository;
    private final MenuRestaurantReviewRepository menuRestaurantReviewRepository;
    private final ReviewFoodImgRepository reviewFoodImgRepository;
    private final RestaurantOrderRepository restaurantOrderRepository;
    private final UserRepository userRepository;
    private final HotelRepository hotelRepository;
    @Override
    public Menu findById(int id) {
        return menuRepository.findById(id).get();
    }

    @Override
    public void review(FoodReviewRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        User userLogin = userRepository.findById(principal.getId()).get();
        if (restaurantOrderRepository.checkProductBuyed(userLogin.getId(),request.getMenuIdReview())==1){
            if (menuRepository.checkRated(userLogin.getId(),request.getMenuIdReview())==0){
                Menu menu = menuRepository.findById(request.getMenuIdReview()).get();
                MenuRestaurantReview review = request.getFoodReviewRequest(menu);
                review.setUserReview(userLogin);
                MenuRestaurantReview saved = menuRestaurantReviewRepository.save(review);
                request.getListImg().stream().forEach(item -> {
                    ReviewFoodImg img = new ReviewFoodImg();
                    img.setImgUrl(item.getImgUrl());
                    img.setReview(saved);
                    reviewFoodImgRepository.save(img);
                });
            }else{
                throw new IllegalArgumentException("Bạn Đã Đánh Gia Sản Phẩm Này");
            }
        }else{
            throw new IllegalArgumentException("Bạn Chưa Mua Sản Phẩm");
        }
    }

    @Override
    @Transactional
    public void reply(ReplyFoodReviewRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        User userLogin = userRepository.findById(principal.getId()).orElseThrow(() -> new IllegalArgumentException("User không tồn tại"));

        MenuRestaurantReview review = menuRestaurantReviewRepository.findById(request.getReviewId()).orElseThrow(() -> new IllegalArgumentException("Review không tồn tại"));

        // Kiểm tra quyền admin của người dùng đối với khách sạn
        if (hotelRepository.isAdminHotel(review.getMenuReview().getRestaurantSell().getHotelRestaurant().getHotelId(), userLogin.getId()) == 1) {

            if (request.getRepLy() != null && !request.getRepLy().isEmpty()) {
                review.setReply(request.getRepLy());
            } else {
                throw new IllegalArgumentException("Phản hồi không hợp lệ");
            }

            // Lưu lại đối tượng review
            menuRestaurantReviewRepository.save(review);
            menuRestaurantReviewRepository.flush();
        } else {
            throw new IllegalArgumentException("Bạn không phải Admin");
        }
    }

}
