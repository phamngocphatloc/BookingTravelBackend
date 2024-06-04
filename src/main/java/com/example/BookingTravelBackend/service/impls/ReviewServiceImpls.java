package com.example.BookingTravelBackend.service.impls;

import com.example.BookingTravelBackend.Repository.ReviewHotelRepository;
import com.example.BookingTravelBackend.entity.ReviewHotel;
import com.example.BookingTravelBackend.entity.User;
import com.example.BookingTravelBackend.payload.Request.ReviewRequest;
import com.example.BookingTravelBackend.payload.respone.ReviewRespone;
import com.example.BookingTravelBackend.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpls implements ReviewService {
    private final ReviewHotelRepository reviewHotelRepository;

    @Override
    public List<ReviewRespone> selectAll() {
        List<ReviewRespone> listResponse = new ArrayList<>();
        reviewHotelRepository.findAll().stream().forEach(item -> {
            listResponse.add(new ReviewRespone(item));
        });
        return listResponse;
    }

    @Override
    public ReviewRespone Comment(ReviewRequest request, User user) {
        ReviewHotel rv = new ReviewHotel();
        rv.setReview(request.getReview());
        rv.setRate(request.getRate());
        rv.setUserReview(user);
        ReviewHotel response =  reviewHotelRepository.save(rv);
        return new ReviewRespone(rv);
    }
}
